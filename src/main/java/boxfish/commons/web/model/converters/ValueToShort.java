package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;
import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to Short,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToShort extends AbstractValueConverter<Short> {

    public ValueToShort(final Object value) {
        super(value);
    }

    @Override
    public Short parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new Short((String) getValue());

        if (Byte.class.equals(getValueClass()))
            return ((Byte) getValue()).shortValue();

        if (Short.class.equals(getValueClass()))
            return (Short) getValue();

        if (Integer.class.equals(getValueClass()))
            return ((Integer) getValue()).shortValue();

        if (Long.class.equals(getValueClass()))
            return ((Long) getValue()).shortValue();

        if (BigDecimal.class.equals(getValueClass()))
            return ((BigDecimal) getValue()).shortValue();

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to Short",
            getValueClass().getName()));
    }

}
