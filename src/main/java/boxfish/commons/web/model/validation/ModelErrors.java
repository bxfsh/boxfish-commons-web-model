package boxfish.commons.web.model.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The collection with error messages ready to be presented
 * as response body of a 400 result.
 * 
 * @author Hudson Mendes
 *
 */
public class ModelErrors implements Iterable<ModelError> {
    private final Map<String, ModelError> errors = new HashMap<>();
    private List<ModelError> sorted;

    /**
     * Returns true if there is any error.
     * 
     * @return true if any error, false otherwise.
     */
    public Boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * The iterator of the internal sorted collection.
     */
    @Override
    public Iterator<ModelError> iterator() {
        ensureSorted();
        return sorted.iterator();
    }

    /**
     * Allows you add model errors when they happen.
     * Used by the validator to report field value
     * errors against requirements and rules.
     * 
     * @param fieldName the fieldname that failed the condition.
     * @param errorMessage the error message.
     */
    public synchronized void addError(
            final String fieldName,
            final String errorMessage) {
        final ModelError error = new ModelError(fieldName, errorMessage);
        if (!errors.containsKey(error.getKey())) {
            errors.put(error.getKey(), error);
            sorted = null;
        }
    }

    /**
     * How many errors occurred.
     * 
     * @return the size of the error collection.
     */
    public Integer size() {
        return errors.size();
    }

    /**
     * Gets the position i of the sorted error list.
     * 
     * @param i the position
     * @return the ith item of the sorted error list.
     */
    public ModelError get(final Integer i) {
        ensureSorted();
        return sorted.get(i);
    }

    private void ensureSorted() {
        sorted = sorted();
    }

    private List<ModelError> sorted() {
        return errors
            .entrySet()
            .stream()
            .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
            .map(b -> b.getValue())
            .collect(Collectors.toList());
    }
}
