package domain;

import java.util.Objects;

public final class Student implements Cloneable {
    private final String seatNo;
    private final String name;
    private final SectionName sectionName;
    private static int createdCount = 0;

    public Student(String seatNo, String name, SectionName sectionName) {
        Objects.requireNonNull(seatNo, "seatNo");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(sectionName, "sectionName");
        if (seatNo.isBlank()) throw new IllegalArgumentException("seatNo is blank");
        if (name.isBlank()) throw new IllegalArgumentException("name is blank");
        this.seatNo = seatNo;
        this.name = name;
        this.sectionName = sectionName;
        createdCount++;
    }

    public Student(Student other) {
        this(Objects.requireNonNull(other, "other").seatNo, other.name, other.sectionName);
    }

    public String getSeatNo() { return seatNo; }
    public String getName() { return name; }
    public SectionName getSectionName() { return sectionName; }

    @Override
    public Student clone() {
        try {
            Student copy = (Student) super.clone();
            createdCount++;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Student shallowCopy() { return clone(); }

    public Student deepCopy() { return new Student(this); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return seatNo.equals(((Student) o).seatNo);
    }

    @Override
    public int hashCode() { return seatNo.hashCode(); }

    @Override
    public String toString() { return seatNo + " | " + name + " | " + sectionName; }

    public static int getCreatedCount() { return createdCount; }
}