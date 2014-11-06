package test.org.franwork.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringTest {

	@Test
	public void testWhiteSpace() {
		System.out.println(StringUtils.remove("Hello world baby", " "));
	}
}
