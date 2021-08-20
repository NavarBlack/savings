package su.savings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            System.out.println();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MainViews.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Бююююджееееет!!!!");
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/NBank.png"))));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (LoadException ex) {
            ex.printStackTrace();
            Label helloWorldLabel = new Label(ex.getCause().getMessage());
            helloWorldLabel.setAlignment(Pos.CENTER);
            Scene primaryScene = new Scene(helloWorldLabel);
            primaryStage.setScene(primaryScene);
            primaryStage.show();
        } catch (NullPointerException ex){
            ex.printStackTrace();
            Label helloWorldLabel = new Label("Что то пошло не так");
            helloWorldLabel.setAlignment(Pos.CENTER);
            Scene primaryScene = new Scene(helloWorldLabel);
            primaryStage.setTitle("Упс");
            primaryStage.setScene(primaryScene);
            primaryStage.show();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
