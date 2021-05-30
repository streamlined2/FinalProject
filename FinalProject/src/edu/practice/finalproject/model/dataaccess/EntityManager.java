package edu.practice.finalproject.model.dataaccess;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntPredicate;
import javax.sql.DataSource;

import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public final class EntityManager {
	
	private final DataSource dataSource;
	
	public EntityManager(final DataSource dataSource) {
		Objects.requireNonNull(dataSource);
		this.dataSource=dataSource;
	}
	
	private static final String[] ENTITY_BEANS_PACKAGE_PREFIXES={
			"edu.practice.finalproject.controller.admin.",
			"edu.practice.finalproject.model.entity.",
			"edu.practice.finalproject.model.entity.document.",
			"edu.practice.finalproject.model.entity.domain."
	};
	private static final Set<Class<? extends Entity>> discoveredEntityClasses=Inspector.discoverEntityClasses(ENTITY_BEANS_PACKAGE_PREFIXES);
	
	public static boolean isConcreteClass(final Class<?> cl) {
		final int modifiers=cl.getModifiers();
		return Modifier.isPublic(modifiers) && !Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers);
	}
	
	public static <E extends Entity> Set<Class<E>> getConcreteDescendantsOf(final Class<E> baseClass){
		final Set<Class<E>> set=new HashSet<>();
		discoveredEntityClasses.forEach(
				cl->{ 
					if(isConcreteClass(cl) && baseClass.isAssignableFrom(cl)) 
						set.add((Class<E>) cl);});
		return set;
	}
	
	public <E extends Entity> List<E> fetchEntities(final Class<E> cl) {
		return fetchEntities(cl,false);
	}
	
	public <E extends Entity> List<E> fetchEntities(final Class<E> cl,final boolean skipID) {
		final List<E> entities=new ArrayList<>();
		final StringBuilder clause=StatementBuilder.getFetchEntitiesStatement(cl,skipID);

		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
			
			final List<Method> setters=Inspector.getSetters(cl,skipID);
			while(rs.next()) {
				final E entity=Inspector.createEntity(cl);
				fillEntityValues(entity,setters,rs);
				entities.add(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return entities;
	}
	
	public <E extends Entity> boolean persist(final E entity) {
		final StringBuilder clause=StatementBuilder.getInsertStatement(entity);
		
		try(final Connection conn=dataSource.getConnection()){
	    	conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				final int count=statement.executeUpdate(clause.toString(), Statement.RETURN_GENERATED_KEYS);
				try(final ResultSet keys=statement.getGeneratedKeys()){
					if(count==1 && keys.next()) {
						final Object key=keys.getObject(1);
						if(key instanceof Number) {
							entity.setId(((Number)key).longValue());
							conn.commit();
							return true;
						}
					}
				}
			}finally{
				conn.rollback();
			}
			return false;
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
    public <M extends Entity,S extends Entity> List<S> fetchLinks(final M master,final Class<S> slaveClass) {
		final List<S> entities=new ArrayList<>();
		final StringBuilder clause=StatementBuilder.getFetchSlaveEntitiesStatement(master,slaveClass);

		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
			
			final List<Method> setters=Inspector.getSetters(slaveClass,false);
			while(rs.next()) {
				final S entity=Inspector.createEntity(slaveClass);
				fillEntityValues(entity,setters,rs);
				entities.add(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return entities;
    }

	public <M extends Entity,S extends Entity> boolean persistLinks(final M masterEntity,final S... slaveEntities) {
		final StringBuilder clause=StatementBuilder.getInsertLinksStatement(masterEntity,(Class<S>)slaveEntities.getClass().getComponentType());

		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final PreparedStatement statement=conn.prepareStatement(clause.toString())){
				boolean done=true;
				for(final S slaveEntity:slaveEntities) {
					statement.setLong(1, slaveEntity.getId());
					if(!statement.execute() && statement.getUpdateCount()!=1) {
						done=false;
						break;
					}
				}
				if(done) {
					conn.commit();
					return true;
				}
			}finally {
				conn.rollback();
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return false;
	}
	
	public <E extends Entity> boolean remove(final E entity) {
		final StringBuilder clause=StatementBuilder.getDeleteStatement(entity);
		
		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				return performUpdate(clause, conn, statement, count->count==1);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public <E extends Entity> boolean merge(final E entity) {
		final StringBuilder clause=StatementBuilder.getUpdateStatement(entity);

		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				return performUpdate(clause, conn, statement, count->count>0);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public <M extends Entity,S extends Entity> boolean removeLinks(final M master,final Class<S> slaveClass) {
		final StringBuilder clause=StatementBuilder.getDeleteLinkStatement(master,slaveClass);
		
		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				return performUpdate(clause, conn, statement, count->count>0);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	private <K extends Comparable<? super K>> boolean performUpdate(final StringBuilder clause, final Connection conn, final Statement statement, final IntPredicate condition)
			throws SQLException {
		try {
			final int count=statement.executeUpdate(clause.toString());
			if(condition.test(count)) {
				conn.commit();
				return true;
			}			
		}finally {
			conn.rollback();
		}
		return false;
	}
	
	public <K extends Comparable<? super K>,E extends NaturalKeyEntity<K>> Optional<E> findByKey(final Class<E> cl,final K key) {
		
		final E entity=Inspector.createEntity(cl);
		final StringBuilder clause=StatementBuilder.getSelectByNaturalKeyStatement(cl,entity,key);

		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			if(rs.next()) {
				fillEntityValues(entity, Inspector.getSetters(cl,false), rs);
				return Optional.of(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return Optional.empty();
	}

	public <E extends Entity,V> Optional<E>findByCompositeKey(final Class<E> cl,final Map<String,V> keyPairs) {
		final StringBuilder clause=StatementBuilder.getSelectByKeyStatement(cl,keyPairs);
		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			final E entity=Inspector.createEntity(cl);
			if(rs.next()) {
				fillEntityValues(entity, Inspector.getSetters(cl,false), rs);
				return Optional.of(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return Optional.empty();
	}

	public <E extends Entity,V> List<E> fetchByCompositeKeyOrdered(final Class<E> cl,final Map<String,V> keyPairs,final Map<String,Boolean> orderKeys) {
		final List<E> list=new LinkedList<>();
		final StringBuilder clause=StatementBuilder.getSelectByKeyOrderedStatement(cl,keyPairs,orderKeys);
		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			while(rs.next()) {
				final E entity=Inspector.createEntity(cl);
				fillEntityValues(entity, Inspector.getSetters(cl,false), rs);
				list.add(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return list;
	}

	private <E extends Entity> void fillEntityValues(
			final E entity, final List<Method> setters, final ResultSet rs) {
		int k=1;
		try {
			for(final Method setter:setters) {
				Class<?>[] paramTypes=setter.getParameterTypes();
				Object value = rs.getObject(k++);
				Object obj=mapValueToObject(paramTypes[0],value);
				Inspector.set(entity,setter,obj);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	private static <V> V mapValueToObject(final Class<V> cl,final Object value) {
		if(cl.isEnum()) {
			return (V)((Class<Enum<?>>)cl).getEnumConstants()[((Number)value).intValue()];
		}else if(LocalDate.class.isAssignableFrom(cl)) {
			return (V)((Date)value).toLocalDate();
		}else {
			return (V)value;
		}
	}
	
}
