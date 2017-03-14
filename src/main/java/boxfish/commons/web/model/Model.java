package boxfish.commons.web.model;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import boxfish.commons.web.model.sanitization.Sanitizer;
import boxfish.commons.web.model.validation.ConditionCheck;
import boxfish.commons.web.model.validation.ConditionFactory;
import boxfish.commons.web.model.validation.ModelErrors;
import boxfish.commons.web.model.validation.ModelValidator;
import boxfish.commons.web.model.validation.ValidationListener;
import boxfish.commons.web.model.validation.ValidationOfChildListener;
import boxfish.commons.web.model.validation.Validator;

/**
 * The Model used to be bound from any external input
 * being RequestBody or QueryString, which will have
 * all its field names standardised. It allows you to
 * scrutinize the input (permitting, requiring, defining
 * rules) for the input and that will only provide
 * permitted fields to get in, avoinding overwritting of
 * data that is not supposed to be ovewritten.
 *
 * @author Hudson Mendes
 *
 */
public class Model implements Map<String, Object> {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String FIELD_LEVEL_SEPARATOR = "\\.";

    /**
     * Inline creates a new instance of Model.
     *
     * @return newly created instance of Model.
     */
    public static Model create() {
        return new Model();
    }

    /**
     * Inline creates a new instance of model and
     * populates it with the data received from any compatible map.
     *
     * @param input the map that will be loaded.
     * @return the model created.
     */
    public static Model from(final Map<String, Object> input) {
        final Model created = create();
        created.putAll(input);
        return created;
    }

    private final List<String> permitteds = new ArrayList<>();
    private final List<String> requireds = new ArrayList<>();
    private final Map<String, Object> data = new LinkedHashMap<>();
    private final Map<String, Object> baseline = new LinkedHashMap<>();
    private final Map<String, List<Validator>> rules = new ConcurrentHashMap<>();
    private final Map<String, List<Validator>> childreenRules = new ConcurrentHashMap<>();

    /**
     * Permit a field_name to ever be retrieved.
     * If you do NOT permit the field, it will not appear
     * as part of the model and will always return as null.
     *
     * @param fields the list of fields which are allowed to even appear as part of the map.
     * @return self
     */
    public Model permit(final String... fields) {
        if (fields != null && fields.length != 0)
            for (final String field : fields) {
                final List<String> fieldAndSubFields = asList(field.split(FIELD_LEVEL_SEPARATOR));

                if (!fieldAndSubFields.isEmpty()) {
                    final String fieldOfThisLevel = fieldAndSubFields.get(0);

                    final String treated = key(fieldOfThisLevel);
                    if (!permitteds.contains(treated))
                        permitteds.add(treated);

                    if (fieldAndSubFields.size() > 1) {
                        final String fieldOfDownwardLevels = fieldAndSubFields.stream().skip(1).collect(joining(FIELD_LEVEL_SEPARATOR));
                        Value nextLevelValue = get(fieldOfThisLevel);
                        if (nextLevelValue.isNull()) {
                            nextLevelValue = new Value(Model.create());
                            value(fieldOfThisLevel, nextLevelValue);
                        }

                        final Model nextLevelModel = nextLevelValue.asModel();
                        if (nextLevelModel != null)
                            nextLevelModel.permit(fieldOfDownwardLevels);
                    }
                }
            }
        return this;
    }

    /**
     * Make a particular field required, causing isValid and
     * errors to return validation failures and helping the
     * exposition of validation errors to the external world.
     *
     * @param fields which are going to be required.
     * @return self
     */
    public Model require(final String... fields) {
        if (fields != null && fields.length != 0)
            for (final String field : fields) {
                final List<String> fieldAndSubFields = asList(field.split(FIELD_LEVEL_SEPARATOR));

                if (!fieldAndSubFields.isEmpty()) {
                    final String fieldOfThisLevel = fieldAndSubFields.get(0);

                    final String treated = key(fieldOfThisLevel);
                    if (!requireds.contains(treated)) {
                        requireds.add(treated);
                        permitteds.add(treated);
                    }

                    if (fieldAndSubFields.size() > 1) {
                        final String fieldOfDownwardLevels = fieldAndSubFields.stream().skip(1).collect(joining(FIELD_LEVEL_SEPARATOR));
                        Value nextLevelValue = get(fieldOfThisLevel);
                        if (nextLevelValue.isNull()) {
                            nextLevelValue = new Value(Model.create());
                            value(fieldOfThisLevel, nextLevelValue);
                        }

                        final Model nextLevelModel = nextLevelValue.asModel();
                        if (nextLevelModel != null)
                            nextLevelModel.require(fieldOfDownwardLevels);
                    }
                }
            }
        return this;
    }

    /**
     * Define validation rules used to consider the input valid.
     * If it fails to apply any of this rules, it allows the errors
     * model exposing failures as validation failures (400 errors).
     *
     * @param field that will be validated
     * @param validatorBuilder the validation rule. Do put lambdas here to use.
     * @param <TValue> type of the value being validated.
     * @return self
     */
    public <TValue> Model rules(
            final String field,
            final ValidationListener<TValue> validatorBuilder) {
        final ConditionFactory condition = new ConditionFactory(this);
        final Validator validator = validatorBuilder.produce(condition);
        rules.merge(
            key(field),
            new ArrayList<>(Arrays.asList(validator)),
            (p, c) -> {
                p.addAll(c);
                return p;
            });
        return permit(field);
    }

    /**
     * Defines validation rules used on each child of a list value.
     *
     * @param field the name of the list field.
     * @param validatorBuilder
     * @return
     */
    public Model rulesOnEachChildOf(final String field, final ValidationOfChildListener validatorBuilder) {
        final ConditionCheck<Value> condition = new ConditionFactory(this).forType(Value.class);
        final Validator validator = validatorBuilder.produce(condition);
        childreenRules.merge(
            key(field),
            new ArrayList<>(Arrays.asList(validator)),
            (p, c) -> {
                p.addAll(c);
                return p;
            });
        return permit(field);
    }

    /**
     * Sets the value for a particular field
     *
     * @param field the field you are setting the value to.
     * @param value the value of the field.
     * @return self
     */
    public Model value(final String field, final Object value) {
        put(field, value);
        return this;
    }

    /**
     * Sets the DEFAULT VALUE for a particular field.
     * This value is returned if no value is found.
     *
     * @param field the field you are setting the value to.
     * @param value the default value of the field that it will assume in case no value has been defined.
     * @return self
     */
    public Model baseline(final String field, final Object value) {
        final String treated = key(field);
        baseline.put(treated, value);
        return this;
    }

    /**
     * True if there is no model errors. False otherwise.
     * If any of the rules throw an error, it will return false.
     *
     * @return the validation state.
     */
    public Boolean isValid() {
        return new ModelValidator(this, requireds, rules, childreenRules).isValid();
    }

    /**
     * The list of model errors (failures meeting requirements or rules).
     *
     * @return the list of model errors.
     * @throws Exception in case any of the rules throw an error.
     */
    public ModelErrors errors() throws Exception {
        return new ModelValidator(this, requireds, rules, childreenRules).validate();
    }

    /**
     * If permitted, returned a wrapped Value.
     * In case a value is not found for the field and a baseline value is found,
     * we return the baseline value as the value.
     *
     * @param field the field you want to retrieve.
     * @return the wrapped Value.
     */
    public Value get(final String field) {
        final String treated = key(field);
        if (permitteds.contains(treated))
            if (data.containsKey(treated))
                return new Value(data.get(treated));
            else if (baseline.containsKey(treated))
                return new Value(baseline.get(treated));

        return new Value(null);
    }

    /**
     * Clear the entire map of values.
     */
    @Override
    public void clear() {
        data.clear();
    }

    /**
     * Returns true in case the _permitted_ value is contained in the map.
     *
     * @param key the key which will be treated and searched for.
     */
    @Override
    public boolean containsKey(final Object key) {
        final String treated = key(String.valueOf(key));
        if (!permitteds.contains(treated))
            return false;

        return data.containsKey(treated) && data.get(treated) != null;
    }

    /**
     * Alias for 'containsKey'
     *
     * @param key the key which will be treated and searched for.
     */
    public boolean has(final String key) {
        return containsKey(key);
    }

    /**
     * Check if the field contains value, if it is not null and, in case of string,
     * if it is not empty.
     *
     * @param key the key which will be treated and searched for.
     */
    public boolean hasNonBlank(final String key) {
        if (!has(key))
            return false;

        final Value value = get(key);
        if (value.isNull())
            return false;

        if (String.class.equals(value.getValueClass()))
            return !value.asString().trim().equals("");

        final List<Value> valueAsList = value.asList();
        if (valueAsList != null)
            return !valueAsList.isEmpty();

        if (Model.class.equals(value.getValueClass()))
            return !value.asModel().isEmpty();

        return value != null;
    }

    /**
     * NOT SUPPORTED, will fail in any scenario.
     */
    @Override
    public boolean containsValue(final Object value) {
        throw new UnsupportedOperationException("The 'containsValue' is not implemented.");
    }

    /**
     * Returns a set of entries, but only the allowed ones.
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return data
            .entrySet()
            .stream()
            .filter(i -> permitteds.contains(i.getKey()))
            .collect(Collectors.toSet());
    }

    /**
     * Returns the wrapped value of a field, but boxed into Object.
     */
    @Override
    public Object get(final Object field) {
        if (field == null)
            throw new IllegalArgumentException("'field' can't be null");
        try {
            return get(String.valueOf(field));
        }
        catch (final Exception e) {
            return null;
        }
    }

    /**
     * Check if there's no permitted entities in the map.
     */
    @Override
    public boolean isEmpty() {
        return !data
            .keySet()
            .stream()
            .filter(fieldName -> permitteds.contains(fieldName))
            .findAny()
            .isPresent();
    }

    /**
     * The list of permitted fields.
     */
    @Override
    public Set<String> keySet() {
        return data
            .keySet()
            .stream()
            .filter(fieldName -> permitteds.contains(fieldName))
            .collect(Collectors.toSet());
    }

    /**
     * Define a value to a field, normalizing the field name.
     */
    @Override
    public Object put(final String key, final Object value) {
        try {
            return data.put(
                key(key),
                new Sanitizer(value).sanitize());
        }
        catch (Exception e) {
            LOGGER.log(
                Level.SEVERE,
                format("Model failed to put :%s => '%s'", key, value),
                e);
            return null;
        }
    }

    /**
     * Define the value to a range of fields, normalising all the field names.
     *
     * @param m the map that will be used to setup all the values.
     */
    @Override
    public void putAll(final Map<? extends String, ?> m) {
        if (m != null && !m.isEmpty())
            m.entrySet().forEach(e -> {
                final String treated = key(e.getKey());
                if (!data.containsKey(treated))
                    data.put(treated, e.getValue());
            });
    }

    /**
     * Remove the field, normalising the field name.
     */
    @Override
    public Object remove(final Object key) {
        return data.remove(key(String.valueOf(key)));
    }

    /**
     * Returns the size of the the map, but only considering the permitted fields.
     */
    @Override
    public int size() {
        return (int) data
            .keySet()
            .stream()
            .filter(fieldName -> permitteds.contains(fieldName))
            .count();
    }

    /**
     * The list of permitted field values.
     */
    @Override
    public Collection<Object> values() {
        return data
            .entrySet()
            .stream()
            .filter(entry -> permitteds.contains(entry.getKey()))
            .map(entry -> entry.getValue())
            .collect(Collectors.toList());
    }

    /**
     * Parses the complex object (Model) into a readable simpler
     * inline string that can be used for debugging and dumping purposes.
     */
    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder();
        output.append("[");
        keySet()
            .stream()
            .sorted()
            .forEach(key -> {
                try {
                    final Value raw = get(key);
                    if (raw != null) {
                        final String value = raw.asString();
                        if (output.length() > 1)
                            output.append(";");
                        output.append(key);
                        output.append("=");
                        output.append(value);
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            });
        output.append("]");
        return output.toString();
    }

    /**
     * Replies if the fieldName is accepted (or required).
     *
     * @param fieldName the name of the field which is being verified.
     * @return true in case permit or require mention the field, and false otherwise.
     */
    public boolean isAccepted(final String fieldName) {
        return permitteds.contains(key(fieldName))
               || requireds.contains(key(fieldName));
    }

    private String key(final String field) {
        return new Key(field).build();
    }

}
