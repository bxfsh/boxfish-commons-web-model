package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;
import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to Double,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 * 
 * @author Hudson Mendes
 *
 */
public class ValueToDouble extends AbstractValueConverter<Double> {

    public ValueToDouble(final Object value) {
        super(value);
    }

    @Override
    public Double parse() throws Exception {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new Double((String) getValue());

        if (Float.class.equals(getValueClass()))
            return Double.valueOf(((Float) getValue()).toString());

        if (Double.class.equals(getValueClass()))
            return Double.valueOf((Double) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return Double.valueOf(((BigDecimal) getValue()).toString());

        if (Byte.class.equals(getValueClass()))
            return Double.valueOf((Byte) getValue());

        if (Short.class.equals(getValueClass()))
            return Double.valueOf((Short) getValue());

        if (Integer.class.equals(getValueClass()))
            return Double.valueOf((Integer) getValue());

        if (Long.class.equals(getValueClass()))
            return Double.valueOf((Long) getValue());

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to Double",
            getValueClass().getName()));
    }
}
