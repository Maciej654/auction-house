package pl.poznan.put.controller.browser;

import antlr.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.experimental.SuperBuilder;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BrowserController {
    @FXML
    private TextField auction_name;
    @FXML
    private TextField auction_type;

    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> category_column;
    @FXML
    private TableColumn<Data, String>  seller_column;
    @FXML
    private TableColumn<Data, Double>  price_column;
    @FXML
    private TableColumn<Data, LocalDateTime> end_date_column;
    @FXML
    private TableColumn<Data, String>  item_name_column;
    @FXML
    private TableColumn<Data, String>  auction_name_column;
    @FXML
    private TableColumn<Data, Button> details_column;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @SuperBuilder
    @lombok.Data
    public static class Data{
        private String AuctionName;
        private String itemName;
        private LocalDateTime endDate;
        private double price;
        private String seller;
        private String category;
        private String details;
    }

    @FXML
    private void click(MouseEvent mouseEvent) {
        
        List<Auction> listofAuctions = new ArrayList<>();

        if (em != null) {
            TypedQuery<Auction> query = em.createQuery("select auction from Auction auction ", Auction.class);
            listofAuctions = query.getResultStream()
                            .filter(auction -> auction.getStatus().equals(Auction.Status.CREATED))
                            .filter(this::filterByName)
                            .filter(this::filterByType)
                            .collect(Collectors.toList());
        }
        List<Data> list = listofAuctions.stream()
                            .map(auction -> Data.builder()
                                    .AuctionName(auction.getAuctionName())
                                    .itemName(auction.getItemName())
                                    .endDate(auction.getEndDate())
                                    .price(auction.getPrice())
                                    .seller(auction.getSeller().getEmail())
                                    .category(auction.getCategory())
                                    .details("click here")
                                    .build()).collect(Collectors.toList());

        ObservableList<Data> obsList = FXCollections.observableArrayList(list);
        table.setItems(obsList);
        //toDo dodaać przycisk w miejsce Details pozwalający wyświetlić szczegóły aukcji
    }

    private boolean filterByName(Auction auction){
        String name = auction_name.getCharacters().toString().toUpperCase();
        if(name.equals("")){
            return true;
        }
        String regex = String.format(".*%s.*", name);
        String auctionName = auction.getAuctionName().toUpperCase();
        return auctionName.matches(regex);
    }

    private boolean filterByType(Auction auction) {
        String type = auction_type.getCharacters().toString().toUpperCase();
        if (type.equals("")) {
            return true;
        }
        String auctionType = auction.getCategory().toUpperCase();
        return Objects.equals(type, auctionType);
    }
    public void setup(){
        category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        seller_column.setCellValueFactory(new PropertyValueFactory<>("seller"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        end_date_column.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        item_name_column.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        auction_name_column.setCellValueFactory(new PropertyValueFactory<>("AuctionName"));
        details_column.setCellValueFactory(new PropertyValueFactory<>("details"));
    }


}

