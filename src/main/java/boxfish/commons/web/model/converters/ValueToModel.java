package boxfish.commons.web.model.converters;

import static boxfish.commons.web.model.utils.JsonUtils.isJsonObject;

import java.util.Map;

import boxfish.commons.web.model.RestModel;

/**
 * Sophisticated type conversion and parsing from Object to RestModel,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToModel extends AbstractValueConverter<RestModel> {
    public ValueToModel(final Object value) {
        super(value);
    }

    @Override
    public RestModel parse() {
        if (RestModel.class.equals(getValueClass()))
            return (RestModel) getValue();

        if (Map.class.isAssignableFrom(getValueClass()))
            reportIllegalMap();

        if (isJsonObject(getValue()))
            reportIllegalJson();

        return RestModel.newRestModel();
    }

    private void reportIllegalMap() {
        throw new IllegalStateException("The value should have been sanitized and may NOT be a map."
                                        + "All maps must become RestModel before being kept as data.");
    }

    private void reportIllegalJson() {
        throw new IllegalStateException("The value should have been sanitized and may NOT be a JSON."
                                        + "All JSONs must become RestModel before being kept as data.");
    }

}
