package boxfish.commons.web.model;

import static java.lang.Thread.State.BLOCKED;
import static java.time.Instant.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.Thread.State;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Triplet;
import org.junit.Test;

public class RestValueTest {
    @Test
    public void asString() throws Exception {
        final String expected = "text";
        assertEquals(expected, new RestValue("text").asString());
    }

    @Test
    public void asLong() throws Exception {
        final long expected = 12l;
        assertEquals(expected, new RestValue(12l).asLong().longValue());
    }

    @Test
    public void asInteger() throws Exception {
        final int expected = 15;
        assertEquals(expected, new RestValue(expected).asInteger().intValue());
    }

    @Test
    public void asShort() throws Exception {
        final short expected = (short) 77;
        assertEquals(expected, new RestValue(expected).asShort().shortValue());
    }

    @Test
    public void asByte() throws Exception {
        final byte expected = (byte) 245;
        assertEquals(expected, new RestValue(expected).asByte().byteValue());
    }

    @Test
    public void asBoolean() throws Exception {
        final boolean expected = true;
        assertEquals(expected, new RestValue(expected).asBoolean().booleanValue());
    }

    @Test
    public void asBigDecimal() throws Exception {
        final BigDecimal expected = new BigDecimal("123.34");
        assertEquals(expected, new RestValue(expected).asBigDecimal());
    }

    @Test
    public void asFloat() throws Exception {
        final Float expected = new Float("12.3");
        assertEquals(expected, new RestValue(expected).asFloat());
    }

    @Test
    public void asDouble() throws Exception {
        final Double expected = new Double("12.3");
        assertEquals(expected, new RestValue(expected).asDouble());
    }

    @Test
    public void asInstant() throws Exception {
        final Instant expected = Instant.now();
        assertEquals(expected, new RestValue(expected).asInstant());
    }

    @Test
    public void asList() throws Exception {
        final Instant now = now();
        final RestModel restModel = RestModel.newRestModel();
        final List<Triplet<Class<?>, Object, Object>> fixtures = new ArrayList<>();
        fixtures.add(new Triplet<>(Boolean.class, Arrays.asList(true), Arrays.asList(true)));
        fixtures.add(new Triplet<>(Byte.class, Arrays.asList((byte) 1), Arrays.asList((byte) 1)));
        fixtures.add(new Triplet<>(Short.class, Arrays.asList((short) 1), Arrays.asList((short) 1)));
        fixtures.add(new Triplet<>(Integer.class, Arrays.asList(1), Arrays.asList(1)));
        fixtures.add(new Triplet<>(Long.class, Arrays.asList(1l), Arrays.asList(1l)));
        fixtures.add(new Triplet<>(String.class, Arrays.asList("name"), Arrays.asList("name")));
        fixtures.add(new Triplet<>(State.class, Arrays.asList("BLOCKED"), Arrays.asList(BLOCKED)));
        fixtures.add(new Triplet<>(Instant.class, Arrays.asList(now), Arrays.asList(now)));
        fixtures.add(new Triplet<>(RestModel.class, Arrays.asList(restModel), Arrays.asList(restModel)));
        for (int i = 0; i < fixtures.size(); i++) {
            final Triplet<Class<?>, Object, Object> fixture = fixtures.get(i);
            final Class<?> type = fixture.getValue0();
            final Object input = fixture.getValue1();
            final Object expected = fixture.getValue2();
            final List<?> output = new RestValue(input).asListOf(type);
            assertEquals(expected, output);
        }
    }

    @Test
    public void asModel() throws Exception {
        final RestModel expected = new RestModel().permit("field1").value("field1", "test");
        assertEquals(
            expected.get("field1").asString(),
            new RestValue(expected).asModel().get("field1").asString());
    }

    @Test
    public void asEnum() throws Exception {
        final State expected = State.BLOCKED;
        assertEquals(expected, new RestValue("BLOCKED").asEnum(State.class));
    }

    @Test
    public void asOriginal() {
        final BigDecimal expected = new BigDecimal("12.3");
        assertEquals(expected, new RestValue(expected).asOriginal());
    }

    @Test
    public void getValueClass() throws Exception {
        final BigDecimal originalDecimal = new BigDecimal("12.3");
        assertEquals(BigDecimal.class, new RestValue(originalDecimal).getValueClass());
        final String originalString = "4525j4";
        assertEquals(String.class, new RestValue(originalString).getValueClass());
    }

    @Test
    public void isNull() {
        assertTrue(new RestValue((BigDecimal) null).isNull());
        assertFalse(new RestValue(BigDecimal.ONE).isNull());
    }

}
