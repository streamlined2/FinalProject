package edu.practice.finalproject.model.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.analysis.Inspector;

public abstract class NaturalKeyEntity extends Entity {

	public abstract List<Method> keyGetters();
	
	/**
	 * Natural key based {@code equals} method
	 * @return true if parameter is not null, refers to another entity of the same class and has equal natural key that is returned by {@code getKey}
	 */
	@Override public boolean equals(final Object o) {
		if(o==null) return false;
		if(getClass()!=o.getClass()) return false;
		return naturalKeyValues().equals(((NaturalKeyEntity)o).naturalKeyValues());
	}
	
	/**
	 * Natural key based hash code
	 * @return hash code of natural key that is specified by {@code getKey}
	 */
	@Override public int hashCode() {
			return naturalKeyValues().hashCode();
	}

	private List<Object> naturalKeyValues() {
		try {
			final List<Method> methods = keyGetters();
			final List<Object> values=new ArrayList<>(methods.size());
			for(final Method method:methods) {
				values.add(method.invoke(this));
			}
			return values;
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new EntityException(e);
		}
	}
	
	@Override public String toString() {
		final StringJoiner join=new StringJoiner(": ", "[", "]");
		for(final Object value:naturalKeyValues()) {
			join.add(Inspector.getReadableValue(value.getClass(),value));
		}
		return join.toString();
	}
}
