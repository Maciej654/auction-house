package pl.poznan.put.controller.auction.crud.create.photos;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.engine.jdbc.BlobProxy;
import pl.poznan.put.controller.auction.crud.create.photos.list.AuctionCreatePhotosListController;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.auction.validation.PhotosListNotEmptyPropertyValidator;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.picture.Picture;
import pl.poznan.put.util.file.ProjectFileUtils;
import pl.poznan.put.util.validation.Validation;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class AuctionCreatePhotosController extends AbstractValidatedController {
    @FXML
    private ImageView warning;

    @FXML
    private AuctionCreatePhotosListController auctionCreatePhotosListController;

    @Override
    protected void installValidation() {
        Validation.install(
                auctionCreatePhotosListController.getPhotosListEmptyProperty(),
                informationValid,
                warning,
                new PhotosListNotEmptyPropertyValidator()
        );
    }

    @Override
    protected void setupInitialValues() {
        warning.setVisible(true);
    }

    @FXML
    private void addPicturesButtonClick() {
        val chooser = new FileChooser();
        chooser.setTitle("Choose pictures");
        chooser.setInitialDirectory(ProjectFileUtils.homeDirectory);
        val filter = new ExtensionFilter("Pictures (.png, .jpg, .jpeg)", "*.png", "*.jpg", "*.jpeg");
        chooser.getExtensionFilters().add(filter);
        val files = chooser.showOpenMultipleDialog(null);
        if (files != null && !files.isEmpty()) auctionCreatePhotosListController.addPhotos(files);
    }

    public List<Picture> getPictures(Auction auction) {
        return auctionCreatePhotosListController
                .getPhotos()
                .stream()
                .map(file -> {
                    try {
                        val bytes = Files.readAllBytes(file.toPath());
                        val blob  = BlobProxy.generateProxy(bytes);
                        return new Picture(auction, file.getName(), blob);
                    }
                    catch (IOException e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
