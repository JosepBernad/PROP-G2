package user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class LogInController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    private Stage stage;

    public void signUpButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SignUpView.fxml").openStream());
        SignUpController controller = loader.getController();
        controller.setInformation("TUPUTAMADREEEEEEEE");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logInButtonPressed(ActionEvent actionEvent) {
        System.out.println("LogIn Pressed");
        User user = User.getUserByUsername(usernameField.getText());
        if (user != null) {
            if (true) {//passwordField.getText() == user.getPassword()) {
                System.out.println("Right user");
            }
            System.out.println("LogIn Pressed");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
