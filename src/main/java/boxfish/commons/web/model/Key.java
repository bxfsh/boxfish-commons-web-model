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
    private static final Character SPLITTER = '_';
    private final String field;

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
        return treat(field);
    }

    private void assertRequirements() throws IllegalAccessError {
        if (field == null || field.trim().equals(""))
            throw new IllegalAccessError("The 'field' can't be null");
    }

    private String treat(final String field) {
        final StringBuilder output = new StringBuilder();
        if (field != null)
            for (int i = 0; i < field.length(); i++) {
                final char letter = field.charAt(i);
                processSplitter(output, letter);
                processAlphabeticLetter(output, letter);
                processDigitLetter(output, letter);
            }
        return output.toString();
    }

    private void processSplitter(
            final StringBuilder output,
            final char letter) {
        final Boolean hasAnyChar = hasAnyChar(output);
        final Boolean lastASplitter = isLastASplitter(output);
        final Boolean isSplitter = SPLITTER.equals(letter);
        if (hasAnyChar && !lastASplitter && isSplitter)
            output.append(SPLITTER);
    }

    private void processAlphabeticLetter(
            final StringBuilder output,
            final char letter) {

        if (Character.isAlphabetic(letter)) {
            final Boolean shouldBeSeparated = Character.isUpperCase(letter) || isLastADigit(output);
            final Boolean hasAnyChar = hasAnyChar(output);
            final Boolean lastASplitter = isLastASplitter(output);
            if (hasAnyChar && !lastASplitter && shouldBeSeparated)
                output.append(SPLITTER);

            output.append(Character.toLowerCase(letter));
        }
    }

    private void processDigitLetter(
            final StringBuilder output,
            final char letter) {

        if (Character.isDigit(letter)) {
            final Boolean hasAnyChar = hasAnyChar(output);
            final Boolean lastASplitter = isLastASplitter(output);
            final Boolean lastADigit = isLastADigit(output);
            if (hasAnyChar && !lastASplitter && !lastADigit)
                output.append(SPLITTER);

            output.append(letter);
        }
    }

    private Boolean isLastADigit(final StringBuilder output) {
        final Boolean hasAnyChar = hasAnyChar(output);
        if (!hasAnyChar)
            return false;

        final char lastChar = output.charAt(output.length() - 1);
        return Character.isDigit(lastChar);
    }

    private Boolean hasAnyChar(final StringBuilder output) {
        return output.length() > 0;
    }

    private Boolean isLastASplitter(final StringBuilder output) {
        final Boolean hasAnyChar = hasAnyChar(output);
        if (!hasAnyChar)
            return false;

        final char lastChar = output.charAt(output.length() - 1);
        return SPLITTER.equals(lastChar);
    }
}
