package boxfish.commons.web.model;

/**
 * Utility to generate standardised Hash keys, avoiding
 * casing problems translating the REST environment to
 * the Java environment, and vice versa. Specially useful
 * when URLs are standardised (as commonly) in snake case
 * and when Java is entirely wrapped around camelCase.
 * 
 * @author Hudson Mendes
 *
 */
class Key {
    
    private final String field;

    private static final String REGEX = "([a-z])([A-Z]+)";
    
    /**
     * Constructs the key with a key using any case
     * for the field name.
     * 
     * @param field the field name with any case.
     */
    public Key(final String field) {
        this.field = field;
    }

    /**
     * Constructs the key translated to snake_case
     * despite the case originally used for the field name.
     * 
     * @return the field name with snake_case.
     */
    public String build() {
        assertRequirements();
        return treat(this.field);
    }

    private void assertRequirements() throws IllegalAccessError {
        if (this.field == null || this.field.trim().equals(""))
            throw new IllegalAccessError("The 'field' can't be null");
    }

    private String treat(final String field) {
        return field.replaceAll(REGEX, "$1_$2").toLowerCase();
    }

}
