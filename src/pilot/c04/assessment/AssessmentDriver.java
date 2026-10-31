package pilot.c04.assessment;

import domain.SectionName;
import domain.Student;
import domain.reflection.ReflectionMatrixView;
import domain.reflection.ReflectionQuestion;
import domain.reflection.SelfReflection;
import domain.reflection.StudentPortfolio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * C04 demo: three-student Reflection pilot (3 x 1 overview) and the
 * six-question detail view (3 x 6). All values below are TEST DATA.
 */
public final class AssessmentDriver {

    private static final String COURSE = "CS-352";
    private static final String TERM = "2026-Semester-II";
    private static final SectionName SECTION = SectionName.C;
    private static final String MISSING = "-";

    private AssessmentDriver() {
    }

    public static void main(String[] args) {
        // ---- students (official seat numbers are the row IDs) ----
        Student affan = new Student("B25110006008", "Affan Rauf", SECTION);
        Student aown = new Student("B25110006026", "Aown Ali", SECTION);
        Student abdul = new Student("B25110006004", "Abdul Rehman", SECTION);
        List<Student> students = List.of(affan, aown, abdul);

        // ---- one FORMATIVE Reflection activity, stable ID "A1" ----
        Schedule schedule = new Schedule(
                LocalDateTime.of(2026, 10, 12, 9, 0),
                LocalDateTime.of(2026, 10, 12, 23, 59));
        Reflection a1 = new Reflection("A1", COURSE, TERM, SECTION,
                Purpose.FORMATIVE, schedule);
        List<Assessment> assessments = List.of(a1);

        // ---- test forms: Affan and Aown submitted, Abdul Rehman did not ----
        Map<ReflectionQuestion, String> affanAnswers = new EnumMap<>(ReflectionQuestion.class);
        affanAnswers.put(ReflectionQuestion.CONSULTATIONS, "None");
        affanAnswers.put(ReflectionQuestion.CODING_STRENGTH, "Positive");
        affanAnswers.put(ReflectionQuestion.EXPECTED_MIDTERM_GRADE, "B");

        Map<ReflectionQuestion, String> aownAnswers = new EnumMap<>(ReflectionQuestion.class);
        aownAnswers.put(ReflectionQuestion.CONSULTATIONS, "Once");
        aownAnswers.put(ReflectionQuestion.KEY_ISSUE, "Attendance");
        aownAnswers.put(ReflectionQuestion.READING_HABIT, "Videos");
        aownAnswers.put(ReflectionQuestion.CODING_STRENGTH, "Struggling");
        aownAnswers.put(ReflectionQuestion.EXPECTED_MIDTERM_GRADE, "C");

        StudentPortfolio affanForm = new StudentPortfolio(affan.getSeatNo(), COURSE, TERM,
                "Test form 1 (Affan)", new SelfReflection(affanAnswers, ""));
        StudentPortfolio aownForm = new StudentPortfolio(aown.getSeatNo(), COURSE, TERM,
                "Test form 2 (Aown)", new SelfReflection(aownAnswers, ""));

        // Affan has a known time; Aown's time is unknown (null).
        List<AssessmentRecord> records = List.of(
                new ReflectionRecord(affan, a1, affanForm, LocalDateTime.of(2026, 10, 12, 10, 30)),
                new ReflectionRecord(aown, a1, aownForm, null));

        AssessmentMatrix matrix = new AssessmentMatrix(COURSE, TERM, SECTION,
                students, assessments, records);

        // ---- overview: 3 x 1 ----
        System.out.println("=== C04 Assessment Overview (test data) ===");
        System.out.println(COURSE + " | " + TERM + " | Section " + SECTION);
        System.out.println("Rows: " + matrix.rows() + " | Columns: " + matrix.cols());
        for (int c = 0; c < matrix.cols(); c++) {
            Assessment a = matrix.getAssessment(c);
            System.out.println("Column " + a.getId() + " " + a.getName()
                    + " (" + a.getPurpose() + ")");
        }
        for (int r = 0; r < matrix.rows(); r++) {
            Student s = matrix.getStudent(r);
            StringBuilder line = new StringBuilder();
            line.append(s.getSeatNo()).append(" | ").append(s.getName());
            for (int c = 0; c < matrix.cols(); c++) {
                line.append(" | ").append(describe(matrix.get(r, c)));
            }
            System.out.println(line);
        }

        // ---- detail: extract only A1 ReflectionRecord portfolios ----
        int a1Col = matrix.getColumnIds().indexOf("A1");
        if (a1Col < 0) {
            throw new IllegalStateException("A1 column not found");
        }
        List<StudentPortfolio> portfolios = new ArrayList<>();
        for (int r = 0; r < matrix.rows(); r++) {
            AssessmentRecord record = matrix.get(r, a1Col);
            if (record instanceof ReflectionRecord) {
                portfolios.add(((ReflectionRecord) record).getPortfolio());
            }
        }

        // pass ALL three seats so the missing form still gets a row of nulls
        ReflectionMatrixView detail = new ReflectionMatrixView(COURSE, TERM,
                matrix.getRowIds(), portfolios);

        System.out.println();
        System.out.println("=== A1 Reflection detail (test data) ===");
        System.out.println("Rows: " + detail.rows() + " | Columns: " + detail.cols());
        System.out.println("Columns: " + detail.getColumnIds());
        for (int r = 0; r < detail.rows(); r++) {
            StringBuilder line = new StringBuilder();
            line.append(detail.getRowIds().get(r));
            for (int c = 0; c < detail.cols(); c++) {
                String answer = detail.get(r, c);
                line.append(" | ").append(answer == null ? MISSING : answer);
            }
            System.out.println(line);
        }
        System.out.println("\"" + MISSING + "\" = missing answer or form (null). "
                + "\"None\" is a chosen answer.");
    }

    /** One overview cell: missing form, or received with its submission time. */
    private static String describe(AssessmentRecord record) {
        if (record == null) {
            return MISSING;
        }
        String when = "Not recorded";
        if (record instanceof ReflectionRecord) {
            LocalDateTime t = ((ReflectionRecord) record).getSubmittedAt();
            if (t != null) {
                when = t.toString();
            }
        }
        return "Received / verification pending | submitted: " + when;
    }
}
