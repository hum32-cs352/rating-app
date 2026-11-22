package domain;

import java.util.Objects;

public final class Faculty {
    private final String facultyId;
    private final String name;
    private final String designation;
    private final FacultyType facultyType;

    public Faculty(String facultyId, String name, String designation, FacultyType facultyType) {
        Objects.requireNonNull(facultyId, "facultyId");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(designation, "designation");
        Objects.requireNonNull(facultyType, "facultyType");
        if (facultyId.isBlank()) throw new IllegalArgumentException("facultyId is blank");
        if (name.isBlank()) throw new IllegalArgumentException("name is blank");
        if (designation.isBlank()) throw new IllegalArgumentException("designation is blank");
        this.facultyId = facultyId;
        this.name = name;
        this.designation = designation;
        this.facultyType = facultyType;
    }

    public String getFacultyId() { return facultyId; }
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public FacultyType getFacultyType() { return facultyType; }

    @Override
    public String toString() {
        return facultyId + " | " + name + " | " + designation + " | " + facultyType;
    }
}