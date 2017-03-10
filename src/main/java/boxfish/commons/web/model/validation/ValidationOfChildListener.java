package boxfish.commons.web.model.validation;

import boxfish.commons.web.model.Value;

/**
 * Interface used for lambda anonymous implementation
 * of callables that will produce the condition used
 * as a rule validator of childreen of list fields,
 * that will be inexorably type Value.
 * 
 * @author Hudson Mendes
 *
 */
public interface ValidationOfChildListener {
    public Validator produce(ConditionCheck<Value> condition);
}
