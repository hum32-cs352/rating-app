package domain;

import java.util.Objects;

public final class Student implements Cloneable {
    private final String seatNo;
    private final String name;
    private final SectionName sectionName;
    private final String enrolmentNo; // null means "not recorded"
    private static int createdCount = 0;

    /** Original constructor: the enrolment number is not recorded (null). */
    public Student(String seatNo, String name, SectionName sectionName) {
        this(seatNo, name, sectionName, null);
    }

    /** Enrolment number may be null; if not null it must be non-blank with no outer whitespace. */
    public Student(String seatNo, String name, SectionName sectionName, String enrolmentNo) {
        Objects.requireNonNull(seatNo, "seatNo");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(sectionName, "sectionName");
        if (seatNo.isBlank()) throw new IllegalArgumentException("seatNo is blank");
        if (name.isBlank()) throw new IllegalArgumentException("name is blank");
        if (enrolmentNo != null) {
            if (enrolmentNo.isBlank())
                throw new IllegalArgumentException("enrolmentNo is blank");
            if (!enrolmentNo.equals(enrolmentNo.strip()))
                throw new IllegalArgumentException("enrolmentNo has leading or trailing whitespace");
        }
        this.seatNo = seatNo;
        this.name = name;
        this.sectionName = sectionName;
        this.enrolmentNo = enrolmentNo;
        createdCount++;
    }

    public Student(Student other) {
        this(Objects.requireNonNull(other, "other").seatNo, other.name,
                other.sectionName, other.enrolmentNo);
    }

    public String getSeatNo() { return seatNo; }
    public String getName() { return name; }
    public SectionName getSectionName() { return sectionName; }
    public String getEnrolmentNo() { return enrolmentNo; }

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