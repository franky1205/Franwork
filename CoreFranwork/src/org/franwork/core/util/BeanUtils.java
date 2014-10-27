package org.franwork.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This class provides Java Beans related operations.
 * 
 * @author Frankie
 *
 */
public final class BeanUtils {

	/**
	 * The private static org.slf4j.Logger object.
	 */
	private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	
	private BeanUtils() {
		super();
	}
	
	/**
	 * New instance with string class name and initial parameters.
	 * All the initial parameters will be converted to reference data type.
	 * For example, (Integer.TYPE) 52 will be converted and equivalent to (java.lang.Integer) new Integer(52).
	 * As a result, all the initial constructor should take reference data type as arguments instead of primitive data type.
	 * 
	 * @param className The giving instance class type.
	 * @param initParams Initial parameters.
	 * @return The created class with giving instance class type and initiated with parameters.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className, Object... initParams) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		if (StringUtils.isBlank(className)) {
			return null;
		}
		return BeanUtils.newInstance((Class<T>) Class.forName(className), initParams);
	}
	
	/**
	 * New instance by the giving instance class and initial parameters.
	 * All the initial parameters will be converted to reference data type.
	 * For example, (Integer.TYPE) 52 will be converted and equivalent to (java.lang.Integer) new Integer(52).
	 * As a result, all the initial constructor should take reference data type as arguments instead of primitive data type.
	 * 
	 * @param instanceClass The giving instance class type.
	 * @param initParams Initial parameters.
	 * @return The created class with giving instance class type and initiated with parameters.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> T newInstance(Class<T> instanceClass, Object... initParams) 
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, 
			SecurityException, IllegalArgumentException, InvocationTargetException {
		if (instanceClass == null) {
			return null;
		}
		if (ArrayUtils.isEmpty(initParams)) {
			return BeanUtils.newInstanceByDefaultConstructor(instanceClass);
		}
		Class<?>[] paramTypes = new Class<?>[initParams.length];
		for (int i = 0 ; i < initParams.length ; i++) {
			paramTypes[i] = initParams[i].getClass();
		}
		return BeanUtils.newInstance(instanceClass, initParams, paramTypes);
	}
	
	private static <T> boolean hasDefaultConstructor(Class<T> classType) {
		if (classType == null) {
			logger.error("The given class type is NULL.");
			return false;
		}
		try {
			classType.getDeclaredConstructor();
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (NoSuchMethodException e) {
			logger.error("The given Class does not have default constructor or deny access : " + classType);
			return false;
		}
		return true;
	}
	
	/**
	 * New instance by the giving instance class and initial parameter objects and value types.
	 * 
	 * @param instanceClass The giving instance class type.
	 * @param initParams Initial parameter objects.
	 * @param paramTypes Initial parameter value types.
	 * @return The created class with giving instance class type and initiated with parameters.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> T newInstance(Class<T> instanceClass, Object[] initParams, Class<?>[] paramTypes) 
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, 
			SecurityException, IllegalArgumentException, InvocationTargetException {
		if (ArrayUtils.isEmpty(initParams) || ArrayUtils.isEmpty(paramTypes)) {
			return BeanUtils.newInstanceByDefaultConstructor(instanceClass);
		}
		if (initParams.length != paramTypes.length) {
			throw new IllegalArgumentException("Init parameter values is not equal to init parameter types");
		}
		Constructor<T> paramConstructor = instanceClass.getDeclaredConstructor(paramTypes);
		paramConstructor.setAccessible(true);
		return paramConstructor.newInstance(initParams);
	}
	
	private static <T> T newInstanceByDefaultConstructor(Class<T> instanceClass) 
			throws NoSuchMethodException, IllegalArgumentException, InstantiationException, 
			IllegalAccessException, InvocationTargetException {
		if (!BeanUtils.hasDefaultConstructor(instanceClass)) {
			throw new NoSuchMethodException("The given instanceClass has no Default Constructor.");
		}
		Constructor<T> defaultConstructor = instanceClass.getDeclaredConstructor();
		defaultConstructor.setAccessible(true);
		return defaultConstructor.newInstance();
	}
	
	/**
	 * Initiated an instance field values by another entity's fields with containsNull is false, 
	 * which is named in entityFieldNames array. If the giving entity object or entityFieldNames array
	 * is null or empty then the instance will not be initiated with any values.
	 * 
	 * @param instanceObj The instance object which will be initiated.
	 * @param entity The entity object which is used to initiate the instance object.
	 * @param entityFieldNames Defines which field is about to initiate from entity to instance object.
	 */
	public static void initiatedInstanceFields(Object instanceObj, Object entity, String... entityFieldNames) {
		BeanUtils.initiatedInstanceFields(instanceObj, entity, false, entityFieldNames);
	}
	
	/**
	 * Initiated an instance field values by another  entity's fields, 
	 * which is named in entityFieldNames array. If the giving entity object or entityFieldNames array
	 * is null or empty then the instance will not be initiated with any values. If containsNull is true,
	 * the value obtained from entity by field name will be set to the instance object.
	 * 
	 * @param instanceObj The instance object which will be initiated.
	 * @param entity The entity object which is used to initiate the instance object.
	 * @param containsNull The null value obtained from entity will be initiated to instance object or not.
	 * @param entityFieldNames Defines which field is about to initiate from entity to instance object.
	 */
	public static void initiatedInstanceFields(Object instanceObj, Object entity, boolean containsNull, String... entityFieldNames) {
		if (instanceObj == null || entity == null || ArrayUtils.isEmpty(entityFieldNames)) {
			return ;
		}
		for (String fieldName : entityFieldNames) {
			Object fieldValue = OgnlUtils.getValue(fieldName, entity);
			if (fieldValue != null || (fieldValue == null && containsNull)) {
				OgnlUtils.setValue(fieldName, instanceObj, fieldValue);
			}
		}
	}
	
	/**
	 * Get the getter method represented attribute name.
	 * @param method the getter method instance.
	 * @return the getter method represented attribute name.
	 */
	public static String getMethodAttrName(Method method) {
		if (method == null) {
			return "";
		}
		String methodName = method.getName();
		if (!methodName.startsWith("get")) {
			return methodName;
		}
		methodName = methodName.substring("get".length());
		return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
	}

	/**
	 * Get the given class and super classes' all fields 
	 * in any modifier (private, public, protected, none). 
	 * 
	 * @param instanceClass
	 * @return
	 */
	public static List<Field> getClassFields(Class<? extends Object> instanceClass) {
		List<Field> fields = new LinkedList<Field>();
		if (instanceClass == null || Object.class.equals(instanceClass)) {
			return fields;
		}
		Class<?> superClass = instanceClass;
		do {
			fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
		} while (!Object.class.equals(superClass = superClass.getSuperclass()));
		return fields;
	}
	
	/**
	 * Get the given class and super classes' all methods 
	 * in any modifier (private, public, protected, none). 
	 * 
	 * @param instanceClass
	 * @return
	 */
	public static List<Method> getClassMethods(Class<? extends Object> instanceClass) {
		List<Method> methods = new LinkedList<Method>();
		if (instanceClass == null || Object.class.equals(instanceClass)) {
			return methods;
		}
		Class<?> superClass = instanceClass;
		do {
			methods.addAll(Arrays.asList(superClass.getDeclaredMethods()));
		} while (!Object.class.equals(superClass = superClass.getSuperclass()));
		return methods;
	}
	
	/**
     * Invoke Bean Method.
     *
     * @author Frankie
     * @param beanObj
     * @param methodName
     * @param argTypes
     * @param methodArgs
     * @param ignoreError
     * @return
     */
    public static Object invokeMethod(Object beanObj, String methodName, Class<?>[] argTypes, 
            Object[] methodArgs, boolean ignoreError) {
        Preconditions.checkArgument(beanObj != null && StringUtils.isNotBlank(methodName), 
                "The Given Arguements is incorrect : " + beanObj + " " + methodName);
        Class<?> beanType = beanObj.getClass();
        Method declaredMethod = null;
        try {
            if (ArrayUtils.isEmpty(argTypes)) {
                declaredMethod = beanType.getMethod(methodName);
            } else {
                declaredMethod = beanType.getMethod(methodName, argTypes);
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            if (!ignoreError) {
                throw new RuntimeException(e);
            }
        }
        if (declaredMethod == null) {
            return null;
        }
        Object returnObj = null;
        try {
            if (ArrayUtils.isEmpty(argTypes)) {
                returnObj = declaredMethod.invoke(beanObj);
            } else {
                returnObj = declaredMethod.invoke(beanObj, methodArgs);
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            returnObj = null;
            if (!ignoreError) {
                throw new RuntimeException(e);
            }
        }
        return returnObj;
    }

    /**
     * Get Bean Field Value.
     * 
     * @author Frankie
     * @param beanObj
     * @param fieldName
     * @param ignoreError
     * @return
     */
    public static Object getFieldValue(Object beanObj, String fieldName, boolean ignoreError) {
        Preconditions.checkArgument(beanObj != null && StringUtils.isNotBlank(fieldName), 
                "The Given Arguements is incorrect : " + beanObj + " " + fieldName);
        Class<?> beanType = beanObj.getClass();
        Field declaredField = null;
        try {
            declaredField = beanType.getDeclaredField(fieldName);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            if (!ignoreError) {
                throw new RuntimeException(e);
            }
        }
        if (declaredField == null) {
            return null;
        }
        declaredField.setAccessible(true);
        Object returnObj = null;
        try {
            returnObj = declaredField.get(beanObj);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            returnObj = null;
            if (!ignoreError) {
                throw new RuntimeException(e);
            }
        }
        return returnObj;
    }
    
    /**
	 * Get all the methods in instance class type with the given annotation class.
	 * 
	 * @param instance
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> List<Method> getAnnotatedMethods(Object instance, Class<T> annotationClass) {
		return BeanUtils.getAnnotatedMethods(instance, annotationClass, null);
	}
	
	/**
	 * Get all the methods in instance class type with the given annotation class and 
	 * method parameter types.
	 * 
	 * @param instance
	 * @param annotationClass
	 * @param methodParamTypes
	 * @return
	 */
	public static <T extends Annotation> List<Method> getAnnotatedMethods(Object instance, 
			Class<T> annotationClass, Class<?>[] methodParamTypes) {
		if (annotationClass == null) {
			throw new IllegalArgumentException("The given annotation class type is null.");
		}
		List<Method> methods = BeanUtils.getClassMethods(instance.getClass());
		if (CollectionUtils.isEmpty(methods)) {
			return methods;
		}
		List<Method> annotatedMethods = new LinkedList<Method>();
		for (Method method : methods) {
			T annotation = method.getAnnotation(annotationClass);
			if (annotation == null) {
				continue;
			}
			if (ArrayUtils.isEmpty(methodParamTypes) || 
					Arrays.equals(method.getParameterTypes(), methodParamTypes)) {
				annotatedMethods.add(method);
				continue;
			}
			logger.warn("Method annotated with " + annotationClass.getName() + " is not " +
					"match with " + Arrays.toString(methodParamTypes));
		}
		return annotatedMethods;
	}
}
