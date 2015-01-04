package org.franwork.core.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.franwork.core.util.group.GroupByElement;
import org.franwork.core.util.group.GroupingElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Grouping function utility class.
 * 
 * @author Frankie
 *
 */
public final class GroupingUtils {
	
	/**
	 * The private static org.slf4j.Logger object.
	 */
	private static Logger logger = LoggerFactory.getLogger(GroupingUtils.class);
	
	/**
	 * Private Unused Constructor.
	 */
	private GroupingUtils() {
		throw new IllegalAccessError("GroupinpUtils unable to initiate.");
	}
	
	/**
	 * Ordering GroupByElements data Collection By default Comparator.
	 * 
	 * @author Frankie
	 * @param groupByElements
	 * @param asc
	 * @return
	 */
	public static <T> List<GroupByElement<T>> orderingGroupByElements(
			Collection<GroupByElement<T>> groupByElements, final boolean asc) {
		return GroupingUtils.orderingGroupByElements(groupByElements, new Comparator<GroupByElement<T>>() {
			public int compare(GroupByElement<T> element1, GroupByElement<T> element2) {
				Map<String, Object> element1GroupPropValues = element1.getGroupByPropertyValues();
				Map<String, Object> element2GroupPropValues = element2.getGroupByPropertyValues();
				if (element1GroupPropValues.size() != element2GroupPropValues.size()) {
					return (element1GroupPropValues.size() - element2GroupPropValues.size()) * (asc ? 1 : -1);
				}
				for (Map.Entry<String, Object> group1PropEntry : element1GroupPropValues.entrySet()) {
					if (!element2GroupPropValues.containsKey(group1PropEntry.getKey())) {
						return asc ? 1 : -1;
					}
					int comparingResult = ObjectUtils.compareObjs(group1PropEntry.getValue(), 
							element2GroupPropValues.get(group1PropEntry.getKey()));
					if (comparingResult == 0) {
						continue;
					}
					return comparingResult * (asc ? 1 : -1);
				}
				return 0;
			}
		}, asc);
	}
	
	/**
	 * Ordering GroupByElements data Collection By specific Comparator.
	 *  
	 * @author Frankie
	 * @param groupByElements
	 * @param elementComparator
	 * @param asc
	 * @return
	 */
	public static <T> List<GroupByElement<T>> orderingGroupByElements(
			Collection<GroupByElement<T>> groupByElements, 
			Comparator<GroupByElement<T>> elementComparator, final boolean asc) {
		List<GroupByElement<T>> orderingElements = new LinkedList<GroupByElement<T>>();
		if (CollectionUtils.isEmpty(groupByElements)) {
			return orderingElements;
		}
		orderingElements.addAll(groupByElements);
		Collections.sort(orderingElements, elementComparator);
		return orderingElements;
	}
	
	/**
	 * Group By Properties with given dataList, GroupByElement class type and grouping properties.
	 * 
	 * @author Frankie
	 * @param groupByType
	 * @param dataList
	 * @param groupingProperties
	 * @return
	 * @throws InvocationTargetException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public static <T> Collection<GroupByElement<T>> groupByProperties(List<T> dataList,
			Class<? extends GroupByElement<T>> groupByType, String... groupingProperties) 
					throws SecurityException, IllegalArgumentException, InstantiationException, 
					IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Preconditions.checkArgument(groupByType != null && ArrayUtils.isNotEmpty(groupingProperties), 
				"GroupByType or GroupingProperties is Null or Empty");
		Collection<GroupByElement<T>> groupByElements = new HashSet<GroupByElement<T>>();
		if (CollectionUtils.isEmpty(dataList)) {
			return groupByElements;
		}
		for (T dataElement : dataList) {
			if (dataElement == null) {
				continue;
			}
			GroupByElement<T> groupByElement = GroupingUtils.retrieveOrInitGroupByElement(
					groupByElements, groupByType, dataElement, groupingProperties);
			assert groupByElement != null : "Grouping Algorithm retrieveOrInitGroupByElement Error";
			if (!groupByElements.contains(groupByElement)) {
				groupByElements.add(groupByElement);
			}
			boolean addingResult = groupByElement.addDataElement(dataElement);
			assert addingResult : "Grouping Algorithm retrieveOrInitGroupByElement Error";
		}
		return groupByElements;
	}
	
	/**
	 * Private static function to retrieve or initiate a GroupByElement instance.
	 * 
	 * @param groupByElements
	 * @param groupByType
	 * @param dataElement
	 * @param groupingProperties
	 * @return
	 * @throws InvocationTargetException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private static <T> GroupByElement<T> retrieveOrInitGroupByElement(
			Collection<GroupByElement<T>> groupByElements, Class<? extends GroupByElement<T>> groupByType,
			T dataElement, String... groupingProperties) throws SecurityException, IllegalArgumentException, 
			InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		if (CollectionUtils.isEmpty(groupByElements)) {
			return GroupingUtils.newGroupByElement(groupByType, dataElement, groupingProperties);
		}
		for (GroupByElement<T> groupByElement : groupByElements) {
			if (groupByElement == null) {
				continue;
			}
			if (groupByElement.isBelongsToCurrent(dataElement)) {
				return groupByElement;
			}
		}
		return GroupingUtils.newGroupByElement(groupByType, dataElement, groupingProperties);
	}
	
	/**
	 * Private static function to new GroupByElement instance.
	 * 
	 * @param groupByType
	 * @param dataElement
	 * @param groupingProperties
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	private static <T> GroupByElement<T> newGroupByElement(Class<? extends GroupByElement<T>> groupByType, 
			T dataElement, String... groupingProperties) throws SecurityException, IllegalArgumentException, 
			InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		GroupByElement<T> newGroupByElement = BeanUtils.newInstance(groupByType);
		newGroupByElement.initGroupByElement(dataElement, groupingProperties);
		return newGroupByElement;
	}
	
	/**
	 * Get Grouping set data map.
	 * 
	 * @param dataType
	 * @param groupingProperty
	 * @param keyProperty
	 * @param originDataList
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	public static <K, T extends GroupingElement> Map<K, List<T>> getGroupingSetDataMap(Class<T> dataType, 
			String keyProperty, String groupingProperty, List<?> originDataList) throws 
			IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		if (StringUtils.isBlank(groupingProperty) || StringUtils.isBlank(keyProperty) || 
				CollectionUtils.isEmpty(originDataList)) {
			return new HashMap<K, List<T>>();
		}
		Map<K, List<Object>> originSetDataMap = new HashMap<K, List<Object>>();
		for (Object originData : originDataList) {
			Object keyValue = PropertyUtils.getPropertyValue(originData, keyProperty);
			if (keyValue == null) {
				continue;
			}
			@SuppressWarnings("unchecked")
			K keyTypeValue = (K) keyValue;
			if (!originSetDataMap.containsKey(keyTypeValue)) {
				originSetDataMap.put(keyTypeValue, new ArrayList<Object>());
			}
			originSetDataMap.get(keyTypeValue).add(originData);
		}
		Map<K, List<T>> groupingSetDataMap = new HashMap<K, List<T>>();
		for (Map.Entry<K, List<Object>> originSetDataEntry : originSetDataMap.entrySet()) {
			groupingSetDataMap.put(originSetDataEntry.getKey(), 
					GroupingUtils.getGroupingDataSet(dataType, groupingProperty, originSetDataEntry.getValue()));
		}
		return groupingSetDataMap;
	}
	
	/**
	 * Get grouping data set.
	 * 
	 * @param dataType
	 * @param groupingProperty
	 * @param originDataList
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static <T extends GroupingElement> List<T> getGroupingDataSet(Class<T> dataType, 
			String groupingProperty, List<?> originDataList) throws InstantiationException, 
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<T> groupingDataSet = new ArrayList<T>();
		if (StringUtils.isBlank(groupingProperty) || CollectionUtils.isEmpty(originDataList)) {
			return groupingDataSet;
		}
		T groupingData = null;
		BigDecimal previousNumber = null;
		for (Object originData : originDataList) {
			try {
				BigDecimal propertyNumber = GroupingUtils.getPropertyDecimalNumber(groupingProperty, originData);
				if (propertyNumber == null) {
					continue;
				}
				if (previousNumber != null && propertyNumber.equals(previousNumber.add(BigDecimal.ONE))) {
					groupingData.setGroupingSeqNoEnd(propertyNumber);
					previousNumber = propertyNumber;
					continue;
				}
				groupingData = GroupingUtils.newInstanceAndCopy(dataType, originData);
				groupingData.setGroupingNoRange(propertyNumber, propertyNumber);
				groupingDataSet.add(groupingData);
				previousNumber = propertyNumber;
			} catch (IllegalArgumentException e) {
				logger.warn(e.getMessage(), e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return groupingDataSet;
	}
	
	/**
	 * Get the property Decimal Type value.
	 * 
	 * @param propertyName
	 * @param dataBean
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static BigDecimal getPropertyDecimalNumber(String propertyName, Object dataBean) throws 
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object propertyValue = PropertyUtils.getPropertyValue(dataBean, propertyName);
		if (propertyValue == null) {
			return null;
		}
		if (propertyValue instanceof Number) {
			return new BigDecimal(propertyValue.toString());
		}
		if (propertyValue instanceof BigDecimal) {
			return BigDecimal.class.cast(propertyValue);
		}
		Preconditions.checkArgument(propertyValue instanceof String && 
				NumberUtils.isNumber(propertyValue.toString()), "Property value is not String and Number.");
		return new BigDecimal(propertyValue.toString());
	}
	
	/**
	 * Create a new instance and copy data from source.
	 * 
	 * @param dataType
	 * @param sourceData
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	private static <T> T newInstanceAndCopy(Class<T> dataType, Object sourceData) throws IllegalAccessException, 
			InvocationTargetException, NoSuchMethodException, InstantiationException {
		Preconditions.checkArgument(sourceData != null, "The given source data is NULL.");
		T groupingData = dataType.newInstance();
		PropertyUtils.copyBeansByProperties(groupingData, sourceData);
		return groupingData;
	}
}
