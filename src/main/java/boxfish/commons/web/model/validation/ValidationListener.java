package boxfish.commons.web.model.validation;

/**
 * Interface used for lambda anonymous implementation
 * of callables that will produce the condition used
 * as a rule validator.
 * 
 * @author Hudson Mendes
 *
 * @param <TValue> the type of the value that will che checked.
 */
public interface ValidationListener<TValue> {
    public Validator produce(ConditionFactory condition);
}
