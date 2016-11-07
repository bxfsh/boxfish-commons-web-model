package boxfish.commons.web.model;

/**
 * Utility to generate standardised Hash keys, avoiding
 * casing problems translating the REST environment to
 * the Java environment, and vice versa. Specially useful
 * when URLs are standardised (as commonly) in snake case
 * and when Java is entirely wrapped around camelCase.
 * 
 * @author Hudson Mendes
 * @author Thiago Neves
 *
 */
class Key {

    private final String field;

    private static final String SPECIAL_CHARACTERS = "[^\\w\\s\\-_]";
    private static final String SNAKE_CASE = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])|(?<=[a-zA-‌​Z])(?=[0-9])";
    private static final String MULTIPLE_UNDERSCORES = "_+";

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
        return field
            .replaceAll(SPECIAL_CHARACTERS, "")
            .replaceAll(SNAKE_CASE, "_$0")
            .replaceAll(MULTIPLE_UNDERSCORES, "_")
            .toLowerCase();

    }

}
