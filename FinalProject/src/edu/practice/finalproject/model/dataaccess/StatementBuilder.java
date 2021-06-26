package edu.practice.finalproject.model.dataaccess;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.utilities.Utils;

/**
 * SQL query statement construction class
 * @author Serhii Pylypenko
 *
 */
public final class StatementBuilder {
	
	private static final String DELETE_CLAUSE = "DELETE FROM ";
	private static final String ON_CLAUSE = " ON ";
	private static final String JOIN_CLAUSE = " JOIN ";
	private static final String VALUES_CLAUSE = ") VALUES (";
	private static final String INSERT_CLAUSE = "INSERT INTO ";
	private static final String SET_CLAUSE = " SET ";
	private static final String UPDATE_CLAUSE = "UPDATE ";
	private static final String IN_CLAUSE = " IN ( ";
	private static final String NOT_IN_CLAUSE = " NOT IN ( ";
	private static final String WHERE_CLAUSE = " WHERE ";
	private static final String FROM_CLAUSE = " FROM ";
	private static final String SELECT_CLAUSE = "SELECT ";
	private static final String AND_CLAUSE = " AND ";
	private static final String ORDER_BY_CLAUSE = " ORDER BY ";
	private static final String LIMIT_CLAUSE = " LIMIT ";
	private static final String INNER_JOIN_CLAUSE = " INNER JOIN ";
	private static final String NULL_VALUE_DENOMINATOR = "NULL";
	private static final String COUNT_CLAUSE = " COUNT(*) ";

	private static final String CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS = "Can't find foreign reference from slave class %s to master class %s";

	private StatementBuilder() {}
	
	private static String getQualifiedAttribute(final String qualifier,final String attribute) {
		return qualifier+"."+attribute;
	}

	/**
	 * Helper method to map entity class {@code cl} to table name
	 * @param cl entity class
	 * @return mapped table name
	 */
	public static String getTableName(final Class<? extends Entity> cl) {
		return cl.getSimpleName().toLowerCase();
	}

	/**
	 * Returns name of link table which couples master and slave entity class tables
	 * @param <M>master type
	 * @param <S> slave type
	 * @param master master class
	 * @param slave slave class
	 * @return name of link table 
	 */
	public static <M extends Entity,S extends Entity> String getLinkTableName(
			final Class<M> master,final Class<S> slave) {
		return getTableName(master)+"_"+getTableName(slave);
	}
	
	/**
	 * Returns link field name of class for given {@code entity} within link table  
	 * @param entity 
	 * @return link field name for given {@code entity}
	 */
	public static String getLinkName(final Entity entity) {
		return getLinkName(entity.getClass());
	}
	
	/**
	 * Returns link field name of class for given entity class {@code cl} within link table  
	 * @param <E> entity type
	 * @param cl entity class
	 * @return link field name for given entity class {@code cl}
	 */
	public static <E extends Entity> String getLinkName(final Class<E> cl) {
		return cl.getSimpleName().toLowerCase()+"_"+Entity.ID_FIELD;
	}
	
	/**
	 * Composes field list string of {@code accessor} that is separated by {@code separator}
	 * @param accessor list of accessor methods
	 * @param separator
	 * @return string field list of accessors
	 */
	private static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator) {
		return getFieldList(accessor,separator,"");
	}
	
	/**
	 * Composes qualified field list string of {@code accessor} that is separated by {@code separator}
	 * @param accessor list of accessor methods
	 * @param separator
	 * @param prefix for qualified field name
	 * @return string field list of accessors for qualified fields
	 */
	private static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator,final String prefix) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<Method> i=accessor.iterator();
		if(i.hasNext()) {
			sb.append(Inspector.getFieldName(prefix,i.next()));
			while(i.hasNext()) {
				sb.append(separator).append(Inspector.getFieldName(prefix,i.next()));
			}
		}
		return sb;
	}
	
	/**
	 * Composes order keys string of field names, separated by commas
	 * @param orderKeys map of order fields where key is field name and boolean value should be true if ordered ascending, false otherwise
	 * @return order fields string
	 */
	private static StringBuilder getOrderFieldList(final Map<String,Boolean> orderKeys) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<Entry<String, Boolean>> i=orderKeys.entrySet().iterator();
		if(i.hasNext()) {
			Entry<String, Boolean> entry=i.next();
			addOrderItem(sb,entry.getKey(),entry.getValue());
			while(i.hasNext()) {
				entry=i.next();
				sb.append(",");
				addOrderItem(sb,entry.getKey(),entry.getValue());
			}
		}
		return sb;
	}
	
	private static void addOrderItem(final StringBuilder sb,final String orderKey,final boolean ascending) {
		sb.append(orderKey).append(" ").append(ascending?"ASC":"DESC");
	}
	
	private static StringBuilder getFieldValueList(final Iterable<Method> fields,final Object[] values,final String separator) {
		return getFieldValueList(fields,values,separator,"");
	}

	/**
	 * Composes string representation of pairs of qualified field names and its values, separated by {@code separator}
	 * @param fields list of {@code fields} represented by getter or setter methods
	 * @param values array of field values
	 * @param separator
	 * @param prefix field name qualifier
	 * @return string of pairs of qualified field names and values
	 */
	private static StringBuilder getFieldValueList(final Iterable<Method> fields,final Object[] values,final String separator,final String prefix) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<Method> fieldIterator=fields.iterator();
		int k=0;
		if(fieldIterator.hasNext() && k<values.length) {
			sb.append(Inspector.getFieldName(prefix,fieldIterator.next())).append("=").append(values[k++]);//StatementBuilder.mapObjectToString()
			while(fieldIterator.hasNext() && k<values.length) {
				sb.append(separator).
				append(Inspector.getFieldName(prefix,fieldIterator.next())).append("=").append(values[k++]);//StatementBuilder.mapObjectToString()
			}
		}
		return sb;
	}

	/**
	 * Composes string representation of {@code list} every item of which is mapped to string by {@code mapper} method reference
	 * @param separator separates string representation of values
	 * @param prefix prepends mapped value if not empty
	 * @param mapper mapping method reference to extract string representation from array item {@code list}
	 * @param list of values
	 * @return string representation of array of values
	 */
	private static StringBuilder getValueList(final String separator,final String prefix,final Function<Object,String> mapper,final Object... list) {
		final StringBuilder sb=new StringBuilder();
		int k=0;
		if(k<list.length) {
			sb.append(mapper.apply(list[k++]));
			while(k<list.length) {
				sb.append(separator).append(prefix.trim().isEmpty()?"":prefix+".").append(mapper.apply(list[k++]));
			}
		}
		return sb;
	}

	/**
	 * Computes value for every getter method from {@code getters} list and entity {@code entity}, returns them as object array
	 * @param entity entity which field values should be computed
	 * @param getters list of getter methods of {@code entity} entity
	 * @return object array of computed values for every {@entity} field which has getter in {@code getters}
	 */
	private static Object[] getValues(final Entity entity,final List<Method> getters) {
		final Object[] values=new Object[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			values[k++]=StatementBuilder.mapObjectToString(Inspector.get(entity,getter));
		}
		return values;
	}
	
	/**
	 * Composes SQL statement for entity modification 
	 * @param entity to save in database
	 * @return string which contains SQL update statement
	 */
	public static StringBuilder getUpdateStatement(final Entity entity) {
		final List<Method> getters=Inspector.getGetters(entity.getClass(),true);
		final Object[] values=getValues(entity,getters);
		
		return new StringBuilder(UPDATE_CLAUSE).
				append(StatementBuilder.getTableName(entity.getClass())).
				append(SET_CLAUSE).
				append(StatementBuilder.getFieldValueList(getters,values,",")).
				append(WHERE_CLAUSE).
				append(Entity.ID_FIELD).
				append(IN_CLAUSE).
				append(StatementBuilder.mapObjectToString(entity.getId())).
				append(" )");
	}

	/**
	 * Composes SQL statement to fetch entity of class {@code cl} by primary key {@code key}
	 * @param cl entity class to fetch
	 * @param key primary key for entity to fetch
	 * @return string representation of SQL statement for entity fetching
	 */
	public static StringBuilder getSelectByIdStatement(final Class<? extends Entity> cl,final Long key) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,false),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl)).
				append(WHERE_CLAUSE).
				append(Entity.ID_FIELD).
				append(IN_CLAUSE).
				append(StatementBuilder.mapObjectToString(key)).
				append(" )");
	}

	/**
	 * Composes SQL statement to fetch entity of class {@code cl} by its composite natural key {@code keys}
	 * @param <E> type of natural key entity
	 * @param cl entity class to fetch
	 * @param entity entity which fields compose natural key components
	 * @param keys array of composite natural key values
	 * @return string representation of SQL statement for entity fetching
	 */
	public static <E extends NaturalKeyEntity> StringBuilder getSelectByNaturalKeyStatement(final Class<E> cl,final E entity,final Object...keys) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,false),",","B")).
				append(FROM_CLAUSE).
				append(getTableName(cl)).append(" B ").
				append(WHERE_CLAUSE).
				append("(").append(getFieldList(entity.keyGetters(),",","B")).append(")").
				append(IN_CLAUSE).
				append(getValueList(",","A",StatementBuilder::mapObjectToString,keys)).
				append(" )");
	}
	
	public static StringBuilder getSelectByKeyStatement(final Class<? extends Entity> cl,final Map<String,?> keyPairs) {
		return getSelectByKeyOrderedStatement(cl,keyPairs,null);
	}
	
	/**
	 * Composes SQL statement to count entities for class {@code cl} filtered by {@code keyPairs}
	 * @param cl entity class to fetch
	 * @param keyPairs map of filtering keys and values
	 * @return string representation of SQL statement for entity counting
	 */
	public static StringBuilder getCountByKeyStatement(final Class<? extends Entity> cl,final Map<String,?> keyPairs) {
		final StringBuilder sb=new StringBuilder(SELECT_CLAUSE).
				append(COUNT_CLAUSE).
				append(FROM_CLAUSE).
				append(getTableName(cl));
		if(Objects.nonNull(keyPairs) && !keyPairs.isEmpty()) {
			sb.append(WHERE_CLAUSE).append(getKeyPairWhereClause(keyPairs));
		}
		return sb;
	}

	/**
	 * Composes SQL statement to count entities for class {@code cl} filtered by {@code keyPairs}
	 * @param cl entity class to fetch
	 * @param keyPairs map of filtering keys and values
	 * @return string representation of SQL statement for entity counting
	 */
	public static StringBuilder getCountMissingEntitiesStatement(final Class<? extends Entity> masterClass,final Class<? extends Entity> slaveClass) {
		final Optional<Method> foreignRef = Inspector.getForeignReference(masterClass,slaveClass);
		if(foreignRef.isPresent()) {
			return new StringBuilder(SELECT_CLAUSE).
					append(COUNT_CLAUSE).
					append(FROM_CLAUSE).
					append(getTableName(masterClass)).append(" B ").
					append(WHERE_CLAUSE).
					append(getQualifiedAttribute("B",Entity.ID_FIELD)).
					append(NOT_IN_CLAUSE).
						append(SELECT_CLAUSE).
						append(getFieldList(List.of(foreignRef.get()),",","A")).
						append(FROM_CLAUSE).
						append(getTableName(slaveClass)).append(" A ").
					append(" )");
		}else throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,slaveClass,masterClass));
	}

	/**
	 * Composes SQL statement to count entities for class {@code masterClass} present in {@code slaveClass} filtered by {@code keyPairs} and missing in {@code missingClass}
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @param keyPairs map of filtering keys and values
	 * @param missingClass missing class
	 * @return string representation of SQL statement for entity counting
	 */
	public static StringBuilder getCountLinkedMissingEntitiesStatement(final Class<? extends Entity> masterClass,final Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final Class<? extends Entity> missingClass) {
		final Optional<Method> foreignRef = Inspector.getForeignReference(masterClass,slaveClass);
		final Optional<Method> missingFRef = Inspector.getForeignReference(masterClass,missingClass);
		if(foreignRef.isPresent()) {
			if(missingFRef.isPresent()) {
				final StringBuilder sb = new StringBuilder(SELECT_CLAUSE).
						append(COUNT_CLAUSE).
						append(FROM_CLAUSE).
						append(getTableName(masterClass)).append(" B ").
						append(INNER_JOIN_CLAUSE).
						append(getTableName(slaveClass)).append(" A ").
						append(ON_CLAUSE).
						append(getQualifiedAttribute("B",Entity.ID_FIELD)).append("=").
						append(getFieldList(List.of(foreignRef.get()),",","A")).
						append(WHERE_CLAUSE).
						append(getQualifiedAttribute("B",Entity.ID_FIELD)).
						append(NOT_IN_CLAUSE).
							append(SELECT_CLAUSE).
							append(getFieldList(List.of(missingFRef.get()),",","C")).
							append(FROM_CLAUSE).
							append(getTableName(missingClass)).append(" C ").
						append(" )");
				if(Objects.nonNull(keyPairs) && !keyPairs.isEmpty()) {
				    sb.append(AND_CLAUSE).append(getKeyPairWhereClause(keyPairs,'A'));
				}
				return sb;
			}else  throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,missingClass,masterClass));
		}else throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,slaveClass,masterClass));
	}

	/**
	 * Composes SQL statement to fetch ordered entity list of class {@code cl} filtered by {@code keyPairs}, ordered by {@code orderKeys}, starting from {@code startElement} and ending at {@code endElement}
	 * @param cl entity class to fetch
	 * @param keyPairs map of filtering keys and values
	 * @param orderKeys sort keys
	 * @param startElement starting element of query
	 * @param endElement ending element of query
	 * @return string representation of SQL statement for entity fetching
	 */
	public static StringBuilder getSelectByKeyOrderedStatement(final Class<? extends Entity> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys,final long startElement,final long endElement) {
		final StringBuilder sb=getSelectByKeyOrderedStatement(cl,keyPairs,orderKeys);
		appendLimiters(sb, startElement, endElement);
		return sb;
	}

	/**
	 * Helper method to add LIMIT clause to passed SQL statement {@code sb} to limit query starting from {@code startElement} up to {@code endElement} 
	 * @param sb SQL statement for query to limit size of result
	 * @param startElement starting element
	 * @param endElement ending element
	 */
	private static void appendLimiters(final StringBuilder sb, final long startElement, final long endElement) {
		final long count=Math.max(endElement-startElement+1,0L);
		if(count>0) {
			sb.append(LIMIT_CLAUSE).append(startElement).append(",").append(count);
		}
	}

	/**
	 * Composes SQL statement to fetch ordered entity list of class {@code cl} filtered by {@code keyPairs}, ordered by {@code orderKeys}
	 * @param cl entity class to fetch
	 * @param keyPairs map of filtering keys and values
	 * @param orderKeys sort keys
	 * @return string representation of SQL statement for entity fetching
	 */
	public static StringBuilder getSelectByKeyOrderedStatement(final Class<? extends Entity> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys) {
		final StringBuilder sb=new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,false),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl));
		if(Objects.nonNull(keyPairs) && !keyPairs.isEmpty()) {
			sb.append(WHERE_CLAUSE).append(getKeyPairWhereClause(keyPairs));
		}
		if(Objects.nonNull(orderKeys) && !orderKeys.isEmpty()) {
			sb.append(ORDER_BY_CLAUSE).append(getOrderFieldList(orderKeys));
		}
		return sb;
	}
	
	private static <V> StringBuilder getKeyPairWhereClause(final Map<String,V> entries) {
		return getKeyPairWhereClause(entries,null);
	}

	/**
	 * Helper method to compose WHERE clause by scanning {@code entries} map and interpreting its key entry as attribute name, prefixed by {@code alias}, and entry value for filtering 
	 * @param <V> entry value type
	 * @param entries map of filtering keys and values
	 * @param alias alias of table used as attribute prefix 
	 * @return WHERE clause for SQL statement
	 */
	private static <V> StringBuilder getKeyPairWhereClause(final Map<String,V> entries,final Character alias) {
		final String prefix = Objects.isNull(alias)?"":alias+".";
		final StringBuilder sb=new StringBuilder();
		final Iterator<Entry<String,V>> i=entries.entrySet().iterator();
		if(i.hasNext()) {
			Map.Entry<String,V> entry=i.next();
			sb.append(prefix).append(entry.getKey()).append("=").append(StatementBuilder.mapObjectToString(entry.getValue()));
			while(i.hasNext()) {
				entry=i.next();
				sb.append(AND_CLAUSE).append(prefix).append(entry.getKey()).append("=").append(StatementBuilder.mapObjectToString(entry.getValue()));
			}
		}
		return sb;
	}
	
	/**
	 * Composes simple SQL statement to fetch all entities from mapped table for {@code cl}
	 * @param cl class of entities to fetch 
	 * @param skipID do not include primary key of entity, if true; false otherwise
	 * @return SQL statement to fetch entities from given table for entity {@code cl}
	 */
	public static StringBuilder getFetchEntitiesStatement(final Class<? extends Entity> cl,final boolean skipID) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,skipID),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl));
	}

	/**
	 * Simple method to compose SQL statement to fetch entities from mapped table for {@code cl} starting from {@code startElement} up to {@code endElement}
	 * @param cl class of entities to fetch 
	 * @param skipID do not include primary key of entity, if true; false otherwise
	 * @param startElement starting element of query
	 * @param endElement ending element of query
	 * @return SQL statement to fetch entities from given table for entity {@code cl}
	 */
	public static StringBuilder getFetchEntitiesStatement(final Class<? extends Entity> cl,final boolean skipID,final long startElement,final long endElement) {
		final StringBuilder sb=getFetchEntitiesStatement(cl,skipID);
		appendLimiters(sb, startElement, endElement);
		return sb;
	}

	/**
	 * Composes SQL statement to insert new tuple for given {@code entity}
	 * @param entity entity object to insert into table
	 * @return SQL statement for insertion operation
	 */
	public static StringBuilder getInsertStatement(final Entity entity) {
		final List<Method> getters=Inspector.getGetters(entity.getClass(),true);
		return new StringBuilder(INSERT_CLAUSE).
				append(getTableName(entity.getClass())).append(" (").
				append(getFieldList(getters,",")).
				append(VALUES_CLAUSE).
				append(getValueList(",","",Object::toString,getValues(entity,getters))).
				append(")");
	}
	
	/**
	 * Composes SQL statement to insert tuple in link table that couples {@code master} entity and {@code slaveClass} class
	 * @param master master entity
	 * @param slaveClass slave entity class
	 * @return SQL statement for insertion operation
	 */
	public static StringBuilder getInsertLinksStatement(final Entity master,final Class<? extends Entity> slaveClass) {
		return new StringBuilder(INSERT_CLAUSE).
				append(getLinkTableName(master.getClass(),slaveClass)).append(" (").
				append(getLinkName(master)).append(",").append(getLinkName(slaveClass)).
				append(VALUES_CLAUSE).
				append(StatementBuilder.mapObjectToString(master.getId())).append(",").append("?").
				append(")");
	}
	
	/**
	 * Composes SQL statement from table of slave entities linked to given {@code master} entity 
	 * @param master master entity that slave entity are linked to
	 * @param slaveClass slave entity class
	 * @return SQL statement
	 */
	public static StringBuilder getFetchSlaveEntitiesStatement(final Entity master,final Class<? extends Entity> slaveClass) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(slaveClass,false),",","B")).
				append(FROM_CLAUSE).
				append(getLinkTableName(master.getClass(),slaveClass)).append(" A ").
				append(JOIN_CLAUSE).
				append(getTableName(slaveClass)).append(" B ").
				append(ON_CLAUSE).append(getQualifiedAttribute("A",getLinkName(slaveClass))).append("=").append(getQualifiedAttribute("B",Entity.ID_FIELD)).
				append(WHERE_CLAUSE).
				append(getQualifiedAttribute("A",getLinkName(master))).
				append(IN_CLAUSE).
				append(StatementBuilder.mapObjectToString(master.getId())).
				append(" )");
	}

	/**
	 * Composes SQL statement which fetches list of master {@code masterClass} entities that are absent in {@code slaveClass} table, starting from {@code startElement} and ending at {@code endElement}
	 * @param masterClass master class
	 * @param slaveClass slave class
	 * @param startElement starting element of query
	 * @param endElement ending element of query
	 * @return SQL statement to fetch list of master class entities missing in slave class table
	 */
	public static StringBuilder getSelectMissingEntitiesStatement(
			final Class<? extends Entity> masterClass,final Class<? extends Entity> slaveClass,final long startElement,final long endElement) {

		final Optional<Method> foreignRef = Inspector.getForeignReference(masterClass,slaveClass);
		if(foreignRef.isPresent()) {
			final StringBuilder sb=new StringBuilder(SELECT_CLAUSE).
					append(getFieldList(Inspector.getSetters(masterClass,false),",","B")).
					append(FROM_CLAUSE).
					append(getTableName(masterClass)).append(" B ").
					append(WHERE_CLAUSE).
					append(getQualifiedAttribute("B",Entity.ID_FIELD)).
					append(NOT_IN_CLAUSE).
						append(SELECT_CLAUSE).
						append(getFieldList(List.of(foreignRef.get()),",","A")).
						append(FROM_CLAUSE).
						append(getTableName(slaveClass)).append(" A ").
					append(" )");
			appendLimiters(sb, startElement, endElement);
			return sb;
		}else throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,slaveClass,masterClass));
	}

	/**
	 * Composes SQL statement which fetches list of master {@code masterClass} entities that are present in {@code slaveClass} table, filtered by {@code keyPairs}, starting from {@code startElement} and ending at {@code endElement}
	 * @param masterClass master class
	 * @param slaveClass slave class to join with
	 * @param keyPairs map of filtering keys and values
	 * @param startElement starting element of query
	 * @param endElement ending element of query
	 * @return SQL statement to fetch list of master class entities that are present in slave class table
	 */
	public static StringBuilder getSelectLinkedEntitiesStatement(
			final Class<? extends Entity> masterClass,final Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final long startElement,final long endElement) {

		final Optional<Method> foreignRef = Inspector.getForeignReference(masterClass,slaveClass);
		if(foreignRef.isPresent()) {
			final StringBuilder sb=new StringBuilder(SELECT_CLAUSE).
					append(getFieldList(Inspector.getSetters(masterClass,false),",","B")).
					append(FROM_CLAUSE).
					append(getTableName(masterClass)).append(" B ").
					append(INNER_JOIN_CLAUSE).
					append(getTableName(slaveClass)).append(" A ").
					append(ON_CLAUSE).
					append(getQualifiedAttribute("B",Entity.ID_FIELD)).append("=").
					append(getFieldList(List.of(foreignRef.get()),",","A"));
			if(Objects.nonNull(keyPairs) && !keyPairs.isEmpty()) {
			    sb.append(WHERE_CLAUSE).append(getKeyPairWhereClause(keyPairs,'A'));
			}
			appendLimiters(sb, startElement, endElement);
			return sb;
		}else throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,slaveClass,masterClass));
	}
	
	/**
	 * Composes SQL statement which fetches list of master {@code masterClass} entities that are present in {@code slaveClass} table, but absent in {@code missingClass} table, filtered by {@code keyPairs} in slave class table, starting from {@code startElement} and ending at {@code endElement}
	 * @param masterClass master class
	 * @param slaveClass slave class to join with
	 * @param keyPairs map of filtering keys and values
	 * @param missingClass tuple from resulting list shouldn't be present in missing class table
	 * @param startElement starting element of query
	 * @param endElement ending element of query
	 * @return SQL statement to fetch list of master class entities that are present in slave class table but missing in missing class table
	 */
	public static StringBuilder getSelectLinkedMissingEntitiesStatement(
			final Class<? extends Entity> masterClass,final Class<? extends Entity> slaveClass,final Map<String,?> keyPairs,final Class<? extends Entity> missingClass,final long startElement,final long endElement) {

		final Optional<Method> foreignRef = Inspector.getForeignReference(masterClass,slaveClass);
		final Optional<Method> missingFRef = Inspector.getForeignReference(masterClass,missingClass);
		if(foreignRef.isPresent()) {
			if(missingFRef.isPresent()) {
				final StringBuilder sb=new StringBuilder(SELECT_CLAUSE).
						append(getFieldList(Inspector.getSetters(masterClass,false),",","B")).
						append(FROM_CLAUSE).
						append(getTableName(masterClass)).append(" B ").
						append(INNER_JOIN_CLAUSE).
						append(getTableName(slaveClass)).append(" A ").
						append(ON_CLAUSE).
						append(getQualifiedAttribute("B",Entity.ID_FIELD)).append("=").
						append(getFieldList(List.of(foreignRef.get()),",","A")).
						append(WHERE_CLAUSE).
						append(getQualifiedAttribute("B",Entity.ID_FIELD)).
						append(NOT_IN_CLAUSE).
							append(SELECT_CLAUSE).
							append(getFieldList(List.of(missingFRef.get()),",","C")).
							append(FROM_CLAUSE).
							append(getTableName(missingClass)).append(" C ").
						append(" )");
				if(Objects.nonNull(keyPairs) && !keyPairs.isEmpty()) {
				    sb.append(AND_CLAUSE).append(getKeyPairWhereClause(keyPairs,'A'));
				}
				appendLimiters(sb, startElement, endElement);
				return sb;
			}else  throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,missingClass,masterClass));
		}else throw new DataAccessException(String.format(CANT_FIND_FOREIGN_REFERENCE_FROM_SLAVE_TO_MASTER_CLASS,slaveClass,masterClass));
	}
	
	/**
	 * Composes SQL statement to delete entity from its table by primary key {@code entity.getId}
	 * @param entity holds primary key used to locate tuple within table
	 * @return SQL statement to delete entity
	 */
	public static StringBuilder getDeleteStatement(final Entity entity) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getTableName(entity.getClass())).
			append(WHERE_CLAUSE).
			append(Entity.ID_FIELD).
			append(IN_CLAUSE).
			append(StatementBuilder.mapObjectToString(entity.getId())).
			append(" )");
	}
	
	/**
	 * Composes SQL statement to delete tuple from link table that couples {@code master} entity and {@code slaveClass} table
	 * @param master master entity
	 * @param slaveClass slave class
	 * @return SQL statement
	 */
	public static StringBuilder getDeleteLinkStatement(final Entity master,final Class<? extends Entity> slaveClass) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getLinkTableName(master.getClass(),slaveClass)).
			append(WHERE_CLAUSE).
			append(getLinkName(master)).
			append(IN_CLAUSE).
			append(StatementBuilder.mapObjectToString(master.getId())).
			append(" )");
	}

	/**
	 * Helper method to convert given {@code value} to its string representation
	 * @param value value to be converted to string
	 * @return string representation of {@code value}
	 */
	public static String mapObjectToString(final Object value) {
		if(Objects.isNull(value)) return NULL_VALUE_DENOMINATOR;
		final StringBuilder sb=new StringBuilder();
		final Class<?> cl=value.getClass();
		if(Entity.class.isAssignableFrom(cl)) {
			sb.append(((Entity)value).getId());
		}else if(cl.isEnum()) {
			sb.append(((Enum<?>)value).ordinal());
		}else if(cl==String.class) { 
			sb.append("'").append(Utils.escapeQuote(value.toString())).append("'");
		}else if(cl.isArray() && cl.getComponentType()==byte.class){
			sb.append("X'").append(Utils.byteArray2String((byte[])value)).append("'");
		}else if(LocalDateTime.class.isAssignableFrom(cl)){
			final String rep=((LocalDateTime)value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ');
			sb.append("'").append(rep).append("'");
		}else if(LocalDate.class.isAssignableFrom(cl)){
			sb.append("'").append(((LocalDate)value).format(DateTimeFormatter.ISO_LOCAL_DATE)).append("'");
		}else if(cl==Boolean.class){
			final boolean b=(Boolean)value; 
			sb.append(b?1:0);
		}else {
			sb.append(value);
		}
		return sb.toString();
	}
}
