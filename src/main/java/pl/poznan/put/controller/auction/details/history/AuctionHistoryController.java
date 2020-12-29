package pl.poznan.put.controller.auction.details.history;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.auction.log.AuctionLog;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AuctionHistoryController {
    @FXML
    private TableView<AuctionLog> historyTableView;

    @FXML
    private TableColumn<AuctionLog, Long> dateTableColumn;

    @FXML
    private TableColumn<AuctionLog, String> descriptionTableColumn;

    @FXML
    private void initialize() {
        log.info("initialize");

        dateTableColumn.setCellFactory(column -> new SimpleDateFormatTableCell());
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        historyTableView.getSortOrder().add(dateTableColumn);
        historyTableView.getItems().addListener((ListChangeListener<? super AuctionLog>) change -> {
            if (change.next() && change.getAddedSize() > 0) historyTableView.sort();
        });
    }

    public void hello() {
        log.info("hello");
    }

    private static class SimpleDateFormatTableCell extends TableCell<AuctionLog, Long> {
        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        protected void updateItem(Long item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) setText("");
            else {
                val date = new Date(item);
                setText(FORMAT.format(date));
            }
        }
    }
}
