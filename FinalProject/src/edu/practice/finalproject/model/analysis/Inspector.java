package edu.practice.finalproject.model.analysis;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import edu.practice.finalproject.model.entity.Entity;
import utilities.Utils;

public final class Inspector {
	
	private Inspector() {}
	
	public static <E extends Entity> E createEntity(final Class<E> cl) {
		try {
			return cl.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new EntityException(e);
		}
	}
	
	public static <E extends Entity> Object get(final E entity,final Method method) {
		try {
			return method.invoke(entity);
		} catch (ReflectiveOperationException e) {
			throw new EntityException(e);
		}
	}
	
	public static <E extends Entity> void set(final E entity,final Method setter,final Object value) {
		try {
			setter.invoke(entity, value);
		} catch (ReflectiveOperationException e) {
			throw new EntityException(e);
		}
	}
	
	private static boolean isIDField(final Method method) {
		return getFieldName(method).equals(Entity.ID_FIELD);
	}

	private static boolean isClass(final Class<?> type) {
		return Class.class.isAssignableFrom(type);
	}
	
	public static <E extends Entity> List<Method> getGetters(final Class<E> cl){
		return getGetters(cl,false);
	}

	public static List<Method> getGetters(final Class<? extends Entity> cl,final boolean skipID){
		final List<Method> list=new LinkedList<>();
		final Method[] methods=cl.getMethods();
		for(final Method method:methods) {
			if(
					Modifier.isPublic(method.getModifiers()) &&
					!Modifier.isStatic(method.getModifiers()) &&
					method.getName().startsWith("get") && 
					method.getParameterCount()==0 && 
					(!isIDField(method) || isIDField(method) && !skipID) &&
					!isClass(method.getReturnType())) {
				
				list.add(method);
			}
		}
		return list;
	}
	
	public static <E extends Entity> List<Method> getSetters(final Class<E> cl,final boolean skipID){
		final List<Method> list=new LinkedList<>();
		final Method[] methods=cl.getMethods();
		for(final Method method:methods) {
			if(
					Modifier.isPublic(method.getModifiers()) &&
					!Modifier.isStatic(method.getModifiers()) &&
					method.getName().startsWith("set") && 
					method.getParameterCount()==1 && 
					(!isIDField(method) || isIDField(method) && !skipID) &&
					method.getReturnType()==void.class) {
				
				list.add(method);
			}
		}
		return list;
	}
	
	public static String getFieldName(final Method accessor) {
		return getFieldName(accessor.getName());
	}
	
	public static String getFieldName(final String prefix,final Method accessor) {
		return getFieldName(prefix,accessor.getName());
	}
	
	public static String getFieldName(final String accessorName) {
		return getFieldName("",accessorName);
	}
	
	public static String getFieldName(final String prefix,final String accessorName) {
		final StringBuilder sb=new StringBuilder(prefix);
		if(!prefix.isEmpty()) sb.append(".");
		sb.append(accessorName.substring(3));
		return sb.toString().toUpperCase();
	}
	
	public static <E extends Entity> String toString(final E entity) {
		final StringBuilder sb=new StringBuilder("[");
		final Iterator<Method> i=getGetters(entity.getClass()).iterator();
		if(i.hasNext()) {
			Method method=i.next();
			sb.append(getFieldName(method)).append("=").append(Inspector.get(entity,method));
			while(i.hasNext()) {
				method=i.next();
				sb.append(",");
				sb.append(getFieldName(method)).append("=").append(Inspector.get(entity,method));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static <E extends Entity> String[] getCaptions(final Class<E> cl) {
		final List<Method> getters=getGetters(cl,true);
		final String[] captions=new String[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			captions[k++]=Inspector.getFieldName(getter);
		}
		return captions;
	}

	public static <E extends Entity> Object[] getValues(final E entity) {
		return getValues(entity,getGetters(entity.getClass(),true));
	}
	
	public static <E extends Entity> Object[] getValues(final E entity,final List<Method> getters) {
		final Object[] values=new Object[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			values[k++]=get(entity,getter).toString();
		}
		return values;
	}
	
	public static <E extends Entity> List<Object> getValuesForEntities(final Class<E> cl,final Iterable<E> entities) {
		final List<Method> getters=getGetters(cl,true);
		final List<Object> values=new LinkedList<>();
		for(final E entity:entities) {
			values.add(getValues(entity,getters));
		}
		return values;
	}
	
	/**
	 * Finds entity classes that are located within classpath and collects them in resulting set
	 * @return set of entity classes
	 * @throws EntityException incorrect URI or class file not found 
	 */
	public static Set<Class<? extends Entity>> discoverEntityClasses(final String[] packagePrefixes) {
		final Set<Class<? extends Entity>> entityClasses=new HashSet<>();
		final ClassLoader cl=Thread.currentThread().getContextClassLoader();
		for(final String packagePrefix:packagePrefixes) {
			final URL packageURL=cl.getResource(
					packagePrefix.replace(".", "/")+"package-info.class");
			try {
				final File[] entityFiles=new File(packageURL.toURI()).getParentFile().listFiles(
						(dir,name)->name.endsWith(".class"));
				for(final File f:entityFiles) {
					try {
						final Class<?> cls=cl.loadClass(getPackageClass(f,packagePrefix));
						if(Entity.class.isAssignableFrom(cls)) entityClasses.add((Class<? extends Entity>) cls);
					} catch (ClassNotFoundException e) {
						//intentionally skip class if search failed
					}
				}
			} catch (URISyntaxException e) {
				throw new EntityException(e);
			}
		}
		return entityClasses;
	}
	
	private static String getPackageClass(final File path,final String packagePrefix) {
		final int index=path.getName().indexOf(".");
		return packagePrefix+
				(index!=-1?path.getName().substring(0,index):path.getName());
	}

	public static String[] getLabels(final Class<? extends Enum<?>> cl) {
		final String[] labels=new String[cl.getEnumConstants().length];
		int k=0;
		for(final Enum<?> value:cl.getEnumConstants()) {
			labels[k++]=value.toString();
		}
		return labels;
	}

	public static <V extends Enum<?>> Optional<V> getByLabel(final Class<V> cl,final String label){
		if(Objects.isNull(label)) return Optional.empty();
		for(final V value:cl.getEnumConstants()) {
			if(label.equals(value.toString())) return Optional.of(value);
		}
		throw new IllegalArgumentException("incorrect label "+label+" for enum class "+cl.getName());
	}

	public static String mapObjectToString(final Object value) {
		final StringBuilder sb=new StringBuilder();
		final Class<?> cl=value.getClass();
		if(cl.isEnum()) {
			sb.append(((Enum<?>)value).ordinal());
		}else if(cl==String.class) { 
			sb.append("'").append(value).append("'");
		}else if(cl.isArray() && cl.getComponentType()==byte.class){
			sb.append("X'").append(Utils.byteArray2String((byte[])value)).append("'");
		}else {
			sb.append(value);
		}
		return sb.toString();
	}

}
