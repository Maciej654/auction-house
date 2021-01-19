package pl.poznan.put.controller.auction.details.history;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
public class AuctionHistoryController {
    @FXML
    private TableView<Data> historyTableView;

    @FXML
    private TableColumn<AuctionLog, LocalDateTime> dateTableColumn;

    @FXML
    private TableColumn<AuctionLog, String> descriptionTableColumn;

    private TypedQuery<AuctionLog> selectQuery;

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();


    @FXML
    private void initialize() {
        log.info("initialize");

        val em = EntityManagerProvider.getEntityManager();
        if (em != null) selectQuery = em.createNamedQuery(AuctionLog.QUERY_FIND_ALL_BY_AUCTION, AuctionLog.class);

        auctionProperty.addListener((observable, oldValue, newValue) -> updateHistory());

        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @lombok.Data
    @AllArgsConstructor
    public static class Data {
        private LocalDateTime timestamp;
        private String        description;
    }

    public void updateHistory() {
        val auction = auctionProperty.get();
        if (auction == null) {
            log.warn("auction is null");
            return;
        }

        if (selectQuery == null) {
            log.warn("query is null");
            return;
        }

        selectQuery.setParameter(AuctionLog.PARAM_AUCTION, auction);
        val resultList = selectQuery.getResultStream()
                                    .map(r -> new Data(r.getTimestamp(), r.getDescription()))
                                    .collect(Collectors.toList());
        val auctionLogs = FXCollections.observableArrayList(resultList);
        historyTableView.setItems(auctionLogs);
    }
}
