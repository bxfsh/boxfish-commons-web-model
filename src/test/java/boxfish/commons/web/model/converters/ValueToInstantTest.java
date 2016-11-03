package boxfish.commons.web.model.converters;

import static org.junit.Assert.assertEquals;

import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

import boxfish.commons.web.model.Model;

public class ValueToInstantTest {

    @Test
    public void parse_from_string() throws Exception {
        final String expected = "2016-03-21T23:32:59Z";
        assertEquals(Instant.parse(expected), new ValueToInstant(expected).parse());
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_boolean() throws Exception {
        final Boolean expected = true;
        new ValueToInstant(expected).parse();
    }

    @Test
    public void parse_from_byte() throws Exception {
        final byte expected = (byte) 32;
        assertEquals(Instant.ofEpochMilli(32), new ValueToInstant(expected).parse());
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_decimal() throws Exception {
        final BigDecimal expected = new BigDecimal("15.19384");
        new ValueToInstant(expected).parse();
    }

    @Test
    public void parse_from_instant() throws Exception {
        final Instant expected = Instant.now();
        assertEquals(expected, new ValueToInstant(expected).parse());
    }

    @Test
    public void parse_from_short() throws Exception {
        final Short expected = 39;
        assertEquals(Instant.ofEpochMilli(39), new ValueToInstant(expected).parse());
    }

    @Test
    public void parse_from_integer() throws Exception {
        final Integer expected = 33;
        assertEquals(Instant.ofEpochMilli(33), new ValueToInstant(expected).parse());
    }

    @Test
    public void parse_from_long() throws Exception {
        final Long expected = 123l;
        assertEquals(Instant.ofEpochMilli(123), new ValueToInstant(expected).parse());
    }

    @Test(expected = InvalidClassException.class)
    public void parse_from_model() throws Exception {
        final Model expected = new Model()
            .permit("field1", "field2", "field3", "field4")
            .value("field1", "1341234asdasds")
            .value("field2", Long.valueOf(123481))
            .value("field3", new Model().permit("subField1").value("subField1", 1))
            .value("field4", true);
        new ValueToInstant(expected).parse();
    }

}
