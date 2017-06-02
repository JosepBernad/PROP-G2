package answer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import question.*;
import survey.Survey;
import survey.SurveyDetailController;
import user.User;
import utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnswerCreatorController {

    @FXML
    public Label surveyTitle;

    @FXML
    public Label surveyDescription;

    @FXML
    public VBox questionsBox;

    @FXML
    public JFXButton backButton;

    @FXML
    public JFXButton saveButton;

    private Integer surveyId;
    private User user;
    private Stage stage;

    private Set<AnswerBuilder> answerBuilders = new HashSet<>();

    public void init(Stage stage, Integer surveyId, User user) {
        this.stage = stage;
        this.surveyId = surveyId;
        this.user = user;
        Survey survey = Survey.getSurveyById(this.surveyId);
        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        List<Question> questions = new ArrayList<>();
        try {
            questions = Survey.getSurveyById(surveyId).getQuestions();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Question question : questions) {
            String statement = question.getStatement();
            questionsBox.getChildren().add(new Label(statement));
            if (question instanceof FreeQuestion) createFreeAnswer((FreeQuestion) question);
            else if (question instanceof NumericQuestion) createNumericAnswer((NumericQuestion) question);
            else if (question instanceof UnsortedQualitativeQuestion)
                createUnivaluedQualitativeAnswer((UnsortedQualitativeQuestion) question);
            else if (question instanceof SortedQualitativeQuestion)
                createUnivaluedQualitativeAnswer((SortedQualitativeQuestion) question);
            else if (question instanceof MultivaluedUnsortedQualitativeQuestion)
                createMultivaluedQualitativeAnswer((MultivaluedUnsortedQualitativeQuestion) question);
            questionsBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }
    }

    private void createMultivaluedQualitativeAnswer(MultivaluedUnsortedQualitativeQuestion question) {
        Set<CheckBox> checkBoxes = new HashSet<>();
        for (Option option : question.getOptions()) {
            JFXCheckBox checkBox = new JFXCheckBox(option.getValue());
            questionsBox.getChildren().add(checkBox);
            checkBoxes.add(checkBox);
        }
        MultivaluedQualitativeAnswerBuilder builder = new MultivaluedQualitativeAnswerBuilder();
        builder.setOptions(checkBoxes)
                .setQuestionId(question.getId())
                .setSurveyId(surveyId)
                .setUsername(user.getUsername());
        answerBuilders.add(builder);
    }

    private void createUnivaluedQualitativeAnswer(QualitativeQuestion question) {
        ToggleGroup options = new ToggleGroup();
        for (Option option : question.getOptions()) {
            JFXRadioButton radioButton = new JFXRadioButton(option.getValue());
            radioButton.setToggleGroup(options);
            questionsBox.getChildren().add(radioButton);
        }
        UnivaluedQualitativeAnswerBuilder builder = new UnivaluedQualitativeAnswerBuilder();
        builder.setOptions(options)
                .setQuestionId(question.getId())
                .setSurveyId(surveyId)
                .setUsername(user.getUsername());
        answerBuilders.add(builder);
    }

    private void createNumericAnswer(NumericQuestion question) {
        JFXTextField valueField = new JFXTextField();
        questionsBox.getChildren().addAll(
                new Label("Accepted range: " + question.getMin() + " <= x <=" + question.getMax()),
                valueField);
        NumericAnswerBuilder builder = new NumericAnswerBuilder();
        builder.setValue(valueField)
                .setQuestionId(question.getId())
                .setSurveyId(surveyId)
                .setUsername(user.getUsername());
        answerBuilders.add(builder);
    }

    private void createFreeAnswer(FreeQuestion question) {
        JFXTextField valueField = new JFXTextField();
        questionsBox.getChildren().addAll(
                new Label("Max size for the answer: " + question.getMaxSize().toString()),
                valueField);
        FreeAnswerBuilder builder = new FreeAnswerBuilder();
        builder.setValue(valueField)
                .setQuestionId(question.getId())
                .setSurveyId(surveyId)
                .setUsername(user.getUsername());
        answerBuilders.add(builder);
    }

    public void backButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("/views/SurveyDetailView.fxml").openStream());
        SurveyDetailController controller = loader.getController();
        controller.init(stage, surveyId, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    //TODO: try catch exceptions
    public void saveAnswer() throws EmptyRequiredAttributeException, NotInRangeException {
        Set<Answer> answers = new HashSet<>();
        for (AnswerBuilder builder : answerBuilders)
            answers.add(builder.build());
        Answer.saveAnswersInFile(answers);

        showAnswer();
    }

    private void showAnswer() {
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/views/AnswerDetailView.fxml").openStream());
        } catch (IOException ignored) {
        }
        AnswerDetailController controller = loader.getController();
        controller.init(stage, surveyId, user);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Constants.STYLE, Constants.FONTS);
        stage.setScene(scene);
        stage.show();
    }

    private abstract class AnswerBuilder {

        private Integer questionId;
        private Integer surveyId;
        private String username;

        public abstract Answer build() throws EmptyRequiredAttributeException, NotInRangeException;

        void isEmpty(TextField... textFields) throws EmptyRequiredAttributeException {
            for (TextField textField : textFields)
                if (textField.getText().isEmpty()) throw new EmptyRequiredAttributeException();
        }

        Integer getQuestionId() {
            return questionId;
        }

        public AnswerBuilder setQuestionId(Integer questionId) {
            this.questionId = questionId;
            return this;
        }

        Integer getSurveyId() {
            return surveyId;
        }

        public AnswerBuilder setSurveyId(Integer surveyId) {
            this.surveyId = surveyId;
            return this;
        }

        String getUsername() {
            return username;
        }

        public AnswerBuilder setUsername(String username) {
            this.username = username;
            return this;
        }
    }

    private class FreeAnswerBuilder extends AnswerBuilder {

        private TextField value;

        public FreeAnswer build() throws EmptyRequiredAttributeException {
            isEmpty(value);
            FreeAnswer freeAnswer = new FreeAnswer();
            freeAnswer.setValue(value.getText());
            freeAnswer.setUsername(getUsername());
            freeAnswer.setQuestionId(getQuestionId());
            freeAnswer.setSurveyId(getSurveyId());
            return freeAnswer;
        }

        public FreeAnswerBuilder setValue(TextField value) {
            this.value = value;
            return this;
        }
    }

    private class NumericAnswerBuilder extends AnswerBuilder {

        private TextField value;

        public NumericAnswer build() throws EmptyRequiredAttributeException, NotInRangeException {
            isEmpty(value);
            NumericAnswer numericAnswer = new NumericAnswer();
            try {
                numericAnswer.setValue(Double.parseDouble(value.getText()));
            } catch (NumberFormatException e) {
                throw new NotInRangeException();
            }
            numericAnswer.setUsername(getUsername());
            numericAnswer.setQuestionId(getQuestionId());
            numericAnswer.setSurveyId(getSurveyId());
            return numericAnswer;
        }

        public NumericAnswerBuilder setValue(TextField value) {
            this.value = value;
            return this;
        }
    }

    private class UnivaluedQualitativeAnswerBuilder extends AnswerBuilder {

        private ToggleGroup options;

        public UnivaluedQualitativeAnswer build() {
            UnivaluedQualitativeAnswer answer = new UnivaluedQualitativeAnswer();
            answer.setOption(findOption());
            answer.setUsername(getUsername());
            answer.setQuestionId(getQuestionId());
            answer.setSurveyId(getSurveyId());
            return answer;
        }

        private Option findOption() {
            QualitativeQuestion question = (QualitativeQuestion) Survey.getSurveyById(getSurveyId()).getQuestion(getQuestionId());
            String value = ((RadioButton) options.getSelectedToggle()).getText();
            if (question instanceof SortedQualitativeQuestion)
                for (Option opt : question.getOptions())
                    if (opt.getValue().equals(value))
                        return opt;
            return new Option(value);
        }

        public UnivaluedQualitativeAnswerBuilder setOptions(ToggleGroup options) {
            this.options = options;
            return this;
        }
    }

    private class MultivaluedQualitativeAnswerBuilder extends AnswerBuilder {

        private Set<CheckBox> options;

        public MultivaluedQualitativeAnswer build() {
            MultivaluedQualitativeAnswer answer = new MultivaluedQualitativeAnswer();
            Set<Option> selectedOptions = new HashSet<>();
            for (CheckBox checkBox : options)
                if (checkBox.isSelected())
                    selectedOptions.add(new Option(checkBox.getText()));
            answer.setOptions(selectedOptions);
            answer.setUsername(getUsername());
            answer.setQuestionId(getQuestionId());
            answer.setSurveyId(getSurveyId());
            return answer;
        }

        public MultivaluedQualitativeAnswerBuilder setOptions(Set<CheckBox> options) {
            this.options = options;
            return this;
        }
    }
}
