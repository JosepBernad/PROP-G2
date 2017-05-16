import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LogIn.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Enquestator 1.0");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void signUpButtonPressed(ActionEvent actionEvent) throws IOException {

    }

    public void logInButtonPressed(ActionEvent actionEvent) {
        System.out.println("LogIn Pressed");
    }
}
