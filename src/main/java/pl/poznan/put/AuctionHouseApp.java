package pl.poznan.put;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.user.info.UserInfo;
import pl.poznan.put.controller.user.info.UserInfo.Action;
import pl.poznan.put.controller.user.login.UserLogin;
import pl.poznan.put.controller.user.page.UserPage;
import pl.poznan.put.model.user.User;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public class AuctionHouseApp extends Application {
    private Stage primaryStage;

    private <T> void runPage(String path, Consumer<T> setup) {
        val resource = AuctionHouseApp.class.getResource(path);
        try (val stream = resource.openStream()) {
            val loader     = new FXMLLoader(resource);
            val root       = loader.<Parent>load(stream);
            val controller = loader.<T>getController();
            setup.accept(controller);
            val scene = new Scene(root);
            primaryStage.setScene(scene);
        }
        catch (NullPointerException | IOException e) {
            log.error("An error occurred", e);
        }
    }

    private void runPrivateUserPage(User user) {
        log.info("private user page");

        this.<UserPage>runPage("view/user/page/user-page.fxml", controller -> {
            controller.setUser(user);
        });
    }

    private void runPublicUserPage(User user) {
        log.info("public user page");
    }

    private void runLoginPage() {
        log.info("login page");

        this.<UserLogin>runPage("view/user/login/user-login.fxml", controller -> {
            controller.setLoginCallback(this::runPrivateUserPage);
            controller.setRegisterCallback(this::runRegisterPage);
        });
    }

    private void runRegisterPage(String email, String password) {
        log.info("register page");

        this.<UserInfo>runPage("view/user/info/user-info.fxml", controller -> {
            controller.setAction(Action.CREATE);
            controller.setEmail(email);
            controller.setPassword(password);
            controller.setSaveCallback(this::runPrivateUserPage);
            controller.setBackCallback(this::runLoginPage);
        });
    }

    @Override
    public void start(Stage primaryStage) {
        log.info("starting...");

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Auction House");

        runLoginPage();
        primaryStage.show();

        log.info("started");
    }
}
