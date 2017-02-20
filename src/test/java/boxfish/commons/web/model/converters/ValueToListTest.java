package boxfish.commons.web.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import boxfish.commons.web.model.Model;

public class ValueToListTest {
    @Test
    public void parse_from_string() throws Exception {
        final String expected = "true";
        assertEquals(expected, new ValueToList(expected).parse().get(0).asString());
    }

    @Test
    public void parse_from_boolean() throws Exception {
        final Boolean expected = true;
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_byte() throws Exception {
        final byte expected = (byte) 32;
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_decimal() throws Exception {
        final BigDecimal expected = new BigDecimal("12.5793");
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_float() throws Exception {
        final Float expected = new Float("12.5793");
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_double() throws Exception {
        final Double expected = new Double("12.5793");
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_instant() throws Exception {
        final Instant expected = Instant.now();
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_short() throws Exception {
        final Short expected = 79;
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_integer() throws Exception {
        final Integer expected = 38481;
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_long() throws Exception {
        final Long expected = 123l;
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_model() throws Exception {
        final Model expected = new Model()
            .permit("field1", "field2", "field3", "field4")
            .value("field1", "1341234asdasds")
            .value("field2", Long.valueOf(123481))
            .value("field3", new Model().permit("subField1").value("subField1", 1))
            .value("field4", true);
        assertNull(new ValueToList(expected).parse());
    }

    @Test
    public void parse_from_joined_with_semicolon_string() throws Exception {
        final String expected = "item1;item2";
        assertEquals(2, new ValueToList(expected).parse().size());
        assertEquals("item1", new ValueToList(expected).parse().get(0).asString());
        assertEquals("item2", new ValueToList(expected).parse().get(1).asString());
    }

    @Test
    public void parse_from_joined_with_coma_string() throws Exception {
        final String expected = "item1,item2";
        assertEquals(2, new ValueToList(expected).parse().size());
        assertEquals("item1", new ValueToList(expected).parse().get(0).asString());
        assertEquals("item2", new ValueToList(expected).parse().get(1).asString());
    }

    @Test
    public void parse_from_joined_with_semicolon_having_coma_items_string() throws Exception {
        final String expected = "item1,subitem1,subitem2;item2,subitem1";
        assertEquals(2, new ValueToList(expected).parse().size());
        assertEquals("item1,subitem1,subitem2", new ValueToList(expected).parse().get(0).asString());
        assertEquals("item2,subitem1", new ValueToList(expected).parse().get(1).asString());
    }

    @Test
    public void parse_from_array_object() throws Exception {
        final Object[] expected = new Object[] {new Object(), new Object()};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asOriginal());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asOriginal());
    }

    @Test
    public void parse_from_array_string() throws Exception {
        final String[] expected = new String[] {"a", "b"};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asString());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asString());
    }

    @Test
    public void parse_from_array_byte() throws Exception {
        final Byte[] expected = new Byte[] {1, 3};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asByte());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asByte());
    }

    @Test
    public void parse_from_array_short() throws Exception {
        final Short[] expected = new Short[] {1, 3};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asShort());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asShort());
    }

    @Test
    public void parse_from_array_integer() throws Exception {
        final Integer[] expected = new Integer[] {1, 3};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asInteger());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asInteger());
    }

    @Test
    public void parse_from_array_long() throws Exception {
        final Long[] expected = new Long[] {1l, 3l};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asLong());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asLong());
    }

    @Test
    public void parse_from_array_decimal() throws Exception {
        final BigDecimal[] expected = new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ONE};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asBigDecimal());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asBigDecimal());
    }

    @Test
    public void parse_from_array_float() throws Exception {
        final Float[] expected = new Float[] {1.1f, 1.3f};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asFloat());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asFloat());
    }

    @Test
    public void parse_from_array_double() throws Exception {
        final Double[] expected = new Double[] {1d, 3d};
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0], new ValueToList(expected).parse().get(0).asDouble());
        assertEquals(expected[1], new ValueToList(expected).parse().get(1).asDouble());
    }

    @Test
    public void parse_from_array_model() throws Exception {
        final Model[] expected = new Model[] {
            Model.create().permit("a").value("a", 1),
            Model.create().permit("b").value("b", 2)
        };
        assertEquals(expected.length, new ValueToList(expected).parse().size());
        assertEquals(expected[0].get("a").asInteger(), new ValueToList(expected).parse().get(0).asModel().get("a").asInteger());
        assertEquals(expected[1].get("b").asInteger(), new ValueToList(expected).parse().get(1).asModel().get("b").asInteger());
    }

    @Test
    public void parse_from_list() throws Exception {
        final List<BigDecimal> expected = new ArrayList<>();
        expected.add(BigDecimal.ZERO);
        expected.add(BigDecimal.ONE);
        assertEquals(expected.size(), new ValueToList(expected).parse().size());
        assertEquals(expected.get(0), new ValueToList(expected).parse().get(0).asBigDecimal());
        assertEquals(expected.get(1), new ValueToList(expected).parse().get(1).asBigDecimal());
    }
}
