package edu.practice.finalproject.model.dataaccess;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import edu.practice.finalproject.model.analysis.EntityInspector;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public abstract class StatementBuilder {
	
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
	
	public static String getStringValue(final Object value) {
		final StringBuilder sb=new StringBuilder();
		if(value.getClass()==String.class) { 
			sb.append("'").append(value).append("'");
		}else { 
			sb.append(value);
		}
		return sb.toString();
	}
	
	public static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator) {
		return getFieldList(accessor,separator,"");
	}
	
	public static StringBuilder getFieldList(final Iterable<Method> accessor,final String separator,final String prefix) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<Method> i=accessor.iterator();
		if(i.hasNext()) {
			sb.append(EntityInspector.getFieldName(prefix,i.next()));
			while(i.hasNext()) {
				sb.append(separator).append(EntityInspector.getFieldName(prefix,i.next()));
			}
		}
		return sb;
	}
	
	public static StringBuilder getValueList(final Iterable<?> iterable,final String separator) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<?> i=iterable.iterator();
		if(i.hasNext()) {
			sb.append(getStringValue(i.next()));
			while(i.hasNext()) {
				sb.append(separator).append(getStringValue(i.next()));
			}
		}
		return sb;
	}
	
	public static StringBuilder getFieldValueList(
			final Iterable<Method> fields,final Iterable<?> values,final String separator) {
		return getFieldValueList(fields,values,separator,"");
	}
	
	public static StringBuilder getFieldValueList(
			final Iterable<Method> fields,final Iterable<?> values,final String separator,final String prefix) {
		final StringBuilder sb=new StringBuilder();
		final Iterator<Method> fieldIterator=fields.iterator();
		final Iterator<?> valueIterator=values.iterator();
		if(fieldIterator.hasNext() && valueIterator.hasNext()) {
			sb.append(EntityInspector.getFieldName(prefix,fieldIterator.next())).append("=").append(getStringValue(valueIterator.next()));
			while(fieldIterator.hasNext() && valueIterator.hasNext()) {
				sb.append(separator).
				append(EntityInspector.getFieldName(prefix,fieldIterator.next())).append("=").append(getStringValue(valueIterator.next()));
			}
		}
		return sb;
	}
	
	public static <E extends Entity> StringBuilder getUpdateStatement(final E entity) {
		final Class<E> cl=(Class<E>) entity.getClass();
		final List<Method> getters=EntityInspector.getGetters(cl,true);
		final List<Object> values=EntityInspector.getValues(entity,getters);
		
		return new StringBuilder(UPDATE_CLAUSE).
				append(StatementBuilder.getTableName(cl)).
				append(SET_CLAUSE).
				append(StatementBuilder.getFieldValueList(getters,values,",")).
				append(WHERE_CLAUSE).
				append(Entity.ID_FIELD).
				append(IN_CLAUSE).
				append(StatementBuilder.getStringValue(entity.getId())).
				append(" )");
	}

	public static <K extends Comparable<? super K>,E extends NaturalKeyEntity<K>> StringBuilder getSelectByNaturalKeyStatement(
			final Class<E> cl,final E entity,final K key) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(EntityInspector.getSetters(cl,false),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl)).
				append(WHERE_CLAUSE).
				append(NaturalKeyEntity.keyFieldName(entity)).
				append(IN_CLAUSE).
				append(getStringValue(key)).
				append(" )");
	}
	
	public static <E extends Entity> StringBuilder getFetchEntitiesStatement(
			final Class<E> cl,final boolean skipID) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(EntityInspector.getSetters(cl,skipID),",")).
				append(FROM_CLAUSE).
				append(getTableName(cl));
	}
	
	public static <E extends Entity> StringBuilder getInsertStatement(final E entity) {
		final Class<E> cl=(Class<E>) entity.getClass();
		final List<Method> getters=EntityInspector.getGetters(cl,true);
		return new StringBuilder(INSERT_CLAUSE).
				append(getTableName(cl)).append(" (").
				append(getFieldList(getters,",")).
				append(VALUES_CLAUSE).
				append(getValueList(EntityInspector.getValues(entity,getters),",")).
				append(")");
	}
	
	public static <M extends Entity,S extends Entity> StringBuilder getInsertLinksStatement(
			final M master,final Class<S> slaveClass) {
		return new StringBuilder(INSERT_CLAUSE).
				append(getLinkTableName((Class<M>)master.getClass(),slaveClass)).append(" (").
				append(getLinkName(master)).append(",").append(getLinkName(slaveClass)).
				append(VALUES_CLAUSE).
				append(getStringValue(master.getId())).append(",").append("?").
				append(")");
	}
	
	public static <M extends Entity,S extends Entity> StringBuilder getFetchSlaveEntitiesStatement(
			final M master,final Class<S> slaveClass) {
		return new StringBuilder(SELECT_CLAUSE).
				append(getFieldList(EntityInspector.getSetters(slaveClass,false),",","B")).
				append(FROM_CLAUSE).
				append(getLinkTableName((Class<M>)master.getClass(),slaveClass)).append(" A ").
				append(JOIN_CLAUSE).
				append(getTableName(slaveClass)).append(" B ").
				append(ON_CLAUSE).append(getQualifiedAttribute("A",getLinkName(slaveClass))).append("=").append(getQualifiedAttribute("B",Entity.ID_FIELD)).
				append(WHERE_CLAUSE).
				append(getQualifiedAttribute("A",getLinkName(master))).
				append(IN_CLAUSE).
				append(getStringValue(master.getId())).
				append(" )");
	}
	
	public static <E extends Entity> StringBuilder getDeleteStatement(
			final E entity) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getTableName((Class<E>) entity.getClass())).
			append(WHERE_CLAUSE).
			append(Entity.ID_FIELD).
			append(IN_CLAUSE).
			append(getStringValue(entity.getId())).
			append(" )");
	}
	
	public static <M extends Entity,S extends Entity> StringBuilder getDeleteLinkStatement(
			final M master,final Class<S> slaveClass) {
		return new StringBuilder(DELETE_CLAUSE).
			append(getLinkTableName((Class<M>)master.getClass(),slaveClass)).
			append(WHERE_CLAUSE).
			append(getLinkName(master)).
			append(IN_CLAUSE).
			append(getStringValue(master.getId())).
			append(" )");
	}
}
