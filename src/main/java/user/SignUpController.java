package user;

import com.jfoenix.controls.JFXTextField;
import exceptions.DuplicatedUsernameException;
import exceptions.EmptyRequiredAttributeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private  JFXTextField nameField;

    @FXML
    private Label textInformation;

    private Stage stage;

    public void createUserButtonPressed(ActionEvent actionEvent) throws IOException, EmptyRequiredAttributeException {
        User user = new User();
        user.setUsername(usernameField.getText());
        user.setName(nameField.getText());
        try {
            user.save();
            textInformation.setText("User created successfully");
        } catch (DuplicatedUsernameException e) {
            textInformation.setText("This user is already taken");
        }
        catch (EmptyRequiredAttributeException e) {
            textInformation.setText("Missing field");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
