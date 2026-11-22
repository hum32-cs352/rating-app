package pilot.c04.assessment;

import domain.SectionName;
import domain.Student;
import domain.reflection.Matrix;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Overview of one offering: rows are students, columns are activities. */
public final class AssessmentMatrix implements Matrix<AssessmentRecord> {
    private final String courseCode;
    private final String semester;
    private final SectionName sectionName;
    private final List<Student> students;
    private final List<Assessment> assessments;
    private final List<String> rowIds;
    private final List<String> columnIds;
    private final AssessmentRecord[][] cells;

    public AssessmentMatrix(String courseCode, String semester, SectionName sectionName,
                            List<Student> students, List<Assessment> assessments,
                            List<AssessmentRecord> records) {
        this.courseCode = Assessment.requireText(courseCode, "courseCode");
        this.semester = Assessment.requireText(semester, "semester");
        if (sectionName == null) throw new IllegalArgumentException("sectionName is required");
        if (students == null || assessments == null || records == null) {
            throw new IllegalArgumentException("lists are required");
        }
        this.sectionName = sectionName;

        // copy roster
        List<Student> rosterCopy = new ArrayList<>();
        List<String> seats = new ArrayList<>();
        Map<String, Integer> rowOf = new HashMap<>();
        for (Student s : students) {
            if (s == null) throw new IllegalArgumentException("null student");
            if (s.getSectionName() != sectionName) {
                throw new IllegalArgumentException("student section differs from matrix section");
            }
            if (rowOf.containsKey(s.getSeatNo())) {
                throw new IllegalArgumentException("duplicate seat " + s.getSeatNo());
            }
            rowOf.put(s.getSeatNo(), rowCount(rosterCopy));
            rosterCopy.add(s);
            seats.add(s.getSeatNo());
        }

        // copy activities
        List<Assessment> activityCopy = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        Map<String, Integer> colOf = new HashMap<>();
        for (Assessment a : assessments) {
            if (a == null) throw new IllegalArgumentException("null assessment");
            if (!a.getCourseCode().equals(courseCode) || !a.getSemester().equals(semester)
                    || a.getSectionName() != sectionName) {
                throw new IllegalArgumentException("assessment " + a.getId()
                        + " is from a different offering");
            }
            if (colOf.containsKey(a.getId())) {
                throw new IllegalArgumentException("duplicate activity ID " + a.getId());
            }
            colOf.put(a.getId(), activityCopy.size());
            activityCopy.add(a);
            ids.add(a.getId());
        }

        this.students = Collections.unmodifiableList(rosterCopy);
        this.assessments = Collections.unmodifiableList(activityCopy);
        this.rowIds = Collections.unmodifiableList(seats);
        this.columnIds = Collections.unmodifiableList(ids);

        // place records; sizes come from the lists
        this.cells = new AssessmentRecord[rosterCopy.size()][activityCopy.size()];
        for (AssessmentRecord r : records) {
            if (r == null) throw new IllegalArgumentException("null record");
            Integer row = rowOf.get(r.getStudent().getSeatNo());
            Integer col = colOf.get(r.getAssessment().getId());
            if (row == null) throw new IllegalArgumentException("record student is not in the roster");
            if (col == null) throw new IllegalArgumentException("record activity is not a column");
            if (activityCopy.get(col) != r.getAssessment()) {
                throw new IllegalArgumentException("record uses a different Assessment object");
            }
            if (cells[row][col] != null) {
                throw new IllegalArgumentException("duplicate cell");
            }
            cells[row][col] = r;
        }
    }

    private static int rowCount(List<Student> list) { return list.size(); }

    @Override public int rows() { return students.size(); }
    @Override public int cols() { return assessments.size(); }
    @Override public List<String> getRowIds() { return rowIds; }
    @Override public List<String> getColumnIds() { return columnIds; }

    @Override
    public AssessmentRecord get(int row, int col) {
        checkRow(row);
        checkCol(col);
        return cells[row][col];
    }

    public String getCourseCode() { return courseCode; }
    public String getSemester() { return semester; }
    public SectionName getSectionName() { return sectionName; }

    public Student getStudent(int row) {
        checkRow(row);
        return students.get(row);
    }

    public Assessment getAssessment(int col) {
        checkCol(col);
        return assessments.get(col);
    }

    private void checkRow(int row) {
        if (row < 0 || row >= rows()) throw new IndexOutOfBoundsException("row " + row);
    }

    private void checkCol(int col) {
        if (col < 0 || col >= cols()) throw new IndexOutOfBoundsException("col " + col);
    }
}
