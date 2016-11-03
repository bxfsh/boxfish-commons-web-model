package boxfish.commons.web.model.converters;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

import boxfish.commons.web.model.Model;

public class ValueToStringTest {

    @Test
    public void parse_from_string() throws Exception {
        final String expected = "expected";
        assertEquals(expected, new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_boolean() throws Exception {
        final Boolean expected = true;
        assertEquals("true", new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_byte() throws Exception {
        final byte expected = (byte) 32;
        assertEquals("32", new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_decimal() throws Exception {
        final BigDecimal expected = new BigDecimal("12.5793");
        assertEquals("12.5793", new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_instant() throws Exception {
        final Instant expected = Instant.now();
        assertEquals(expected.toString(), new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_short() throws Exception {
        final Short expected = 79;
        assertEquals("79", new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_integer() throws Exception {
        final Integer expected = 38481;
        assertEquals("38481", new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_long() throws Exception {
        final Long expected = 3848149525l;
        assertEquals("3848149525", new ValueToString(expected).parse());
    }

    @Test
    public void parse_from_model() throws Exception {
        final Model expected = new Model()
            .permit("field1", "field2", "field3")
            .value("field1", "1341234asdasds")
            .value("field2", Long.valueOf(123481))
            .value("field3", new Model().permit("subField1").value("subField1", 1))
            .value("field4", true);
        assertEquals("[field_1=1341234asdasds;field_2=123481;field_3=[sub_field_1=1]]", new ValueToString(expected).parse());
    }

}
