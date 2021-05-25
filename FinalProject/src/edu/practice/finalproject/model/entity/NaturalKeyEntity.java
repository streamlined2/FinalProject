package edu.practice.finalproject.model.entity;

import edu.practice.finalproject.model.analysis.Inspector;

public abstract class NaturalKeyEntity<K extends Comparable<? super K>> extends Entity {

	/**
	 * Natural key getter method
	 * @return value of natural key
	 */
	protected abstract K getKey();
	/**
	 * Name of natural key field getter
	 * Should be replaced with specific annotation to help discover appropriate field automatically 
	 * @return name of natural key getter method
	 */
	protected abstract String keyFieldGetter();

	public static <K extends Comparable<? super K>,E extends NaturalKeyEntity<K>> String keyFieldName(final E entity) {
		return Inspector.getFieldName(entity.keyFieldGetter());
	}

	/**
	 * Natural key based {@code equals} method
	 * @return true if parameter is not null, refers to another entity of the same class and has equal natural key that is returned by {@code getKey}
	 */
	@Override public boolean equals(final Object o) {
		if(o==null) return false;
		if(getClass()!=o.getClass()) return false;
		return getKey().equals(((NaturalKeyEntity)o).getKey());
	}
	
	/**
	 * Natural key based hash code
	 * @return hash code of natural key that is specified by {@code getKey}
	 */
	@Override public int hashCode() {
		return getKey().hashCode();
	}

}
