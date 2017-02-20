package boxfish.commons.web.model.converters;

public abstract class AbstractValueConverter<TValue> {
    private final Object value;

    public AbstractValueConverter(final Object value) {
        this.value = value;
    }

    protected Object getValue() {
        return value;
    }

    protected Class<? extends Object> getValueClass() {
        if (getValue() == null)
            return Object.class;
        Class<? extends Object> valueClass = getValue().getClass();
        if (valueClass == null)
            return Object.class;

        return valueClass;
    }

    /**
     * Attempts to convert the original value into the
     * specific TValue in all possible ways.
     * 
     * @return the original value converted or parsed into TValue.
     */
    public abstract TValue parse();
}
