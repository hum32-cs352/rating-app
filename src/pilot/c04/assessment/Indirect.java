package pilot.c04.assessment;

import domain.SectionName;

/** Assessment that gives indirect evidence (opinions, reflections). */
public abstract class Indirect extends Assessment {
    protected Indirect(String id, String courseCode, String semester,
                       SectionName sectionName, Purpose purpose, Schedule schedule) {
        super(id, courseCode, semester, sectionName, purpose, schedule);
    }
}
