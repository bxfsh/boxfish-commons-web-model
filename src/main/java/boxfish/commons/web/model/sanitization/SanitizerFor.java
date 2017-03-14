package boxfish.commons.web.model.sanitization;

/**
 * Template for sanitizers with basic functionality.
 *
 * @author Hudson Mendes
 *
 * @param <TOutput> The output type that is supposed to come out as sanitized value.
 * @param <TExpectedValueClass> The expected input value class.
 */
abstract class SanitizerFor<TOutput extends Object, TExpectedValueClass> {
    private final Class<TExpectedValueClass> expectedClass;
    private final Object rawValue;

    SanitizerFor(final Object rawValue, final Class<TExpectedValueClass> expectedClass) {
        this.rawValue = rawValue;
        this.expectedClass = expectedClass;
    }

    Object sanitize() {
        if (rawValue != null)
            if (expectedClass.isAssignableFrom(rawValue.getClass()))
                return sanitizedValue();
        return rawValue;
    }

    /**
     * Custom implementation of the sanitization logic.
     * 
     * @return the sanitized value.
     */
    protected abstract TOutput sanitizedValue();

    /**
     * The expected value class, defined by the Sanitizer.
     * 
     * @return the expected value class,
     */
    protected Class<TExpectedValueClass> getExpectedClass() {
        return expectedClass;
    }

    /**
     * The class of the value, found by getClass.
     * This method never returns null. For null values
     * we return Object.class.
     * 
     * @return the getClass() of the value, OR Object.class, never null.
     */
    protected Object getRawClass() {
        if (rawValue != null)
            return rawValue.getClass();
        else
            return Object.class;
    }

    /**
     * The raw value, as is.
     * 
     * @return The raw value, as is.
     */
    protected Object getRawValue() {
        return rawValue;
    }
}
