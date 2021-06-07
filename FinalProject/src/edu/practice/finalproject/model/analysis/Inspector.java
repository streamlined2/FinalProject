package edu.practice.finalproject.model.analysis;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.practice.finalproject.model.entity.Entity;

public final class Inspector {
	
	private Inspector() {}
	
	public static <E extends Entity> E createEntity(final Class<E> cl) {
		try {
			return cl.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new EntityException(e);
		}
	}
	
	public static Object get(final Entity entity,final Method method) {
		try {
			return method.invoke(entity);
		} catch (ReflectiveOperationException e) {
			throw new EntityException(e);
		}
	}
	
	public static void set(final Entity entity,final Method setter,final Object value) {
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
	
	public static List<Method> getGetters(final Class<? extends Entity> cl){
		return getGetters(cl,false);
	}

	public static List<Method> getGetters(final Class<? extends Entity> cl,final boolean skipID){
		return getAccessors(cl, skipID, Inspector::isGetter);
	}

	public static List<Method> getSetters(final Class<? extends Entity> cl,final boolean skipID){
		return getAccessors(cl, skipID, Inspector::isSetter);
	}

	private static List<Method> getAccessors(final Class<? extends Entity> cl, final boolean skipID,final BiPredicate<Method,Boolean> checker) {
		final List<Method> list=new LinkedList<>();
		final Map<String,Method> methodMap = Arrays.stream(
				cl.getMethods()).filter(method->checker.test(method,skipID)).
				collect(Collectors.toMap(Inspector::getFieldName,Function.identity()));

		for(final Field field:cl.getDeclaredFields()) {
			if(!Modifier.isTransient(field.getModifiers())) {
				addAccessor(list, methodMap.remove(field.getName()), skipID, checker);
			}
		}
		for(final Map.Entry<String,Method> entry:methodMap.entrySet()) {
			addAccessor(list, entry.getValue(), skipID, checker);
		}
		return list;
	}

	private static void addAccessor(final List<Method> list, final Method method, final boolean skipID, final BiPredicate<Method, Boolean> check) {
		if(check.test(method, skipID)) {
			list.add(method);
		}
	}

	public static boolean isGetter(final Method method, final boolean skipID) {
		return 
				method!=null &&
				Modifier.isPublic(method.getModifiers()) &&
				!Modifier.isStatic(method.getModifiers()) &&
				method.getName().startsWith("get") && 
				method.getParameterCount()==0 && 
				(!isIDField(method) || isIDField(method) && !skipID) &&
				!isClass(method.getReturnType());
	}
	
	public static boolean isSetter(final Method method, final boolean skipID) {
		return 
				method!=null &&
				Modifier.isPublic(method.getModifiers()) &&
				!Modifier.isStatic(method.getModifiers()) &&
				method.getName().startsWith("set") && 
				method.getParameterCount()==1 && 
				(!isIDField(method) || isIDField(method) && !skipID) &&
				method.getReturnType()==void.class;
	}
	
	private static String getSetterName(final Field field) {
		return "set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
	}
	
	public static Optional<Method> getForeignReference(final Class<? extends Entity> masterClass,final Class<? extends Entity> slaveClass) {
		final List<Method> getters=getGetters(slaveClass,true);
		for(final Method getter:getters) {
			if(masterClass.isAssignableFrom(getter.getReturnType())) return Optional.of(getter);
		}
		return Optional.empty();
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
		sb.append(accessorName.substring(3,4).toString().toLowerCase());
		sb.append(accessorName.substring(4));
		return sb.toString();
	}
	
	public static String toString(final Entity entity) {
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

	public static String[] getCaptions(final Class<? extends Entity> cl) {
		final List<Method> getters=getGetters(cl,true);
		final String[] captions=new String[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			captions[k++]=getCaption(getter);
		}
		return captions;
	}
	
	private static String getCaption(final Method method) {
		final String str=Inspector.getFieldName(method);
		final StringBuilder sb=new StringBuilder();
		for(int k=0;k<str.length();k++) {
			if(k==0) {
				sb.append(Character.toUpperCase(str.charAt(k)));
			}else if(Character.isLowerCase(str.charAt(k-1)) && Character.isUpperCase(str.charAt(k))) {
				sb.append(" ").append(Character.toLowerCase(str.charAt(k)));
			}else {
				sb.append(str.charAt(k));
			}
		}
		return sb.toString();
	}

	public static Object[] getValues(final Entity entity) {
		return getValues(entity,getGetters(entity.getClass(),true));
	}
	
	public static Object[] getValues(final Entity entity,final List<Method> getters) {
		final Object[] values=new Object[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			values[k++]=getEncodedValue(getter.getReturnType(),get(entity,getter));
		}
		return values;
	}
	
	private static String getEncodedValue(final Class<?> type,final Object value) {
		if(type==Boolean.class || type==boolean.class) 	return Character.toString('\u2716');
		if(type==LocalDate.class) return ((LocalDate)value).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)); 	
		if(type==LocalDateTime.class) return ((LocalDateTime)value).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
		return value.toString();
	}
	
	public static <E extends Entity> List<Object> getValuesForEntities(final Class<E> cl,final Iterable<E> entities) {
		final List<Method> getters=getGetters(cl,true);
		final List<Object> values=new LinkedList<>();
		for(final E entity:entities) {
			values.add(getValues(entity,getters));
		}
		return values;
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
}
