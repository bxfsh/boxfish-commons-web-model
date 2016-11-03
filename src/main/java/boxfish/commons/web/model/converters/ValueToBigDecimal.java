package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;
import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to BigDecimal,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToBigDecimal extends AbstractValueConverter<BigDecimal> {

    public ValueToBigDecimal(final Object value) {
        super(value);
    }

    @Override
    public BigDecimal parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new BigDecimal((String) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return (BigDecimal) getValue();

        if (Byte.class.equals(getValueClass()))
            return BigDecimal.valueOf((Byte) getValue());

        if (Short.class.equals(getValueClass()))
            return BigDecimal.valueOf((Short) getValue());

        if (Integer.class.equals(getValueClass()))
            return BigDecimal.valueOf((Integer) getValue());

        if (Long.class.equals(getValueClass()))
            return BigDecimal.valueOf((Long) getValue());

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to BigDecimal",
            getValueClass().getName()));
    }
}
