package user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotExistingUserException;
import exceptions.NotSamePasswordException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import survey.SurveyListControllerView;
import utils.Constants;

import java.io.IOException;


public class LogInControllerView {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label errorText;

    private Stage stage;

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void signUpButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SignUpView.fxml").openStream());
        SignUpControllerView controller = loader.getController();
        controller.init(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void logInButtonPressed() throws IOException {
        try {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) throw new EmptyRequiredAttributeException();
            User.validateCredentials(usernameField.getText(), passwordField.getText());
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
            SurveyListControllerView controller = loader.getController();
            controller.init(stage, User.getUserByUsername(usernameField.getText()));
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
            stage.setScene(scene);
            stage.show();
        } catch (NotExistingUserException e) {
            errorText.setText("Wrong user");
        } catch (NotSamePasswordException e) {
            errorText.setText("Wrong password");
        } catch (EmptyRequiredAttributeException e) {
            errorText.setText("Missing filed(s)");
        }
    }
}
