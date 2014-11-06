package org.franwork.core.util.comp;

/**
 * EqualsInitiator interface used for CommonUtils.isEqualsAfterInitial.
 * 
 * @see also CommonUtils.isEqualsAfterInitial
 * @author Frankie
 *
 */
public interface EqualsInitiator {

	/**
	 * Initial the given object before equals operation.
	 * 
	 * @param object the given object to be initialized.
	 * @return the initialized object.
	 */
	public Object initialize(Object object);
}
