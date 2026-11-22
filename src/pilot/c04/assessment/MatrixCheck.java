package pilot.c04.assessment;

import domain.SectionName;
import domain.Student;
import domain.reflection.ReflectionMatrixView;
import domain.reflection.ReflectionQuestion;
import domain.reflection.SelfReflection;
import domain.reflection.StudentPortfolio;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Checks AssessmentMatrix and AssessmentView (T04 to T09, T12, T14). Prints nothing. */
public final class MatrixCheck {
    private static final String COURSE = "CS-352";
    private static final String TERM = "2026-Semester-II";
    private static final SectionName SEC = SectionName.C;
    private static final Schedule SCH = new Schedule(LocalDateTime.of(2026, 10, 12, 9, 0),
            LocalDateTime.of(2026, 10, 12, 10, 0));

    private MatrixCheck() { }

    // ---- test-only future Direct activity and its record ----
    private static final class TestDirect extends Direct {
        private final String name;
        TestDirect(String id, String name, Purpose purpose) {
            super(id, COURSE, TERM, SEC, purpose, SCH);
            this.name = name;
        }
        @Override public String getName() { return name; }
    }

    private static final class TestDirectRecord extends AssessmentRecord {
        TestDirectRecord(Student s, Assessment a) { super(s, a); }
        @Override public String getEvidenceReference() { return "test-direct"; }
    }

    public static void run() {
        Student s1 = new Student("B25110006008", "Affan Rauf", SEC);
        Student s2 = new Student("B25110006026", "Aown Ali", SEC);
        Student s3 = new Student("B25110006004", "Abdul Rehman", SEC);
        List<Student> roster = List.of(s1, s2, s3);

        Reflection a1 = new Reflection("A1", COURSE, TERM, SEC, Purpose.FORMATIVE, SCH);
        StudentPortfolio p1 = portfolio(s1, Map.of(ReflectionQuestion.CONSULTATIONS, "None"));
        StudentPortfolio p2 = portfolio(s2, Map.of(ReflectionQuestion.CONSULTATIONS, "Once"));
        ReflectionRecord r1 = new ReflectionRecord(s1, a1, p1, null);
        ReflectionRecord r2 = new ReflectionRecord(s2, a1, p2, null);

        // T04: pilot 3 x 1, two forms, one missing cell; input order changes nothing
        AssessmentMatrix pilot = new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1), List.of(r1, r2));
        AssessmentMatrix reordered = new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1), List.of(r2, r1));
        verify(pilot.rows() == 3 && pilot.cols() == 1, "pilot is 3 x 1");
        verify(pilot.getRowIds().equals(List.of("B25110006008", "B25110006026", "B25110006004")),
                "row IDs are the official seats");
        verify(pilot.getColumnIds().equals(List.of("A1")), "column ID is A1");
        verify(pilot.getAssessment(0).getName().equals("Reflection"), "A1 name");
        verify(pilot.get(0, 0) == r1 && pilot.get(1, 0) == r2 && pilot.get(2, 0) == null,
                "two records and one missing cell");
        for (int r = 0; r < 3; r++) {
            verify(pilot.get(r, 0) == reordered.get(r, 0), "reordering changes no cell, row " + r);
        }
        verify(pilot.getCourseCode().equals(COURSE) && pilot.getSemester().equals(TERM)
                && pilot.getSectionName() == SEC, "context getters");

        // T05: missing stays null; "None" stays "None"; B05 answers unchanged
        List<StudentPortfolio> found = new ArrayList<>();
        for (int r = 0; r < pilot.rows(); r++) {
            AssessmentRecord rec = pilot.get(r, 0);
            if (rec instanceof ReflectionRecord) found.add(((ReflectionRecord) rec).getPortfolio());
        }
        ReflectionMatrixView detail = new ReflectionMatrixView(COURSE, TERM, pilot.getRowIds(), found);
        int consult = ReflectionQuestion.CONSULTATIONS.ordinal();
        verify("None".equals(detail.get(0, consult)), "chosen None stays None");
        verify("Once".equals(detail.get(1, consult)), "Once stays Once");
        for (int c = 0; c < detail.cols(); c++) {
            verify(detail.get(2, c) == null, "missing form is null, col " + c);
        }
        verify(detail.rows() == 3 && detail.cols() == 6, "detail is 3 x 6");
        verify("None".equals(p1.getReflection().getAnswer(ReflectionQuestion.CONSULTATIONS)),
                "original B05 answer unchanged");

        // T06: duplicates, wrong context, outside records, different Assessment object
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, List.of(s1, s1),
                List.of(a1), List.of()), "duplicate seat");
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1, a1), List.of()), "duplicate column ID");
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1), List.of(r1, r1)), "duplicate cell");
        expectIae(() -> new AssessmentMatrix("CS-351", TERM, SEC, roster,
                List.of(a1), List.of()), "wrong course context");
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SectionName.B, roster,
                List.of(a1), List.of()), "wrong section context");
        Student outsider = new Student("B25110009999", "Outsider", SEC);
        ReflectionRecord outside = new ReflectionRecord(outsider, a1,
                portfolio(outsider, Map.of()), null);
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1), List.of(outside)), "record outside roster");
        Reflection a9 = new Reflection("A9", COURSE, TERM, SEC, Purpose.FORMATIVE, SCH);
        ReflectionRecord unknown = new ReflectionRecord(s1, a9, p1, null);
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1), List.of(unknown)), "unknown activity");
        Reflection a1Twin = new Reflection("A1", COURSE, TERM, SEC, Purpose.FORMATIVE, SCH);
        ReflectionRecord twin = new ReflectionRecord(s1, a1Twin, p1, null);
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1), List.of(twin)), "same ID but different Assessment object");
        expectIae(() -> new AssessmentMatrix(COURSE, TERM, SEC, null,
                List.of(a1), List.of()), "null roster");

        // mixed 3 x 3 test matrix: A1 Reflection, A2 Midterm, A3 Final Lab (left empty)
        TestDirect a2 = new TestDirect("A2", "Midterm", Purpose.SUMMATIVE);
        TestDirect a3 = new TestDirect("A3", "Final Lab Examination", Purpose.SUMMATIVE);
        TestDirectRecord mid1 = new TestDirectRecord(s1, a2);
        TestDirectRecord mid3 = new TestDirectRecord(s3, a2);
        AssessmentMatrix mixed = new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1, a2, a3), List.of(r1, r2, mid1, mid3));
        verify(mixed.rows() == 3 && mixed.cols() == 3, "mixed is 3 x 3");
        verify(mixed.get(0, 1) == mid1 && mixed.get(2, 1) == mid3 && mixed.get(1, 1) == null,
                "Direct records placed by seat and ID");
        for (int r = 0; r < 3; r++) {
            verify(mixed.get(r, 2) == null, "A3 column is empty, row " + r);
        }

        // T07: indexes and [A3, A1] selection
        expectIoobe(() -> mixed.get(-1, 0), "row -1");
        expectIoobe(() -> mixed.get(3, 0), "row 3");
        expectIoobe(() -> mixed.get(0, -1), "col -1");
        expectIoobe(() -> mixed.get(0, 3), "col 3");
        expectIoobe(() -> mixed.getStudent(3), "getStudent 3");
        expectIoobe(() -> mixed.getAssessment(-1), "getAssessment -1");
        AssessmentView sel = new AssessmentView(mixed, List.of("A3", "A1"));
        verify(sel.rows() == 3 && sel.cols() == 2, "selection is 3 x 2");
        verify(sel.getColumnIds().equals(List.of("A3", "A1")), "selection keeps requested order");
        verify(sel.getRowIds().equals(mixed.getRowIds()), "all rows kept");
        verify(sel.getAssessment(0) == a3 && sel.getAssessment(1) == a1, "getAssessment mapped");
        for (int r = 0; r < 3; r++) {
            verify(sel.get(r, 0) == mixed.get(r, 2), "A3 cell is the same object, row " + r);
            verify(sel.get(r, 1) == mixed.get(r, 0), "A1 cell is the same object, row " + r);
            verify(sel.getStudent(r) == roster.get(r), "student mapped, row " + r);
        }
        expectIoobe(() -> sel.get(0, 2), "view col 2");
        expectIoobe(() -> sel.get(3, 0), "view row 3");
        verify(sel.getCourseCode().equals(COURSE) && sel.getSemester().equals(TERM)
                && sel.getSectionName() == SEC, "view context delegates");

        // T08: later changes to inputs cannot change the matrices
        List<Student> mutableRoster = new ArrayList<>(roster);
        List<Assessment> mutableActs = new ArrayList<>(List.of(a1));
        List<AssessmentRecord> mutableRecs = new ArrayList<>(List.of(r1, r2));
        AssessmentMatrix snap = new AssessmentMatrix(COURSE, TERM, SEC,
                mutableRoster, mutableActs, mutableRecs);
        mutableRoster.clear();
        mutableActs.clear();
        mutableRecs.clear();
        verify(snap.rows() == 3 && snap.cols() == 1 && snap.get(0, 0) == r1,
                "matrix unaffected by input list changes");
        List<String> selection = new ArrayList<>(List.of("A3", "A1"));
        AssessmentView copySel = new AssessmentView(mixed, selection);
        selection.clear();
        verify(copySel.cols() == 2 && copySel.getColumnIds().equals(List.of("A3", "A1")),
                "view unaffected by selection list changes");
        expectUoe(() -> mixed.getRowIds().add("x"), "row IDs editable");
        expectUoe(() -> mixed.getColumnIds().add("x"), "column IDs editable");
        expectUoe(() -> sel.getColumnIds().add("x"), "view column IDs editable");
        expectUoe(() -> sel.getRowIds().add("x"), "view row IDs editable");
        expectIae(() -> new AssessmentView(mixed, List.of("A1", "A1")), "duplicate selected ID");
        expectIae(() -> new AssessmentView(mixed, List.of("A7")), "unknown selected ID");
        expectIae(() -> new AssessmentView(mixed, Collections.singletonList(null)), "null selected ID");
        expectIae(() -> new AssessmentView(mixed, null), "null selection");
        expectIae(() -> new AssessmentView(null, List.of("A1")), "null source");
        for (Field f : AssessmentMatrix.class.getDeclaredFields()) {
            if (f.getType().isArray()) {
                verify(Modifier.isPrivate(f.getModifiers()), "backing array is private");
            }
        }

        // T09: empty shapes
        List<Student> none = List.of();
        AssessmentMatrix zeroByZero = new AssessmentMatrix(COURSE, TERM, SEC, none, List.of(), List.of());
        AssessmentMatrix threeByZero = new AssessmentMatrix(COURSE, TERM, SEC, roster, List.of(), List.of());
        AssessmentMatrix zeroByThree = new AssessmentMatrix(COURSE, TERM, SEC, none,
                List.of(a1, a2, a3), List.of());
        verify(zeroByZero.rows() == 0 && zeroByZero.cols() == 0, "0 x 0");
        verify(threeByZero.rows() == 3 && threeByZero.cols() == 0, "3 x 0");
        verify(zeroByThree.rows() == 0 && zeroByThree.cols() == 3, "0 x 3");
        expectIoobe(() -> zeroByZero.get(0, 0), "get on 0 x 0");
        expectIoobe(() -> threeByZero.get(0, 0), "get on 3 x 0");
        expectIoobe(() -> zeroByThree.get(0, 0), "get on 0 x 3");
        AssessmentView emptySel = new AssessmentView(mixed, List.of());
        verify(emptySel.rows() == 3 && emptySel.cols() == 0, "empty selection is N x 0");
        ReflectionMatrixView emptyDetail = new ReflectionMatrixView(COURSE, TERM, List.of(), List.of());
        verify(emptyDetail.rows() == 0 && emptyDetail.cols() == 6, "empty B05 detail is 0 x 6");
        expectIoobe(() -> emptyDetail.get(0, 0), "empty detail get");

        // T12: select Direct activities by type, without changing matrix code
        List<String> directIds = new ArrayList<>();
        for (int c = 0; c < mixed.cols(); c++) {
            if (mixed.getAssessment(c) instanceof Direct) directIds.add(mixed.getColumnIds().get(c));
        }
        AssessmentView directs = new AssessmentView(mixed, directIds);
        verify(directs.getColumnIds().equals(List.of("A2", "A3")), "Direct columns are A2, A3");
        verify(directs.get(0, 0) == mid1, "Direct view keeps the record");

        // T14: purposes retained; purpose filters
        AssessmentMatrix purposes = new AssessmentMatrix(COURSE, TERM, SEC, roster,
                List.of(a1, new TestDirect("A2", "Midterm", Purpose.SUMMATIVE),
                        new TestDirect("A3", "Final Lab Examination", Purpose.BOTH)),
                List.of(r1));
        verify(purposes.getAssessment(0).getPurpose() == Purpose.FORMATIVE
                && purposes.getAssessment(1).getPurpose() == Purpose.SUMMATIVE
                && purposes.getAssessment(2).getPurpose() == Purpose.BOTH, "purposes retained");
        List<String> formative = filter(purposes, Purpose.FORMATIVE);
        List<String> summative = filter(purposes, Purpose.SUMMATIVE);
        verify(formative.equals(List.of("A1", "A3")), "FORMATIVE filter gives A1, A3");
        verify(summative.equals(List.of("A2", "A3")), "SUMMATIVE filter gives A2, A3");
        AssessmentView fv = new AssessmentView(purposes, formative);
        AssessmentView sv = new AssessmentView(purposes, summative);
        verify(fv.cols() == 2 && sv.cols() == 2, "A3 appears once in each view");
        verify(fv.get(0, 0) == r1 && purposes.get(0, 0) == r1 && purposes.cols() == 3,
                "records and source matrix unchanged");
    }

    private static List<String> filter(AssessmentMatrix m, Purpose wanted) {
        List<String> ids = new ArrayList<>();
        for (int c = 0; c < m.cols(); c++) {
            Purpose p = m.getAssessment(c).getPurpose();
            if (p == wanted || p == Purpose.BOTH) ids.add(m.getColumnIds().get(c));
        }
        return ids;
    }

    private static StudentPortfolio portfolio(Student s, Map<ReflectionQuestion, String> answers) {
        return new StudentPortfolio(s.getSeatNo(), COURSE, TERM, "form-" + s.getSeatNo(),
                new SelfReflection(answers, ""));
    }

    private static void verify(boolean condition, String what) {
        if (!condition) throw new AssertionError("MatrixCheck: " + what);
    }

    private static void expectIae(Runnable action, String what) {
        expect(IllegalArgumentException.class, action, what);
    }

    private static void expectIoobe(Runnable action, String what) {
        expect(IndexOutOfBoundsException.class, action, what);
    }

    private static void expectUoe(Runnable action, String what) {
        expect(UnsupportedOperationException.class, action, what);
    }

    private static void expect(Class<? extends Throwable> type, Runnable action, String what) {
        try {
            action.run();
        } catch (Throwable t) {
            if (type.isInstance(t)) return;
            throw new AssertionError("MatrixCheck: " + what + " threw " + t, t);
        }
        throw new AssertionError("MatrixCheck: " + what + " was accepted");
    }
}
