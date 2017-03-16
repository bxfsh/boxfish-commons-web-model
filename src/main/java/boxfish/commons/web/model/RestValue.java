package boxfish.commons.web.model;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import boxfish.commons.web.model.converters.ValueToBigDecimal;
import boxfish.commons.web.model.converters.ValueToBoolean;
import boxfish.commons.web.model.converters.ValueToByte;
import boxfish.commons.web.model.converters.ValueToDouble;
import boxfish.commons.web.model.converters.ValueToEnum;
import boxfish.commons.web.model.converters.ValueToFloat;
import boxfish.commons.web.model.converters.ValueToInstant;
import boxfish.commons.web.model.converters.ValueToInteger;
import boxfish.commons.web.model.converters.ValueToList;
import boxfish.commons.web.model.converters.ValueToLong;
import boxfish.commons.web.model.converters.ValueToModel;
import boxfish.commons.web.model.converters.ValueToShort;
import boxfish.commons.web.model.converters.ValueToString;

/**
 * Wrapps the original value allowing easy type
 * conversions and obvious parsings.
 *
 * @author Hudson Mendes
 *
 */
public class RestValue {
    private final Object value;

    public RestValue(final Object value) {
        this.value = value;
    }

    /**
     * Presents the value as String.
     *
     * @return a string representing the value
     */
    public String asString() {
        return new ValueToString(value).parse();
    }

    /**
     * Presents the value as long. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return a long representing the value.
     */
    public Long asLong() {
        return new ValueToLong(value).parse();
    }

    /**
     * Presents the value as Integer. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return an integer representing the value.
     */
    public Integer asInteger() {
        return new ValueToInteger(value).parse();
    }

    /**
     * Presents the value as Short. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return a short representing the value
     */
    public Short asShort() {
        return new ValueToShort(value).parse();
    }

    /**
     * Presents the value as Byte. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return a byte representing the value.
     */
    public Byte asByte() {
        return new ValueToByte(value).parse();
    }

    /**
     * Presents the value as Boolean.
     *
     * @return a boolean representing a value.
     */
    public Boolean asBoolean() {
        return new ValueToBoolean(value).parse();
    }

    /**
     * Presents the value as BigDecimal. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return a BigDecimal representing the value.
     */
    public BigDecimal asBigDecimal() {
        return new ValueToBigDecimal(value).parse();
    }

    /**
     * Presets the value as Float. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return a Float representing the value.
     */
    public Float asFloat() {
        return new ValueToFloat(value).parse();
    }

    /**
     * Presets the value as Double. Works on most
     * of Numeric types and numeric Strings.
     *
     * @return a Double representing the value.
     */
    public Double asDouble() {
        return new ValueToDouble(value).parse();
    }

    /**
     * Presents the value as Instant.
     *
     * @return an Instant representing the value.
     */
    public Instant asInstant() {
        return new ValueToInstant(value).parse();
    }

    /**
     * Presents the Instant as Timestamp.
     * Specially useful when mapping to databases.
     * 
     * @return a Timestamp representing the instant value.
     */
    public Timestamp asTimestamp() {
        Instant instant = asInstant();
        if (instant != null)
            return Timestamp.from(instant);
        else
            return null;
    }

    /**
     * Presents the value as a RestModel. Used for nested
     * objects that may come in a JSON like or similar
     * structures.
     *
     * @return a RestModel representing the value when it's a complex object.
     */
    public RestModel asModel() {
        return new ValueToModel(value).parse();
    }

    /**
     * Presents the value as an item of an Enum.
     *
     * @param enumType the type of the Enum.
     * @param <TEnum> the type of the enum that will be used as return value.
     * @return the value of the Enum that was parsed from the original value.
     */
    public <TEnum extends Enum<TEnum>>TEnum asEnum(final Class<TEnum> enumType) {
        return new ValueToEnum<>(value, enumType).parse();
    }

    /**
     * Presents the value as a List of wrapped values.
     *
     * @return a List of {@link RestValue} representing the value.
     */
    public List<RestValue> asList() {
        return new ValueToList(value).parse();
    }

    /**
     * * Presents the value as a List of a particular type.
     * 
     * @param clazz the type in which we will display the childreen.
     * @param <TValue> the value to which we will try to parse.
     * @return a typed list of values.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <TValue> List<TValue> asListOf(Class<TValue> clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("'clazz' can't be null.");

        if (BigDecimal.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asBigDecimal()).collect(toList());
        else if (Boolean.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asBoolean()).collect(toList());
        else if (Byte.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asByte()).collect(toList());
        else if (Double.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asDouble()).collect(toList());
        else if (clazz.isEnum())
            return (List<TValue>) asList().stream().map(v -> (TValue) v.asEnum((Class<Enum>) clazz)).collect(toList());
        else if (Float.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asFloat()).collect(toList());
        else if (Instant.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asInstant()).collect(toList());
        else if (Integer.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asInteger()).collect(toList());
        else if (Long.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asLong()).collect(toList());
        else if (RestModel.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asModel()).collect(toList());
        else if (Short.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asShort()).collect(toList());
        else if (String.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asString()).collect(toList());
        else if (Object.class.equals(clazz))
            return asList().stream().map(v -> (TValue) v.asOriginal()).collect(toList());
        else
            throw new UnsupportedOperationException(format("We cannot represent the value as a list of %s", clazz.getName()));
    }

    /**
     * Presents the original value without type conversion.
     *
     * @return the original value.
     */
    public Object asOriginal() {
        return value;
    }

    /**
     * Presents the Class of ? of the value, or
     * Object.class when the value is null.
     *
     * @return the class of the value.
     */
    public Class<?> getValueClass() {
        if (value == null)
            return Object.class;
        return value.getClass();
    }

    /**
     * Exposes if the object is null without an equality check.
     *
     * @return true if null, false otherwise.
     */
    public boolean isNull() {
        return value == null;
    }
}
