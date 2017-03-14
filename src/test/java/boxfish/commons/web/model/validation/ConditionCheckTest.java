package boxfish.commons.web.model.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.function.BiFunction;

import org.junit.Before;
import org.junit.Test;

import boxfish.commons.web.model.RestModel;

public class ConditionCheckTest {

    private RestModel model;
    private ConditionCheck<Instant> condition;

    @Before
    public void setup() {
        model = mock(RestModel.class);
        condition = new ConditionCheck<>(model, Instant.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_nullModel() {
        new ConditionCheck<>(null, Instant.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_nullClassType() {
        new ConditionCheck<>(model, null);
    }

    @Test
    public void failsOn() {
        final BiFunction<RestModel, Instant, Boolean> test = (all, v) -> false;
        final ConditionWarn<Instant> actual = condition.ifValueFailsOn(test);
        assertNotNull(actual);
        assertEquals(model, actual.getHashModel());
        assertEquals(test, actual.getTest());
        assertEquals(Instant.class, actual.getValueClass());
    }
}
