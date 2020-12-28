package pl.poznan.put;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

@Slf4j
public class AuctionHouseApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        val resource = AuctionHouseApp.class.getResource("view/user/login/user-login.fxml");
        val root     = FXMLLoader.<Parent>load(resource);
        val scene    = new Scene(root);
        primaryStage.setTitle("Auction House");
        primaryStage.setScene(scene);
        primaryStage.show();
        log.info("started");
    }
}
