package pl.poznan.put.controller.auction.crud.create.photos.list.cell;

import javafx.scene.control.ListCell;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.photos.list.item.AuctionCreatePhotosListItemController;
import pl.poznan.put.util.view.loader.ViewLoader;

import java.io.File;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class AuctionCreatePhotosListCell extends ListCell<File> {
    private final Consumer<File> onDeleteButtonClick;

    private AuctionCreatePhotosListItemController controller;

    @Override
    protected void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
            controller = null;
            return;
        }

        if (controller == null) {
            val root = ViewLoader.getParent(AuctionCreatePhotosListItemController.class, controller -> {
                controller.setOnDeleteButtonClick(onDeleteButtonClick);
                this.controller = controller;
            });
            setGraphic(root);
        }
        controller.getFileProperty().set(item);
    }
}
