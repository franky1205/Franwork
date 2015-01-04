package org.franwork.core.util.group;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Group by element data interface.
 * 
 * @author Frankie
 *
 */
public interface GroupByElement<T> {

	/**
	 * Init current GroupByElement with bean object and grouping property names.
	 * 
	 * @author Frankie
	 * @param groupByObj
	 * @param groupByProperties
	 */
	public void initGroupByElement(Object groupByObj, String... groupByProperties);
	
	/**
	 * Get the Group Max Value
	 * 
	 * @author Frankie
	 * @param propertyType
	 * @param propertyName
	 * @return
	 */
	public <P extends Comparable<P>> P getMaxValue(Class<P> propertyType, String propertyName);
	
	/**
	 * Get the Group Min Value
	 * 
	 * @author Frankie
	 * @param propertyType
	 * @param propertyName
	 * @return
	 */
	public <P extends Comparable<P>> P getMinValue(Class<P> propertyType, String propertyName);
	
	/**
	 * Get the group property summation value.
	 * 
	 * @author Frankie
	 * @param property
	 * @return
	 */
	public BigDecimal getSumValue(String property);
	
	/**
	 * Get the Grouping By Property Value
	 * 
	 * @author Frankie
	 * @param propertyType
	 * @param propertyName
	 * @return
	 */
	public <P> P getGroupByPropertyValue(Class<P> propertyType, String propertyName);
	
	/**
	 * Get Grouping by property values.
	 * 
	 * @author Frankie
	 * @return
	 */
	public Map<String, Object> getGroupByPropertyValues();
	
	/**
	 * Add Data into current Group. If the groupBy Properties is invalid 
	 * false will be returned and no data can be inserted into current 
	 * group by element object.
	 * 
	 * @author Frankie
	 * @param dataElement
	 * @return 
	 */
	public boolean addDataElement(T dataElement);
	
	/**
	 * Check if the given data element belongs to current GroupByElement.
	 * 
	 * @param dataElement
	 * @return
	 */
	public boolean isBelongsToCurrent(Object dataElement);
	
	/**
	 * Get the Grouping Data List.
	 * 
	 * @return
	 */
	public List<T> getGroupingDataList();
	
	/**
	 * Sort Grouping Data List by given Comparator.
	 * 
	 * @param comparator
	 */
	public void sort(Comparator<? super T> comparator);
}
