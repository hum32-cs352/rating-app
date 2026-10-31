package pilot.c04.assessment;

import domain.SectionName;

/** Assessment that gives direct evidence of achievement. */
public abstract class Direct extends Assessment {
    protected Direct(String id, String courseCode, String semester,
                     SectionName sectionName, Purpose purpose, Schedule schedule) {
        super(id, courseCode, semester, sectionName, purpose, schedule);
    }
}
