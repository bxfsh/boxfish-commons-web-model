package boxfish.commons.web.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

public class ValueTest {

    @Test
    public void asString() throws Exception {
        String expected = "text";
        assertEquals(expected, new Value("text").asString());
    }

    @Test
    public void asLong() throws Exception {
        long expected = 12l;
        assertEquals(expected, new Value(12l).asLong().longValue());
    }

    @Test
    public void asInteger() throws Exception {
        int expected = 15;
        assertEquals(expected, new Value(expected).asInteger().intValue());
    }

    @Test
    public void asShort() throws Exception {
        short expected = (short) 77;
        assertEquals(expected, new Value(expected).asShort().shortValue());
    }

    @Test
    public void asByte() throws Exception {
        byte expected = (byte) 245;
        assertEquals(expected, new Value(expected).asByte().byteValue());
    }

    @Test
    public void asBoolean() throws Exception {
        boolean expected = true;
        assertEquals(expected, new Value(expected).asBoolean().booleanValue());
    }

    @Test
    public void asBigDecimal() throws Exception {
        BigDecimal expected = new BigDecimal("123.34");
        assertEquals(expected, new Value(expected).asBigDecimal());
    }

    @Test
    public void asFloat() throws Exception {
        Float expected = new Float("12.3");
        assertEquals(expected, new Value(expected).asFloat());
    }

    @Test
    public void asDouble() throws Exception {
        Double expected = new Double("12.3");
        assertEquals(expected, new Value(expected).asDouble());
    }

    @Test
    public void asInstant() throws Exception {
        Instant expected = Instant.now();
        assertEquals(expected, new Value(expected).asInstant());
    }

    @Test
    public void asModel() throws Exception {
        Model expected = new Model().permit("field1").value("field1", "test");
        assertEquals(
            expected.get("field1").asString(),
            new Value(expected).asModel().get("field1").asString());
    }

    @Test
    public void asOriginal() {
        BigDecimal expected = new BigDecimal("12.3");
        assertEquals(expected, new Value(expected).asOriginal());
    }

    @Test
    public void getValueClass() throws Exception {
        BigDecimal originalDecimal = new BigDecimal("12.3");
        assertEquals(BigDecimal.class, new Value(originalDecimal).getValueClass());
        String originalString = "4525j4";
        assertEquals(String.class, new Value(originalString).getValueClass());
    }

    @Test
    public void isNull() {
        assertTrue(new Value((BigDecimal) null).isNull());
        assertFalse(new Value(BigDecimal.ONE).isNull());
    }

}
