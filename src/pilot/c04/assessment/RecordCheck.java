package pilot.c04.assessment;

import domain.SectionName;
import domain.Student;
import domain.reflection.ReflectionQuestion;
import domain.reflection.SelfReflection;
import domain.reflection.StudentPortfolio;
import java.time.LocalDateTime;
import java.util.Map;

/** Checks AssessmentRecord through ReflectionRecord. Prints nothing. */
public final class RecordCheck {
    private RecordCheck() { }

    public static void run() {
        Schedule schedule = new Schedule(LocalDateTime.of(2026, 10, 12, 9, 0),
                LocalDateTime.of(2026, 10, 12, 10, 0));
        Reflection a1 = new Reflection("A1", "CS-352", "2026-Semester-II",
                SectionName.C, Purpose.FORMATIVE, schedule);
        Student s = new Student("B25110006008", "Affan Rauf", SectionName.C);
        StudentPortfolio p = new StudentPortfolio("B25110006008", "CS-352", "2026-Semester-II",
                "form-1", new SelfReflection(Map.of(ReflectionQuestion.CONSULTATIONS, "None"), ""));

        AssessmentRecord r = new ReflectionRecord(s, a1, p, null);

        // shared references, not copies
        verify(r.getStudent() == s, "student is the same object");
        verify(r.getAssessment() == a1, "assessment is the same object");

        // polymorphic evidence access
        verify("form-1".equals(r.getEvidenceReference()), "evidence through AssessmentRecord");

        // required references
        expectIae(() -> new ReflectionRecord(null, a1, p, null), "null student");
        expectIae(() -> new ReflectionRecord(s, null, p, null), "null assessment");

        // different section
        Student other = new Student("B25110006008", "Affan Rauf", SectionName.B);
        expectIae(() -> new ReflectionRecord(other, a1, p, null), "different section");
    }

    private static void verify(boolean condition, String what) {
        if (!condition) throw new AssertionError("RecordCheck: " + what);
    }

    private static void expectIae(Runnable action, String what) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        } catch (Throwable other) {
            throw new AssertionError("RecordCheck: " + what + " threw " + other, other);
        }
        throw new AssertionError("RecordCheck: " + what + " was accepted");
    }
}
