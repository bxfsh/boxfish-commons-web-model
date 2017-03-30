package boxfish.commons.web.model.sanitization;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.lang.Thread.State;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import boxfish.commons.web.model.RestModel;

public class SanitizerTest {

    @Test
    public void maps() throws Exception {
        final Map<String, Object> expected = new HashMap<>();
        expected.put("field1", "345827345asdfasdf");

        final Object actual = new Sanitizer(expected).sanitize();

        assertNotEquals(expected, actual);
        assertThat(actual, instanceOf(RestModel.class));

        final RestModel parsed = (RestModel) actual;
        parsed.permit("field_1");
        assertEquals(expected.get("field1"), parsed.get("field_1").asString());
    }

    @Test
    public void json() throws Exception {
        final String expected = "{field1:123.12345454, \"field2\":\"value\"}";

        final Object actual = new Sanitizer(expected).sanitize();

        assertNotEquals(expected, actual);
        assertThat(actual, instanceOf(RestModel.class));

        final RestModel parsed = (RestModel) actual;
        parsed.permit("field_1", "field_2");
        assertEquals(new BigDecimal("123.12345454"), parsed.get("field_1").asBigDecimal());
        assertEquals("value", parsed.get("field_2").asString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void lists() throws Exception {
        final LinkedList<Object> expected = new LinkedList<>();
        final Map<String, Object> subMap = new HashMap<>();
        expected.add("1243123");
        expected.add(subMap);
        subMap.put("field1", "345827345asdfasdf");

        final Object actual = new Sanitizer(expected).sanitize();

        assertNotEquals(expected, actual);
        assertThat(actual, instanceOf(List.class));

        final List<Object> parsed = (List<Object>) actual;
        assertEquals(expected.get(0), parsed.get(0));

        assertThat(parsed.get(1), instanceOf(RestModel.class));
        final RestModel parsedItem = (RestModel) parsed.get(1);
        assertEquals(
            subMap.get("field1"),
            parsedItem.permit("field_1").get("field_1").asString());
    }

    @Test
    public void unaffectedValues() throws Exception {
        final List<Object> unnafected = asList(
            BigDecimal.valueOf(2348.123),
            false,
            (byte) 1234,
            (double) 132948,
            State.RUNNABLE,
            (float) 352.123,
            now(),
            8482539,
            new ArrayList<>(),
            (long) 1234512,
            (short) 3174,
            "strigsy495");

        for (final Object value : unnafected)
            assertEquals(value, new Sanitizer(value).sanitize());
    }
}
