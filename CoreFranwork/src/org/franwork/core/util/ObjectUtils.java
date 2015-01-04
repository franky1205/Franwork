package org.franwork.core.util;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Frankie
 *
 */
public final class ObjectUtils {
	
	private ObjectUtils() {
		throw new IllegalAccessError("ObjectUtils unable to initiate.");
	}
	
	/**
	 * Compare two object instance.
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compareObjs(Object obj1, Object obj2) {
    	if (ObjectUtils.isEquals(obj1, obj2)) {
    		return 0;
    	}
    	if (obj1 == null || obj2 == null) {
    		return (obj1 != null ? 1 : -1);
    	}
    	Preconditions.checkArgument((obj1.getClass().equals(obj2.getClass())), 
    			"Different types of Objects cannot be compared");
    	Preconditions.checkArgument(obj1 instanceof Comparable,
    			"Comparing Object does not implements Comparable");
    	return ((Comparable) obj1).compareTo(obj2);
    }

	/**
     * Whether two objects is equals.
     * 
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isNotEquals(Object obj1, Object obj2) {
    	return !ObjectUtils.isEquals(obj1, obj2);
    }
    
    /**
     * Whether two objects is equals or not.
     * 
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isEquals(Object obj1, Object obj2) {
    	if (obj1 == obj2) {
    		return true;
    	}
    	if (obj1 == null || obj2 == null) {
            return false;
        }
        if (StringUtils.isBlank(obj1.toString()) && StringUtils.isBlank(obj2.toString())) {
            return true;
        }
        return obj1.equals(obj2);
    }
}
