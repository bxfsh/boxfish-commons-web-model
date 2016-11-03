package boxfish.commons.web.model.validation;

import boxfish.commons.web.model.Model;

/**
 * Allows you to start the first state of the condition
 * building process by setting the type of the field value
 * that will be tested and initialising the check state.
 * 
 * @author Hudson Mendes
 *
 */
public class ConditionFactory {
    private final Model hashModel;

    public ConditionFactory(Model hashModel) {
        this.hashModel = hashModel;
    }

    /**
     * Initialises the first state (check) of the ConditionBuilder
     * defining the type of value that will be later tested.
     * 
     * @param valueClass the type fo the value that will be later checked.
     * @return the next state (check) of the condition builder.
     */
    public <TValue extends Object> ConditionCheck<TValue> forType(Class<TValue> valueClass) {
        return new ConditionCheck<TValue>(hashModel, valueClass);
    }
}
