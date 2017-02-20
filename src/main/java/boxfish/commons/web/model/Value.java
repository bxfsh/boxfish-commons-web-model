package boxfish.commons.web.model;

import java.math.BigDecimal;
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
public class Value {
    private final Object value;

    public Value(final Object value) {
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
     * Presents the value as a Model. Used for nested
     * objects that may come in a JSON like or similar
     * structures.
     *
     * @return a Model representing the value when it's a complex object.
     */
    public Model asModel() {
        return new ValueToModel(value).parse();
    }

    /**
     * Presents the value as an item of an Enum.
     *
     * @param enumType the type of the Enum.
     * @param <TEnum> the type of the enum that will be used as return value.
     * @return the value of the Enum that was parsed from the original value.
     */
    public <TEnum extends Enum<TEnum>> TEnum asEnum(final Class<TEnum> enumType) {
        return new ValueToEnum<>(value, enumType).parse();
    }

    /**
     * Presents the value as a List of a particular type.
     *
     * @return a List of {@link Value} representing the value.
     */
    public List<Value> asList() {
        return new ValueToList(value).parse();
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
