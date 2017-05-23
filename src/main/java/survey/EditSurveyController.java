package survey;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import question.FreeQuestion;
import question.Question;


public class EditSurveyController {

    @FXML
    private JFXListView<JFXListCell> list;

    private Survey survey;

    public void initialize() {
        System.out.print("Survey with name: " + survey.getTitle());
        for (Question question : survey.getQuestions()) {
            System.out.print("Question with name: " + question.getStatement());
                if (question.getClass() == "FreeQuestion")
                {
                    list.getItems().add(getFreeQuestion(question));
                }
        }
    }

    private JFXListCell getFreeQuestion(FreeQuestion question)
    {
        JFXListCell cell;
        cell.
        Label statement = new Label(question.getStatement());
        JFXTextField textField = new JFXTextField();
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

}
