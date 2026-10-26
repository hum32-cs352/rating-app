package domain.reflection;
import java.util.HashMap;
import java.util.Map;

public final class SelfReflection {
    private final String remarks;
    private final Map<ReflectionQuestion,String> answers;

    public SelfReflection(Map<ReflectionQuestion,String> answers,String remarks) {
        if(remarks == null) {
            throw new IllegalArgumentException("Remarks cannot be null");
        }
        if(answers == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }

        Map<ReflectionQuestion, String> copy = new HashMap<>();

        for(Map.Entry<ReflectionQuestion,String> entry : answers.entrySet()) {
            String value = entry.getValue();
            ReflectionQuestion key = entry.getKey();

            if(value == null) {
                throw new IllegalArgumentException("there needs to be a value");
            }

            if(key == null) {
                throw new IllegalArgumentException("no key found ");
            }

            if (!key.isAllowed(value)) {
                throw new IllegalArgumentException("Invalid answer for " + key.getLabel() + ": " + value);
            }

            copy.put(key,value);
        }

        this.remarks = remarks;
        this.answers = copy;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public String getAnswer(ReflectionQuestion question) {
        if(question == null) {
            throw new IllegalArgumentException("question cannot be empty");
        }
        String answer = this.answers.get(question);
        return answer;
    }
}
