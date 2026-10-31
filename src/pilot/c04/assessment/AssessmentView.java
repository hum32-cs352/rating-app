package pilot.c04.assessment;

import domain.SectionName;
import domain.Student;
import domain.reflection.Matrix;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A selection of columns from an AssessmentMatrix; all rows are kept. */
public final class AssessmentView implements Matrix<AssessmentRecord> {
    private final AssessmentMatrix source;
    private final List<String> selectedColumnIds;
    private final int[] sourceColumns;

    public AssessmentView(AssessmentMatrix source, List<String> selectedColumnIds) {
        if (source == null) throw new IllegalArgumentException("source is required");
        if (selectedColumnIds == null) throw new IllegalArgumentException("selection is required");
        List<String> copy = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        int[] mapped = new int[selectedColumnIds.size()];
        int i = 0;
        for (String id : selectedColumnIds) {
            if (id == null) throw new IllegalArgumentException("null selected ID");
            if (!seen.add(id)) throw new IllegalArgumentException("duplicate selected ID " + id);
            int col = source.getColumnIds().indexOf(id);
            if (col < 0) throw new IllegalArgumentException("unknown selected ID " + id);
            mapped[i++] = col;
            copy.add(id);
        }
        this.source = source;
        this.selectedColumnIds = Collections.unmodifiableList(copy);
        this.sourceColumns = mapped;
    }

    @Override public int rows() { return source.rows(); }
    @Override public int cols() { return selectedColumnIds.size(); }
    @Override public List<String> getRowIds() { return source.getRowIds(); }
    @Override public List<String> getColumnIds() { return selectedColumnIds; }

    @Override
    public AssessmentRecord get(int row, int col) {
        checkRow(row);
        checkCol(col);
        return source.get(row, sourceColumns[col]);
    }

    public String getCourseCode() { return source.getCourseCode(); }
    public String getSemester() { return source.getSemester(); }
    public SectionName getSectionName() { return source.getSectionName(); }

    public Student getStudent(int row) {
        checkRow(row);
        return source.getStudent(row);
    }

    public Assessment getAssessment(int col) {
        checkCol(col);
        return source.getAssessment(sourceColumns[col]);
    }

    private void checkRow(int row) {
        if (row < 0 || row >= rows()) throw new IndexOutOfBoundsException("row " + row);
    }

    private void checkCol(int col) {
        if (col < 0 || col >= cols()) throw new IndexOutOfBoundsException("col " + col);
    }
}
