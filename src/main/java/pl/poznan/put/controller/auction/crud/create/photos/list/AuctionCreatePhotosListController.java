package pl.poznan.put.controller.auction.crud.create.photos.list;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.photos.list.cell.AuctionCreatePhotosListCell;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public class AuctionCreatePhotosListController {
    @FXML
    private ListView<File> photosListView;

    private final ListProperty<File> photosList = new SimpleListProperty<>(FXCollections.observableArrayList());

    public void addPhotos(Collection<File> files) {
        photosList.addAll(files);
    }

    public Collection<File> getPhotos() {
        val array = photosList.toArray(File[]::new);
        val list  = Arrays.asList(array);
        return Collections.unmodifiableCollection(list);
    }

    public ReadOnlyBooleanProperty getPhotosListEmptyProperty() {
        return photosList.emptyProperty();
    }

    @FXML
    private void initialize() {
        log.info("initialize");

        photosListView.setItems(photosList);
        photosListView.setCellFactory(view -> new AuctionCreatePhotosListCell(photosList::remove));
    }
}
