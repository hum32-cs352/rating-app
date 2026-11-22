package pilot.c04.assessment;

import domain.SectionName;
import java.time.LocalDateTime;

/** Checks Reflection through Assessment references. Prints nothing. */
public final class ReflectionCheck {
    private ReflectionCheck() { }

    public static void run() {
        Schedule schedule = new Schedule(LocalDateTime.of(2026, 10, 12, 9, 0),
                LocalDateTime.of(2026, 10, 12, 10, 0));
        Assessment a = new Reflection("A1", "CS-352", "2026-Semester-II",
                SectionName.C, Purpose.FORMATIVE, schedule);

        verify("Reflection".equals(a.getName()), "name");
        verify("A1".equals(a.getId()), "id");
        verify("CS-352".equals(a.getCourseCode()), "course");
        verify("2026-Semester-II".equals(a.getSemester()), "semester");
        verify(a.getSectionName() == SectionName.C, "section");
        verify(a.getSchedule() == schedule, "schedule is the same object");
        verify(a.getPurpose() == Purpose.FORMATIVE, "purpose FORMATIVE");
        verify(a instanceof Indirect && !(a instanceof Direct), "Reflection is Indirect, not Direct");

        for (Purpose p : Purpose.values()) {
            Assessment each = new Reflection("A1", "CS-352", "2026-Semester-II",
                    SectionName.C, p, schedule);
            verify(each.getPurpose() == p, "purpose retained: " + p);
        }

        try {
            new Reflection("A1", "CS-352", "2026-Semester-II", SectionName.C, null, schedule);
        } catch (IllegalArgumentException expected) {
            return;
        } catch (Throwable other) {
            throw new AssertionError("ReflectionCheck: null purpose threw " + other, other);
        }
        throw new AssertionError("ReflectionCheck: null purpose was accepted");
    }

    private static void verify(boolean condition, String what) {
        if (!condition) throw new AssertionError("ReflectionCheck: " + what);
    }
}
