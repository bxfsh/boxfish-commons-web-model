package boxfish.commons.web.model.converters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boxfish.commons.web.model.Model;
import boxfish.commons.web.model.Value;

/**
 * Sophisticated type conversion and parsing from Object to List,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToList extends AbstractValueConverter<List<Value>> {
    public ValueToList(final Object value) {
        super(value);
    }

    @Override
    public List<Value> parse() {
        if (getValue() == null)
            return null;

        if (Object[].class.equals(getValueClass()))
            return collectAsList((Object[]) getValue());

        if (String[].class.equals(getValueClass()))
            return collectAsList((String[]) getValue());

        if (Byte[].class.equals(getValueClass()))
            return collectAsList((Byte[]) getValue());

        if (Short[].class.equals(getValueClass()))
            return collectAsList((Short[]) getValue());

        if (Integer[].class.equals(getValueClass()))
            return collectAsList((Integer[]) getValue());

        if (Long[].class.equals(getValueClass()))
            return collectAsList((Long[]) getValue());

        if (BigDecimal[].class.equals(getValueClass()))
            return collectAsList((BigDecimal[]) getValue());

        if (Float[].class.equals(getValueClass()))
            return collectAsList((Float[]) getValue());

        if (Double[].class.equals(getValueClass()))
            return collectAsList((Double[]) getValue());

        if (Model[].class.equals(getValueClass()))
            return collectAsList((Model[]) getValue());

        if (List.class.isAssignableFrom(getValueClass()))
            return ((List<?>) getValue())
                .stream()
                .map(v -> new Value(v))
                .collect(Collectors.toList());

        if (String.class.equals(getValueClass()))
            return stringAsListOf((String) getValue());

        return null;
    }

    private List<Value> stringAsListOf(final String value) {
        if (value != null) {
            String[] frags;

            if (value.contains(";"))
                frags = value.split(";");
            else if (value.contains(""))
                frags = value.split(",");
            else
                frags = new String[] {value};

            return collectAsList(frags);
        }

        return null;
    }

    private <TOriginal extends Object> List<Value> collectAsList(final TOriginal[] objects) {
        return Arrays
            .stream(objects)
            .map(v -> new Value(v))
            .collect(Collectors.toList());
    }

}
