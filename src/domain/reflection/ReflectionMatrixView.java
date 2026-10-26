package domain.reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReflectionMatrixView implements Matrix<String> {
    private final String courseCode;
    private final String termId;
    private final List<String> seatNos;
    private final List<ReflectionQuestion> questions;
    private final Map<String, StudentPortfolio> portfoliosBySeat;
    // private final LinkedHashMap<String, StudentPortfolio> portfoliosBySeat;

    public ReflectionMatrixView(String courseCode, String termId, List<String> seatNos,
            List<StudentPortfolio> portfolios) {
        if (courseCode == null || termId == null || courseCode.isBlank() || termId.isBlank()
                || !courseCode.equals(courseCode.trim()) || !termId.equals(termId.trim())) {
            throw new IllegalArgumentException("Invalid course or term ID");
        }

        if (seatNos == null || portfolios == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        this.courseCode = courseCode;
        this.termId = termId;

        List<String> roster = new ArrayList<>();

        for (String seat : seatNos) {
            if (seat == null || seat.isBlank() || !seat.equals(seat.trim()) || roster.contains(seat)) {
                throw new IllegalArgumentException("Invalid or duplicate seat");
            }

            roster.add(seat);
        }
        this.seatNos = Collections.unmodifiableList(roster);
        ArrayList<ReflectionQuestion> qList = new ArrayList<>();
        for (ReflectionQuestion q : ReflectionQuestion.values()) {
            qList.add(q);
        }

        this.questions = Collections.unmodifiableList(qList);
        this.portfoliosBySeat = new LinkedHashMap<>();

        for (StudentPortfolio p : portfolios) {

            if (p == null) {
                throw new IllegalArgumentException("Portfolio cannot be null");
            }

            String seat = p.getSeatNo();

            if (seat == null || seat.isBlank() || !seat.equals(seat.trim())) {
                throw new IllegalArgumentException("Invalid portfolio seat");
            }
            if (!roster.contains(seat)) {
                throw new IllegalArgumentException("Seat not in roster");
            }
            if (!courseCode.equals(p.getCourseCode()) || !termId.equals(p.getTermId())) {
                throw new IllegalArgumentException("Course or term mismatch");
            }
            if (portfoliosBySeat.containsKey(seat)) {
                throw new IllegalArgumentException("Duplicate portfolio");
            }
            portfoliosBySeat.put(seat, p);
        }
    }

    @Override
    public int rows() {
        return seatNos.size();
    }

    @Override
    public int cols() {
        return questions.size();
    }

    @Override
    public List<String> getRowIds() {
        return seatNos;
    }

    @Override
    public List<String> getColumnIds() {
        ArrayList<String> labels = new ArrayList<>();
        for (ReflectionQuestion q : questions) {
            labels.add(q.getLabel());
        }
        return Collections.unmodifiableList(labels);
    }

    @Override
    public String get(int row, int col) {
        if (row < 0 || row >= rows() || col < 0 || col >= cols()) {
            throw new IndexOutOfBoundsException("Invalid row or column");
        }
        String seat = seatNos.get(row);
        StudentPortfolio p = portfoliosBySeat.get(seat);
        if (p == null || p.getReflection() == null) {
            return null;
        }
        return p.getReflection().getAnswer(questions.get(col));
    }

    public int getAnsweredCount() {
        int count = 0;
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < cols(); c++) {
                String answer = get(r, c);
                if (answer != null && !answer.isBlank()) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isRowComplete(int row) {
        if (row < 0 || row >= rows()) {
            throw new IndexOutOfBoundsException("Invalid row");
        }
        for (int c = 0; c < cols(); c++) {
            String answer = get(row, c);
            if (answer == null || answer.isBlank()) {
                return false;
            }
        }
        return true;
    }

    public List<ReflectionQuestion> getUnanswerdQuestions(int row) {
        if (row < 0 || row >= rows()) {
            throw new IndexOutOfBoundsException("Invalid row");
        }
        ArrayList<ReflectionQuestion> unanswered = new ArrayList<>();
        for (int c = 0; c < cols(); c++) {
            String answer = get(row, c);
            if (answer == null || answer.isBlank()) {
                unanswered.add(questions.get(c));
            }
        }
        return Collections.unmodifiableList(unanswered);
    }
}
