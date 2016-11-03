package boxfish.commons.web.model;

import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import boxfish.commons.web.model.validation.ModelError;
import boxfish.commons.web.model.validation.ModelErrors;
import boxfish.commons.web.model.validation.ValidationListener;

public class ModelTest {
    private static final String FIELD_NAME = "fieldName";
    private static final BigDecimal FIELD_VALUE = new BigDecimal("9405249.592");
    private Model model;

    @Before
    public void setup() {
        model = new Model();
    }

    @Test
    public void create() {
        final Model actual = Model.create();
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void permit() throws Exception {
        model.value(FIELD_NAME, FIELD_VALUE);
        assertNull(model.get(FIELD_NAME));
        model.permit(FIELD_NAME);
        assertNotNull(model.get(FIELD_NAME));
        assertFalse(model.get(FIELD_NAME).isNull());
        assertEquals(FIELD_VALUE, model.get(FIELD_NAME).asBigDecimal());
    }

    @Test
    public void isValid_required() {
        model.require(FIELD_NAME);
        assertFalse(model.isValid());
        model.value(FIELD_NAME, FIELD_VALUE);
        assertTrue(model.isValid());
    }

    @Test
    public void isValid_rules() {
        model.value(FIELD_NAME, FIELD_VALUE);
        model.rules(
            FIELD_NAME,
            condition -> condition
                .forType(BigDecimal.class)
                .ifValueFailsOn((all, v) -> v.compareTo(ZERO) < 0)
                .warnWith("The 'fieldName' must be smaller than ZERO"));
        assertFalse(model.isValid());
        model.value(FIELD_NAME, -1);
        assertTrue(model.isValid());
    }

    @Test
    public void value() throws Exception {
        model.permit("any").value("any", 1285);
        assertEquals(1285, model.get("any").asInteger().intValue());
    }

    @Test
    public void errors_required() throws Exception {
        model
            .permit("field1", "field5", "field6")
            .require("field2", "field3", "field4");

        model
            .value("field1", 1)
            .value("field3", null)
            .value("field4", 12)
            .value("field5", null)
            .value("field6", 1)
            .errors();

        final ModelErrors errors = model.errors();
        assertEquals(2, errors.size().intValue());

        final ModelError error1 = errors.get(0);
        assertEquals("field_2", error1.getFieldName());
        assertEquals("The 'field_2' is required", error1.getErrorMessage());

        final ModelError error2 = errors.get(1);
        assertEquals("field_3", error2.getFieldName());
        assertEquals("The 'field_3' is required", error2.getErrorMessage());
    }

    @Test
    public void errors_rules() throws Exception {
        final ValidationListener<Integer> smallerThan10Rule = condition -> condition
            .forType(Integer.class)
            .ifValueFailsOn((all, v) -> v >= 10)
            .warnWith("The value must be smaller than 10");

        model
            .permit("field1", "field5", "field6")
            .rules("field1", smallerThan10Rule)
            .rules("field2", smallerThan10Rule)
            .rules("field3", smallerThan10Rule)
            .rules("field4", smallerThan10Rule)
            .rules("field5", smallerThan10Rule)
            .rules("field6", smallerThan10Rule);

        model
            .value("field1", 1)
            .value("field3", null)
            .value("field4", 12)
            .value("field5", null)
            .value("field6", 1)
            .errors();

        final ModelErrors errors = model.errors();
        assertEquals(2, errors.size().intValue());

        final ModelError error1 = errors.get(0);
        assertEquals("field_1", error1.getFieldName());
        assertEquals("The value must be smaller than 10", error1.getErrorMessage());

        final ModelError error2 = errors.get(1);
        assertEquals("field_6", error2.getFieldName());
        assertEquals("The value must be smaller than 10", error2.getErrorMessage());
    }

    @Test
    public void get() throws Exception {
        model.permit("field___2").value("field2", "4i5n245345");
        assertEquals("4i5n245345", model.get("field_2").asString());
    }

    @Test
    public void clear() {
        model.permit("field1").value("field1", 495);
        assertEquals(1, model.entrySet().size());
        model.clear();
        assertEquals(0, model.entrySet().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void containsValue() {
        model.containsValue("");
    }

    @Test
    public void containsKey() {
        model.permit("field_1").value("field1", "4i5n245345");
        assertTrue(model.containsKey("field_1"));
    }

    @Test
    public void entrySet() {
        assertEquals(0, model.entrySet().size());
        model.value("field1", "4i5n245345");
        assertEquals(0, model.entrySet().size());
        model.permit("field_1");
        assertEquals(1, model.entrySet().size());
        model.remove("field_1");
        assertEquals(0, model.entrySet().size());
    }

    @Test
    public void isEmpty() {
        assertTrue(model.isEmpty());
        model.permit("field1").value("field1", 495);
        assertFalse(model.isEmpty());
        model.clear();
        assertTrue(model.isEmpty());
    }

    @Test
    public void keySet() {
        model
            .permit("field1")
            .value("field1", 495)
            .value("field2", 1000);
        assertEquals(1, model.keySet().size());
        assertEquals("field_1", model.keySet().stream().collect(Collectors.toList()).get(0));
    }

    @Test
    public void put() throws Exception {
        model.permit("field1").value("field1", 495);
        assertEquals(495, model.get("field_1").asInteger().intValue());
        model.put("field_1", 819381);
        assertEquals(819381, model.get("field_1").asInteger().intValue());
    }

    @Test
    public void putAll() throws Exception {
        model.permit("field1", "field2");

        final Map<String, Object> map = new HashMap<>();
        map.put("field1", 58545);
        map.put("field2", new BigDecimal("135.34"));
        model.putAll(map);

        assertEquals(Integer.valueOf(58545), model.get("field_1").asInteger());
        assertEquals(new BigDecimal("135.34"), model.get("field_2").asBigDecimal());
    }

    @Test
    public void remove() {
        model.permit("field1").value("field1", 495);
        assertEquals(1, model.entrySet().size());
        model.remove("field_1");
        assertEquals(0, model.entrySet().size());
    }

    @Test
    public void size() {
        model
            .permit("field1")
            .value("field1", 495)
            .value("field2", "945725");
        assertEquals(1, model.entrySet().size());
        model.permit("field2");
        assertEquals(2, model.entrySet().size());
        model.permit("field3");
        assertEquals(2, model.entrySet().size());
        model.remove("field_1");
        assertEquals(1, model.entrySet().size());
    }

    @Test
    public void values() {
        model
            .permit("field1")
            .value("field1", 495)
            .value("field2", "945725");

        assertEquals(1, model.values().size());
        model.permit("field2");
        assertEquals(2, model.values().size());

        List<Object> values = model.values().stream().collect(Collectors.toList());
        assertEquals(495, values.get(0));
        assertEquals("945725", values.get(1));
    }
}