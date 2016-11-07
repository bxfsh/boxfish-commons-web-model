package boxfish.commons.web.model;

public interface Scrutinizer {

    public void forQuery(Model model);

    public void forCreate(Model model);

    public void forUpdate(Model model);

}
