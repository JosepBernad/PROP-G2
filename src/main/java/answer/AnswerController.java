package answer;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import survey.Survey;

import java.util.ArrayList;
import java.util.List;

public class AnswerController {
    @FXML
    public Label surveyTitle;

    @FXML
    public Label surveyDescription;

    @FXML
    public VBox answers;

    public void init(Integer surveyId, String username) {
        Survey survey = Survey.getSurveyById(surveyId);
        surveyTitle.setText(survey.getTitle());
        surveyDescription.setText(survey.getDescription());

        List<Answer> answersForUser = new ArrayList<>();
        try {
            answersForUser = Answer.getAnswersByUsernameAndSurveyID(username, surveyId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Answer answer : answersForUser) {
            String statement = survey.getQuestion(answer.getQuestionId()).getStatement();
            answers.getChildren().add(new Label(statement));
            if (answer instanceof FreeAnswer) createFreeAnswer((FreeAnswer) answer);
            else if (answer instanceof NumericAnswer) createNumericAnswer((NumericAnswer) answer);
            else if (answer instanceof UnivaluedQualitativeAnswer) createUnivaluedQualitativeAnswer((UnivaluedQualitativeAnswer) answer);
            else if (answer instanceof MultivaluedQualitativeAnswer) createMultivaluedQualitativeAnswer((MultivaluedQualitativeAnswer) answer);
            answers.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }
    }

    private void createMultivaluedQualitativeAnswer(MultivaluedQualitativeAnswer answer) {
        answer.getOptions().forEach(option -> answers.getChildren().add(new Label(option.getValue())));
    }

    private void createUnivaluedQualitativeAnswer(UnivaluedQualitativeAnswer answer) {
        answers.getChildren().add(new Label(answer.getOption().getValue()));
    }

    private void createNumericAnswer(NumericAnswer answer) {
        answers.getChildren().add(new Label(answer.getValue().toString()));
    }

    private void createFreeAnswer(FreeAnswer answer) {
        answers.getChildren().add(new Label(answer.getValue()));
    }
}
