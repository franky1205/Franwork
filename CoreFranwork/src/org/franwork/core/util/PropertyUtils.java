package org.franwork.core.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

/**
 * PropertyUtils provides property related operations.
 * 
 * @author Frankie
 *
 */
public final class PropertyUtils {
	
	/**
	 * The private static org.slf4j.Logger object.
	 */
	private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

	/**
	 * Private default Constructor.
	 */
	private PropertyUtils() {
		throw new IllegalAccessError("PropertyUtils unable to initiate.");
	}
	
	/**
	 * Copy Bean value from dest to source by Properties.
	 * 
	 * @author Frankie
	 * @param dest
	 * @param source
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void copyBeansByProperties(Object dest, Object source) throws 
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		@SuppressWarnings("unchecked")
		Map<String, Object> sourcePropertyMap = org.apache.commons.beanutils.PropertyUtils.describe(source);
		PropertyUtils.setBeanProperties(sourcePropertyMap, dest);
	}
	
	/**
     * Set Bean Property values by property values map.
     * 
     * @author Frankie
     * @param propertyMap
     * @param beanValue
     */
    public static void setBeanProperties(Map<String, Object> propertyMap, Object beanValue) {
    	if (MapUtils.isEmpty(propertyMap) || beanValue == null) {
    		return ;
    	}
    	for (Map.Entry<String, Object> propertyEntry : propertyMap.entrySet()) {
    		try {
    			if (!org.apache.commons.beanutils.PropertyUtils.isWriteable(beanValue, propertyEntry.getKey())) {
    				continue;
    			}
    			org.apache.commons.beanutils.PropertyUtils.setProperty(beanValue, propertyEntry.getKey(), propertyEntry.getValue());
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				logger.error(e.getMessage(), e);
			}
    	}
    }
	
	/**
	 * Convert Object Value to BigDecimal
	 * 
	 * @author Webcomm Frankie
	 * @param objValue
	 * @return
	 */
	public static BigDecimal convertToBigDecimal(Object objValue) {
		if (objValue == null) {
			return BigDecimal.ZERO;
		}
		if (objValue instanceof Number) {
			return new BigDecimal(objValue.toString());
		}
		if (objValue instanceof String && NumberUtils.isNumber(objValue.toString())) {
			return new BigDecimal(objValue.toString());
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * Get the Property Values Object Array.
	 * 
	 * @author Webcomm Frankie
	 * @param beanObj
	 * @param properties
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static Object[] getPropertyValues(Object beanObj, String... properties) {
		if (beanObj == null || ArrayUtils.isEmpty(properties)) {
			return new Object[0];
		}
		List<Object> propertyValues = new ArrayList<Object>(properties.length);
		for (String property : properties) {
			propertyValues.add(PropertyUtils.getPropertyValue(beanObj, property));
		}
		return propertyValues.toArray();
	}
	
	/**
	 * Get Property Value of Object.
	 * 
	 * @author Webcomm Frankie
	 * @param beanObj
	 * @param property
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static Object getPropertyValue(Object beanObj, String property) {
		if (!org.apache.commons.beanutils.PropertyUtils.isReadable(beanObj, property)) {
			throw new IllegalArgumentException("The Bean Object's Property : " + property + " is Not Readable.");
		}
		try {
			return org.apache.commons.beanutils.PropertyUtils.getProperty(beanObj, property);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Create property values map by given property names and values.
	 * 
	 * @param propertyNames the property names array.
	 * @param propertyValues the property values array.
	 * @return the property values map.
	 */
	public static Map<String, Object> createPropertyMap(String[] propertyNames, Object[] propertyValues) {
		Map<String, Object> propertyMap = new LinkedHashMap<String, Object>();
		if (ArrayUtils.isEmpty(propertyNames) || ArrayUtils.isEmpty(propertyNames)) {
			return propertyMap;
		}
		if (propertyNames.length != propertyValues.length) {
			logger.warn("Name array size not equal to value array size.");
		}
		for (int i = 0 ; i < propertyNames.length && i < propertyValues.length ; i++) {
			propertyMap.put(propertyNames[i], propertyValues[i]);
		}
		return propertyMap;
	}
	
	/**
	 * Create the object by given bean type and property values map.
	 * Use org.apache.commons.beanutils.BeanUtils.populate.
	 * 
	 * @param beanType the bean type.
	 * @param propertyMap the property values map.
	 * @return the created bean which is populated with given property values map.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public static <T> T createBeanByPropertyMap(Class<T> beanType, Map<String, Object> propertyMap) 
			throws InstantiationException, IllegalAccessException, InvocationTargetException, 
			SecurityException, IllegalArgumentException, NoSuchMethodException {
		T beanObj = org.franwork.core.util.BeanUtils.newInstance(beanType);
		propertyMap = Maps.filterValues(propertyMap, new Predicate<Object>(){
			public boolean apply(Object input) {
				return input != null;
			}
		});
		BeanUtils.populate(beanObj, propertyMap);
		return beanObj;
	}
}
