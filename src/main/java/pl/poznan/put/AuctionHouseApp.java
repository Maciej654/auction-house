package pl.poznan.put;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.auction.details.AuctionDetailsController;
import pl.poznan.put.controller.browser.BrowserController;
import pl.poznan.put.controller.user.crud.create.UserCreateController;
import pl.poznan.put.controller.user.crud.update.UserUpdateController;
import pl.poznan.put.controller.user.login.UserLoginController;
import pl.poznan.put.controller.user.page.UserPageController;
import pl.poznan.put.controller.user.page.UserPageController.Type;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public class AuctionHouseApp extends Application {
    private Stage primaryStage;
    private Scene prevScene;
    private Scene currScene;

    private <T> String getViewName(Class<T> clazz) {
        return clazz.getSimpleName().replace("Controller", "View") + ".fxml";
    }

    private <T> String getViewDirectory(Class<T> clazz) {
        return clazz.getPackageName()
                    .replace('.', '/')
                    .replace("/controller/", "/view/");
    }

    private <T> String getViewResourcePath(Class<T> clazz) {
        return '/' + getViewDirectory(clazz) + '/' + getViewName(clazz);
    }

    private void updateScene() {
        primaryStage.setScene(currScene);
    }

    private <T> void runPage(Class<T> clazz, Consumer<T> setup) {
        val path     = getViewResourcePath(clazz);
        val resource = AuctionHouseApp.class.getResource(path);
        try (val stream = resource.openStream()) {
            val loader     = new FXMLLoader(resource);
            val root       = loader.<Parent>load(stream);
            val controller = loader.<T>getController();
            setup.accept(controller);
            val scene = new Scene(root);
            prevScene = currScene;
            currScene = scene;
            updateScene();
        }
        catch (NullPointerException | IOException e) {
            log.error("An error occurred", e);
        }
    }

    private void runPrevPage() {
        val tmp = currScene;
        currScene = prevScene;
        prevScene = tmp;
        updateScene();
    }

    private void runPrivateUserPage(User user) {
        log.info("private user page");

        this.runPage(UserPageController.class, controller -> {
            controller.setType(Type.PRIVATE);
            controller.setUser(user);
            controller.setEditCallback(this::runUserUpdatePage);
            controller.setAuctionsCallback(() -> {});
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

    private void runBrowser() {
        log.info("browser page");

        this.runPage(BrowserController.class, controller -> {
            controller.setup();
            controller.setShowAuctionDetails(this::runAuctionDetails);
        });
    }

    private void runAuctionDetails(Auction auction) {
          this.runPage(AuctionDetailsController.class, controller -> {
              controller.setAuction(auction);
              controller.setLabels();
              controller.setKeyCallBack(this::runPrevPage);
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
