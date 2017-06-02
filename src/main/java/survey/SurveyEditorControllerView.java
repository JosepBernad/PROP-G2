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
import utils.Constants;

import java.io.IOException;
import java.util.*;

public class SurveyEditorControllerView {

    private static final String INTEGER = "^([-]?[1-9]\\d*|0)$";
    private static final String NATURAL = "^([1-9]\\d*)$";

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
    public JFXTextField surveyTitle = new JFXTextField();
    ;

    @FXML
    public JFXTextField surveyDescription = new JFXTextField();

    @FXML
    public VBox vPrincipalBox;

    @FXML
    public Label missingLabel;

    private List<QuestionBuilder> questionBuilders = new ArrayList<>();

    private Stage stage;
    private User user;
    private Survey survey;

    public void init(Stage stage, User user, Survey survey) {
        this.stage = stage;
        this.user = user;
        this.survey = survey;

        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        setQuestionBuilders(survey);
    }

    private void setQuestionBuilders(Survey survey) {
        for (Question question : survey.getQuestions()) {
            if (question instanceof FreeQuestion) {
                createFreeQuestion((FreeQuestion) question);
            } else if (question instanceof NumericQuestion) {
                createNumericQuestion((NumericQuestion) question);
            } else if (question instanceof SortedQualitativeQuestion) {
                createSortedQualitativeQuestion((SortedQualitativeQuestion) question);
            } else if (question instanceof UnsortedQualitativeQuestion) {
                createUnsortedQualitativeQuestion((UnsortedQualitativeQuestion) question);
            } else if (question instanceof MultivaluedUnsortedQualitativeQuestion) {
                createMultivaluedQualitativeQuestion((MultivaluedUnsortedQualitativeQuestion) question);
            }
        }
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

    private void addOptionWithWeight(VBox optionsVBox, Map<TextField, TextField> options) {
        HBox optionHBox = new HBox();

        JFXTextField optionTextField = new JFXTextField();
        optionTextField.setPromptText("Option value");
        JFXTextField weightTextField = new JFXTextField();
        weightTextField.setPromptText("Weight");
        options.put(weightTextField, optionTextField);
        JFXButton deleteButton = new JFXButton("X");
        optionHBox.getChildren().addAll(optionTextField, weightTextField, deleteButton);

        optionsVBox.getChildren().add(optionHBox);

        deleteButton.setOnAction(event -> deleteOptionWithWeight(optionsVBox, optionHBox, options));
    }

    private void deleteOptionWithWeight(VBox containerBox, HBox optionBox, Map<TextField, TextField> options) {
        JFXTextField optionWeightField = (JFXTextField) optionBox.getChildren().get(1);
        options.remove(optionWeightField);
        containerBox.getChildren().remove(optionBox);
    }

    private void deleteOption(VBox containerBox, HBox optionBox, Set<TextField> options) {
        JFXTextField optionField = (JFXTextField) optionBox.getChildren().get(0);
        options.remove(optionField);
        containerBox.getChildren().remove(optionBox);
    }

    private void createMultivaluedQualitativeQuestion(MultivaluedUnsortedQualitativeQuestion question) {
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);
        HBox parametersHBox = new HBox();
        parametersHBox.setSpacing(10);
        HBox manageOptionsBox = new HBox();
        manageOptionsBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        Set<TextField> options = new HashSet<>();
        for (Option option : question.getOptions()) {
            HBox optionBox = new HBox();

            JFXTextField valueField = new JFXTextField(option.getValue());
            valueField.setPromptText("Option value");
            options.add(valueField);
            JFXButton deleteButton = new JFXButton("X");

            optionBox.getChildren().addAll(valueField, deleteButton);
            optionsBox.getChildren().add(optionBox);

            deleteButton.setOnAction(event -> deleteOption(optionsBox, optionBox, options));
        }
        manageOptionsBox.getChildren().add(optionsBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOption(optionsBox, options));
        manageOptionsBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(question.getStatement());

        parametersHBox.getChildren().add(new Label("Max options: "));
        JFXTextField maxOptionsField = createTextFieldWithRegEx(NATURAL);
        maxOptionsField.setText(String.valueOf(question.getnMaxAnswers()));
        parametersHBox.getChildren().add(maxOptionsField);

        elementsBox.getChildren().addAll(statementField, parametersHBox, manageOptionsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsBox, deleteQuestionButton);

        MultivaluedUnsortedQualitativeQuestionBuilder builder = new MultivaluedUnsortedQualitativeQuestionBuilder();
        builder.setOptions(options).setMaxAnswers(maxOptionsField).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);
        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void createUnsortedQualitativeQuestion(UnsortedQualitativeQuestion question) {
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);
        HBox manageOptionsBox = new HBox();
        manageOptionsBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        Set<TextField> options = new HashSet<>();
        for (Option option : question.getOptions()) {
            HBox optionHBox = new HBox();

            JFXTextField valueField = new JFXTextField(option.getValue());
            valueField.setPromptText("Option value");
            options.add(valueField);
            JFXButton deleteButton = new JFXButton("X");

            optionHBox.getChildren().addAll(valueField, deleteButton);
            optionsBox.getChildren().add(optionHBox);

            deleteButton.setOnAction(event -> deleteOption(optionsBox, optionHBox, options));
        }
        manageOptionsBox.getChildren().add(optionsBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOption(optionsBox, options));
        manageOptionsBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(question.getStatement());

        elementsBox.getChildren().addAll(statementField, manageOptionsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsBox, deleteQuestionButton);

        UnsortedQualitativeQuestionBuilder builder = new UnsortedQualitativeQuestionBuilder();
        builder.setOptions(options).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);
        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void createSortedQualitativeQuestion(SortedQualitativeQuestion question) {
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);
        HBox manageOptionsBox = new HBox();
        manageOptionsBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        Map<TextField, TextField> options = new HashMap<>();
        for (Option option : question.getOptions()) {
            HBox optionHBox = new HBox();

            JFXTextField weightField = new JFXTextField(String.valueOf(option.getWeight()));
            weightField.setPromptText("Option weight");
            JFXTextField valueField = new JFXTextField(option.getValue());
            valueField.setPromptText("Option value");
            options.put(weightField, valueField);
            JFXButton deleteButton = new JFXButton("X");

            optionHBox.getChildren().addAll(valueField, weightField, deleteButton);
            optionsBox.getChildren().add(optionHBox);

            deleteButton.setOnAction(event -> deleteOptionWithWeight(optionsBox, optionHBox, options));
        }
        manageOptionsBox.getChildren().add(optionsBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOptionWithWeight(optionsBox, options));
        manageOptionsBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(question.getStatement());
        elementsBox.getChildren().addAll(statementField, manageOptionsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsBox, deleteQuestionButton);

        SortedQualitativeQuestionBuilder builder = new SortedQualitativeQuestionBuilder();
        builder.setOptions(options).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void createNumericQuestion(NumericQuestion question) {
        HBox parametersBox = new HBox();
        parametersBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        parametersBox.getChildren().add(new Label("Min:"));
        JFXTextField minField = new JFXTextField();
        minField.setText(String.valueOf(question.getMin()));
        parametersBox.getChildren().add(checkRegExField(minField, INTEGER));

        parametersBox.getChildren().add(new Label("Max:"));
        JFXTextField maxField = new JFXTextField();
        maxField.setText(String.valueOf(question.getMax()));
        parametersBox.getChildren().add(checkRegExField(maxField, INTEGER));

        JFXTextField statementField = new JFXTextField();
        statementField.setText(question.getStatement());
        elementsBox.getChildren().add(statementField);

        elementsBox.getChildren().add(parametersBox);

        questionBox.getChildren().add(elementsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().add(deleteQuestionButton);

        NumericQuestionBuilder builder = new NumericQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMinValue(minField);
        builder.setMaxValue(maxField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);
        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void createFreeQuestion(FreeQuestion question) {
        HBox parametersBox = new HBox();
        parametersBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        parametersBox.getChildren().add(new Label("Max length:"));

        JFXTextField maxLenghtField = new JFXTextField();
        maxLenghtField.setText(String.valueOf(question.getMaxSize()));
        parametersBox.getChildren().add(checkRegExField(maxLenghtField, NATURAL));

        JFXTextField statementField = new JFXTextField();
        statementField.setText(question.getStatement());
        elementsBox.getChildren().add(statementField);

        elementsBox.getChildren().add(parametersBox);

        questionBox.getChildren().add(elementsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().add(deleteQuestionButton);

        FreeQuestionBuilder builder = new FreeQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMaxLength(maxLenghtField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addMultivaluedQualitativeQuestion(String statement) {
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);
        HBox parametersBox = new HBox();
        parametersBox.setSpacing(10);
        HBox manageOptionsBox = new HBox();
        manageOptionsBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        Set<TextField> options = new HashSet<>();
        manageOptionsBox.getChildren().add(optionsBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOption(optionsBox, options));
        manageOptionsBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(statement);

        parametersBox.getChildren().add(new Label("Max options: "));
        JFXTextField maxOptionsField = createTextFieldWithRegEx(NATURAL);
        parametersBox.getChildren().add(maxOptionsField);

        elementsBox.getChildren().addAll(statementField, parametersBox, manageOptionsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsBox, deleteQuestionButton);

        MultivaluedUnsortedQualitativeQuestionBuilder builder = new MultivaluedUnsortedQualitativeQuestionBuilder();
        builder.setOptions(options).setMaxAnswers(maxOptionsField).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addUnsortedQualitativeQuestion(String statement) {
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);
        HBox manageOptionsBox = new HBox();
        manageOptionsBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        Set<TextField> options = new HashSet<>();
        manageOptionsBox.getChildren().add(optionsBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOption(optionsBox, options));
        manageOptionsBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(statement);

        elementsBox.getChildren().addAll(statementField, manageOptionsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsBox, deleteQuestionButton);

        UnsortedQualitativeQuestionBuilder builder = new UnsortedQualitativeQuestionBuilder();
        builder.setOptions(options).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addSortedQualitativeQuestion(String statement) {
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);
        HBox manageOptionsBox = new HBox();
        manageOptionsBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        Map<TextField, TextField> options = new HashMap<>();
        manageOptionsBox.getChildren().add(optionsBox);

        JFXButton addOptionButton = new JFXButton("Add option");
        addOptionButton.setOnAction(event -> addOptionWithWeight(optionsBox, options));
        manageOptionsBox.getChildren().add(addOptionButton);

        JFXTextField statementField = new JFXTextField(statement);
        elementsBox.getChildren().addAll(statementField, manageOptionsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().addAll(elementsBox, deleteQuestionButton);

        SortedQualitativeQuestionBuilder builder = new SortedQualitativeQuestionBuilder();
        builder.setOptions(options).setStatement(statementField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addNumericQuestion(String statement) {
        HBox parametersBox = new HBox();
        parametersBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        parametersBox.getChildren().add(new Label("Min:"));
        JFXTextField minField = new JFXTextField();
        parametersBox.getChildren().add(checkRegExField(minField, INTEGER));

        parametersBox.getChildren().add(new Label("Max:"));
        JFXTextField maxField = new JFXTextField();
        parametersBox.getChildren().add(checkRegExField(maxField, INTEGER));

        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        elementsBox.getChildren().add(statementField);

        elementsBox.getChildren().add(parametersBox);

        questionBox.getChildren().add(elementsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().add(deleteQuestionButton);

        NumericQuestionBuilder builder = new NumericQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMinValue(minField);
        builder.setMaxValue(maxField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);

        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void addFreeQuestion(String statement) {
        HBox parametersBox = new HBox();
        parametersBox.setSpacing(10);
        VBox elementsBox = new VBox();
        elementsBox.setSpacing(10);
        HBox questionBox = new HBox();
        questionBox.setSpacing(10);

        // Question parameter: Max length
        parametersBox.getChildren().add(new Label("Max length:"));

        JFXTextField maxLenghtField = new JFXTextField();
        parametersBox.getChildren().add(checkRegExField(maxLenghtField, NATURAL));


        // Question elements: Tile and parameter
        JFXTextField statementField = new JFXTextField();
        statementField.setText(statement);
        elementsBox.getChildren().addAll(statementField, parametersBox);

        // Question box: elements and delete button
        questionBox.getChildren().add(elementsBox);

        JFXButton deleteQuestionButton = new JFXButton("X");
        questionBox.getChildren().add(deleteQuestionButton);

        FreeQuestionBuilder builder = new FreeQuestionBuilder();
        builder.setStatement(statementField);
        builder.setMaxLength(maxLenghtField);
        questionBuilders.add(builder);

        vPrincipalBox.getChildren().add(questionBox);
        deleteQuestionButton.setOnAction(event -> deleteQuestion(vPrincipalBox.getChildren().indexOf(questionBox), builder));
    }

    private void deleteQuestion(int i, QuestionBuilder builder) {
        vPrincipalBox.getChildren().remove(i);
        questionBuilders.remove(builder);
    }

    public void cancelButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyListView.fxml").openStream());
        SurveyListControllerView controller = loader.getController();
        controller.init(stage, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
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
        survey.setId(this.survey.getId());
        survey.setTitle(surveyTitle.getText());
        survey.setDescription(surveyDescription.getText());
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

}
