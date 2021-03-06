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
import edu.practice.finalproject.utilities.Utils;

/**
 * Entity inspection and analysis class 
 * @author Serhii Pylypenko
 *
 */
public final class Inspector {
	
	private static final String INCORRECT_LABEL_FOR_ENUM_CLASS = "inspector.incorrect-label-for-enum";

	private static final String[] ENTITY_BEANS_PACKAGE_PREFIXES={
			"edu.practice.finalproject.model.entity.",
			"edu.practice.finalproject.model.entity.userrole.",
			"edu.practice.finalproject.model.entity.document.",
			"edu.practice.finalproject.model.entity.domain."
	};
	private static final Set<Class<? extends Entity>> discoveredEntityClasses=discoverEntityClasses(ENTITY_BEANS_PACKAGE_PREFIXES);

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

	/**
	 * Fetches list of accessor methods for passed class {@code cl}
	 * @param cl class to analyze
	 * @param skipID include entity primary key ({@code Entity.getId}) if false, skip otherwise
	 * @param checker checks if passed method reference is acceptable as either getter or setter
	 * @return list of collected methods
	 */
	private static List<Method> getAccessors(final Class<? extends Entity> cl, final boolean skipID, final BiPredicate<Method,Boolean> checker) {
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

	/**
	 * Tries to discover foreign reference located in {@code slaveClass} to entity of given class {@code masterClass}
	 * @param masterClass target class
	 * @param slaveClass class to be inspected
	 * @return reference getter for master class or {@code Optional.empty} if not found 
	 */
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
	
	/**
	 * Returns qualified table field name that is associated with given entity accessor {@code accessorName}
	 * @param prefix table's alias
	 * @param accessorName getter/setter name
	 * @return qualified name of table field name this {@accessorName} is to be mapped  
	 */
	public static String getFieldName(final String prefix,final String accessorName) {
		final StringBuilder sb=new StringBuilder(prefix);
		if(!prefix.isEmpty()) sb.append(".");
		sb.append(accessorName.substring(3,4).toLowerCase());
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

	/**
	 * Fetches array of table data captions for given entity class {@code cl}
	 * @param cl class to analyze
	 * @return string array of field names 
	 */
	public static String[] getCaptions(final Class<? extends Entity> cl) {
		final List<Method> getters=getGetters(cl,true);
		final String[] captions=new String[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			captions[k++]=getCaption(getter);
		}
		return captions;
	}
	
	/**
	 * Converts accessor method {@code method} name to table column caption 
	 * @param method caption's entity accessor
	 * @return caption for passed {@code method}
	 */
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

	/**
	 * Evaluates entity properties icluding {@code Entity.getId} and collects them into resulting array 
	 * @param entity object which fields are to be evaluated
	 * @return array of entity values
	 */
	public static Object[] getValues(final Entity entity) {
		return getValues(entity,getGetters(entity.getClass(),true));
	}

	/**
	 *  Evaluates entity properties icluding {@code Entity.getId} and collects them into resulting array
	 * @param entity object which fields are to be evaluated
	 * @param getters list of getters to evaluate
	 * @return array of entity values
	 */
	public static Object[] getValues(final Entity entity,final List<Method> getters) {
		final Object[] values=new Object[getters.size()];
		int k=0;
		for(final Method getter:getters) {
			values[k++]=getReadableValue(getter.getReturnType(),get(entity,getter));
		}
		return values;
	}
	
	/**
	 * Converts passed {@code value} of type {@code type} into human readable string
	 * @param type type of {@code value}
	 * @param value value to convert
	 * @return string representation for {@code value} of type {@code type}
	 */
	public static String getReadableValue(final Class<?> type,final Object value) {
		if(type==Boolean.class || type==boolean.class) 	return ((Boolean)value).booleanValue()?Character.toString('\u2716'):" ";
		if(type==LocalDate.class) return ((LocalDate)value).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)); 	
		if(type==LocalDateTime.class) return ((LocalDateTime)value).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
		return value.toString();
	}
	
	/**
	 * Evaluates field values for every entity from {@code entities} and collects resulting arrays into list 
	 * @param <E> type of entity
	 * @param cl class of entity
	 * @param entities list of entities to evaluate
	 * @return list of array of field values for every entity from {@code entities}
	 */
	public static <E extends Entity> List<Object> getValuesForEntities(final Class<E> cl,final Iterable<E> entities) {
		final List<Method> getters=getGetters(cl,true);
		final List<Object> values=new LinkedList<>();
		for(final E entity:entities) {
			values.add(getValues(entity,getters));
		}
		return values;
	}
	
	/**
	 * Evaluates string representations for every constant of given enum class {@code cl} and collects them into resulting array 
	 * @param cl enum class to analyze
	 * @return string representation array of constants for given enum class {@code cl} 
	 */
	public static String[] getLabels(final Class<? extends Enum<?>> cl) {
		final String[] labels=new String[cl.getEnumConstants().length];
		int k=0;
		for(final Enum<?> value:cl.getEnumConstants()) {
			labels[k++]=value.toString();
		}
		return labels;
	}

	/**
	 * Finds enum class {@code cl} constant by given {@code label} 
	 * @param <V> enum type
	 * @param cl enum class
	 * @param label string representation of enum constant to find
	 * @return enum class constant to locate by {@code label}
	 */
	public static <V extends Enum<?>> Optional<V> getByLabel(final Class<V> cl,final String label){
		if(Objects.isNull(label)) return Optional.empty();
		for(final V value:cl.getEnumConstants()) {
			if(label.equals(value.toString())) return Optional.of(value);
		}
		throw new IllegalArgumentException(Utils.format(INCORRECT_LABEL_FOR_ENUM_CLASS, label,cl.getName()));
	}

	/**
	 * Scans entity packages with {@code packagePrefixes} and collects entity classes in resulting set
	 * @return set of discovered entity classes
	 * @throws EntityException in case of malformed URI 
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

	public static boolean isConcreteClass(final Class<?> cl) {
		final int modifiers=cl.getModifiers();
		return Modifier.isPublic(modifiers) && !Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers);
	}

	public static <E extends Entity> Set<Class<E>> getConcreteDescendantsOf(final Class<E> baseClass){
		final Set<Class<E>> set=new HashSet<>();
		discoveredEntityClasses.forEach(
				cl->{ 
					if(isConcreteClass(cl) && baseClass.isAssignableFrom(cl)) 
						set.add((Class<E>) cl);});
		return set;
	}
}
