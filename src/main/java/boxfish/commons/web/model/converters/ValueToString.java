package boxfish.commons.web.model.converters;

/**
 * Sophisticated type conversion and parsing from Object to String,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToString extends AbstractValueConverter<String> {

    public ValueToString(final Object value) {
        super(value);
    }

    @Override
    public String parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return (String) getValue();

        return getValue().toString();
    }

}
