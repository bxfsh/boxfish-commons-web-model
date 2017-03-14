package boxfish.commons.web.model.sanitization;

import java.util.Map;

import boxfish.commons.web.model.Model;

@SuppressWarnings("rawtypes")
class SanitizerForMaps extends SanitizerFor<Model, Map> {

    SanitizerForMaps(final Object rawValue) {
        super(rawValue, Map.class);
    }

    @Override
    protected Model sanitizedValue() {
        if (Model.class.equals(getRawClass()))
            return (Model) getRawValue();

        if (Map.class.isAssignableFrom(getExpectedClass()))
            return newModelFromMap();

        throw new UnsupportedOperationException(
            "Although we had a Map, we could not sanitize it into a Model.");
    }

    private Model newModelFromMap() {
        final Model translation = Model.create();
        if (getRawValue() != null) {
            final Map<?, ?> parsedMap = (Map<?, ?>) getRawValue();
            parsedMap.forEach((k, v) -> {
                if (k != null)
                    translation.put(k.toString(), v);
            });
        }
        return translation;
    }
}
