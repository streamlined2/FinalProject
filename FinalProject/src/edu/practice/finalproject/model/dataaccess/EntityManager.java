package edu.practice.finalproject.model.dataaccess;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntPredicate;
import javax.sql.DataSource;

import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.model.entity.domain.Car;

/**
 * Generic data access manager
 * @author Serhii Pylypenko
 *
 */
public final class EntityManager {
	
	private static final String RESULT_SET_OF_COUNTING_STATEMENT_DOESN_T_CONTAIN_ANY_DATA = "Result set of counting statement doesn't contain any data";
	private static final String DATASOURCE_SHOULDNT_BE_NULL = "Provide non-null datasource parameter for constructor";
	
	private final DataSource dataSource;
	
	public EntityManager(final DataSource dataSource) {
		Objects.requireNonNull(dataSource,DATASOURCE_SHOULDNT_BE_NULL);
		this.dataSource=dataSource;
	}
	
	/**
	 * Saves passed {@code entity} as new tuple in mapped table
	 * @param entity entity to save
	 * @return true if entity was saved successfully, false otherwise
	 * @throws DataAccessException wraps underlying SQLException 
	 */
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
	
	/**
	 * Saves set of tuples into link table 
	 * @param <S> slave entity type
	 * @param masterEntity master entity
	 * @param slaveEntities array of slave entities
	 * @return true if every tuple was saved successfully, false otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
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
	
	/**
	 * Locates tuple of mapped table by {@code getId} and updates its fields with values of passed {@code entity}
	 * @param entity object which holds field values for tuple
	 * @return true if tuple updated successfully, false otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
	public boolean merge(final Entity entity) {
		final StringBuilder clause=StatementBuilder.getUpdateStatement(entity);

		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				return updateOnCondition(clause, conn, statement, count->count>0);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	/**
	 * Locates tuple by primary key {@code entity.getId} and removes it
	 * @param entity entity to remove
	 * @return true if entity removed successfully, false otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
	public boolean remove(final Entity entity) {
		final StringBuilder clause=StatementBuilder.getDeleteStatement(entity);
		
		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				return updateOnCondition(clause, conn, statement, count->count==1);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Removes set of links between {@code master} entity and linked tuples in mapped table for {@code slaveClass}
	 * @param master entity
	 * @param slaveClass linked slave class
	 * @return true if links removed successfully, false otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
	public boolean removeLinks(final Entity master,final Class<? extends Entity> slaveClass) {
		final StringBuilder clause=StatementBuilder.getDeleteLinkStatement(master,slaveClass);
		
		try(final Connection conn=dataSource.getConnection()){
			conn.setAutoCommit(false);
			try(final Statement statement=conn.createStatement()){
				return updateOnCondition(clause, conn, statement, count->count>0);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Executes SQL statement, obtains and checks its result for given {@code condition}, commits on success or rolls back on failure 
	 * @param clause SQL statement to execute
	 * @param conn connection object
	 * @param statement statement object
	 * @param condition statement execution result check method
	 * @return true if statement executed successfully and changes commites, false otherwise and changes rolled back
	 * @throws SQLException if statement failed
	 */
	private boolean updateOnCondition(final StringBuilder clause, final Connection conn, final Statement statement, final IntPredicate condition) throws SQLException {
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
	
	/**
	 * Finds tuple by its primary key {@code id} and returns entity object of {@code cl} class filled with its values 
	 * @param <E> entity type
	 * @param cl entity class
	 * @param id primary key for tuple
	 * @return entity object of {@code cl} class wrapped in {@code Optional} if tuple found, or {@code Optional.empty} otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
	public <E extends Entity> Optional<E> find(final Class<E> cl,final Long id) {
		final StringBuilder clause=StatementBuilder.getSelectByIdStatement(cl,id);

		try (
				final Connection conn=dataSource.getConnection();
				final Statement statement=conn.createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			if(rs.next()) {
				final E entity=Inspector.createEntity(cl);
				setProperties(entity, Inspector.getSetters(cl,false), rs);
				return Optional.of(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return Optional.empty();
	}

	/**
	 * Finds tuple by its natural key {@code id} and returns entity object of {@code cl} class filled with its values 
	 * @param <E> entity type
	 * @param cl entity class
	 * @param key natural key for tuple
	 * @return entity object of {@code cl} class wrapped in {@code Optional} if tuple found, or {@code Optional.empty} otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
	public <E extends NaturalKeyEntity> Optional<E> findByKey(final Class<E> cl,final Object... key) {
		
		final E entity=Inspector.createEntity(cl);
		final StringBuilder clause=StatementBuilder.getSelectByNaturalKeyStatement(cl,entity,key);

		try (
				final Connection conn=dataSource.getConnection();
				final Statement statement=conn.createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
		
			if(rs.next()) {
				setProperties(entity, Inspector.getSetters(cl,false), rs);
				return Optional.of(entity);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		return Optional.empty();
	}

	/**
	 * Finds first tuple that satisfies given filtering conditions {@code keyPairs} and returns entity object of {@code cl} class filled with its values 
	 * @param <E> entity type
	 * @param cl entity class
	 * @param keyPairs set of filtering pairs key/value
	 * @return entity object of {@code cl} class wrapped in {@code Optional} if tuple found, or {@code Optional.empty} otherwise
	 * @throws DataAccessException wraps underlying SQLException
	 */
	public <E extends Entity> Optional<E> findByCompositeKey(final Class<E> cl,final Map<String,?> keyPairs) {

		for(final Class<E> entityClass:Inspector.getConcreteDescendantsOf(cl)) {
			final StringBuilder clause=StatementBuilder.getSelectByKeyStatement(entityClass,keyPairs);
			try (
					final Connection conn=dataSource.getConnection();				
					final Statement statement=conn.createStatement();
					final ResultSet rs=statement.executeQuery(clause.toString())){
			
				if(rs.next()) {
					final E entity=Inspector.createEntity(entityClass);
					setProperties(entity, Inspector.getSetters(entityClass,false), rs);
					return Optional.of(entity);
				}
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
		return Optional.empty();
	}

	/**
	 * Scans through given result set {@code rs}, creates new entity for every tuple and adds them in provided list {@code list}  
	 * @param <E> entity type
	 * @param cl entity class
	 * @param list resulting list
	 * @param rs result set to scan
	 * @param skipID if true primary should not be set from result set
	 * @throws SQLException if error occurs during result set scan
	 */
	private <E extends Entity> void formEntityListFromResultSet(final Class<E> cl, final List<E> list, final ResultSet rs,final boolean skipID) throws SQLException {
		final List<Method> setters = Inspector.getSetters(cl,skipID);
		while(rs.next()) {
			final E entity=Inspector.createEntity(cl);
			setProperties(entity, setters, rs);
			list.add(entity);
		}
	}

	/**
	 * Executes given SQL statement and composes list of entities from resulting {@code ResultSet}
	 * @param <E> entity type
	 * @param cl entity class
	 * @param skipID if true primary should not be set from result set
	 * @param clause SQL statement to execute
	 * @return list of entities gathered from SQL statement resulting set
	 * @throws DataAccessException if error occurs during result set scan
	 */
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
	
	/**
	 * Executes given SQL statement and returns resulting count
	 * @param clause SQL statement to execute
	 * @param <E> entity type
	 * @return counting result from SQL statement
	 * @throws DataAccessException if error occurs during execution
	 */
	private <E extends Entity> Long getCountFromQuery(final StringBuilder clause) {
		try (
				final Connection conn=dataSource.getConnection();
				final Statement statement=conn.createStatement();
				final ResultSet rs=statement.executeQuery(clause.toString())){
			if(rs.next()) {
				return rs.getLong(1);
			}else throw new DataAccessException(RESULT_SET_OF_COUNTING_STATEMENT_DOESN_T_CONTAIN_ANY_DATA);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	/**
	 * Fetches data from entity class {@code cl} table and composes entity list
	 * @param <E> entity type
	 * @param cl entity class
	 * @return list of entities from table mapped to {@code cl} entity class
	 */
	public <E extends Entity> List<E> fetchEntities(final Class<E> cl) {
		return fetchEntities(cl,false);
	}

	/**
	 * Fetches data from entity class {@code cl} table, selects from {@code startElement} to {@code endElement}, and composes entity list
	 * @param <E> entity type
	 * @param cl entity class
	 * @param skipID if true do not set primary key for every entity
	 * @param startElement number of first element of query
	 * @param endElement number of last element of query
	 * @return list of entities from table mapped to {@code cl} entity class
	 */
	public <E extends Entity> List<E> fetchEntities(final Class<E> cl,final boolean skipID,final long startElement,final long endElement) {
		final StringBuilder clause=StatementBuilder.getFetchEntitiesStatement(cl,skipID,startElement,endElement);
		return formEntityListFromQuery(cl, skipID, clause);
	}

	/**
	 * Fetches data from entity class {@code cl} table and composes entity list
	 * @param <E> entity type
	 * @param cl entity class
	 * @param skipID if true do not set primary key for every entity
	 * @return list of entities from table mapped to {@code cl} entity class
	 */
	public <E extends Entity> List<E> fetchEntities(final Class<E> cl,final boolean skipID) {
		final StringBuilder clause=StatementBuilder.getFetchEntitiesStatement(cl,skipID);
		return formEntityListFromQuery(cl, skipID, clause);
	}

	/**
	 * Fetches tuples from entity class {@code masterClass} table that absent in {@code slaveClass} table, selects from {@code startElement} to {@code endElement}, and composes entity list
	 * @param <M> master entity type
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @param startElement number of first element of query
	 * @param endElement number of last element of query
	 * @return list of entities from table mapped to {@code masterClass} entity class
	 */
	public <M extends Entity> List<M> fetchMissingEntities(final Class<M> masterClass,final Class<? extends Entity> slaveClass,final long startElement,final long endElement){
		final StringBuilder clause=StatementBuilder.getSelectMissingEntitiesStatement(masterClass,slaveClass,startElement,endElement);
		return formEntityListFromQuery(masterClass, false, clause);
	}

	/**
	 * Count tuples from entity class {@code masterClass} table that absent in {@code slaveClass} table
	 * @param <M> master entity type
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @return count of entities from table mapped to {@code masterClass} entity class
	 */
	public <M extends Entity> Long countMissingEntities(final Class<M> masterClass,final Class<? extends Entity> slaveClass){
		final StringBuilder clause=StatementBuilder.getCountMissingEntitiesStatement(masterClass,slaveClass);
		return getCountFromQuery(clause);
	}

	/**
	 * Fetches tuples from entity class {@code masterClass} table that present in {@code slaveClass} table filtered by {@code keyPairs}, selects from {@code startElement} to {@code endElement}, and then composes entity list
	 * @param <M> master entity type
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @param keyPairs set of filtering key/value pairs for {@code slaveClass} table
	 * @param startElement number of first element of query
	 * @param endElement number of last element of query
	 * @return list of entities from table mapped to {@code masterClass} entity class
	 */
	public <M extends Entity> List<M> fetchLinkedEntities(final Class<M> masterClass,final Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final long startElement,final long endElement){
		final StringBuilder clause=StatementBuilder.getSelectLinkedEntitiesStatement(masterClass,slaveClass,keyPairs,startElement,endElement);
		return formEntityListFromQuery(masterClass,false,clause);
	}

	/**
	 * Fetches tuples from entity class {@code masterClass} table that present in {@code slaveClass} table filtered by {@code keyPairs}, missing in {@code missingClass} table, selects from {@code startElement} to {@code endElement}, and then composes entity list
	 * @param <M> master entity type
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @param keyPairs set of filtering key/value pairs for {@code slaveClass} table
	 * @param missingClass entity class for missing tuples table
	 * @param startElement number of first element of query
	 * @param endElement number of last element of query
	 * @return list of entities from table mapped to {@code masterClass} entity class
	 */
	public <M extends Entity> List<M> fetchLinkedMissingEntities(final Class<M> masterClass,final Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final Class<? extends Entity> missingClass,final long startElement,final long endElement){
		final StringBuilder clause=StatementBuilder.getSelectLinkedMissingEntitiesStatement(masterClass,slaveClass,keyPairs,missingClass,startElement,endElement);
		return formEntityListFromQuery(masterClass,false,clause);
	}

	/**
	 * Fetches tuples from entity class {@code cl} table filtered by {@code keyPairs}, sorts by {@code orderKeys}, selects from {@code startElement} to {@code endElement}, and then composes entity list
	 * @param <E> entity type
	 * @param cl entity class
	 * @param keyPairs set of filtering key/value pairs for entity class {@code cl} table
	 * @param orderKeys list of key fields of entity class {@code cl} table to order by
	 * @param startElement number of first element of query
	 * @param endElement number of last element of query
	 * @return list of entities from table mapped to {@code cl} entity class
	 */
	public <E extends Entity> List<E> fetchByCompositeKeyOrdered(final Class<E> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys,final long startElement,final long endElement) {
		final StringBuilder clause=StatementBuilder.getSelectByKeyOrderedStatement(cl,keyPairs,orderKeys,startElement,endElement);
		return formEntityListFromQuery(cl,false,clause);
	}

	/**
	 * Counts tuples from entity class {@code cl} table filtered by {@code keyPairs}
	 * @param <E> entity type
	 * @param cl entity class
	 * @param keyPairs set of filtering key/value pairs for entity class {@code cl} table
	 * @return count of entities from table mapped to {@code cl} entity class
	 */
	public <E extends Entity> Long countByCompositeKey(final Class<E> cl,final Map<String,?> keyPairs) {
		final StringBuilder clause=StatementBuilder.getCountByKeyStatement(cl,keyPairs);
		return getCountFromQuery(clause);
	}

	/**
	 * Count tuples from entity class {@code masterClass} table that present in {@code slaveClass} table filtered by {@code keyPairs}, missing in {@code missingClass} table
	 * @param <M> master entity type
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @param keyPairs set of filtering key/value pairs for {@code slaveClass} table
	 * @param missingClass entity class for missing tuples table
	 * @return count of entities from table mapped to {@code masterClass} entity class
	 */
	public <M extends Entity> Long countLinkedMissingEntities(final Class<M> masterClass,final Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final Class<? extends Entity> missingClass){
		final StringBuilder clause=StatementBuilder.getCountLinkedMissingEntitiesStatement(masterClass,slaveClass,keyPairs,missingClass);
		return getCountFromQuery(clause);
	}
	
	/**
	 * Composes and executes query to count tuples of lease orders that has already been confirmed by manager for given {@code car} and time period [{@code startTime}..{@code dueTime}]
	 * @param car car for lease
	 * @param startTime lease order start time
	 * @param dueTime lease order end time
	 * @return count of tuples that suit given condition 
	 */
	public Long countConfirmedCarOrders(Car car,LocalDateTime startTime,LocalDateTime dueTime) {
		StringBuilder clause = StatementBuilder.getCountConfirmedCarOrdersStatement(car,startTime,dueTime);
		return getCountFromQuery(clause);
	}

	/**
	 * Count tuples from entity class {@code cl} table
	 * @param <E> entity type
	 * @param cl entity class
	 * @return count of entities from table mapped to {@code cl}
	 */
	public <E extends Entity> Long countEntities(final Class<E> cl){
		final StringBuilder clause=StatementBuilder.getCountByKeyStatement(cl,Map.of());
		return getCountFromQuery(clause);
	}

	/**
	 * Fetches tuples of slave class {@code slaveClass} that are linked to {@code master} entity
	 * @param <S> slave type
	 * @param master master entity
	 * @param slaveClass slave class
	 * @return list of entities from table mapped to {@code slaveClass} entity class
	 */
    public <S extends Entity> List<S> fetchLinks(final Entity master,final Class<S> slaveClass) {
		final StringBuilder clause=StatementBuilder.getFetchSlaveEntitiesStatement(master,slaveClass);
		return formEntityListFromQuery(slaveClass, false, clause);
    }

    /**
     * Sets values enumerated by {@code setters} of given {@code entity} from current tuple of result set {@code rs} 
     * @param entity to set values for
     * @param setters list of setter methods that correspond to fields to set
     * @param rs result set that holds source data
     * @throws DataAccessException wraps underlying SQLException
     */
	private void setProperties(final Entity entity, final List<Method> setters, final ResultSet rs) {
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

	/**
	 * Finds corresponding value of given class {@code cl} for SQL type value {@code value}
	 * @param cl target value class
	 * @param value SQL type value
	 * @return target value
	 */
	private Object mapSQLValueToProperty(final Class<?> cl,final Object value) {
		if(Entity.class.isAssignableFrom(cl) && value instanceof Number) {
			final long id = ((Number)value).longValue();
			final Optional<Entity> entity = find((Class<Entity>)cl,id);
			if(!entity.isPresent()) {
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
