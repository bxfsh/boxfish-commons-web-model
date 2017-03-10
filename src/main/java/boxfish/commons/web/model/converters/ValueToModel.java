package boxfish.commons.web.model.converters;

import java.util.Map;

import boxfish.commons.web.model.Model;

/**
 * Sophisticated type conversion and parsing from Object to Model,
 * which aims to perform any possible casting or parsing in representing
 * the original value as such.
 *
 * @author Hudson Mendes
 *
 */
public class ValueToModel extends AbstractValueConverter<Model> {

    public ValueToModel(final Object value) {
        super(value);
    }

    @Override
    public Model parse() {
        if (getValue() != null)
            if (shouldBeReplacedByModel()) {
                overrideValue(newModelFromMap((Map<?, ?>) getValue()));
            }

        if (Model.class.equals(getValueClass()))
            return (Model) getValue();

        return Model.create();
    }

    /**
     * Analyses if the value is a map and should be replaced by a model.
     * 
     * @return
     */
    private boolean shouldBeReplacedByModel() {
        return !Model.class.equals(getValueClass()) && Map.class.isAssignableFrom(getValueClass());
    }

    /**
     * Translates a map to a model, to avoid keeping Maps in our structure.
     * Maps lack the permit/require instructions and can cause problems
     * for nested permit/require commands.
     *
     * @param value the map that will be translated
     */
    public static Model newModelFromMap(final Map<?, ?> value) {
        if (value != null)
            if (Model.class.equals(value.getClass()))
                return (Model) value;

        final Model translation = Model.create();
        if (value != null)
            value.forEach((k, v) -> {
                if (k != null)
                    translation.put(k.toString(), v);
            });
        return translation;
    }

}
