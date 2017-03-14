package boxfish.commons.web.model.sanitization;

import java.util.Map;

import boxfish.commons.web.model.RestModel;

@SuppressWarnings("rawtypes")
class SanitizerForMaps extends SanitizerFor<RestModel, Map> {

    SanitizerForMaps(final Object rawValue) {
        super(rawValue, Map.class);
    }

    @Override
    protected RestModel sanitizedValue() {
        if (RestModel.class.equals(getRawClass()))
            return (RestModel) getRawValue();

        if (Map.class.isAssignableFrom(getExpectedClass()))
            return newModelFromMap();

        throw new UnsupportedOperationException(
            "Although we had a Map, we could not sanitize it into a RestModel.");
    }

    private RestModel newModelFromMap() {
        final RestModel translation = RestModel.newRestModel();
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
