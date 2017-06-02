package answer;

import analysis.DistanceCalculator;
import question.Option;
import question.QualitativeQuestion;
import survey.Survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aquesta classe és una subclasse de QualitativeAnswer i ens permet gestionar la resposta
 d’un usuari a una pregunta de tipus qualitativa no avaluada d’una enquesta determinada
 *Per aquesta classe disposem d’un atribut (a part dels de la seva superclasse), una opcio on
 guardem la opcio que ha respos l’usuari
 */
public class UnivaluedQualitativeAnswer extends QualitativeAnswer {

    private Option option;

    /**
     * Aquest mètode calcula el centroide d'un conjunt de respostes de tipus UnivaluedQualitativeAnswer
     * @param answers són les respostes
     * @return UnivaluedQualitativeAnswer
     */
    public static UnivaluedQualitativeAnswer calculateCentroid(List<UnivaluedQualitativeAnswer> answers) {
        Map<Option, Integer> occurrences = new HashMap<>();

        for (UnivaluedQualitativeAnswer answer : answers) {
            Option value = answer.getOption();
            if (value != null) {
                if (!occurrences.containsKey(value)) occurrences.put(value, 0);
                occurrences.put(value, occurrences.get(value) + 1);
            }
        }

        Option mode = null;
        Integer max = 0;

        for (Option option : occurrences.keySet()) {
            Integer integer = occurrences.get(option);
            if (integer > max) {
                mode = option;
                max = integer;
            }
        }

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(mode);
        return univaluedQualitativeAnswer;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    /**
     * Aquest mètode retorna la distància (entre 0 i 1) entre una resposta i una altre resposta del
     mateix tipus
     * @param answer és la resposta
     * @return Double
     */
    @Override
    public Double calculateDistance(Answer answer) {
        Option answerOption = ((UnivaluedQualitativeAnswer) answer).getOption();
        if (getOption() == null || answerOption == null) return 1D;

        if (this.option.getWeight() == null)
            return DistanceCalculator.calculateUnsortedUnivaluedQualitative(this.getOption().getValue(), answerOption.getValue());
        QualitativeQuestion question = (QualitativeQuestion) Survey.getSurveyById(getSurveyId()).getQuestion(getQuestionId());
        return DistanceCalculator.calculateSortedUnivaluedQualitative(this.getOption().getWeight(), answerOption.getWeight(), question.getOptions().size());
    }
}
