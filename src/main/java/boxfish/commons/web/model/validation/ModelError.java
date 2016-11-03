package boxfish.commons.web.model.validation;

/**
 * The simple ocurrence of the erros with information
 * about what happened to the field.
 * 
 * @author Hudson Mendes
 *
 */
public class ModelError {
    private final String fieldName;
    private final String errorMessage;

    public ModelError(final String fieldName, final String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    /**
     * The field name.
     * 
     * @return the field name.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * The error message.
     * 
     * @return the error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    String getKey() {
        return getFieldName() + "_" + getErrorMessage();
    }
}
