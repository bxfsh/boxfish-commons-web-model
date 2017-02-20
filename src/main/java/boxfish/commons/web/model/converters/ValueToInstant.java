package boxfish.commons.web.model.converters;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Sophisticated type conversion and parsing from Object to Instant,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToInstant extends AbstractValueConverter<Instant> {

    public ValueToInstant(final Object value) {
        super(value);
    }

    @Override
    public Instant parse() {
        if (String.class.equals(getValueClass()))
            return Instant.parse((String) getValue());

        if (Byte.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((Byte) getValue()).longValue());

        if (Short.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((Short) getValue()).longValue());

        if (Integer.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((Integer) getValue()).longValue());

        if (Long.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((Long) getValue()).longValue());

        if (BigDecimal.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((BigDecimal) getValue()).longValue());

        if (Float.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((Float) getValue()).longValue());

        if (Double.class.equals(getValueClass()))
            return Instant.ofEpochMilli(((Double) getValue()).longValue());

        if (Instant.class.equals(getValueClass()))
            return (Instant) getValue();

        return null;
    }

}
