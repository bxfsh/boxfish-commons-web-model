package boxfish.commons.web.model.sanitization;

import static java.time.Instant.now;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.lang.Thread.State;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import boxfish.commons.web.model.RestModel;

public class SanitizerForListsTest {

    @Test
    public void sanitize_map() {
        final Map<String, Object> expected = new HashMap<>();
        expected.put("field1", "345827345asdfasdf");
        expected.put("field2", BigDecimal.valueOf(123451785.45));
        expected.put("field3", BigDecimal.valueOf(123451785.45));
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_list_withoutMaps() {
        final List<Object> expected = new ArrayList<>();
        expected.add("345827345asdfasdf");
        expected.add(BigDecimal.valueOf(123451785.45));
        expected.add(BigDecimal.valueOf(123451785.45));
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void sanitize_list_withMaps() {
        Map<String, String> translatableMap = new HashMap<>();
        translatableMap.put("test1", "2304572385h");
        translatableMap.put("test_2", BigDecimal.valueOf(12394851.134).toString());

        final List<Object> unexpected = new ArrayList<>();
        unexpected.add("345827345asdfasdf");
        unexpected.add(BigDecimal.valueOf(123451785.45));
        unexpected.add(RestModel.newRestModel().permit("name").value("name", "au8345h2345"));
        unexpected.add(translatableMap);

        List<Object> actual = (List<Object>) new SanitizerForLists(unexpected).sanitize();
        assertNotEquals(unexpected, actual);

        assertThat(actual.get(0), instanceOf(String.class));
        assertThat(actual.get(1), instanceOf(BigDecimal.class));
        assertThat(actual.get(2), instanceOf(RestModel.class));
        assertThat(actual.get(3), instanceOf(RestModel.class));

        RestModel parsedModel = (RestModel) actual.get(3);
        parsedModel.permit("test_1", "test2");
        assertEquals(translatableMap.get("test1"), parsedModel.get("test_1").asString());
        assertEquals(translatableMap.get("test_2"), parsedModel.get("test_2").asString());
    }

    @Test
    public void sanitize_bigDecimal() {
        final BigDecimal expected = BigDecimal.valueOf(2349582345.23458);
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_boolean() {
        final Boolean expected = true;
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_byte() {
        final Byte expected = Byte.valueOf((byte) 134);
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_double() {
        final Double expected = Double.valueOf(134.14);
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_enum() {
        final State expected = State.RUNNABLE;
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_float() {
        final Float expected = Float.valueOf(134.21f);
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_instant() {
        final Instant expected = now();
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_integer() {
        final Integer expected = Integer.valueOf(23495);
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_model() {
        final RestModel expected = RestModel.newRestModel();
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_short() {
        final Short expected = Short.valueOf((short) 12);
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }

    @Test
    public void sanitize_string() {
        final String expected = "whatever-2345";
        assertEquals(expected, new SanitizerForLists(expected).sanitize());
    }
}
