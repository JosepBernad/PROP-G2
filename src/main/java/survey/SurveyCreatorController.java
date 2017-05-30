package survey;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import exceptions.EmptyRequiredAttributeException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import question.FreeQuestionBuilder;
import question.NumericQuestionBuilder;
import question.Question;
import question.QuestionBuilder;

import java.io.IOException;
import java.util.*;

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

    private Set<QuestionBuilder> questionBuilders = new HashSet<>();

    private List<String> questionTypes = Arrays.asList("Free question",
            "Numeric question",
            "Sorted qualitative question",
            "Unsorted qualitative question",
            "Multivalued qualitative question");


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newQuestionButtonPressed(ActionEvent actionEvent)
    {

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
        for (String type : questionTypes) {
            comboBox.getItems().add(new Label(type));
        }
        comboBox.getSelectionModel().select(0);
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

        result.ifPresent(questionStatement -> {
            System.out.println("Username=" + questionStatement.getKey() + ", Password=" + questionStatement.getValue());
            addQuestion(questionStatement.getKey(),questionStatement.getValue());
        });
    }

    private void addQuestion(String statement, String type) {
        if (type.indexOf(questionTypes.get(0)) != -1) {
            addFreeQuestion(statement);
        } else if (type.indexOf(questionTypes.get(1)) != -1) {
            addNumericQuestion(statement);
        } else if (type.indexOf(questionTypes.get(2)) != -1) {
            addSortedQualitativeQuestion(statement);
        } else if (type.indexOf(questionTypes.get(3)) != -1) {
            addUnsortedQualitativeQuestion(statement);
        } else {
            addMultivaluatedQualitativeQuestion(statement);
        }
    }

    private void addMultivaluatedQualitativeQuestion(String statement) {
        HBox individualOptionBox = new HBox();
        VBox allOptionsBox = new VBox();
        HBox optionsBox = new HBox();
        optionsBox.getChildren().add(allOptionsBox);
        optionsBox.getChildren().add(new JFXButton("Add option"));
        HBox questionBox = new HBox();
        questionBox.getChildren().add(optionsBox);
        questionBox.getChildren().add(new JFXButton("X"));
        VBox mainBox = new VBox();
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        mainBox.getChildren().add(statementField);
        mainBox.getChildren().add(questionBox);
        vPrincipalBox.getChildren().add(mainBox);
    }

    private void addUnsortedQualitativeQuestion(String statement) {
        HBox individualOptionBox = new HBox();
        VBox allOptionsBox = new VBox();
        HBox optionsBox = new HBox();
        optionsBox.getChildren().add(allOptionsBox);
        optionsBox.getChildren().add(new JFXButton("Add option"));
        HBox questionBox = new HBox();
        questionBox.getChildren().add(optionsBox);
        questionBox.getChildren().add(new JFXButton("X"));
        VBox mainBox = new VBox();
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        mainBox.getChildren().add(statementField);
        mainBox.getChildren().add(questionBox);
        vPrincipalBox.getChildren().add(mainBox);
    }

    private void addSortedQualitativeQuestion(String statement) {
        HBox individualOptionBox = new HBox();
        VBox allOptionsBox = new VBox();
        HBox optionsBox = new HBox();
        optionsBox.getChildren().add(allOptionsBox);
        optionsBox.getChildren().add(new JFXButton("Add option"));
        HBox questionBox = new HBox();
        questionBox.getChildren().add(optionsBox);
        questionBox.getChildren().add(new JFXButton("X"));
        VBox mainBox = new VBox();
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        mainBox.getChildren().add(statementField);
        mainBox.getChildren().add(questionBox);
        vPrincipalBox.getChildren().add(mainBox);
    }

    private void addNumericQuestion(String statement) {
        VBox vBox = new VBox();

        NumericQuestionBuilder numericQuestionBuilder = new NumericQuestionBuilder();

        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        numericQuestionBuilder.setStatement(statementField);

        vBox.getChildren().add(statementField);
        HBox hBox = new HBox();
        hBox.getChildren().add(new Label("Min:"));
        JFXTextField minField = new JFXTextField();
        numericQuestionBuilder.setMinValue(minField);

        hBox.getChildren().add(forceNumericField(minField));
        hBox.getChildren().add(new Label("Max:"));
        JFXTextField maxField = new JFXTextField();
        numericQuestionBuilder.setMaxValue(maxField);

        questionBuilders.add(numericQuestionBuilder);

        hBox.getChildren().add(forceNumericField(maxField));
        hBox.getChildren().add(new JFXTextField());
        vBox.getChildren().add(hBox);
        HBox mainHBox = new HBox();
        mainHBox.getChildren().add(vBox);
        mainHBox.getChildren().add(new JFXButton("X"));
        vPrincipalBox.getChildren().add(mainHBox);
    }

    private void addFreeQuestion(String statement) {
        VBox vBox = new VBox();

        FreeQuestionBuilder freeQuestionBuilder = new FreeQuestionBuilder();

        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        freeQuestionBuilder.setStatement(statementField);

        vBox.getChildren().add(statementField);
        HBox hBox = new HBox();
        hBox.getChildren().add(new Label("Max length:"));
        JFXTextField maxLenghtField = new JFXTextField();
        freeQuestionBuilder.setMaxLenght(maxLenghtField);

        questionBuilders.add(freeQuestionBuilder);

        hBox.getChildren().add(forceNumericField(maxLenghtField));
        vBox.getChildren().add(hBox);
        maxLenghtField.getParent();
        HBox mainHBox = new HBox();
        mainHBox.getChildren().add(vBox);
        mainHBox.getChildren().add(new JFXButton("X"));

        vPrincipalBox.getChildren().add(mainHBox);

    }

    public void cancelButtonPressed() throws IOException {
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

    private JFXTextField forceNumericField(JFXTextField textField)
    {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        return textField;
    }

    public void saveButtonPressed() {
        System.out.print("Save!");
        Survey survey = new Survey();
        survey.setTitle("Miquel");
        for (QuestionBuilder questionBuilder : this.questionBuilders) {
            survey.addQuestion(questionBuilder.build());
        }
        try {
            survey.save();
        } catch (EmptyRequiredAttributeException e) {
            e.printStackTrace();
        }

    }
}
