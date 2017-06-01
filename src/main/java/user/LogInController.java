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
import survey.SurveyListController;

import java.io.IOException;


public class LogInController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label errorText;

    private Stage stage;

    public void initialize() {

    }

    public void signUpButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SignUpView.fxml").openStream());
        SignUpController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void logInButtonPressed() throws IOException {
        try {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) throw new EmptyRequiredAttributeException();
            User.validateCredentials(usernameField.getText(), passwordField.getText());
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
            SurveyListController controller = loader.getController();
            controller.setStage(stage);
            controller.setUser(User.getUserByUsername(usernameField.getText()));
            controller.init();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(STYLE);
            scene.getStylesheets().add(FONTS);
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
