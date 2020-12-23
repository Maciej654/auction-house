package pl.poznan.put.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FxmlSample {
    public TableView<MyDataType> table1;

    private static final ObservableList<MyDataType> data = FXCollections.observableArrayList(
            new MyDataType("aaaaaaaa","baaaaaa","caaaaaaaa","daaaaaa", "eaaaaa", "faaaaa", "daaaaaa"));



    public void click() {

        table1.setItems(data);

    }
}
