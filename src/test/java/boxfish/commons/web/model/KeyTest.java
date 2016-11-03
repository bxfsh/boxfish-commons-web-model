package boxfish.commons.web.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class KeyTest {
    private final static String CAMEL_CASED_FIELD_NAME = "thisField11234,.IsWrittenInCamelCaseToBeConverted";
    private Key namer;

    @Before
    public void setup() {
        namer = new Key(CAMEL_CASED_FIELD_NAME);
    }

    @Test
    public void build() {
        final String actual = namer.build();
        assertNotNull(actual);
        assertEquals("this_field_11234_is_written_in_camel_case_to_be_converted", actual);
    }

}
