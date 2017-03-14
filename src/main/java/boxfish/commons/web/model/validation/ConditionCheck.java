package boxfish.commons.web.model.validation;

import java.util.function.BiFunction;

import boxfish.commons.web.model.RestModel;

/**
 * State instance that is ready to receive and send forwards
 * the validation rule test procedure that will be used in
 * the validation process.
 * 
 * @author Hudson Mendes
 *
 * @param <TValue> the type of the value that will be tested.
 */
public class ConditionCheck<TValue> {
    private final RestModel hashModel;
    private final Class<TValue> valueClass;

    ConditionCheck(final RestModel hashModel, final Class<TValue> valueClass) {
        if (hashModel == null)
            throw new IllegalArgumentException("'hashModel' can't be null.");

        if (valueClass == null)
            throw new IllegalArgumentException("'valueClass' can't be null.");

        this.hashModel = hashModel;
        this.valueClass = valueClass;
    }

    /**
     * Declare now the test through which your field value will
     * have to pass, otherwise causing a validation error.
     * 
     * @param test the check procedure, use Java8 lambdas here to implement the anonymous interface.
     * @return the next state in the condition builder that allows you to create set the error warning.
     */
    public ConditionWarn<TValue> ifValueFailsOn(final BiFunction<RestModel, TValue, Boolean> test) {
        if (test == null)
            throw new IllegalArgumentException("'test' can't be null.");

        return new ConditionWarn<>(hashModel, test, valueClass);
    }
}
