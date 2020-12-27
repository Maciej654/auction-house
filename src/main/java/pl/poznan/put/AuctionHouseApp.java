package pl.poznan.put;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AuctionHouseApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        var resource = AuctionHouseApp.class.getResource("view/user/info/user_info.fxml");
        var root     = FXMLLoader.<Parent>load(resource);
        var scene    = new Scene(root);
        primaryStage.setTitle("Welcome to Auction House");
        primaryStage.setScene(scene);
        primaryStage.show();
        log.info("started");
    }
}
