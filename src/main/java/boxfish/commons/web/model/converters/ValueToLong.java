package boxfish.commons.web.model.converters;

import java.math.BigDecimal;

/**
 * Sophisticated type conversion and parsing from Object to Long,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToLong extends AbstractValueConverter<Long> {

    public ValueToLong(final Object value) {
        super(value);
    }

    @Override
    public Long parse() {
        if (getValue() == null)
            return null;

        if (String.class.equals(getValueClass()))
            return Long.valueOf((String) getValue());

        if (BigDecimal.class.equals(getValueClass()))
            return ((BigDecimal) getValue()).longValue();

        if (Float.class.equals(getValueClass()))
            return ((Float) getValue()).longValue();

        if (Double.class.equals(getValueClass()))
            return ((Double) getValue()).longValue();

        if (Byte.class.equals(getValueClass()))
            return ((Byte) getValue()).longValue();

        if (Short.class.equals(getValueClass()))
            return ((Short) getValue()).longValue();

        if (Integer.class.equals(getValueClass()))
            return ((Integer) getValue()).longValue();

        if (Long.class.equals(getValueClass()))
            return (Long) getValue();

        return null;
    }

}
