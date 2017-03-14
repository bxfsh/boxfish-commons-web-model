package boxfish.commons.web.model.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import boxfish.commons.web.model.RestModel;
import boxfish.commons.web.model.RestValue;

/**
 * The logic that validates models using requirements and rules.
 * This class is completely stateless and can only be used against
 * a snapshot of model, requiring to be recreated whenever a new
 * check is going to be performed.
 *
 * @author Hudson Mendes
 *
 */
public class ModelValidator {
    private static final String REQUIRED_MESSAGE = "The '%s' is required";

    private final RestModel hashModel;
    private final List<String> requireds = new ArrayList<>();
    private final Map<String, List<Validator>> rules = new HashMap<>();
    private final Map<String, List<Validator>> childreenRules = new HashMap<>();

    public ModelValidator(
            final RestModel hashModel,
            final List<String> requireds,
            final Map<String, List<Validator>> validators,
            final Map<String, List<Validator>> childreenRules) {

        if (hashModel == null)
            throw new IllegalArgumentException("'hashModel' can't be null.");

        this.hashModel = hashModel;

        if (requireds != null && !requireds.isEmpty())
            this.requireds.addAll(requireds.stream().distinct().collect(Collectors.toList()));

        if (validators != null && !validators.isEmpty())
            rules.putAll(validators);

        if (childreenRules != null && !childreenRules.isEmpty())
            this.childreenRules.putAll(childreenRules);
    }

    /**
     * Checks and returns if the model is valid
     * against the rules or not.
     *
     * @return false if there are errors; true otherwise.
     */
    public Boolean isValid() {
        try {
            return attemptIsValid();
        }
        catch (final Exception e) {
            return false;
        }
    }

    private Boolean attemptIsValid() throws Exception {
        final ModelErrors result = validate();
        return !result.hasErrors();
    }

    /**
     * Checks and retrieves the list of model errors
     * occurred when validated against the requirements
     * and against the rules.
     *
     * @return the collection of errors.
     * @throws Exception in case the validation fails to run rules.
     */
    public ModelErrors validate() throws Exception {
        final ModelErrors errors = new ModelErrors();
        for (final String required : requireds)
            if (!hashModel.containsKey(required) || hashModel.get(required).isNull())
                errors.addError(required, String.format(REQUIRED_MESSAGE, required));

        for (final String ruleField : rules.keySet())
            validationRules(errors, ruleField, rules);

        for (final String ruleField : childreenRules.keySet())
            validationRulesOnChildreenOf(errors, ruleField, childreenRules);

        return errors;
    }

    private void validationRules(
            final ModelErrors errors,
            final String ruleField,
            final Map<String, List<Validator>> validators) {
        if (hashModel.containsKey(ruleField)) {
            final RestValue value = hashModel.get(ruleField);

            for (final Validator validator : validators.get(ruleField))
                if (validator.accepts(value.getValueClass()))
                    if (!validator.isValid(value.asOriginal()))
                        errors.addError(ruleField, validator.errorMessage());
        }
    }

    private void validationRulesOnChildreenOf(
            final ModelErrors errors,
            final String ruleField,
            final Map<String, List<Validator>> validators) {
        if (hashModel.containsKey(ruleField)) {
            final RestValue value = hashModel.get(ruleField);
            final List<RestValue> childreen = value.asList();
            if (childreen != null) {
                for (final RestValue childValue : childreen)
                    for (final Validator validator : validators.get(ruleField))
                        if (!validator.isValid(childValue))
                            errors.addError(ruleField, validator.errorMessage());
            }
        }
    }
}
