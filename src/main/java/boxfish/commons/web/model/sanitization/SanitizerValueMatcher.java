package boxfish.commons.web.model.sanitization;

public interface SanitizerValueMatcher {
    Boolean shouldBeSanitized(Class<?> c, Object v);
}
