package edu.practice.finalproject.model.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import edu.practice.finalproject.model.analysis.Inspector;

/**
 * Abstract root class of entity hierarchy
 * @author Serhii Pylypenko
 *
 */
public abstract class Entity implements Serializable, Iterable<Object> {

	protected long id;
	public static final String ID_FIELD="id";
	
	public long getId() { return id;}
	public void setId(long id) { this.id=id;}
	
	/**
	 * Surrogate primary key based {@code equals} method
	 * @return true if parameter is not null and refers to another entity of the same class and has same {@code getId} surrogate primary key
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
	
	/**
	 * Helper iterator to iterate through set of entity attributes 
	 */
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			private final Object[] data=Inspector.getValues(Entity.this);
			private int k=0;

			@Override public boolean hasNext() {
				return k<data.length;
			}

			@Override public Object next() {
				if(k>=data.length) throw new NoSuchElementException("iterator passed beyond last item");
				return data[k++];
			}
		};
	}
}
