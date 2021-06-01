package edu.practice.finalproject.model.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import edu.practice.finalproject.model.analysis.Inspector;

public abstract class Entity implements Serializable, Iterable<Object> {

	protected long id;
	public static final String ID_FIELD="id".toUpperCase();
	
	public long getId() { return id;}
	public void setId(long id) { this.id=id;}
	
	/**
	 * Surrogate primary key based {@code equals} method
	 * @return true if parameter is not null, it refers to another entity of the same class and has {@code getId} surrogate primary key
	 */
	@Override public boolean equals(final Object o) {
		if(o==null) return false;
		if(getClass()!=o.getClass()) return false;
		return getId()==((Entity)o).getId();
	}
	
	/**
	 * Surrogate primary key based hash code
	 * @return hash code of primary key that is specified by {@code getId}
	 */
	@Override public int hashCode() {
		return Objects.hash(getId());
	}

	@Override public String toString() {
		return Inspector.toString(this);
	}
	
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			private final Iterator<Object> i=Inspector.getValues(Entity.this).iterator();  

			@Override public boolean hasNext() {
				return i.hasNext();
			}

			@Override public Object next() {
				return i.next();
			}
		};
	}
}
