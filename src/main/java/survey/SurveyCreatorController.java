package survey;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import question.*;
import user.User;

import java.io.IOException;
import java.util.*;

public class SurveyCreatorController {

    private static final String STYLE = "/views/Style.css";
    private static final String FONTS = "/views/fonts.css";

    private static final String INTEGER = "^([-]?[1-9]\\d*|0)$";
    private static final String NATURAL = "^([1-9]\\d*)$";

    private static final Boolean UNIVALUED = Boolean.TRUE;
    private static final Boolean MULTIVALUED = Boolean.FALSE;
    private static final String FREE_QUESTION = "Free question";
    private static final String NUMERIC_QUESTION = "Numeric question";
    private static final String SORTED_QUALITATIVE_QUESTION = "Sorted qualitative question";
    private static final String UNSORTED_QUALITATIVE_QUESTION = "Unsorted qualitative question";
    private static final String MULTIVALUED_QUALITATIVE_QUESTION = "Multivalued qualitative question";

    private static final List<String> questionTypes = Arrays.asList(
            FREE_QUESTION,
            NUMERIC_QUESTION,
            SORTED_QUALITATIVE_QUESTION,
            UNSORTED_QUALITATIVE_QUESTION,
            MULTIVALUED_QUALITATIVE_QUESTION);

    @FXML
    public JFXTextField surveyTitle;

    @FXML
    public JFXTextField surveyDescription;

    @FXML
    public VBox vPrincipalBox;

    @FXML
    public Label missingLabel;

    private Stage stage;

    private Set<QuestionBuilder> questionBuilders = new HashSet<>();

    private User user;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newQuestionButtonPressed() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New question");
        dialog.setHeaderText("Add a new question");

        ButtonType okButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField statement = new TextField();
        statement.setPromptText("Question statement");
        JFXComboBox<Label> comboBox = new JFXComboBox<>();
        for (String type : questionTypes)
            comboBox.getItems().add(new Label(type));
        comboBox.getSelectionModel().select(0);
        grid.add(new Label("Question:"), 0, 0);
        grid.add(statement, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(comboBox, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        statement.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(statement::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType)
                return new Pair<>(statement.getText(), comboBox.getSelectionModel().getSelectedItem().getText());
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(questionStatement -> addQuestion(questionStatement.getKey(), questionStatement.getValue()));
    }

    private void addQuestion(String statement, String type) {
        if (FREE_QUESTION.contains(type)) {
            addFreeQuestion(statement);
        } else if (NUMERIC_QUESTION.contains(type)) {
            addNumericQuestion(statement);
        } else if (SORTED_QUALITATIVE_QUESTION.contains(type)) {
            addSortedQualitativeQuestion(statement);
        } else if (UNSORTED_QUALITATIVE_QUESTION.contains(type)) {
            addUnsortedQualitativeQuestion(statement);
        } else if (MULTIVALUED_QUALITATIVE_QUESTION.contains(type)) {
            addMultivaluedQualitativeQuestion(statement);
        }
    }

    private void addOption(VBox optionsVBox, Set<TextField> options) {
        HBox optionHBox = new HBox();

        JFXTextField optionTextField = new JFXTextField();
        optionTextField.setPromptText("Option value");
        options.add(optionTextField);
        JFXButton deleteButton = new JFXButton("X");
        optionHBox.getChildren().addAll(optionTextField, deleteButton);

        optionsVBox.getChildren().add(optionHBox);

        deleteButton.setOnAction(event -> deleteOption(optionsVBox, optionHBox, options));
    }

    private void deleteOption(VBox optionsVBox, HBox optionBox, Set<TextField> options) {
        JFXTextField option = (JFXTextField) optionBox.getChildren().get(0);
        options.remove(option);
        optionsVBox.getChildren().remove(optionBox);
    }

    private void addMultivaluedQualitativeQuestion(String statement) {
        VBox optionsVBox = new VBox();
        HBox parametersHBox = new HBox();
        HBox manageOptionsHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionBox = new HBox();

        Set<TextField> options = new HashSet<>();
        manageOptionsHBox.getChildren().add(optionsVBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOption(optionsVBox, options));
        manageOptionsHBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(statement);

        parametersHBox.getChildren().add(new Label("Max options: "));
        JFXTextField maxOptionsField = createTextFieldWithRegEx(NATURAL);
        parametersHBox.getChildren().add(maxOptionsField);

        elementsVBox.getChildren().addAll(statementField, parametersHBox, manageOptionsHBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsVBox, deleteQuestionButton);

        MultivaluedUnsortedQualitativeQuestionBuilder builder = new MultivaluedUnsortedQualitativeQuestionBuilder();
        builder.setOptions(options).setMaxAnswers(maxOptionsField).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addUnsortedQualitativeQuestion(String statement) {
        VBox optionsVBox = new VBox();
        HBox manageOptionsHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionBox = new HBox();

        Set<TextField> options = new HashSet<>();
        manageOptionsHBox.getChildren().add(optionsVBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOption(optionsVBox, options));
        manageOptionsHBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(statement);

        elementsVBox.getChildren().addAll(statementField, manageOptionsHBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsVBox, deleteQuestionButton);

        UnsortedQualitativeQuestionBuilder builder = new UnsortedQualitativeQuestionBuilder();
        builder.setOptions(options).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addSortedQualitativeQuestion(String statement) {

        VBox optionsVBox = new VBox();
        HBox manageOptionsHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionHBox = new HBox();

        // Options box: all options
        // addOption(optionsVBox);


        // Manage options box: options box and add option button.
        manageOptionsHBox.getChildren().add(optionsVBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        Set<TextField> options = null;
        addOptionButton.setOnAction(event -> addOption(optionsVBox, options));
        manageOptionsHBox.getChildren().add(addOptionButton);

        // Elements box: title and manage options
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        elementsVBox.getChildren().add(statementField);

        elementsVBox.getChildren().add(manageOptionsHBox);

        // Question box: elements box and delete button
        questionHBox.getChildren().add(elementsVBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionHBox.getChildren().add(deleteQuestionButton);


        SortedQualitativeQuestionBuilder builder = new SortedQualitativeQuestionBuilder();
        builder.setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox), builder));
    }

    private void addNumericQuestion(String statement) {
        HBox parametersHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionHBox = new HBox();


        // Question parameter: Max and min value
        parametersHBox.getChildren().add(new Label("Min:"));
        JFXTextField minField = new JFXTextField();
        parametersHBox.getChildren().add(checkRegExField(minField, INTEGER));

        parametersHBox.getChildren().add(new Label("Max:"));
        JFXTextField maxField = new JFXTextField();
        parametersHBox.getChildren().add(checkRegExField(maxField, INTEGER));


        // Question elements: Tile and parameter
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        elementsVBox.getChildren().add(statementField);

        elementsVBox.getChildren().add(parametersHBox);

        // Question box: elements and delete button
        questionHBox.getChildren().add(elementsVBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionHBox.getChildren().add(deleteQuestionButton);


        NumericQuestionBuilder builder = new NumericQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMinValue(minField);
        builder.setMaxValue(maxField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox), builder));
    }

    private void addFreeQuestion(String statement) {
        HBox parametersHVox = new HBox();
        VBox elementsHBox = new VBox();
        HBox questionHBox = new HBox();


        // Question parameter: Max length
        parametersHVox.getChildren().add(new Label("Max length:"));

        JFXTextField maxLenghtField = new JFXTextField();
        parametersHVox.getChildren().add(checkRegExField(maxLenghtField, NATURAL));


        // Question elements: Tile and parameter
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        elementsHBox.getChildren().add(statementField);

        elementsHBox.getChildren().add(parametersHVox);

        // Question box: elements and delete button
        questionHBox.getChildren().add(elementsHBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionHBox.getChildren().add(deleteQuestionButton);


        FreeQuestionBuilder builder = new FreeQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMaxLength(maxLenghtField);
        questionBuilders.add(builder);


        vPrincipalBox.getChildren().add(questionHBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox), builder));
    }

    private void deleteQuestion(int i, QuestionBuilder builder) {
        vPrincipalBox.getChildren().remove(i);
        questionBuilders.remove(builder);
    }

    public void cancelButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListController controller = loader.getController();
        controller.setUser(user);
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE);
        scene.getStylesheets().add(FONTS);
        stage.setScene(scene);
        stage.show();
    }

    private JFXTextField checkRegExField(JFXTextField textField, String regex) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> textField.setFocusColor(newValue.matches(regex) ? Color.BLUE : Color.RED));
        return textField;
    }

    private JFXTextField createTextFieldWithRegEx(String regex) {
        JFXTextField jfxTextField = new JFXTextField();
        return checkRegExField(jfxTextField, regex);
    }

    public void saveButtonPressed() throws IOException {
        Survey survey = new Survey();
        survey.setTitle(surveyTitle.getText());
        Boolean error = Boolean.FALSE;
        for (Iterator<QuestionBuilder> iterator = this.questionBuilders.iterator(); !error && iterator.hasNext(); ) {
            QuestionBuilder questionBuilder = iterator.next();
            try {
                survey.addQuestion(questionBuilder.build());
            } catch (NotInRangeException e) {
                error = true;
                missingLabel.setText("Incorrect values");
            } catch (EmptyRequiredAttributeException e) {
                error = true;
                missingLabel.setText("Empty fields");
            }
        }
        if (!error) {
            try {
                survey.save();
                missingLabel.setText("Survey created successfully");
                cancelButtonPressed();
            } catch (EmptyRequiredAttributeException e) {
                missingLabel.setText("Empty survey title");
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}
