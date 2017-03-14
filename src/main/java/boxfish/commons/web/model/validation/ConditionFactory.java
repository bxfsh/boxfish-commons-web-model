package boxfish.commons.web.model.validation;

import boxfish.commons.web.model.RestModel;

/**
 * Allows you to start the first state of the condition
 * building process by setting the type of the field value
 * that will be tested and initialising the check state.
 * 
 * @author Hudson Mendes
 *
 */
public class ConditionFactory {
    private final RestModel hashModel;

    public ConditionFactory(RestModel hashModel) {
        this.hashModel = hashModel;
    }

    /**
     * Initialises the first state (check) of the ConditionBuilder
     * defining the type of value that will be later tested.
     * 
     * @param valueClass the type fo the value that will be later checked.
     * @param <TValue> The type of the value that will be checked.
     * @return the next state (check) of the condition builder.
     */
    public <TValue extends Object> ConditionCheck<TValue> forType(Class<TValue> valueClass) {
        return new ConditionCheck<TValue>(hashModel, valueClass);
    }
}
