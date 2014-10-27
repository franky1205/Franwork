package org.franwork.core.util.comp;

import java.math.BigDecimal;

/**
 * Default BigDecimal EqualsInitiator object.
 * 
 * @author Frankie
 *
 */
public class BigDecimalEqualsInitiator implements EqualsInitiator {

	/**
	 * Default Constructor of BigDecimalEqualsInitiator.
	 */
	public BigDecimalEqualsInitiator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.neweb.cgp.tools.common.util.EqualsInitiator#initialize(java.lang.Object)
	 */
	public Object initialize(Object object) {
		return object != null && object instanceof BigDecimal ? 
				((BigDecimal) object).stripTrailingZeros() : object;
	}

}
