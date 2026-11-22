package domain.reflection;
import java.util.*;

public enum ReflectionQuestion {

    STUDY_PATTERN(
        "Study pattern",
        List.of(
            "Consistent from week 1",
            "Became consistent later",
            "Not consistent overall"
        )
    ),

    CONSULTATIONS(
        "Consultations",
        List.of("Once", "Twice", "Thrice", "None")
    ),

    KEY_ISSUE(
        "Key issue",
        List.of(
            "Time management",
            "Technical setup",
            "Attendance",
            "Personal",
            "Other"
        )
    ),

    READING_HABIT(
        "Reading habit",
        List.of(
            "Regular textbook",
            "Occasional textbook",
            "Mostly slides & class notes",
            "Videos"
        )
    ),

    CODING_STRENGTH(
        "Coding strength",
        List.of("Positive", "Struggling", "Negative")
    ),

    EXPECTED_MIDTERM_GRADE(
        "Expected midterm",
        List.of("A", "B", "C", "D")
    );

    private final String label;
    private final List<String> choices;

    private ReflectionQuestion(String label, List<String> choices) {
        this.label = label;
        this.choices = choices;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getChoices() {
        return choices;
    }

    public boolean isAllowed(String answer) {
        return answer != null && !answer.isBlank() && choices.contains(answer);
    }
}


