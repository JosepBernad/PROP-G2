package user;

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
import utils.Constants;

import java.io.IOException;

public class MyAccountController {

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

    public void init(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        usernameField.setText(this.user.getUsername());
        nameField.setText(this.user.getName());
        password1.setText(this.user.getPassword());
        password2.setText(this.user.getPassword());
    }

    public void cancelButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.init(stage, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
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
            controller.init(stage, User.getUserByUsername(usernameField.getText()));
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
            stage.setScene(scene);
            stage.show();

        } catch (EmptyRequiredAttributeException e) {
            textInformation.setText("Missing field(s)");
        } catch (NotSamePasswordException e) {
            textInformation.setText("Not the same password");
        }
    }

}
