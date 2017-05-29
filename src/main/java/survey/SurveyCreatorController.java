package survey;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import question.NumericQuestion;
import question.Question;

import java.io.IOException;
import java.util.Optional;

public class SurveyCreatorController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    @FXML
    public JFXTextField surveyTitle;
    @FXML
    public JFXTextField surveyDescription;

    @FXML
    public VBox vPrincipalBox;

    private Stage stage;

    private enum QuestionType {
        FREE,
        NUMERIC,
        SORTEDQUALITATIVEQUESTION,
        UNSORTEDQUALITATIVEQUESTION,
        MULTIVALUEDQUALITATIVEQUESTION
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newQuestionButtonPressed(ActionEvent actionEvent)
    {
        /*
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        VBox vbox = new VBox(new Text("Enter a new question"));
        vbox.setAlignment(CENTER);
        vbox.setPadding(new Insets(15));
        dialogStage.setTitle("New question");
        dialogStage.setMinHeight(250);
        dialogStage.setMinWidth(300);
        vbox.getChildren().add(new JFXTextField());
        JFXComboBox<Label> comboBox = new JFXComboBox<>();
        comboBox.getItems().add(new Label("One"));
        comboBox.getItems().add(new Label("Two"));
        comboBox.getItems().add(new Label("Three"));
        vbox.getChildren().add(comboBox);
        vbox.getChildren().add(new HBox());
        HBox hBox = new HBox();
        hBox.getChildren().add(new Button("Cancel"));
        hBox.getChildren().add(new Button("Ok"));
        hBox.setAlignment(CENTER);
        vbox.getChildren().add(hBox);
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();*/

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New question");
        dialog.setHeaderText("Add a new question");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Okei", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Question name");
        JFXComboBox<Label> comboBox = new JFXComboBox<>();
        for Question items : QuestionType.values() {

        }


        grid.add(new Label("Question:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(comboBox, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(quesitonType -> {
            System.out.println("Username=" + quesitonType.getKey() + ", Password=" + quesitonType.getValue());
        });
    }

    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }
}
