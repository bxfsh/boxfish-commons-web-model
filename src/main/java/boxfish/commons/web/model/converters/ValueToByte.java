package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;
import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to Byte,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToByte extends AbstractValueConverter<Byte> {

    public ValueToByte(final Object value) {
        super(value);
    }

    @Override
    public Byte parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new Byte((String) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return ((BigDecimal) getValue()).byteValue();

        if (Float.class.equals(getValueClass()))
            return ((Float) getValue()).byteValue();

        if (Double.class.equals(getValueClass()))
            return ((Double) getValue()).byteValue();

        if (Byte.class.equals(getValueClass()))
            return (Byte) getValue();

        if (Short.class.equals(getValueClass()))
            return ((Short) getValue()).byteValue();

        if (Integer.class.equals(getValueClass()))
            return ((Integer) getValue()).byteValue();

        if (Long.class.equals(getValueClass()))
            return ((Long) getValue()).byteValue();

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to Byte",
            getValueClass().getName()));
    }

}
