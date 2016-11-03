package boxfish.commons.web.model.converters;

import static org.junit.Assert.assertEquals;

import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

import boxfish.commons.web.model.Model;

public class ValueToBooleanTest {

    @Test
    public void parse_from_string() throws Exception {
        final String expected = "true";
        assertEquals(Boolean.valueOf(expected), new ValueToBoolean(expected).parse());
    }

    @Test
    public void parse_from_boolean() throws Exception {
        final Boolean expected = true;
        assertEquals(expected, new ValueToBoolean(expected).parse());
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_byte() throws Exception {
        final byte expected = (byte) 32;
        new ValueToBoolean(expected).parse();
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_decimal() throws Exception {
        final BigDecimal expected = new BigDecimal("12.5793");
        new ValueToBoolean(expected).parse();
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_instant() throws Exception {
        final Instant expected = Instant.now();
        new ValueToBoolean(expected).parse();
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_short() throws Exception {
        final Short expected = 79;
        new ValueToBoolean(expected).parse();
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_integer() throws Exception {
        final Integer expected = 38481;
        new ValueToBoolean(expected).parse();
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_long() throws Exception {
        final Long expected = 123l;
        new ValueToBoolean(expected).parse();
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_model() throws Exception {
        final Model expected = new Model()
            .permit("field1", "field2", "field3", "field4")
            .value("field1", "1341234asdasds")
            .value("field2", Long.valueOf(123481))
            .value("field3", new Model().permit("subField1").value("subField1", 1))
            .value("field4", true);
        new ValueToBoolean(expected).parse();
    }

}
