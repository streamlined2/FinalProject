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
import utilities.Utils;

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

	private StatementBuilder() {}
	
	private static String getQualifiedAttribute(final String qualifier,final String attribute) {
		return qualifier+"."+attribute;
	}

	public static String getTableName(final Class<? extends Entity> cl) {
		return cl.getSimpleName().toLowerCase();
	}
	
	public static <M extends Entity,S extends Entity> String getLinkTableName(
			final Class<M> master,final Class<S> slave) {
		return getTableName(master)+"_"+getTableName(slave);
	}
	
	public static String getLinkName(final Entity entity) {
		return getLinkName(entity.getClass());
	}
	
	public static <E extends Entity> String getLinkName(final Class<E> cl) {
		return cl.getSimpleName().toLowerCase()+"_"+Entity.ID_FIELD;
	}
	
	private static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator) {
		return getFieldList(accessor,separator,"");
	}
	
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
	
	private static StringBuilder getValueList(final String separator,final String prefix,final Function<Object,String> mapper,final Object... list) {
		final StringBuilder sb=new StringBuilder();
		int k=0;
		if(k<list.length) {
			sb.append(mapper.apply(list[k++]));
			while(k<list.length) {
				sb.append(separator).append(prefix.isBlank()?"":prefix+".").append(mapper.apply(list[k++]));
			}
		}
		return sb;
	}
	
	private static Object[] getValues(final Entity entity,final List<Method> getters) {
		final Object[] values=new Object[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			values[k++]=StatementBuilder.mapObjectToString(Inspector.get(entity,getter));
		}
		return values;
	}
	
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
	
	public static StringBuilder getSelectByKeyOrderedStatement(final Class<? extends Entity> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys,final long startElement,final long endElement) {
		final StringBuilder sb=getSelectByKeyOrderedStatement(cl,keyPairs,orderKeys);
		appendLimiters(sb, startElement, endElement);
		return sb;
	}

	private static void appendLimiters(final StringBuilder sb, final long startElement, final long endElement) {
		final long count=Math.max(endElement-startElement+1,0L);
		if(count>0) {
			sb.append(LIMIT_CLAUSE).append(startElement).append(",").append(count);
		}
	}

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
	
	public static StringBuilder getFetchEntitiesStatement(final Class<? extends Entity> cl,final boolean skipID) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,skipID),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl));
	}

	public static StringBuilder getFetchEntitiesStatement(final Class<? extends Entity> cl,final boolean skipID,final long startElement,final long endElement) {
		final StringBuilder sb=getFetchEntitiesStatement(cl,skipID);
		appendLimiters(sb, startElement, endElement);
		return sb;
	}
	
	public static StringBuilder getInsertStatement(final Entity entity) {
		final List<Method> getters=Inspector.getGetters(entity.getClass(),true);
		return new StringBuilder(INSERT_CLAUSE).
				append(getTableName(entity.getClass())).append(" (").
				append(getFieldList(getters,",")).
				append(VALUES_CLAUSE).
				append(getValueList(",","",Object::toString,getValues(entity,getters))).
				append(")");
	}
	
	public static StringBuilder getInsertLinksStatement(final Entity master,final Class<? extends Entity> slaveClass) {
		return new StringBuilder(INSERT_CLAUSE).
				append(getLinkTableName(master.getClass(),slaveClass)).append(" (").
				append(getLinkName(master)).append(",").append(getLinkName(slaveClass)).
				append(VALUES_CLAUSE).
				append(StatementBuilder.mapObjectToString(master.getId())).append(",").append("?").
				append(")");
	}
	
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
		}else throw new DataAccessException(String.format("can't find foreign reference from slave class %s to master class %s",slaveClass,masterClass));
	}
	
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
		}else throw new DataAccessException(String.format("can't find foreign reference from slave class %s to master class %s",slaveClass,masterClass));
	}
	
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
			}else  throw new DataAccessException(String.format("can't find foreign reference from slave class %s to master class %s",missingClass,masterClass));
		}else throw new DataAccessException(String.format("can't find foreign reference from slave class %s to master class %s",slaveClass,masterClass));
	}
	
	public static StringBuilder getDeleteStatement(final Entity entity) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getTableName(entity.getClass())).
			append(WHERE_CLAUSE).
			append(Entity.ID_FIELD).
			append(IN_CLAUSE).
			append(StatementBuilder.mapObjectToString(entity.getId())).
			append(" )");
	}
	
	public static StringBuilder getDeleteLinkStatement(final Entity master,final Class<? extends Entity> slaveClass) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getLinkTableName(master.getClass(),slaveClass)).
			append(WHERE_CLAUSE).
			append(getLinkName(master)).
			append(IN_CLAUSE).
			append(StatementBuilder.mapObjectToString(master.getId())).
			append(" )");
	}

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
