package user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exceptions.DuplicatedUsernameException;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotSamePasswordException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import survey.SurveyListController;

import java.io.IOException;

public class SignUpController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXPasswordField password1;

    @FXML
    private JFXPasswordField password2;

    @FXML
    private Label textInformation;

    private Stage stage;

    public void createUserButtonPressed() throws IOException, NotSamePasswordException {
        try {
            User user = new User();
            if (!password1.getText().isEmpty() && !usernameField.getText().isEmpty() && !nameField.getText().isEmpty() &&
                    !password1.getText().equals(password2.getText())) throw new NotSamePasswordException();
            user.setUsername(usernameField.getText());
            user.setName(nameField.getText());
            user.setPassword(password1.getText());
            user.save();
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

        } catch (DuplicatedUsernameException e) {
            textInformation.setText("This user is already taken");
        } catch (EmptyRequiredAttributeException e) {
            textInformation.setText("Missing field(s)");
        } catch (NotSamePasswordException e) {
            textInformation.setText("Not the same password");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void cancelButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/LogInView.fxml").openStream());
        LogInController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }
}
