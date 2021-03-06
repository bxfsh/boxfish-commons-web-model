package boxfish.commons.web.model.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
    public BigDecimal parse() {
        if (getValue() == null)
            return null;

        final BigDecimal decimal = makeBigDecimal();
        if (decimal != null)
            return decimal.stripTrailingZeros();
        else
            return null;
    }

    private BigDecimal makeBigDecimal() {
        if (String.class.equals(getValueClass()))
            return (BigDecimal) formatStringJustInCase();

        if (BigDecimal.class.equals(getValueClass()))
            return (BigDecimal) getValue();

        if (Float.class.equals(getValueClass()))
            return new BigDecimal(((Float) getValue()).toString());

        if (Double.class.equals(getValueClass()))
            return new BigDecimal(((Double) getValue()).toString());

        if (Byte.class.equals(getValueClass()))
            return BigDecimal.valueOf((Byte) getValue());

        if (Short.class.equals(getValueClass()))
            return BigDecimal.valueOf((Short) getValue());

        if (Integer.class.equals(getValueClass()))
            return BigDecimal.valueOf((Integer) getValue());

        if (Long.class.equals(getValueClass()))
            return BigDecimal.valueOf((Long) getValue());

        return null;
    }

    private Number formatStringJustInCase() {
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        final DecimalFormat format = new DecimalFormat("0.0", symbols);
        format.setParseBigDecimal(true);
        try {
            return format.parse((String) getValue());
        }
        catch (final Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
