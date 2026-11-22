package domain;

import java.util.Objects;

public final class Section {
    private final String courseCode;
    private final int academicYear;
    private final int semester;
    private final SectionName sectionName;
    private Faculty faculty;

    public Section(String courseCode, int academicYear, int semester, SectionName sectionName) {
        Objects.requireNonNull(courseCode, "courseCode");
        Objects.requireNonNull(sectionName, "sectionName");
        if (courseCode.isBlank()) throw new IllegalArgumentException("courseCode is blank");
        if (academicYear <= 0) throw new IllegalArgumentException("academicYear must be positive");
        if (semester <= 0) throw new IllegalArgumentException("semester must be positive");
        this.courseCode = courseCode;
        this.academicYear = academicYear;
        this.semester = semester;
        this.sectionName = sectionName;
        this.faculty = null;
    }

    public String getCourseCode() { return courseCode; }
    public int getAcademicYear() { return academicYear; }
    public int getSemester() { return semester; }
    public SectionName getSectionName() { return sectionName; }
    public Faculty getFaculty() { return faculty; }
    public boolean hasFaculty() { return faculty != null; }

    public void assignFaculty(Faculty faculty) {
        this.faculty = Objects.requireNonNull(faculty, "faculty");
    }

    @Override
    public String toString() {
        return courseCode + " | " + academicYear + " | Semester " + semester
                + " | Section " + sectionName
                + " | " + (faculty == null ? "no faculty" : faculty.getName());
    }
}