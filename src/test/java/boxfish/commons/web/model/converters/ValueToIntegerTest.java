package boxfish.commons.web.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

import boxfish.commons.web.model.RestModel;

public class ValueToIntegerTest {

    @Test
    public void parse_from_string() throws Exception {
        final String expected = "12345";
        assertEquals(Integer.valueOf("12345"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_boolean() throws Exception {
        final Boolean expected = true;
        assertNull(new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_byte() throws Exception {
        final byte expected = (byte) 32;
        assertEquals(Integer.valueOf("32"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_decimal() throws Exception {
        final BigDecimal expected = new BigDecimal("12.5793");
        assertEquals(Integer.valueOf("12"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_float() throws Exception {
        final Float expected = Float.valueOf("15.19384");
        assertEquals(Integer.valueOf("15"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_double() throws Exception {
        final Double expected = Double.valueOf("15.19384");
        assertEquals(Integer.valueOf("15"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_instant() throws Exception {
        final Instant expected = Instant.now();
        assertNull(new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_short() throws Exception {
        final Short expected = 79;
        assertEquals(Integer.valueOf("79"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_integer() throws Exception {
        final Integer expected = 38481;
        assertEquals(Integer.valueOf("38481"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_long() throws Exception {
        final Long expected = 123l;
        assertEquals(Integer.valueOf("123"), new ValueToInteger(expected).parse());
    }

    @Test
    public void parse_from_model() throws Exception {
        final RestModel expected = new RestModel()
            .permit("field1", "field2", "field3", "field4")
            .value("field1", "1341234asdasds")
            .value("field2", Long.valueOf(123481))
            .value("field3", new RestModel().permit("subField1").value("subField1", 1))
            .value("field4", true);
        assertNull(new ValueToInteger(expected).parse());
    }

}
