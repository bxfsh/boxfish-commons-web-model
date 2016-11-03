package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;
import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to Integer,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToInteger extends AbstractValueConverter<Integer> {

    public ValueToInteger(final Object value) {
        super(value);
    }

    @Override
    public Integer parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return Integer.valueOf((String) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return ((BigDecimal) getValue()).intValue();

        if (Byte.class.equals(getValueClass()))
            return ((Byte) getValue()).intValue();

        if (Short.class.equals(getValueClass()))
            return ((Short) getValue()).intValue();

        if (Integer.class.equals(getValueClass()))
            return (Integer) getValue();

        if (Long.class.equals(getValueClass()))
            return ((Long) getValue()).intValue();

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to Integer",
            getValueClass().getName()));
    }

}
