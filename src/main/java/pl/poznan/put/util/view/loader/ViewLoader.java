package pl.poznan.put.util.view.loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.function.Consumer;

@Slf4j
@UtilityClass
public class ViewLoader {
    private <T> String getViewName(Class<T> clazz) {
        return clazz.getSimpleName().replace("Controller", "View") + ".fxml";
    }

    private <T> String getViewDirectory(Class<T> clazz) {
        return clazz.getPackageName()
                    .replace('.', '/')
                    .replace("/controller/", "/view/");
    }

    private <T> String getViewResourcePath(Class<T> clazz) {
        return '/' + getViewDirectory(clazz) + '/' + getViewName(clazz);
    }

    private <T> FXMLLoader getFxmlViewLoader(Class<T> clazz) {
        val path = getViewResourcePath(clazz);
        log.info("Loader of {}@{}", clazz.getSimpleName(), path);
        val resource = ViewLoader.class.getResource(path);
        return new FXMLLoader(resource);
    }

    public <T> Parent getParent(Class<T> clazz, Consumer<T> setup) {
        try {
            val loader     = getFxmlViewLoader(clazz);
            val root       = loader.<Parent>load();
            val controller = loader.<T>getController();
            setup.accept(controller);
            return root;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
