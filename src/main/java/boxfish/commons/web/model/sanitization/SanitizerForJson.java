package boxfish.commons.web.model.sanitization;

import static boxfish.commons.web.model.RestModel.newRestModel;
import static boxfish.commons.web.model.utils.JsonUtils.isJsonObject;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.UNICODE_CASE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import boxfish.commons.web.model.RestModel;

class SanitizerForJson extends SanitizerFor<RestModel, String> {
    private static final Pattern JSON_EXTRACTOR = Pattern.compile(
        "[\\\"\\{]?[\\s]*([^\\s\\\"\\{]+)[\\s]*[\\\"]?[\\s]*[\\:][\\s]*(?:(?=\\{)([\\s\\S]*\\})[\\s]*\\,|[\\\"]?[\\s]*[\\\"]?([^\\\"\\,\\}]+))[\\\"\\}]?",
        CASE_INSENSITIVE | UNICODE_CASE | MULTILINE);

    SanitizerForJson(final Object rawValue) {
        super(rawValue, String.class);
    }

    @Override
    protected RestModel sanitizedValue() {
        if (String.class.equals(getRawClass()) && isJsonObject(getRawValue()))
            return newModelFromString((String) getRawValue());

        throw new UnsupportedOperationException(
            "Although we had a JSON, we could not sanitize it into a RestModel.");
    }

    private RestModel newModelFromString(final String raw) {
        final RestModel model = newRestModel();

        final Matcher matcher = JSON_EXTRACTOR.matcher(raw);
        while (matcher.find()) {
            final String key = matcher.group(1);
            final String value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            model.value(key, value);
        }
            
        return model;
    }
}
