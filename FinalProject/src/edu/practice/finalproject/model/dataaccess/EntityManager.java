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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
	
	public boolean persist(final Entity entity) {
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
	
	public <S extends Entity> boolean persistLinks(final Entity masterEntity,final S... slaveEntities) {
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
	
	public boolean remove(final Entity entity) {
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

	public boolean merge(final Entity entity) {
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
	
	public boolean removeLinks(final Entity master,final Class<? extends Entity> slaveClass) {
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

	private boolean performUpdate(final StringBuilder clause, final Connection conn, final Statement statement, final IntPredicate condition) throws SQLException {
		try {
			final int count=statement.executeUpdate(clause.toString());
			if(condition.test(count)) {
				conn.commit();
				return true;
			}			
		} finally {
			conn.rollback();
		}
		return false;
	}
	
	public <E extends Entity> Optional<E> find(final Class<E> cl,final Long id) {
		final StringBuilder clause=StatementBuilder.getSelectByIdStatement(cl,id);

		try (
				final Connection conn=dataSource.getConnection();
				final Statement statement=conn.createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			if(rs.next()) {
				final E entity=Inspector.createEntity(cl);
				fillEntityValues(entity, Inspector.getSetters(cl,false), rs);
				return Optional.of(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return Optional.empty();
	}

	public <E extends NaturalKeyEntity> Optional<E> findByKey(final Class<E> cl,final Object... key) {
		
		final E entity=Inspector.createEntity(cl);
		final StringBuilder clause=StatementBuilder.getSelectByNaturalKeyStatement(cl,entity,key);

		try (
				final Connection conn=dataSource.getConnection();
				final Statement statement=conn.createStatement();
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

	public <E extends Entity> Optional<E>findByCompositeKey(final Class<E> cl,final Map<String,?> keyPairs) {
		final StringBuilder clause=StatementBuilder.getSelectByKeyStatement(cl,keyPairs);
		try (
				final Connection conn=dataSource.getConnection();				
				final Statement statement=conn.createStatement();
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

	private <E extends Entity> void formEntityListFromResultSet(final Class<E> cl, final List<E> list, final ResultSet rs,final boolean skipID) throws SQLException {
		final List<Method> setters = Inspector.getSetters(cl,skipID);
		while(rs.next()) {
			final E entity=Inspector.createEntity(cl);
			fillEntityValues(entity, setters, rs);
			list.add(entity);
		}
	}

	private <E extends Entity> List<E> formEntityListFromQuery(final Class<E> cl, final boolean skipID,final StringBuilder clause) {
		final List<E> entities=new ArrayList<>();
		try (
				final Connection conn=dataSource.getConnection();
				final Statement statement=conn.createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
			
			formEntityListFromResultSet(cl, entities, rs, skipID);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return entities;
	}
	
    public <S extends Entity> List<S> fetchLinks(final Entity master,final Class<S> slaveClass) {
		final StringBuilder clause=StatementBuilder.getFetchSlaveEntitiesStatement(master,slaveClass);
		return formEntityListFromQuery(slaveClass, false, clause);
    }

	public <M extends Entity> List<M> fetchMissingEntities(final Class<M> masterClass,Class<? extends Entity> slaveClass,final long startElement,final long endElement){
		final StringBuilder clause=StatementBuilder.getSelectMissingEntitiesStatement(masterClass,slaveClass,startElement,endElement);
		return formEntityListFromQuery(masterClass, false, clause);
	}
	
	public <M extends Entity> List<M> fetchLinkedEntities(final Class<M> masterClass,Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final long startElement,final long endElement){
		final StringBuilder clause=StatementBuilder.getSelectLinkedEntitiesStatement(masterClass,slaveClass,keyPairs,startElement,endElement);
		return formEntityListFromQuery(masterClass,false,clause);
	}
	
	public <E extends Entity> List<E> fetchByCompositeKeyOrdered(final Class<E> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys,final long startElement,final long endElement) {
		final StringBuilder clause=StatementBuilder.getSelectByKeyOrderedStatement(cl,keyPairs,orderKeys,startElement,endElement);
		return formEntityListFromQuery(cl,false,clause);
	}

	public <E extends Entity> List<E> fetchEntities(final Class<E> cl,final boolean skipID,final long startElement,final long endElement) {
		final StringBuilder clause=StatementBuilder.getFetchEntitiesStatement(cl,skipID,startElement,endElement);
		return formEntityListFromQuery(cl, skipID, clause);
	}

	public <E extends Entity> List<E> fetchEntities(final Class<E> cl,final boolean skipID) {
		final StringBuilder clause=StatementBuilder.getFetchEntitiesStatement(cl,skipID);
		return formEntityListFromQuery(cl, skipID, clause);
	}
	
	private void fillEntityValues(final Entity entity, final List<Method> setters, final ResultSet rs) {
		int k=1;
		try {
			for(final Method setter:setters) {
				final Object obj=mapSQLValueToProperty(setter.getParameterTypes()[0],rs.getObject(k++));
				Inspector.set(entity,setter,obj);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	private Object mapSQLValueToProperty(final Class<?> cl,final Object value) {
		if(Entity.class.isAssignableFrom(cl) && value instanceof Number) {
			final long id = ((Number)value).longValue();
			final Optional<Entity> entity = find((Class<Entity>)cl,id);
			if(entity.isEmpty()) {
				Entity e = Inspector.createEntity((Class<? extends Entity>)cl);
				e.setId(id);
				return e;
			}else return entity.get();
		}else if(cl.isEnum()) {
			return ((Class<Enum<?>>)cl).getEnumConstants()[((Number)value).intValue()];
		}else if(LocalDate.class.isAssignableFrom(value.getClass())) {
			return value;
		}else if(LocalDateTime.class.isAssignableFrom(value.getClass())) {
			return value;
		}else if(LocalDate.class.isAssignableFrom(cl)) {
			return ((Date)value).toLocalDate();
		}else if(LocalDateTime.class.isAssignableFrom(cl)){
			final LocalDate date = ((Date)value).toLocalDate();
			return date.atStartOfDay();
		}else if((cl==boolean.class || cl==Boolean.class) && value instanceof Number){
			final long id = ((Number)value).longValue();
			return Boolean.valueOf(id>0);
		}else {
			return value;
		}
	}
}
