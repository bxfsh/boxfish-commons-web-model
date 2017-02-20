package boxfish.commons.web.model.converters;

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
    public Short parse() {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new Short((String) getValue());

        if (Float.class.equals(getValueClass()))
            return ((Float) getValue()).shortValue();

        if (Double.class.equals(getValueClass()))
            return ((Double) getValue()).shortValue();

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

        return null;
    }

}
