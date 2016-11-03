package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;

/**
 * Sophisticated type conversion and parsing from Object to Boolean,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToBoolean extends AbstractValueConverter<Boolean> {

    public ValueToBoolean(final Object value) {
        super(value);
    }

    @Override
    public Boolean parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new Boolean((String) getValue());

        if (Boolean.class.equals(getValueClass()))
            return (Boolean) getValue();

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to Boolean",
            getValueClass().getName()));
    }

}
