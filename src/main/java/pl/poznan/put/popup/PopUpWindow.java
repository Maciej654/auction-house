package pl.poznan.put.popup;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class PopUpWindow {

    public static void display(String message) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);

        Label  label1  = new Label(message);
        Button button1 = new Button("OK");
        button1.setOnAction(e -> popupWindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 250);
        popupWindow.setScene(scene);
        popupWindow.showAndWait();

    }

}