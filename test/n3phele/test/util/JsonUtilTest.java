package n3phele.test.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import n3phele.util.JsonUtil;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

public class JsonUtilTest {

	@Test
	public void parseToMap_TwoValuesJson_ShouldMapValues() {
		String json = "{\"name\":\"jerry\", \"anotherparam\": \"avalue\"}";
		Map values = null;
		try {
			values = JsonUtil.parseToMap(json);
		} catch (Exception e) {
			fail();
		}
		
		assertEquals("jerry", values.get("name"));
		assertEquals("avalue", values.get("anotherparam"));
	}
	
	@Test
	public void parseToMap_TwoValuesJsonWithOneAsNull_NullShouldBeCorrectlyHandleAsNull() {
		String json = "{\"name\":\"jerry\", \"anotherparam\": null }";
		Map values = null;
		try {
			values = JsonUtil.parseToMap(json);
		} catch (Exception e) {
			fail();
		}
		
		assertEquals(null, values.get("anotherparam"));
	}

	@Test
	public void parseToMap_TwoValuesJsonWithOneEmptyValue_EmptyJsonValueShouldGenerateAnEmptyValue() {
		String json = "{\"name\":\"jerry\", \"anotherparam\": \"\" }";
		Map values = null;
		try {
			values = JsonUtil.parseToMap(json);
		} catch (Exception e) {
			fail();
		}
		
		assertEquals("", values.get("anotherparam"));
	}
	


}
