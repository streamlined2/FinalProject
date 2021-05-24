package edu.practice.finalproject.model.dataaccess;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntPredicate;
import javax.sql.DataSource;

import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.analysis.EntityInspector;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public final class EntityManager {
	
	private final DataSource dataSource;
	
	public EntityManager(final DataSource dataSource) {
		Objects.requireNonNull(dataSource);
		this.dataSource=dataSource;
	}
	
	public DataSource getDataSource() { return dataSource;}
	
	private static final String[] ENTITY_BEANS_PACKAGE_PREFIXES={
			"edu.practice.finalproject.controller.admin.",
			"edu.practice.finalproject.model.entity.",
			"edu.practice.finalproject.model.entity.document.",
			"edu.practice.finalproject.model.entity.domain."
	};
	private static final Set<Class<? extends Entity>> discoveredEntityClasses=EntityInspector.discoverEntityClasses(ENTITY_BEANS_PACKAGE_PREFIXES);
	
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
	
	public static void main(final String... args) {
		System.out.println(discoveredEntityClasses);
		System.out.println(getConcreteDescendantsOf(User.class));
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
			
			final List<Method> setters=EntityInspector.getSetters(cl,skipID);
			while(rs.next()) {
				final E entity=EntityInspector.createEntity(cl);
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
				conn.rollback();
				return false;
			}
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
			
			final List<Method> setters=EntityInspector.getSetters(slaveClass,false);
			while(rs.next()) {
				final S entity=EntityInspector.createEntity(slaveClass);
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

		boolean done=true;
		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final PreparedStatement statement=conn.prepareStatement(clause.toString())){
				for(final S slaveEntity:slaveEntities) {
					statement.setLong(1, slaveEntity.getId());
					if(!statement.execute() && statement.getUpdateCount()!=1) {
						done=false;
						break;
					}
				}
				if(done) {
					conn.commit();			
				}else {
					conn.rollback();
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return done;
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
		final int count=statement.executeUpdate(clause.toString());
		if(condition.test(count)) {
			conn.commit();
			return true;
		}else {
			conn.rollback();
			return false;
		}
	}
	
	public <K extends Comparable<? super K>,E extends NaturalKeyEntity<K>> E findByKey(
			final Class<E> cl,final K key) {
		final E entity=EntityInspector.createEntity(cl);
		final StringBuilder clause=StatementBuilder.getSelectByNaturalKeyStatement(cl,entity,key);

		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			if(rs.next()) {
				fillEntityValues(entity, EntityInspector.getSetters(cl,false), rs);
				return entity;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return null;
	}

	public <E extends Entity> E findByCompositeKey(final Class<E> cl,final String[] keys,final Object[] values) {
		final E entity=EntityInspector.createEntity(cl);
		final StringBuilder clause=StatementBuilder.getSelectByCompositeKeyStatement(cl,keys,values);
		
		try (
				final Statement statement=dataSource.getConnection().createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			if(rs.next()) {
				fillEntityValues(entity, EntityInspector.getSetters(cl,false), rs);
				return entity;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return null;
	}

	private <E extends Entity> void fillEntityValues(
			final E entity, final List<Method> setters, final ResultSet rs) {
		int k=1;
		try {
			for(final Method setter:setters) {
				EntityInspector.set(entity,setter,rs.getObject(k++));
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

}
