package org.franwork.core.util;

import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Provides OGNL functions for Expression Language evaluation.
 * 
 * @author Frankie
 */
public abstract class OgnlUtils {
	
	/**
	 * The private static org.slf4j.Logger object.
	 */
	private static Logger logger = LoggerFactory.getLogger(OgnlUtils.class);
	
	private OgnlUtils() {
		super();
	}
	
	/**
	 * Returns the value of the specific property of the specific bean.
	 * 
	 * @param rootObj
	 * @param expression
	 * @return Object
	 */
	public static Object getValue(String expression, Object rootObj) {
		Object ret = null;
		try {
			ret = Ognl.getValue(expression, rootObj);
		} catch (NoSuchPropertyException e) {
			String errMsg = e.getClass().getName() + ": " + e.getMessage();
			logger.debug(errMsg);
		} catch (Exception e) {
			String errMsg = e.getClass().getName() + ": " + e.getMessage();
			logger.error(errMsg, e);
		}
		return ret;
	}
	
	/**
	 * Sets the value of specific property of the specific bean.
	 * 
	 * @param expression
	 * @param rootObj
	 * @param valueObj
	 */
	public static void setValue(String expression, Object rootObj, Object valueObj) {
		try {
			Ognl.setValue(expression, rootObj, valueObj);
		} catch (NoSuchPropertyException e) {
			String errMsg = e.getClass().getName() + ": " + e.getMessage();
			logger.debug(errMsg);
		} catch (Exception e) {
			String errMsg = e.getClass().getName() + ": " + e.getMessage();
			logger.error(errMsg, e);
		}
	}
    
    /**
     * Get value from field by the given ognl expression.
     * 
     * @author Frankie
     * @param valueObj
     * @param expression
     * @return
     */
    public static Object getFieldOgnlValue(Object valueObj, String expression) {
        Preconditions.checkArgument(StringUtils.isNotBlank(expression), "The given Ognl Expression is Blank");
        if (valueObj == null) {
            return null;
        }
        String[] fieldNames = StringUtils.split(expression, ".");
        if (ArrayUtils.isEmpty(fieldNames)) {
            return null;
        }
        for (String fieldName : fieldNames) {
            valueObj = BeanUtils.getFieldValue(valueObj, fieldName, true);
            if (valueObj == null) {
                return null;
            }
        }
        return valueObj;
    }
    
    /**
     * Get value from field getting method by the given ognl expression.
     * 
     * @author Frankie
     * @param valueObj
     * @param expression
     * @return
     */
    public static Object getMethodOgnlValue(Object valueObj, String expression) {
        Preconditions.checkArgument(StringUtils.isNotBlank(expression), "The given Ognl Expression is Blank");
        if (valueObj == null) {
            return null;
        }
        String[] methodFieldNames = StringUtils.split(expression, ".");
        if (ArrayUtils.isEmpty(methodFieldNames)) {
            return null;
        }
        for (String methodFieldName : methodFieldNames) {
            valueObj = BeanUtils.invokeMethod(valueObj, "get" + methodFieldName.substring(0, 1).toUpperCase() + 
                    methodFieldName.substring(1), null, null, true);
            if (valueObj == null) {
                return null;
            }
        }
        return valueObj;
    }
    
}
