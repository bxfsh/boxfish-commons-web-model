package boxfish.commons.web.model.validation;

/**
 * The validator that can be either used to implement
 * more complex and custom validation routines or
 * to be dynamically implemented as an anonymous
 * interface implementation for building rule conditions.
 * 
 * @author Hudson Mendes
 *
 */
public interface Validator {
    /**
     * The error message that will be raised in case the check fails.
     * 
     * @return the error message
     */
    public String errorMessage();

    /**
     * Checks to see if the type can be at all checked by the rule.
     * 
     * @param clazz the class type
     * @return True if it's accepted or fails in case it's not.
     */
    public Boolean accepts(Class<?> clazz);

    /**
     * Checks and returns if the value is valid or not.
     * 
     * @param object the value
     * @return true if is valid, otherwise returns false.
     */
    public Boolean isValid(Object object);
}
