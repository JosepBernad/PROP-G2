package analysis;

import answer.*;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import question.Question;
import survey.Survey;

import java.util.List;

public class ClusterController {

    @FXML
    public Label surveyTitle;

    @FXML
    public Label surveyDescription;

    @FXML
    public HBox answers;

    public void init(Integer surveyId, List<List<Answer>> listOfLists) {
        Survey survey = Survey.getSurveyById(surveyId);
        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        List<Question> questions = survey.getQuestions();
        VBox statements = new VBox();
        for (Question question : questions)
            statements.getChildren().add(new Label(question.getStatement()));
        answers.getChildren().add(statements);

        for (List<Answer> list : listOfLists) {
            VBox secondColumn = new VBox();
            for (Answer answer : list) {
                if (answer instanceof FreeAnswer) createFreeAnswer((FreeAnswer) answer, secondColumn);
                else if (answer instanceof NumericAnswer) createNumericAnswer((NumericAnswer) answer, secondColumn);
                else if (answer instanceof UnivaluedQualitativeAnswer)
                    createUnivaluedQualitativeAnswer((UnivaluedQualitativeAnswer) answer, secondColumn);
                else if (answer instanceof MultivaluedQualitativeAnswer)
                    createMultivaluedQualitativeAnswer((MultivaluedQualitativeAnswer) answer, secondColumn);
            }
            answers.getChildren().addAll(new Separator(Orientation.VERTICAL), secondColumn);
        }
    }

    private void createMultivaluedQualitativeAnswer(MultivaluedQualitativeAnswer answer, VBox box) {
        HBox hBox = new HBox();
        answer.getOptions().forEach(option -> hBox.getChildren().add(new Label(option.getValue())));
        box.getChildren().add(hBox);
    }

    private void createUnivaluedQualitativeAnswer(UnivaluedQualitativeAnswer answer, VBox box) {
        box.getChildren().add(new Label(answer.getOption().getValue()));
    }

    private void createNumericAnswer(NumericAnswer answer, VBox box) {
        box.getChildren().add(new Label(answer.getValue().toString()));
    }

    private void createFreeAnswer(FreeAnswer answer, VBox box) {
        box.getChildren().add(new Label(answer.getValue()));
    }
}
