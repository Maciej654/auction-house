package pl.poznan.put.controller.auction.details.history;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.auction.log.AuctionLog;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AuctionHistoryController {
    @FXML
    private TableView<AuctionLog> historyTableView;

    @FXML
    private TableColumn<AuctionLog, Date> dateTableColumn;

    @FXML
    private TableColumn<AuctionLog, String> descriptionTableColumn;

    private static class SimpleDateFormatTableCell extends TableCell<AuctionLog, Date> {
        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        protected void updateItem(Date item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) setText("");
            else setText(FORMAT.format(item));
        }
    }

    @FXML
    private void initialize() {
        log.info("initialize");

        dateTableColumn.setCellFactory(column -> new SimpleDateFormatTableCell());
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        historyTableView.getSortOrder().add(dateTableColumn);
        historyTableView.getItems().addListener((ListChangeListener<? super AuctionLog>) change -> {
            if (change.next() && change.getAddedSize() > 0) historyTableView.sort();
        });

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                var log = new AuctionLog(new Date(), "Log #" + i);
                historyTableView.getItems().add(log);
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void hello() {
        log.info("hello");
    }
}
