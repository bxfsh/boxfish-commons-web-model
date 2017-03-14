package boxfish.commons.web.model.sanitization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import boxfish.commons.web.model.RestModel;

@SuppressWarnings("rawtypes")
class SanitizerForLists extends SanitizerFor<List, List> {

    SanitizerForLists(final Object rawValue) {
        super(rawValue, List.class);
    }

    @Override
    protected List sanitizedValue() {
        final List<Object> newList = new ArrayList<>();
        final List<?> parsedList = (List<?>) getRawValue();
        for (final Object item : parsedList)
            if (isItemAMapThatNeedsSanitization(item))
                newList.add(new SanitizerForMaps(item).sanitizedValue());
            else
                newList.add(item);
        return newList;

    }

    private boolean isItemAMapThatNeedsSanitization(final Object item) {
        if (item != null) {
            final Class<?> itemClass = item.getClass();
            final boolean isAKindOfMap = Map.class.isAssignableFrom(itemClass);
            final boolean isOurModelType = RestModel.class.equals(itemClass);
            return isAKindOfMap && !isOurModelType;
        }
        return false;
    }
}
