package org.franwork.core.util.group;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.franwork.core.util.ObjectUtils;
import org.franwork.core.util.PropertyUtils;

import com.google.common.base.Preconditions;

/**
 * 資料群組功能抽象類別
 * 
 * 偉康 Frankie 專用類別勿動 (再說一次 勿動)
 * 
 * 有任何問題需要修改請「直接」與我聯絡
 * 
 * E-MAIL : franky.chao@gmail.com
 * Mobile : 0972-500-080
 * 
 * @author WebComm Frankie
 *
 * @param <T>
 */
public abstract class AbstractGroupByElement<T> implements GroupByElement<T> {
	
	/**
	 * Group Data Key
	 */
	private GroupByElementKey groupByKey;
	
	/**
	 * Current Group Data List Collection
	 */
	private List<T> groupByDataList = new LinkedList<T>();
	
	protected AbstractGroupByElement() {
		super();
	}
	
	/**
	 * Constructor by object and grouping property names.
	 * 
	 * @param groupByObj
	 * @param groupByProperties
	 */
	protected AbstractGroupByElement(Object groupByObj, String... groupByProperties) {
		this.initGroupByElement(groupByObj, groupByProperties);
	}
	
	/**
	 * Protected constructor by grouping property values and names.
	 * 
	 * @param groupByObjs
	 * @param groupByProperties
	 */
	protected AbstractGroupByElement(Object[] groupByObjs, String... groupByProperties) {
		this.initGroupByElement(groupByObjs, groupByProperties);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#initGroupByElement(java.lang.Object, java.lang.String[])
	 */
	public void initGroupByElement(Object groupByObj, String... groupByProperties) {
		this.initGroupByElement(PropertyUtils.getPropertyValues(groupByObj, groupByProperties), groupByProperties);
	}
	
	protected void initGroupByElement(Object[] groupByObjs, String... groupByProperties) {
		Preconditions.checkArgument(!ArrayUtils.isEmpty(groupByObjs) 
				&& !ArrayUtils.isEmpty(groupByProperties) && groupByObjs.length == groupByProperties.length, 
				"AbstractGroupByElement init error 'groupByObjs' or 'groupByProperties' "
				+ "array can not be Empty or with different lengths.");
		this.groupByKey = new GroupByElementKey(groupByObjs, groupByProperties);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#getMaxValue(java.lang.Class, java.lang.String)
	 */
	public <P extends Comparable<P>> P getMaxValue(Class<P> propertyType, String propertyName) {
		Preconditions.checkArgument(propertyType != null && StringUtils.isNotBlank(propertyName), 
				"IllegalArguments of getMaxValue function arguments.");
		if (CollectionUtils.isEmpty(this.groupByDataList)) {
			return null;
		}
		P maxValue = null;
		for (T groupData : this.groupByDataList) {
			Object propertyValue = PropertyUtils.getPropertyValue(groupData, propertyName);
			if (propertyValue == null) {
				continue;
			}
			Preconditions.checkArgument(propertyType.isInstance(propertyValue), 
					"Property Instance Not Match Error : " + propertyValue.getClass());
			P propertyTypeValue = propertyType.cast(propertyValue);
			if (maxValue == null) {
				maxValue = propertyTypeValue;
				continue;
			}
			maxValue = maxValue.compareTo(propertyTypeValue) < 0 ? propertyTypeValue : maxValue;
		}
		return maxValue;
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#getMinValue(java.lang.Class, java.lang.String)
	 */
	public <P extends Comparable<P>> P getMinValue(Class<P> propertyType, String propertyName) {
		Preconditions.checkArgument(propertyType != null && StringUtils.isNotBlank(propertyName), 
				"IllegalArguments of getMinValue function arguments.");
		if (CollectionUtils.isEmpty(this.groupByDataList)) {
			return null;
		}
		P minValue = null;
		for (T groupData : this.groupByDataList) {
			Object propertyValue = PropertyUtils.getPropertyValue(groupData, propertyName);
			if (propertyValue == null) {
				continue;
			}
			Preconditions.checkArgument(propertyType.isInstance(propertyValue), 
					"Property Instance Not Match Error : " + propertyValue.getClass());
			P propertyTypeValue = propertyType.cast(propertyValue);
			if (minValue == null) {
				minValue = propertyTypeValue;
				continue;
			}
			minValue = minValue.compareTo(propertyTypeValue) > 0 ? propertyTypeValue : minValue;
		}
		return minValue;
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#getSum(java.lang.String)
	 */
	public BigDecimal getSumValue(String propertyName) {
		if (StringUtils.isBlank(propertyName) || CollectionUtils.isEmpty(this.groupByDataList)) {
			return BigDecimal.ZERO;
		}
		BigDecimal sumTotal = BigDecimal.ZERO;
		for (T groupData : this.groupByDataList) {
			Object propertyValue = PropertyUtils.getPropertyValue(groupData, propertyName);
			sumTotal = sumTotal.add(PropertyUtils.convertToBigDecimal(propertyValue));
		}
		return sumTotal;
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#getGroupByPropertyValue(java.lang.Class, java.lang.String)
	 */
	public <P> P getGroupByPropertyValue(Class<P> propertyType, String propertyName) {
		Preconditions.checkArgument(propertyType != null && 
				StringUtils.isNotBlank(propertyName), 
				"IllegalArguments of getGroupByPropertyValue function arguments."); 
		Object groupByValue = this.groupByKey.getGroupByValue(propertyName);
		if (groupByValue == null) {
			return null;
		}
		Preconditions.checkArgument(propertyType.isInstance(groupByValue), 
				"The Given PropertyType do not match GroupByPropertyValue : " + groupByValue.getClass());
		return propertyType.cast(groupByValue);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#getGroupByPropertyValues()
	 */
	public Map<String, Object> getGroupByPropertyValues() {
		if (this.groupByKey == null) {
			return new LinkedHashMap<String, Object>();
		}
		return this.groupByKey.keyMap;
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#addDataElement(java.lang.Object)
	 */
	public boolean addDataElement(T dataElement) {
		if (dataElement == null || !this.isBelongsToCurrent(dataElement)) {
			return false;
		}
		return this.groupByDataList.add(dataElement);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#isBelongsToCurrent(java.lang.Object)
	 */
	public boolean isBelongsToCurrent(Object dataElement) {
		if (dataElement == null) {
			return false;
		}
		return this.groupByKey.isIdenticalGroup(dataElement);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#getGroupingDataList()
	 */
	public List<T> getGroupingDataList() {
		return Collections.unmodifiableList(this.groupByDataList);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nta.brt.utils.GroupByElement#sort(java.util.Comparator)
	 */
	public void sort(Comparator<? super T> comparator) {
		Preconditions.checkArgument(comparator != null, 
				"IllegalArguments of sort function arguments.");
		Collections.sort(this.groupByDataList, comparator);
	}
	
	/**
	 * Data Grouping Key class type.
	 * 
	 * @author WebComm Frankie
	 *
	 */
	private class GroupByElementKey {
		/**
		 * Group By Key Value Map
		 */
		private Map<String, Object> keyMap = new LinkedHashMap<String, Object>();
		
		private GroupByElementKey(Object[] groupByObjs, String[] groupByProperties) {
			for (int i = 0 ; i < groupByObjs.length && i < groupByProperties.length ; i++) {
				Preconditions.checkArgument(StringUtils.isNotBlank(groupByProperties[i]), 
						"The Group by Properties is Blank or NULL");
				keyMap.put(groupByProperties[i], groupByObjs[i]);
			}
		}
		
		/**
		 * Whether the given dataElement belongs to current Group.
		 * 
		 * @param dataElement
		 * @return
		 */
		private boolean isIdenticalGroup(Object dataElement) {
			if (MapUtils.isEmpty(this.keyMap)) {
				return false;
			}
			for (Map.Entry<String, Object> keyMapEntry : this.keyMap.entrySet()) {
				Object dataPropertyValue = PropertyUtils.getPropertyValue(dataElement, keyMapEntry.getKey());
				if (ObjectUtils.isNotEquals(keyMapEntry.getValue(), dataPropertyValue)) {
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Get the grouping by value of the given group by key name.
		 * 
		 * @param groupBykey
		 * @return
		 */
		private Object getGroupByValue(String groupBykey) {
			if (!this.keyMap.containsKey(groupBykey)) {
				return null;
			}
			return this.keyMap.get(groupBykey);
		}
	}
}
