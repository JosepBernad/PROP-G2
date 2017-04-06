package survey;


import question.NumericQuestion;
import question.Question;

import java.util.HashSet;
import java.util.Set;

public class Survey
{
    private Integer id;
    private String name;
    private String description;
    private Boolean finished;
    private Boolean visible;
    private Set<Question> questions;

    public Survey() {
        questions = new HashSet<>();
    }

    public void addQuestion(Question question)
    {
        questions.add(question);
    }

    public Set<Question> getQuestions() {
        return questions;
    }
}
