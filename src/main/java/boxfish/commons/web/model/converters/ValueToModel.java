package boxfish.commons.web.model.converters;

import java.io.InvalidClassException;

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
    public Model parse() throws Exception {
        if (getValue() == null)
            return null;

        if (Model.class.equals(getValueClass()))
            return (Model) getValue();

        throw new InvalidClassException(String.format(
            "Impossible to convert %s to Model",
            getValueClass().getName()));
    }

}
