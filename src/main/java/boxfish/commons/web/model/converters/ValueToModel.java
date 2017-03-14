package boxfish.commons.web.model.converters;

import java.util.Map;

import boxfish.commons.web.model.Model;

/**
 * Sophisticated type conversion and parsing from Object to Model,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToModel extends AbstractValueConverter<Model> {

    public ValueToModel(final Object value) {
        super(value);
    }

    @Override
    public Model parse() {
        if (Model.class.equals(getValueClass()))
            return (Model) getValue();

        if (Map.class.isAssignableFrom(getValueClass()))
            reportIllegalState();

        return Model.create();
    }

    private void reportIllegalState() {
        throw new IllegalStateException("The value should have been sanitized and may NOT be a map."
                                        + "All maps must become Model before being kept as data.");
    }

}
