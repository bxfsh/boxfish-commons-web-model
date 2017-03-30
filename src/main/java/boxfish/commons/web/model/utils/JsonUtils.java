package boxfish.commons.web.model.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonUtils {
    private static final Pattern JSON_PATTERN = Pattern.compile("^\\{[\\s\\S]+\\:+[\\s\\S]+\\}$");

    public static boolean isJsonObject(final Object rawString) {
        if (rawString != null) {
            final String possibleJson = rawString.toString();
            final Matcher match = JSON_PATTERN.matcher(possibleJson);
            if (match.matches())
                return true;
        }

        return false;
    }

    private JsonUtils() {}
}
