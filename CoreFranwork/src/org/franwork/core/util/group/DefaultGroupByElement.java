package org.franwork.core.util.group;

/**
 * Default Group By Element class.
 * 
 * @author Frankie
 *
 * @param <T>
 */
public class DefaultGroupByElement<T> extends AbstractGroupByElement<T> {

	public DefaultGroupByElement() {
		super();
	}
	
	public DefaultGroupByElement(Object groupByObj, String... groupByProperties) {
		super(groupByObj, groupByProperties);
	}
}
