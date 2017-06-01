package user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exceptions.DuplicatedUsernameException;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotSamePasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import survey.SurveyListController;

import java.io.IOException;

public class MyAccountController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField password1;

    @FXML
    private JFXTextField password2;

    @FXML
    private Label textInformation;

    private Stage stage;
    private User user;

    public void initialize() {}

    public void init() {
        usernameField.setText(user.getUsername());
        nameField.setText(user.getName());
        password1.setText(user.getPassword());
        password2.setText(user.getPassword());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void cancelButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.setStage(stage);
        controller.setUser(user);
        controller.init();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    public void editUserButtonPressed() throws IOException, DuplicatedUsernameException {
        try {
            if (!password1.getText().isEmpty() && !usernameField.getText().isEmpty() && !nameField.getText().isEmpty() &&
                    !password1.getText().equals(password2.getText())) throw new NotSamePasswordException();
            if (nameField.getText().isEmpty()) throw new EmptyRequiredAttributeException();
            if (password1.getText().isEmpty() || password2.getText().isEmpty()) throw new EmptyRequiredAttributeException();
            user.modifyInformation(nameField.getText(),password1.getText());
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

        } catch (EmptyRequiredAttributeException e) {
            textInformation.setText("Missing field(s)");
        } catch (NotSamePasswordException e) {
            textInformation.setText("Not the same password");
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}
