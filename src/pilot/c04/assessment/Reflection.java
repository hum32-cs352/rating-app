package pilot.c04.assessment;

import domain.SectionName;

public final class Reflection extends Indirect {
    public Reflection(String id, String courseCode, String semester,
                      SectionName sectionName, Purpose purpose, Schedule schedule) {
        super(id, courseCode, semester, sectionName, purpose, schedule);
    }

    @Override
    public String getName() { return "Reflection"; }
}
