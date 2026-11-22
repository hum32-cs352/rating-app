package domain;

public final class Office {
    private final Student student;
    private final Section section;

    public Office(Student student, Section section) {
        if (student == null) {
            throw new NullPointerException("Student cannot be null in Office record.");
        }
        if (section == null) {
            throw new NullPointerException("Section cannot be null in Office record.");
        }

        this.student = student;
        this.section = section;
    }

    public Student getStudent() {
        return student;
    }

    public Section getSection() {
        return section;
    }

    @Override
    public String toString() {
        return "Office{" +
                "student=" + student +
                ", section=" + section +
                '}';
    }
}