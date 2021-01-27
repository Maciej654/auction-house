package pl.poznan.put.controller.auction.crud.create.photos.list.item;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.util.callback.Callbacks;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

@Slf4j
public class AuctionCreatePhotosListItemController {
    @FXML
    private Label label;

    @FXML
    private ImageView imageView;

    @Setter
    private Consumer<File> onDeleteButtonClick = Callbacks::noop;

    @Getter
    private final ObjectProperty<File> fileProperty = new SimpleObjectProperty<>();

    @FXML
    private void initialize() {
        log.info("initialize");

        fileProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                log.info("delete {}", label.getText());
                label.setText(null);
                imageView.setImage(null);
                onDeleteButtonClick.accept(oldValue);
            }
            else try {
                val stream = new FileInputStream(newValue);
                val image  = new Image(stream, 100, 100, true, true);
                imageView.setImage(image);
                label.setText(newValue.getName());
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @FXML
    private void deleteButtonClick() {
        fileProperty.set(null);
    }
}
