package boxfish.commons.web.model.validation;

import java.util.function.BiFunction;

import boxfish.commons.web.model.Model;

/**
 * The final state of the Condition Builder, responsible for
 * defining the failure message in case the condition has failed
 * and creating the validator that will be included in the list
 * of validators in the Model.
 * 
 * @author Hudson Mendes
 *
 * @param <TValue> the type of the value that will be tested.
 */
public class ConditionWarn<TValue> {
    private final Model hashModel;
    private final Class<TValue> valueClass;
    private final BiFunction<Model, TValue, Boolean> test;
    private String alertMessage;

    ConditionWarn(
            final Model hashModel,
            final BiFunction<Model, TValue, Boolean> test,
            final Class<TValue> valueClass) {
        if (hashModel == null)
            throw new IllegalArgumentException("'hashModel' can't be null.");

        if (valueClass == null)
            throw new IllegalArgumentException("'valueClass' can't be null.");

        this.hashModel = hashModel;
        this.test = test;
        this.valueClass = valueClass;
    }

    /**
     * Allows the definition of the failure message
     * in case the check comes to fail and generates the
     * anonymous interface implementation that will be
     * used as validator when we run the check against
     * the model.
     * 
     * @param alertMessage the error message in case the check fails.
     * @return the validator.
     */
    public Validator warnWith(final String alertMessage) {
        this.alertMessage = alertMessage;
        return makeValidator();
    }

    private Validator makeValidator() {
        return new Validator() {

            @Override
            public String errorMessage() {
                return alertMessage;
            }

            @Override
            public Boolean accepts(final Class<?> clazz) {
                return getValueClass().equals(clazz);
            }

            @Override
            @SuppressWarnings("unchecked")
            public Boolean isValid(final Object value) {
                return getTest().apply(getHashModel(), (TValue) value);
            }

        };
    }

    Model getHashModel() {
        return hashModel;
    }

    Class<TValue> getValueClass() {
        return valueClass;
    }

    BiFunction<Model, TValue, Boolean> getTest() {
        return test;
    }
}
