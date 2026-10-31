package pilot.c04.assessment;

import domain.Student;
import domain.reflection.StudentPortfolio;
import java.time.LocalDateTime;

public final class ReflectionRecord extends AssessmentRecord {
    private final StudentPortfolio portfolio;
    private final LocalDateTime submittedAt; // null = not recorded

    public ReflectionRecord(Student student, Reflection assessment,
                            StudentPortfolio portfolio, LocalDateTime submittedAt) {
        super(student, assessment);
        if (portfolio == null) throw new IllegalArgumentException("portfolio is required");
        if (!student.getSeatNo().equals(portfolio.getSeatNo())) {
            throw new IllegalArgumentException("seat number differs from portfolio");
        }
        if (!assessment.getCourseCode().equals(portfolio.getCourseCode())) {
            throw new IllegalArgumentException("course code differs from portfolio");
        }
        if (!assessment.getSemester().equals(portfolio.getTermId())) {
            throw new IllegalArgumentException("semester differs from portfolio term");
        }
        this.portfolio = portfolio;
        this.submittedAt = submittedAt;
    }

    public StudentPortfolio getPortfolio() { return portfolio; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    @Override
    public String getEvidenceReference() { return portfolio.getSourceRef(); }
}
