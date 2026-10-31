package pilot.c04.assessment;

import domain.SectionName;
import domain.Student;
import domain.reflection.ReflectionQuestion;
import domain.reflection.SelfReflection;
import domain.reflection.StudentPortfolio;
import java.time.LocalDateTime;
import java.util.Map;

/** Checks that ReflectionRecord links the right student, activity and form. Prints nothing. */
public final class LinkCheck {
    private static final String COURSE = "CS-352";
    private static final String TERM = "2026-Semester-II";

    private LinkCheck() { }

    public static void run() {
        Schedule schedule = new Schedule(LocalDateTime.of(2026, 10, 12, 9, 0),
                LocalDateTime.of(2026, 10, 12, 10, 0));
        Reflection a1 = new Reflection("A1", COURSE, TERM, SectionName.C,
                Purpose.FORMATIVE, schedule);
        Student s = new Student("B25110006008", "Affan Rauf", SectionName.C);
        StudentPortfolio good = portfolio("B25110006008", COURSE, TERM);

        // matching record; objects retained; time may be null or a late time
        ReflectionRecord noTime = new ReflectionRecord(s, a1, good, null);
        verify(noTime.getStudent() == s && noTime.getAssessment() == a1, "student/reflection kept");
        verify(noTime.getPortfolio() == good, "portfolio kept");
        verify(noTime.getSubmittedAt() == null, "null time kept");
        LocalDateTime late = LocalDateTime.of(2026, 10, 20, 8, 0);
        verify(new ReflectionRecord(s, a1, good, late).getSubmittedAt().equals(late),
                "late form kept");
        verify(noTime.getEvidenceReference().equals(good.getSourceRef()), "evidence is sourceRef");

        // mismatches
        expectIae(() -> new ReflectionRecord(s, a1, portfolio("B25110006026", COURSE, TERM), null),
                "wrong seat");
        expectIae(() -> new ReflectionRecord(s, a1, portfolio("B25110006008", "CS-351", TERM), null),
                "wrong course");
        expectIae(() -> new ReflectionRecord(s, a1, portfolio("B25110006008", COURSE, "2026-Semester-I"), null),
                "wrong semester");
        expectIae(() -> new ReflectionRecord(s, a1, null, null), "null portfolio");
        Student otherSection = new Student("B25110006008", "Affan Rauf", SectionName.A);
        expectIae(() -> new ReflectionRecord(otherSection, a1, good, null), "wrong section");
    }

    private static StudentPortfolio portfolio(String seat, String course, String term) {
        return new StudentPortfolio(seat, course, term, "form-" + seat,
                new SelfReflection(Map.of(ReflectionQuestion.CONSULTATIONS, "Once"), ""));
    }

    private static void verify(boolean condition, String what) {
        if (!condition) throw new AssertionError("LinkCheck: " + what);
    }

    private static void expectIae(Runnable action, String what) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        } catch (Throwable other) {
            throw new AssertionError("LinkCheck: " + what + " threw " + other, other);
        }
        throw new AssertionError("LinkCheck: " + what + " was accepted");
    }
}
