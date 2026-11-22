package pilot.c04.assessment;

import domain.Student;

/** One student's evidence for one assessment (one matrix cell). */
public abstract class AssessmentRecord {
    private final Student student;
    private final Assessment assessment;

    protected AssessmentRecord(Student student, Assessment assessment) {
        if (student == null) throw new IllegalArgumentException("student is required");
        if (assessment == null) throw new IllegalArgumentException("assessment is required");
        if (student.getSectionName() != assessment.getSectionName()) {
            throw new IllegalArgumentException("student and assessment sections differ");
        }
        this.student = student;
        this.assessment = assessment;
    }

    public Student getStudent() { return student; }
    public Assessment getAssessment() { return assessment; }
    public abstract String getEvidenceReference();
}
