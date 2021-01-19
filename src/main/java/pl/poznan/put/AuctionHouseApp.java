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
import pl.poznan.put.controller.browser.BrowserController;
import pl.poznan.put.controller.rating.RatingBrowser;
import pl.poznan.put.controller.rating.RatingCreator;
import pl.poznan.put.controller.user.crud.create.UserCreateController;
import pl.poznan.put.controller.user.crud.update.UserUpdateController;
import pl.poznan.put.controller.user.login.UserLoginController;
import pl.poznan.put.controller.user.page.UserPageController;
import pl.poznan.put.controller.user.page.UserPageController.Type;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.view.loader.ViewLoader;

import java.util.function.Consumer;

@Slf4j
public class AuctionHouseApp extends Application {
    private Stage primaryStage;
    private Scene prevScene;
    private Scene currScene;

    private void updateScene(Scene nextScene) {
        prevScene = currScene;
        currScene = nextScene;
        primaryStage.setScene(nextScene);
    }

    private <T> void runPage(Class<T> clazz, Consumer<T> setup) {
        Platform.runLater(() -> {
            val root      = ViewLoader.getParent(clazz, setup);
            val nextScene = new Scene(root);
            updateScene(nextScene);
        });
    }

    private void runPrevPage() {
        Platform.runLater(() -> updateScene(prevScene));
    }

    private void runAuctionCreatePage(User user) {
        log.info("create auction page");

        this.runPage(AuctionCreateController.class, controller -> {
            controller.getUserProperty().set(user);
            controller.setCreateAuctionCallback(this::runAuctionDetailsPage);
        });
    }

    private void standardUserPageControllerSetup(UserPageController controller, User user) {
        controller.getUserProperty().set(user);
        controller.setAuctionsCallback(() -> {});
    }

    private void runPrivateUserPage(User user) {
        log.info("private user page");

        this.runPage(UserPageController.class, controller -> {
            controller.setType(Type.PRIVATE);
            standardUserPageControllerSetup(controller, user);
            controller.setEditCallback(this::runUserUpdatePage);
            controller.setCreateAuctionCallback(this::runAuctionCreatePage);
            controller.setLogoutCallback(this::runLoginPage);
        });
    }

    private void runLoginPage() {
        log.info("login page");

        this.runPage(UserLoginController.class, controller -> {
            controller.setLoginCallback(this::runPrivateUserPage);
            controller.setRegisterCallback(this::runUserCreatePage);
        });
    }

    private void runUserUpdatePage(User user) {
        log.info("user update page");

        this.runPage(UserUpdateController.class, controller -> {
            controller.setUser(user);
            controller.setOperationCallback(this::runPrivateUserPage);
            controller.setBackCallback(this::runPrevPage);
        });
    }

    private void runUserCreatePage(String email, String password) {
        log.info("user create page");

        this.runPage(UserCreateController.class, controller -> {
            controller.setEmail(email);
            controller.setPassword(password);
            controller.setOperationCallback(this::runPrivateUserPage);
            controller.setBackCallback(this::runPrevPage);
        });
    }

    private void runBrowserPage() {
        log.info("browser page");

        this.runPage(BrowserController.class, controller -> {
            controller.setShowAuctionDetails(this::runAuctionDetailsPage);
        });
    }

    private void runAuctionDetailsPage(Auction auction) {
          this.runPage(AuctionDetailsController.class, controller -> {
              controller.getAuctionProperty().set(auction);
              controller.setKeyCallBack(this::runPrevPage);
        });
    }

    private void runRatingBrowser() {
        this.runPage(RatingBrowser.class, controller -> {
        });
    }

    private void runRatingCreator() {
        this.runPage(RatingCreator.class, controller -> {
        });
    }

    @Override
    public void start(Stage primaryStage) {
        log.info("start");

        primaryStage.setTitle("Auction House");
        primaryStage.getIcons().add(new Image("/icons/auction-32.png"));
        this.primaryStage = primaryStage;

        this.runLoginPage();

        primaryStage.show();
    }
}
