package pl.poznan.put;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.AuctionCreateController;
import pl.poznan.put.controller.auction.details.AuctionDetailsController;
import pl.poznan.put.controller.auction.shoppingHistory.ShoppingHistoryController;
import pl.poznan.put.controller.browser.BrowserController;
import pl.poznan.put.controller.delivery.DeliveryCreatorController;
import pl.poznan.put.controller.followers.FollowersController;
import pl.poznan.put.controller.rating.RatingBrowser;
import pl.poznan.put.controller.rating.RatingCreator;
import pl.poznan.put.controller.shoppingCart.ShoppingCartController;
import pl.poznan.put.controller.user.crud.create.UserCreateController;
import pl.poznan.put.controller.user.crud.update.UserUpdateController;
import pl.poznan.put.controller.user.login.UserLoginController;
import pl.poznan.put.controller.user.page.UserPageController;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.view.loader.ViewLoader;

import java.util.function.Consumer;

@Slf4j
public class AuctionHouseApp extends Application {
    private Stage primaryStage;

    private <T> void runPage(Class<T> clazz, Consumer<T> setup) {
        Platform.runLater(() -> {
            val root      = ViewLoader.getParent(clazz, setup);
            val nextScene = new Scene(root);
            primaryStage.setScene(nextScene);
        });
    }

    private void runAuctionCreatePage(User user) {
        log.info("create auction page");

        this.runPage(AuctionCreateController.class, controller -> {
            controller.getUserProperty().set(user);
            Consumer<User> backCallback          = this::runUserPage;
            Consumer<Auction> createAuctionCallback = auction -> runAuctionDetailsPage(auction,user, backCallback);
            controller.setCreateAuctionCallback(createAuctionCallback);
        });
    }

    private void runUserPage(User user) {
        log.info("user page");

        this.runPage(UserPageController.class, controller -> {
            Consumer<User>          backCallback      = this::runUserPage;
            Consumer<Auction> thumbnailCallback = auction -> runAuctionDetailsPage(auction,user, backCallback);
            controller.setThumbnailCallback(thumbnailCallback);
            controller.getUserProperty().set(user);
            controller.setAuctionsCallback(this::runBrowserPage);
            controller.setEditCallback(this::runUserUpdatePage);
            controller.setCreateAuctionCallback(this::runAuctionCreatePage);

            controller.setReviewCreatorCallback(() -> runRatingCreator(user));
            controller.setShoppingCartBrowserCallback(() -> runShoppingCart(user));
            controller.setReviewBrowserCallback(() -> runRatingBrowser(user));
            controller.setFollowerCreatorCallback(() -> runFollowerCreator(user));
            controller.setDeliveryCallback(() -> runDeliveryPreferencesCreator(user));
            controller.setHistoryCallback(() -> runShoppingHistory(user));
        });
    }

    private void runLoginPage() {
        log.info("login page");

        this.runPage(UserLoginController.class, controller -> controller.setRegisterCallback(this::runUserCreatePage));
    }

    private void runUserUpdatePage(User user) {
        log.info("user update page");

        this.runPage(UserUpdateController.class, controller -> {
            controller.setUser(user);
            controller.setOperationCallback(this::runUserPage);
            controller.setBackCallback(() -> runUserPage(user));
        });
    }

    private void runUserCreatePage(String email, String password) {
        log.info("user create page");

        this.runPage(UserCreateController.class, controller -> {
            controller.setEmail(email);
            controller.setPassword(password);
            controller.setOperationCallback(this::runUserPage);
            controller.setBackCallback(this::runLoginPage);
        });
    }

    private void runBrowserPage(User user) {
        log.info("browser page");
        Consumer<User> consumer = this::runBrowserPage;
        Consumer<Auction> showAuctionDetails = auction -> runAuctionDetailsPage(auction,user, consumer);
        Consumer<BrowserController> setup =
                controller -> controller.setShowAuctionDetails(showAuctionDetails);

        this.runPage(BrowserController.class, controller -> {
            controller.setUserPageCallback(() -> runUserPage(user));
            controller.setShowAuctionDetails(showAuctionDetails);
            controller.getUserProperty().setValue(user);
        });
    }

    private void runAuctionDetailsPage(Auction auction, User user,  Consumer<User> backCallback) {
        this.runPage(AuctionDetailsController.class, controller -> {
            controller.getUserProperty().set(user);
            controller.getAuctionProperty().set(auction);
            controller.setBackCallback(backCallback);
            controller.setUserHyperlinkCallback(this::runUserPage);
        });
    }

    private void runRatingBrowser(User user) {
        this.runPage(RatingBrowser.class, controller -> controller.setUserPageCallback(() -> this.runUserPage(user)));
    }

    private void runRatingCreator(User user) {
        this.runPage(RatingCreator.class, controller -> {
            controller.setUser(user);
            controller.setup();
            controller.setUserPageCallback(() -> runUserPage(user));
        });
    }

    private void runDeliveryPreferencesCreator(User user) {
        this.runPage(DeliveryCreatorController.class,controller -> {
            controller.setUser(user);
            controller.setUserPageCallback(() -> runUserPage(user));
            controller.setup();
        });
    }

    private void runShoppingCart(User user) {
        this.runPage(ShoppingCartController.class, controller ->{
            controller.setUserPageCallback(() -> this.runUserPage(user));
            controller.setup(user);
        } );
    }

    private void runShoppingHistory(User user) {
        this.runPage(ShoppingHistoryController.class, controller -> {
            controller.setUser(user);
            controller.setUserPageCallback(() -> runUserPage(user));
            controller.setup();
        });
    }

    private void runFollowerCreator(User user) {
        this.runPage(FollowersController.class, controller ->{
            controller.setUp(user);
            controller.setUserPageCallback(() -> this.runUserPage(user));
        });
    }


    @Override
    public void start(Stage primaryStage) {
        log.info("start");

        primaryStage.setTitle("Auction House");
        primaryStage.getIcons().add(new Image("/icons/auction-32.png"));
        this.primaryStage = primaryStage;

        CurrentUser.getLoggedInUserProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) runLoginPage();
            else runUserPage(newValue);
        });

        this.runLoginPage();

        primaryStage.show();
    }
}
