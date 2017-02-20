package boxfish.commons.web.model.converters;

import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to Float,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToFloat extends AbstractValueConverter<Float> {

    public ValueToFloat(final Object value) {
        super(value);
    }

    @Override
    public Float parse() {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return new Float((String) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return Float.valueOf(((BigDecimal) getValue()).toString());

        if (Float.class.equals(getValueClass()))
            return Float.valueOf((Float) getValue());

        if (Double.class.equals(getValueClass()))
            return ((Double) getValue()).floatValue();

        if (Byte.class.equals(getValueClass()))
            return Float.valueOf((Byte) getValue());

        if (Short.class.equals(getValueClass()))
            return Float.valueOf((Short) getValue());

        if (Integer.class.equals(getValueClass()))
            return Float.valueOf((Integer) getValue());

        if (Long.class.equals(getValueClass()))
            return Float.valueOf((Long) getValue());

        return null;
    }
}
