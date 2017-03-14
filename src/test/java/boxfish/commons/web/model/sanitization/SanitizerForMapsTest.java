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

public class SanitizerForMapsTest {

    @Test
    public void sanitize_map() {
        final Map<String, Object> expected = new HashMap<>();
        expected.put("field1", "345827345asdfasdf");
        expected.put("field2", BigDecimal.valueOf(123451785.45));
        expected.put("field3", now());

        Object actual = new SanitizerForMaps(expected).sanitize();
        assertNotEquals(expected, actual);
        assertThat(actual, instanceOf(RestModel.class));

        final RestModel parsed = (RestModel) actual;
        parsed.permit("field_1", "field_2", "field_3");
        assertEquals(expected.get("field1"), parsed.get("field_1").asString());
        assertEquals(expected.get("field2"), parsed.get("field_2").asBigDecimal());
        assertEquals(expected.get("field3"), parsed.get("field_3").asInstant());
    }

    @Test
    public void sanitize_list_withoutMaps() {
        final List<Object> expected = new ArrayList<>();
        expected.add("345827345asdfasdf");
        expected.add(BigDecimal.valueOf(123451785.45));
        expected.add(BigDecimal.valueOf(123451785.45));
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_bigDecimal() {
        final BigDecimal expected = BigDecimal.valueOf(2349582345.23458);
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_boolean() {
        final Boolean expected = true;
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_byte() {
        final Byte expected = Byte.valueOf((byte) 134);
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_double() {
        final Double expected = Double.valueOf(134.14);
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_enum() {
        final State expected = State.RUNNABLE;
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_float() {
        final Float expected = Float.valueOf(134.21f);
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_instant() {
        final Instant expected = now();
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_integer() {
        final Integer expected = Integer.valueOf(23495);
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_model() {
        final RestModel expected = RestModel.newRestModel();
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_short() {
        final Short expected = Short.valueOf((short) 12);
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }

    @Test
    public void sanitize_string() {
        final String expected = "whatever-2345";
        assertEquals(expected, new SanitizerForMaps(expected).sanitize());
    }
}
