package pilot.c04.assessment;

import domain.SectionName;

/** One assessment activity (one matrix column). */
public abstract class Assessment {
    private final String id;
    private final String courseCode;
    private final String semester;
    private final SectionName sectionName;
    private final Purpose purpose;
    private final Schedule schedule;

    protected Assessment(String id, String courseCode, String semester,
                         SectionName sectionName, Purpose purpose, Schedule schedule) {
        this.id = requireText(id, "id");
        this.courseCode = requireText(courseCode, "courseCode");
        this.semester = requireText(semester, "semester");
        if (sectionName == null) throw new IllegalArgumentException("sectionName is required");
        if (purpose == null) throw new IllegalArgumentException("purpose is required");
        if (schedule == null) throw new IllegalArgumentException("schedule is required");
        this.sectionName = sectionName;
        this.purpose = purpose;
        this.schedule = schedule;
    }

    /** Rejects null, blank and padded text. */
    static String requireText(String value, String field) {
        if (value == null || value.isBlank() || !value.equals(value.strip())) {
            throw new IllegalArgumentException(field + " is null, blank or padded");
        }
        return value;
    }

    public String getId() { return id; }
    public abstract String getName();
    public String getCourseCode() { return courseCode; }
    public String getSemester() { return semester; }
    public SectionName getSectionName() { return sectionName; }
    public Purpose getPurpose() { return purpose; }
    public Schedule getSchedule() { return schedule; }
}
