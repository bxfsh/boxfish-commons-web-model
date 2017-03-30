package boxfish.commons.web.model.sanitization;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import boxfish.commons.web.model.RestModel;

public class SanitizerForJsonTest {

    @Test
    public void sanitize_json() {
        final String original = "{NAKED_FIELD: true,\n"
                                + "  \"field1\" : \"name-548523 4k3458234 \n"
                                + "asdfhasdfjh \\herhu\n"
                                + "adsfaksdjf0952345\n"
                                + "5234\",\n\n"
                                + "    \"field2\": false, \"field3\": 123.348341234 \n"
                                + "\"field4\": {\n"
                                + "            \"sub-field-1\":false,\n"
                                + "            \"sub-field-2\": {\n"
                                + "                        \"sub-sub-field-1\":false,\n"
                                + "                        \"sub-sub-field-2\": {\n"
                                + "                            \"sub-field\":false\n"
                                + "                        }\n"
                                + "            },\n"
                                + "            \"sub-field-1\":false,\n"
                                + "            \"sub-field-2\": {\n"
                                + "                        \"sub-sub-field-1\":false,\n"
                                + "                        \"sub-sub-field-2\": {\n"
                                + "                            \"sub-field\":false\n"
                                + "                        }\n"
                                + "            }\n"
                                + "},\n"
                                + "    end_field:\"whatever\"}";

        final Object actual = new SanitizerForJson(original).sanitize();
        assertThat(actual, instanceOf(RestModel.class));

        final RestModel parsed = (RestModel) actual;
        parsed.permit("NAKED_FIELD", "field1", "field_2", "field3", "field4", "endField");
        assertEquals(new Boolean("true"), parsed.get("NAKED_FIELD").asBoolean());
        assertEquals("name-548523 4k3458234 \nasdfhasdfjh \\herhu\nadsfaksdjf0952345\n5234", parsed.get("field_1").asString());
        assertEquals(new Boolean("false"), parsed.get("field2").asBoolean());
        assertEquals(new BigDecimal("123.348341234"), parsed.get("field_3").asBigDecimal());
        assertEquals("whatever", parsed.get("endField").asString());
        assertEquals(new Boolean(false), parsed.get("field4").asModel().permit("sub-field-1").get("sub-field-1").asBoolean());
    }
}
