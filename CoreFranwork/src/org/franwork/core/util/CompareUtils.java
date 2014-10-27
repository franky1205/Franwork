package org.franwork.core.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.franwork.core.util.comp.BigDecimalEqualsInitiator;
import org.franwork.core.util.comp.EqualsInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Frankie
 *
 */
public final class CompareUtils {

	private static Logger logger = LoggerFactory.getLogger(CompareUtils.class);
	
	/**
	 * The private static equalsInitiatorMap object.
	 */
	private static Map<Class<?>, EqualsInitiator> equalsInitiatorMap = new HashMap<Class<?>, EqualsInitiator>();
	
	/**
	 * The private static default equalsInitiator object.
	 */
	private static final EqualsInitiator DEFAULT_EQUALS_INITIATOR = new EqualsInitiator() {
		public Object initialize(Object object) {
			return object;
		}
	};
	
	/**
	 * Initial CommonUtils static properties.
	 */
	static {
		CompareUtils.putEqualsInitiator(BigDecimal.class, new BigDecimalEqualsInitiator());
	}
	
	private CompareUtils() {
		super();
	}
	
	/**
	 * Put EqualsInitiator object into CommonUtils.equalsInitiatorMap map object.
	 * @param beanType the corresponding bean type key.
	 * @param initiator the EqualsInitiator object.
	 * @return the previous EqualsInitiator value associated with key, 
	 * 			or null if there was no mapping for key
	 */
	public static EqualsInitiator putEqualsInitiator(Class<?> beanType, EqualsInitiator initiator) {
		return CompareUtils.equalsInitiatorMap.put(beanType, initiator);
	}
	
	/**
	 * Get class type corresponds EqualsInitiator object. If not found in equalsInitiatorMap
	 * defaultEqualsInitiator will be returned instead.
	 * 
	 * @param beanType the class type.
	 * @return the corresponding EqualsInitiator object.
	 */
	public static EqualsInitiator getEqualsInitiator(Class<?> beanType) {
		if (CompareUtils.equalsInitiatorMap.containsKey(beanType)) {
			return CompareUtils.equalsInitiatorMap.get(beanType);
		}
		logger.debug("Custom EqualsInitiator not found return CompareUtils.DEFAULT_EQUALS_INITIATOR instead.");
		return CompareUtils.DEFAULT_EQUALS_INITIATOR;
	}

	/**
	 * Whether or not the given two object are equals after initialize
	 * by the corresponding EqualsInitiator.
	 * 
	 * @param o1 first equals compare object.
	 * @param o2 second equals compare object.
	 * @return equals compare result after initiates.
	 */
	public static boolean isEqualsAfterInitial(Object o1, Object o2) {
		Object initialObj1 = o1 == null ? 
				o1 : CompareUtils.getEqualsInitiator(o1.getClass()).initialize(o1);
		Object initialObj2 = o2 == null ? 
				o2 : CompareUtils.getEqualsInitiator(o2.getClass()).initialize(o2);
		return CompareUtils.isEquals(initialObj1, initialObj2);
	}

	/**
	 * Whether or not the given two object are equals.
	 * 
	 * @param o1 object 1.
	 * @param o2 object 2.
	 * @return true when the two objects is equal via equals method.
	 */
	public static boolean isEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (!o1.getClass().equals(o2.getClass())) {
			return false;
		}
		return o1.equals(o2);
	}
}
