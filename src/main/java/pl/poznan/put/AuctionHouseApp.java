package pl.poznan.put;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final static BooleanProperty runningProperty = new SimpleBooleanProperty();

    public static ReadOnlyBooleanProperty isRunningProperty() {
        return runningProperty;
    }

    private Stage primaryStage;

    private <T> void runPage(Class<T> clazz, Consumer<T> setup) {
        Platform.runLater(() -> {
            val root  = ViewLoader.getParent(clazz, setup);
            var scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                primaryStage.setScene(scene);
            }
            else scene.setRoot(root);
        });
    }

    private void runAuctionCreatePage(User user) {
        log.info("create auction page");

        this.runPage(AuctionCreateController.class, controller -> {
            controller.getUserProperty().set(user);
            Runnable          backCallback          = () -> runUserPage(user);
            Consumer<Auction> createAuctionCallback = auction -> runAuctionDetailsPage(auction, backCallback);
            controller.setBackCallback(backCallback);
            controller.setCreateAuctionCallback(createAuctionCallback);
        });
    }

    private void runUserPage(User user) {
        log.info("user page");

        this.runPage(UserPageController.class, controller -> {
            Runnable          backCallback      = () -> runUserPage(user);
            Consumer<Auction> thumbnailCallback = auction -> runAuctionDetailsPage(auction, backCallback);
            controller.setThumbnailCallback(thumbnailCallback);
            controller.getUserProperty().set(user);
            controller.setAuctionsCallback(() -> runBrowserPage(user));
            controller.setEditCallback(this::runUserUpdatePage);
            controller.setCreateAuctionCallback(this::runAuctionCreatePage);
            controller.setLogoutCallback(this::runLoginPage);

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

        CurrentUser.setLoggedInUser(null);
        this.runPage(UserLoginController.class, controller -> {
            controller.setRegisterCallback(this::runUserCreatePage);
            controller.setLoginCallback(this::runUserPage);
        });
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
        this.runPage(BrowserController.class, controller -> {
            Runnable          backCallback       = () -> runBrowserPage(user);
            Consumer<Auction> showAuctionDetails = auction -> runAuctionDetailsPage(auction, backCallback);
            controller.setShowAuctionDetails(showAuctionDetails);
            controller.setBackCallback(() -> runUserPage(user));
            controller.setOwnProfileCallback(this::runUserPage);
        });
    }

    private void runAuctionDetailsPage(Auction auction, Runnable backCallback) {
        this.runPage(AuctionDetailsController.class, controller -> {
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
        this.runPage(DeliveryCreatorController.class, controller -> {
            controller.setUser(user);
            controller.setUserPageCallback(() -> runUserPage(user));
            controller.setup();
        });
    }

    private void runShoppingCart(User user) {
        this.runPage(ShoppingCartController.class, controller -> {
            controller.setUserPageCallback(() -> this.runUserPage(user));
            controller.setup(user);
        });
    }

    private void runShoppingHistory(User user) {
        this.runPage(ShoppingHistoryController.class, controller -> {
            controller.setUser(user);
            controller.setUserPageCallback(() -> runUserPage(user));
            controller.setup();
        });
    }

    private void runFollowerCreator(User user) {
        this.runPage(FollowersController.class, controller -> {
            controller.setUp();
            controller.setUserPageCallback(() -> this.runUserPage(user));
        });
    }

    @Override
    public void start(Stage primaryStage) {
        log.info("start");
        runningProperty.set(true);

        primaryStage.setTitle("Auction House");
        primaryStage.getIcons().add(new Image("/icons/auction-32.png"));
        this.primaryStage = primaryStage;
        this.runLoginPage();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        log.info("stop");

        runningProperty.set(false);
    }
}
