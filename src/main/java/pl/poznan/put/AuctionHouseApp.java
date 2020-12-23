package pl.poznan.put;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuctionHouseApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        var resource = AuctionHouseApp.class.getResource("fxml_trzy.fxml");
        var root     = FXMLLoader.<Parent>load(resource);
        var scene    = new Scene(root);
        primaryStage.setTitle("Welcome to Auction House");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
