import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.nabsky.router.HTTPRouter;
import ru.nabsky.helper.DatabaseHelper;
import ru.nabsky.module.BindingModule;

public class TimeTableApp {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BindingModule());
        DatabaseHelper.updateDesignDocuments();
        new HTTPRouter(injector);
    }
}
