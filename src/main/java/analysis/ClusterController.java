package analysis;

import answer.*;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import question.Option;
import question.Question;
import survey.Survey;

import java.util.List;
import java.util.stream.Collectors;

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

        VBox statements = new VBox();
        statements.getChildren().addAll(new Label("Statements"), new Separator(Orientation.HORIZONTAL));
        for (Question question : survey.getQuestions())
            statements.getChildren().add(new Label(question.getStatement()));
        answers.getChildren().add(statements);

        for (List<Answer> list : listOfLists) {
            VBox column = new VBox();
            String username = list.get(0).getUsername();
            if (username == null) username = "Centroid";
            column.getChildren().addAll(new Label(username), new Separator(Orientation.HORIZONTAL));
            for (Answer answer : list) {
                if (answer instanceof FreeAnswer) createFreeAnswer((FreeAnswer) answer, column);
                else if (answer instanceof NumericAnswer) createNumericAnswer((NumericAnswer) answer, column);
                else if (answer instanceof UnivaluedQualitativeAnswer)
                    createUnivaluedQualitativeAnswer((UnivaluedQualitativeAnswer) answer, column);
                else if (answer instanceof MultivaluedQualitativeAnswer)
                    createMultivaluedQualitativeAnswer((MultivaluedQualitativeAnswer) answer, column);
            }
            answers.getChildren().addAll(new Separator(Orientation.VERTICAL), column);
        }
    }

    private void createMultivaluedQualitativeAnswer(MultivaluedQualitativeAnswer answer, VBox box) {
        String options = answer.getOptions().stream().
                map(Option::getValue).
                collect(Collectors.joining(", "));
        box.getChildren().add(new Label(options));
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
