import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    public static final String APP_TITLE = "Enquestator 1.0";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/SignUpView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 350);

        primaryStage.setTitle(APP_TITLE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void signUpButtonPressed(ActionEvent actionEvent) throws IOException {

    }

    public void logInButtonPressed(ActionEvent actionEvent) {
        System.out.println("LogIn Pressed");
    }

}
