package test.org.franwork.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.franwork.core.util.GroupingUtils;
import org.franwork.core.util.group.AbstractGroupByElement;
import org.franwork.core.util.group.GroupByElement;
import org.junit.Test;

public class GroupingUtilsTest {

	@Test
	public void testGroupByElement() throws Exception {
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person("Frankie", new BigDecimal("28"), "Honest"));
		persons.add(new Person("Frankie", new BigDecimal("28"), "Aonest"));
		persons.add(new Person("Shawn", new BigDecimal("28"), "Honest"));
		persons.add(new Person("Shawn", new BigDecimal("28"), "Honest"));
		persons.add(new Person("Shawn", new BigDecimal("27"), "Honest"));
		persons.add(new Person("Benjamin", new BigDecimal("35"), "Honest"));
		Collection<GroupByElement<Person>> groupByResults = GroupingUtils.groupByProperties(
				persons, PersonGroupByElement.class, "name", "age");
		groupByResults = GroupingUtils.orderingGroupByElements(groupByResults, true);
		for (GroupByElement<Person> groupByElement : groupByResults) {
			System.out.println(groupByElement.getGroupingDataList());
			System.out.println(groupByElement.getGroupByPropertyValue(String.class, "name"));
		}
//		GroupByElement<Person> groupByElement = new DefaultGroupByElement<Person>(p1, "name", "age");
//		System.out.println(groupByElement.addDataElement(p1));
//		System.out.println(groupByElement.addDataElement(p2));
//		System.out.println(groupByElement.addDataElement(p3));
//		System.out.println(groupByElement.addDataElement(p4));
//		System.out.println(groupByElement.addDataElement(p5));
//		System.out.println(groupByElement.addDataElement(p6));
//		System.out.println(groupByElement.getGroupingDataList());
//		System.out.println(groupByElement.getMaxValue(String.class, "description"));
//		System.out.println(groupByElement.getMinValue(String.class, "description"));
//		System.out.println(groupByElement.getSumValue("age"));
	}
	
	public static class PersonGroupByElement extends AbstractGroupByElement<Person>{
		
	}
	
	public class Person {
		private String name;
		private BigDecimal age;
		private String description;
		public Person(String name, BigDecimal age, String description) {
			super();
			this.name = name;
			this.age = age;
			this.description = description;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public BigDecimal getAge() {
			return age;
		}
		public void setAge(BigDecimal age) {
			this.age = age;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + ", description="
					+ description + "]";
		}
	}
}
