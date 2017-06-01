package survey;


import com.jfoenix.controls.*;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

    private List<String> questionTypes = Arrays.asList("Free question",
            "Numeric question",
            "Sorted qualitative question",
            "Unsorted qualitative question",
            "Multivalued qualitative question");
    private User user;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newQuestionButtonPressed(ActionEvent actionEvent) {

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New question");
        dialog.setHeaderText("Add a new question");

        // Set the button types.
        ButtonType okButtonType = ButtonType.OK;//("Óc.", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

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
        Node loginButton = dialog.getDialogPane().lookupButton(okButtonType);
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
            if (dialogButton == okButtonType) {
                return new Pair<>(username.getText(), comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(questionStatement -> {
            System.out.println("Username=" + questionStatement.getKey() + ", Password=" + questionStatement.getValue());
            addQuestion(questionStatement.getKey(), questionStatement.getValue());
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
            addMultivaluedQualitativeQuestion(statement);
        }
    }

    private void addNewOption(VBox optionsVBox, Boolean univalued) {
        HBox optionHBox = new HBox();
        // TODO: Add option to vPrincipalBox
        if (univalued) {
            JFXRadioButton radioButton = new JFXRadioButton();
            radioButton.setDisable(true);
            optionHBox.getChildren().add(radioButton);
        } else {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setDisable(true);
            optionHBox.getChildren().add(checkBox);
        }

        JFXTextField optionTextField = new JFXTextField();
        optionTextField.setPromptText("Option value");
        optionHBox.getChildren().add(optionTextField);

        JFXButton xButton = new JFXButton("X");
        optionHBox.getChildren().add(xButton);

        optionsVBox.getChildren().add(optionHBox);

        xButton.setOnAction(event -> deleteOption(optionsVBox, optionsVBox.getChildren().indexOf(optionHBox)));
    }

    private void deleteOption(VBox optionsVBox, int index) {
        System.out.println(index);
        // TODO: Delete option from vPrincipalBox
        optionsVBox.getChildren().remove(index);
    }

    private void addMultivaluedQualitativeQuestion(String statement) {
        VBox optionsVBox = new VBox();
        HBox parametersHBox = new HBox();
        HBox manageOptionsHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionHBox = new HBox();

        // Options box: all options
        // addNewOption(optionsVBox);


        // Manage options box: options box and add option button.
        manageOptionsHBox.getChildren().add(optionsVBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addNewOption(optionsVBox, MULTIVALUED));
        manageOptionsHBox.getChildren().add(addOptionButton);

        // Parameters box:
        parametersHBox.getChildren().add(new Label("Max options: "));

        JFXTextField maxOptionsField = new JFXTextField();
        parametersHBox.getChildren().add(checkRegExField(maxOptionsField, NATURAL));

        // Elements box: title, parameter and manage options
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        elementsVBox.getChildren().add(statementField);

        elementsVBox.getChildren().add(parametersHBox);

        elementsVBox.getChildren().add(manageOptionsHBox);

        // Question box: elements box and delete button
        questionHBox.getChildren().add(elementsVBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionHBox.getChildren().add(deleteQuestionButton);


        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox)));
        // TODO: Implement when a multivalued question is deleted
        MultivaluedUnsortedQualitativeQuestionBuilder builder = new MultivaluedUnsortedQualitativeQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMaxAnswers(maxOptionsField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);
    }

    private void addUnsortedQualitativeQuestion(String statement) {
        VBox optionsVBox = new VBox();
        HBox manageOptionsHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionHBox = new HBox();

        // Options box: all options
        // addNewOption(optionsVBox);


        // Manage options box: options box and add option button.
        manageOptionsHBox.getChildren().add(optionsVBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addNewOption(optionsVBox, UNIVALUED));
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


        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox)));
        // TODO: Implement when an unsorted question is deleted
        UnsortedQualitativeQuestionBuilder builder = new UnsortedQualitativeQuestionBuilder();
        builder.setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);
    }

    private void addSortedQualitativeQuestion(String statement) {

        VBox optionsVBox = new VBox();
        HBox manageOptionsHBox = new HBox();
        VBox elementsVBox = new VBox();
        HBox questionHBox = new HBox();

        // Options box: all options
        // addNewOption(optionsVBox);


        // Manage options box: options box and add option button.
        manageOptionsHBox.getChildren().add(optionsVBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addNewOption(optionsVBox, UNIVALUED));
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


        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox)));
        // TODO: Implement when a sorted question is deleted
        SortedQualitativeQuestionBuilder builder = new SortedQualitativeQuestionBuilder();
        builder.setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);
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


        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox)));
        // TODO: Implement when a numeric question is deleted
        NumericQuestionBuilder builder = new NumericQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMinValue(minField);
        builder.setMaxValue(maxField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);
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


        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionHBox)));

        // TODO: Implement when a free question is deleted
        FreeQuestionBuilder builder = new FreeQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMaxLength(maxLenghtField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionHBox);
    }

    private void deleteQuestion(int i) {
        vPrincipalBox.getChildren().remove(i);
        // TODO: Implement in questionBuilders
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

    public void saveButtonPressed() throws IOException {
        Survey survey = new Survey();
        survey.setTitle(surveyTitle.getText());
        Boolean error = new Boolean(false);
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
