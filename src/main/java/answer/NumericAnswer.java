package answer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class NumericAnswer extends Answer {

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public void save() {
        Set<Answer> answers = getAnswers();
        answers.add(this);
        saveAnswersInFile(answers, ANSWERS);
    }

    private void saveAnswersInFile(Set<Answer> answers, String filename) {

        FileWriter fileWriter = null;
        try {
            File file = new File(filename);
            fileWriter = new FileWriter(file);
            if (!file.exists()) file.createNewFile();
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructCollectionType(Set.class, Answer.class);
            fileWriter.write(mapper.writerFor(type).writeValueAsString(answers));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
