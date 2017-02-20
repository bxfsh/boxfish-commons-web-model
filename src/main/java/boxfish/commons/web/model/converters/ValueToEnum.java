package boxfish.commons.web.model.converters;

/**
 * Sophisticated type conversion and parsing from Object to Enum,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToEnum<TEnum extends Enum<TEnum>> extends AbstractValueConverter<TEnum> {
    private final Class<TEnum> enumType;

    public ValueToEnum(final Object value, Class<TEnum> enumType) {
        super(value);
        this.enumType = enumType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TEnum parse() {
        if (enumType == null)
            throw new IllegalStateException("'enumType' is required before parsing.");

        if (enumType.equals(getValueClass()))
            return (TEnum) getValue();

        if (String.class.equals(getValueClass()))
            for (final TEnum item : enumType.getEnumConstants())
                if (item != null && item.name().equals(getValue()))
                    return item;

        final ValueToInteger intParser = new ValueToInteger(getValue());
        final Integer intValue = intParser.parse();
        if (intValue != null && intValue < enumType.getEnumConstants().length)
            return enumType.getEnumConstants()[intValue];

        return null;
    }

}
