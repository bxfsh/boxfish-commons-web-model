package boxfish.commons.web.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import boxfish.commons.web.model.Model;

public class ValueToModelTest {

    @Test
    public void parse_from_string() throws Exception {
        final String expected = "true";
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_boolean() throws Exception {
        final Boolean expected = true;
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_byte() throws Exception {
        final byte expected = (byte) 32;
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_decimal() throws Exception {
        final BigDecimal expected = new BigDecimal("12.5793");
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_float() throws Exception {
        final Float expected = new Float("12.5793");
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_double() throws Exception {
        final Double expected = new Double("12.5793");
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_instant() throws Exception {
        final Instant expected = Instant.now();
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_short() throws Exception {
        final Short expected = 79;
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_integer() throws Exception {
        final Integer expected = 38481;
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_long() throws Exception {
        final Long expected = 123l;
        final Model actual = new ValueToModel(expected).parse();
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void parse_from_model() throws Exception {
        final Model expected = new Model()
            .permit("field1", "field2", "field3", "field4")
            .value("field1", "1341234asdasds")
            .value("field2", Long.valueOf(123481))
            .value("field3", new Model().permit("subField1").value("subField1", 1))
            .value("field4", true);
        final Model actual = new ValueToModel(expected).parse();
        assertEquals(expected.get("field_1").asString(), actual.get("field_1").asString());
        assertEquals(expected.get("field_2").asLong(), actual.get("field_2").asLong());
        assertEquals(
            expected.get("field_3").asModel().get("sub_field_1").asInteger(),
            actual.get("field_3").asModel().get("sub_field_1").asInteger());
    }

    @Test(expected = IllegalStateException.class)
    public void parse_from_map() throws Exception {
        final Map<String, Object> subMap = new LinkedHashMap<>();
        subMap.put("subField1", 1);

        final Map<String, Object> expected = new HashMap<>();
        expected.put("field1", "1341234asdasds");
        expected.put("field2", Long.valueOf(123481));
        expected.put("field3", subMap);
        expected.put("field4", true);

        new ValueToModel(expected)
            .parse()
            .permit(
                "field_1",
                "field_2",
                "field_3",
                "field3.subField1",
                "field4");
    }
}
