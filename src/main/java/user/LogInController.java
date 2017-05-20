package user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class LogInController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    public void initialize() {
        System.out.println("Yeeeeeha");
    }

    public void signUpButtonPressed(ActionEvent actionEvent) throws IOException {
        /*Parent parent = FXMLLoader.load(getClass().getResource("views/SignUpView.fxml"));
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }

    public void logInButtonPressed(ActionEvent actionEvent) {
        System.out.println("LogIn Pressed");
        User user = User.getUserByUsername(usernameField.getText());
        if (user != null) {
            /*
            if (passwordField.getText() == user.getPassword()) {

            }*/
            System.out.println("LogIn Pressed");
        }
    }
}
