package boxfish.commons.web.model.utils;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class JsonUtilsTest {

    @Test
    public void isJsonObject_positive() {
        final List<String> valids = asList(
            "{\"field\": \"value\"}",
            "{\"field\": \"value\", \"field2\": 482345.1234}");

        for (final String valid : valids)
            assertTrue(
                format("Failed the pattern '%s'", valid),
                JsonUtils.isJsonObject(valid));
    }

    @Test
    public void isJsonObject_negative() {
        final List<String> valids = asList(
            "{\"field\"}",
            "[{\"field\": \"value\", \"field2\": 482345.1234}]",
            "akfuasdf",
            "[\"sdfadf\", \"34225\"]");

        for (final String valid : valids)
            assertFalse(
                format("Should have failed the pattern '%s'", valid),
                JsonUtils.isJsonObject(valid));
    }

}
