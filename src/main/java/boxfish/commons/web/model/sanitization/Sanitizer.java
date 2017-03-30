package boxfish.commons.web.model.sanitization;

import static boxfish.commons.web.model.utils.JsonUtils.isJsonObject;
import static java.lang.String.format;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chooses the sanitizer that will be used to treat the value.
 * Important for pre-processing Maps and Lists with maps, because
 * Maps must be converted into RestModel before they are kept as data.
 *
 * @author Hudson Mendes
 *
 */
public class Sanitizer {
    private static final Map<SanitizerValueMatcher, Class<? extends SanitizerFor<? extends Object, ?>>> SANITIZERS;
    static {
        SANITIZERS = new HashMap<>();
        SANITIZERS.put((c, v) -> Map.class.isAssignableFrom(c), SanitizerForMaps.class);
        SANITIZERS.put((c, v) -> List.class.isAssignableFrom(c), SanitizerForLists.class);
        SANITIZERS.put((c, v) -> String.class.equals(c) && isJsonObject(v), SanitizerForJson.class);
    }

    private final Object value;

    public Sanitizer(final Object value) {
        this.value = value;
    }

    /**
     * If any Sanitizer is available for the valueClass,
     * performs sanitization and returns the value. In
     * case no Sanitizer is found (or the value is null),
     * simply return the value that should remain unnaffected.
     *
     * @return the sanitized value if a Sanitizer was found, or the value itself.
     * @throws Exception throws whenever we fail to activate the Sanitizer.
     */
    public Object sanitize() throws Exception {
        if (value != null) {
            final SanitizerFor<? extends Object, ?> chooseSanitizer = chooseSanitizer();
            if (chooseSanitizer != null)
                return chooseSanitizer.sanitizedValue();
        }

        return value;
    }

    private SanitizerFor<? extends Object, ?> chooseSanitizer() throws Exception {
        final Class<?> valueClass = value.getClass();
        for (final SanitizerValueMatcher key : SANITIZERS.keySet())
            if (key.shouldBeSanitized(valueClass, value)) {
                final Class<? extends SanitizerFor<? extends Object, ?>> sanitizer = SANITIZERS.get(key);
                final Constructor<?> constructor = findConstructor(sanitizer);
                validateConstructor(sanitizer, constructor);
                return makeSanitizerFromClass(constructor);
            }
        return null;
    }

    @SuppressWarnings("unchecked")
    private SanitizerFor<? extends Object, ?> makeSanitizerFromClass(
            final Constructor<?> constructor) throws Exception {
        final Constructor<SanitizerFor<? extends Object, ?>> typedConstructor = (Constructor<SanitizerFor<? extends Object, ?>>) constructor;
        return typedConstructor.newInstance(value);
    }

    private void validateConstructor(
            final Class<? extends SanitizerFor<? extends Object, ?>> sanitizer,
            final Constructor<?> constructor) throws Exception {
        final Parameter[] constructorParameters = constructor.getParameters();
        assertTheresOnlyOneParam(sanitizer, constructorParameters);
        assertSingleParamIsObject(sanitizer, constructorParameters);
    }

    private Constructor<?> findConstructor(
            final Class<? extends SanitizerFor<? extends Object, ?>> sanitizer) throws Exception {
        final Constructor<?>[] constructors = sanitizer.getDeclaredConstructors();
        if (constructors.length != 1)
            throw new IllegalClassFormatException(format(
                "'%s' must have a single constructor.",
                sanitizer.getName()));
        return constructors[0];
    }

    private void assertTheresOnlyOneParam(
            final Class<? extends SanitizerFor<? extends Object, ?>> sanitizer,
            final Parameter[] constructorParameters) throws Exception {
        if (constructorParameters.length != 1)
            throw new IllegalClassFormatException(format(
                "'%s' constructor must have a single parameter.",
                sanitizer.getName()));
    }

    private void assertSingleParamIsObject(
            final Class<? extends SanitizerFor<? extends Object, ?>> sanitizer,
            final Parameter[] constructorParameters) throws Exception {
        final Parameter constructorParameter = constructorParameters[0];
        if (!constructorParameter.getParameterizedType().equals(Object.class))
            throw new IllegalClassFormatException(format(
                "'%s' constructor's sole parameter must be an Object.",
                sanitizer.getName()));
    }

}
