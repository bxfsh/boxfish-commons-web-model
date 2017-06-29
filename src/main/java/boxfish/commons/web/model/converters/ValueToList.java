package boxfish.commons.web.model.converters;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boxfish.commons.web.model.RestModel;
import boxfish.commons.web.model.RestValue;

/**
 * Sophisticated type conversion and parsing from Object to List,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToList extends AbstractValueConverter<List<RestValue>> {
    public ValueToList(final Object value) {
        super(value);
    }

    @Override
    public List<RestValue> parse() {
        if (getValue() == null)
            return emptyList();

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

        if (RestModel[].class.equals(getValueClass()))
            return collectAsList((RestModel[]) getValue());

        if (List.class.isAssignableFrom(getValueClass()))
            return ((List<?>) getValue())
                .stream()
                .map(v -> new RestValue(v))
                .collect(Collectors.toList());

        if (String.class.equals(getValueClass()))
            return stringAsListOf((String) getValue());

        return null;
    }

    private List<RestValue> stringAsListOf(String value) {
        if (value != null) {
            value = value.trim().replaceAll("^\\[|\\]$", "");

            String[] frags;
            if (value.contains(";"))
                frags = value.split(";");
            else if (value.contains(""))
                frags = value.split(",");
            else
                frags = new String[] {value};

            return collectAsList(stream(frags)
                .map(v -> v.trim())
                .toArray(size -> new String[size]));
        }

        return null;
    }

    private <TOriginal extends Object> List<RestValue> collectAsList(final TOriginal[] objects) {
        return Arrays
            .stream(objects)
            .map(v -> new RestValue(v))
            .collect(Collectors.toList());
    }

}
