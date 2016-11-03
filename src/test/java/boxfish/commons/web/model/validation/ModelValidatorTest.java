package boxfish.commons.web.model.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import boxfish.commons.web.model.Model;
import boxfish.commons.web.model.Value;

public class ModelValidatorTest {

    private Model model;
    private Value value;
    private List<String> requireds;
    private Map<String, List<Validator>> rules;
    private ModelValidator validator;

    @Before
    public void setup() {
        model = mock(Model.class);
        value = mock(Value.class);
        requireds = new ArrayList<>();
        rules = new HashMap<>();
    }

    @Test
    public void isValid() {
        final String field = "field_name";

        requireds.add(field);
        validator = new ModelValidator(model, requireds, rules);
        assertFalse(validator.isValid());

        when(value.asOriginal()).thenReturn(35);
        when(model.containsKey(field)).thenReturn(true);
        when(model.get(field)).thenReturn(value);
        validator = new ModelValidator(model, requireds, rules);
        assertTrue(validator.isValid());
    }

    @Test
    public void validate() throws Exception {
        final String field = "field_name";

        requireds.add(field);
        validator = new ModelValidator(model, requireds, rules);
        ModelErrors errors = validator.validate();
        assertEquals(1, errors.size().intValue());

        when(value.asOriginal()).thenReturn(35);
        when(model.containsKey(field)).thenReturn(true);
        when(model.get(field)).thenReturn(value);
        validator = new ModelValidator(model, requireds, rules);
        errors = validator.validate();
        assertEquals(0, errors.size().intValue());
    }

}
