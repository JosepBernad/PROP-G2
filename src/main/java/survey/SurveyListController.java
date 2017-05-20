package survey;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SurveyListController {

    @FXML
    private JFXListView<Label> surveyList;

    @FXML
    public void initialize() {
        for (Survey survey : Survey.getSurveys().values())
            surveyList.getItems().add(new Label(survey.getTitle()));
    }
}
