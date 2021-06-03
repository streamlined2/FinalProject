package edu.practice.finalproject.model.dataaccess;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public final class StatementBuilder {
	
	private static final String DELETE_CLAUSE = "DELETE FROM ";
	private static final String ON_CLAUSE = " ON ";
	private static final String JOIN_CLAUSE = " JOIN ";
	private static final String VALUES_CLAUSE = ") VALUES (";
	private static final String INSERT_CLAUSE = "INSERT INTO ";
	private static final String SET_CLAUSE = " SET ";
	private static final String UPDATE_CLAUSE = "UPDATE ";
	private static final String IN_CLAUSE = " IN ( ";
	private static final String WHERE_CLAUSE = " WHERE ";
	private static final String FROM_CLAUSE = " FROM ";
	private static final String SELECT_CLAUSE = "SELECT ";
	private static final String AND_CLAUSE = " AND ";
	private static final String ORDER_BY_CLAUSE = " ORDER BY ";
	private static final String LIMIT_CLAUSE = " LIMIT ";

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
	
	public static <E extends Entity> String getLinkName(final E entity) {
		return getLinkName((Class<E>)entity.getClass());
	}
	
	public static <E extends Entity> String getLinkName(final Class<E> cl) {
		return cl.getSimpleName().toLowerCase()+"_"+Entity.ID_FIELD;
	}
	
	public static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator) {
		return getFieldList(accessor,separator,"");
	}
	
	public static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator,final String prefix) {
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
	
	public static StringBuilder getOrderFieldList(final Map<String,Boolean> orderKeys) {
		final StringBuilder sb=new StringBuilder();
		Iterator<Entry<String, Boolean>> i=orderKeys.entrySet().iterator();
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
	
	public static StringBuilder getValueList(final Iterable<?> iterable,final String separator) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<?> i=iterable.iterator();
		if(i.hasNext()) {
			sb.append(Inspector.mapObjectToString(i.next()));
			while(i.hasNext()) {
				sb.append(separator).append(Inspector.mapObjectToString(i.next()));
			}
		}
		return sb;
	}
	
	public static StringBuilder getValueList(final String separator,final Object... list) {
		final StringBuilder sb=new StringBuilder();
		int k=0;
		if(k<list.length) {
			sb.append(Inspector.mapObjectToString(list[k++]));
			while(k<list.length) {
				sb.append(separator).append(Inspector.mapObjectToString(list[k++]));
			}
		}
		return sb;
	}
	
	public static StringBuilder getFieldValueList(
			final Iterable<Method> fields,final Object[] values,final String separator) {
		return getFieldValueList(fields,values,separator,"");
	}
	
	public static StringBuilder getFieldValueList(
			final Iterable<Method> fields,final Object[] values,final String separator,final String prefix) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<Method> fieldIterator=fields.iterator();
		int k=0;
		if(fieldIterator.hasNext() && k<values.length) {
			sb.append(Inspector.getFieldName(prefix,fieldIterator.next())).append("=").append(Inspector.mapObjectToString(values[k++]));
			while(fieldIterator.hasNext() && k<values.length) {
				sb.append(separator).
				append(Inspector.getFieldName(prefix,fieldIterator.next())).append("=").append(Inspector.mapObjectToString(values[k++]));
			}
		}
		return sb;
	}
	
	public static <E extends Entity> StringBuilder getUpdateStatement(final E entity) {
		final Class<E> cl=(Class<E>) entity.getClass();
		final List<Method> getters=Inspector.getGetters(cl,true);
		final Object[] values=Inspector.getValues(entity,getters);
		
		return new StringBuilder(UPDATE_CLAUSE).
				append(StatementBuilder.getTableName(cl)).
				append(SET_CLAUSE).
				append(StatementBuilder.getFieldValueList(getters,values,",")).
				append(WHERE_CLAUSE).
				append(Entity.ID_FIELD).
				append(IN_CLAUSE).
				append(Inspector.mapObjectToString(entity.getId())).
				append(" )");
	}

	public static <K extends Comparable<? super K>,E extends NaturalKeyEntity<K>> StringBuilder getSelectByNaturalKeyStatement(
			final Class<E> cl,final E entity,final K key) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,false),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl)).
				append(WHERE_CLAUSE).
				append(NaturalKeyEntity.keyFieldName(entity)).
				append(IN_CLAUSE).
				append(Inspector.mapObjectToString(key)).
				append(" )");
	}
	
	public static <E extends Entity> StringBuilder getSelectByKeyStatement(final Class<E> cl,final Map<String,?> keyPairs) {
		return getSelectByKeyOrderedStatement(cl,keyPairs,null);
	}
	
	public static <E extends Entity> StringBuilder getSelectByKeyOrderedStatement(final Class<E> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys,final long startElement,final long endElement) {
		final StringBuilder sb=getSelectByKeyOrderedStatement(cl,keyPairs,orderKeys);
		final long count=Math.max(endElement-startElement+1,0L);
		if(count>0) {
			sb.append(LIMIT_CLAUSE).append(startElement).append(",").append(count);
		}
		return sb;
	}

	public static <E extends Entity> StringBuilder getSelectByKeyOrderedStatement(final Class<E> cl,final Map<String,?> keyPairs,final Map<String,Boolean> orderKeys) {
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
		final StringBuilder sb=new StringBuilder();
		final Iterator<Entry<String,V>> i=entries.entrySet().iterator();
		if(i.hasNext()) {
			Map.Entry<String,V> entry=i.next();
			sb.append(entry.getKey()).append("=").append(Inspector.mapObjectToString(entry.getValue()));
			while(i.hasNext()) {
				entry=i.next();
				sb.append(AND_CLAUSE).append(entry.getKey()).append("=").append(Inspector.mapObjectToString(entry.getValue()));
			}
		}
		return sb;
	}
	
	public static <E extends Entity> StringBuilder getFetchEntitiesStatement(final Class<E> cl,final boolean skipID) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(cl,skipID),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl));
	}
	
	public static <E extends Entity> StringBuilder getInsertStatement(final E entity) {
		final Class<E> cl=(Class<E>) entity.getClass();
		final List<Method> getters=Inspector.getGetters(cl,true);
		return new StringBuilder(INSERT_CLAUSE).
				append(getTableName(cl)).append(" (").
				append(getFieldList(getters,",")).
				append(VALUES_CLAUSE).
				append(getValueList(",",Inspector.getValues(entity,getters))).
				append(")");
	}
	
	public static <M extends Entity,S extends Entity> StringBuilder getInsertLinksStatement(final M master,final Class<S> slaveClass) {
		return new StringBuilder(INSERT_CLAUSE).
				append(getLinkTableName((Class<M>)master.getClass(),slaveClass)).append(" (").
				append(getLinkName(master)).append(",").append(getLinkName(slaveClass)).
				append(VALUES_CLAUSE).
				append(Inspector.mapObjectToString(master.getId())).append(",").append("?").
				append(")");
	}
	
	public static <M extends Entity,S extends Entity> StringBuilder getFetchSlaveEntitiesStatement(	final M master,final Class<S> slaveClass) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(Inspector.getSetters(slaveClass,false),",","B")).
				append(FROM_CLAUSE).
				append(getLinkTableName((Class<M>)master.getClass(),slaveClass)).append(" A ").
				append(JOIN_CLAUSE).
				append(getTableName(slaveClass)).append(" B ").
				append(ON_CLAUSE).append(getQualifiedAttribute("A",getLinkName(slaveClass))).append("=").append(getQualifiedAttribute("B",Entity.ID_FIELD)).
				append(WHERE_CLAUSE).
				append(getQualifiedAttribute("A",getLinkName(master))).
				append(IN_CLAUSE).
				append(Inspector.mapObjectToString(master.getId())).
				append(" )");
	}
	
	public static <E extends Entity> StringBuilder getDeleteStatement(final E entity) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getTableName((Class<E>) entity.getClass())).
			append(WHERE_CLAUSE).
			append(Entity.ID_FIELD).
			append(IN_CLAUSE).
			append(Inspector.mapObjectToString(entity.getId())).
			append(" )");
	}
	
	public static <M extends Entity,S extends Entity> StringBuilder getDeleteLinkStatement(final M master,final Class<S> slaveClass) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getLinkTableName((Class<M>)master.getClass(),slaveClass)).
			append(WHERE_CLAUSE).
			append(getLinkName(master)).
			append(IN_CLAUSE).
			append(Inspector.mapObjectToString(master.getId())).
			append(" )");
	}
}
