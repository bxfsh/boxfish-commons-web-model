package boxfish.commons.web.model.converters;

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
    public Integer parse() {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return Integer.valueOf((String) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return ((BigDecimal) getValue()).intValue();

        if (Float.class.equals(getValueClass()))
            return ((Float) getValue()).intValue();

        if (Double.class.equals(getValueClass()))
            return ((Double) getValue()).intValue();

        if (Byte.class.equals(getValueClass()))
            return ((Byte) getValue()).intValue();

        if (Short.class.equals(getValueClass()))
            return ((Short) getValue()).intValue();

        if (Integer.class.equals(getValueClass()))
            return (Integer) getValue();

        if (Long.class.equals(getValueClass()))
            return ((Long) getValue()).intValue();

        return null;
    }

}
