package pilot.c04.assessment;

import domain.SectionName;
import java.time.LocalDateTime;

/** Checks Schedule and the identifier rules of Assessment (T02). Prints nothing. */
public final class ScheduleCheck {
    private ScheduleCheck() { }

    public static void run() {
        LocalDateTime start = LocalDateTime.of(2026, 10, 12, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 10, 12, 11, 0);

        // valid
        Schedule ok = new Schedule(start, end);
        verify(ok.getStartsAt().equals(start) && ok.getEndsAt().equals(end), "valid times kept");

        // null, equal, reversed
        expectIae(() -> new Schedule(null, end), "null start");
        expectIae(() -> new Schedule(start, null), "null end");
        expectIae(() -> new Schedule(start, start), "equal times");
        expectIae(() -> new Schedule(end, start), "reversed times");

        // Assessment identifiers and required references (through Reflection)
        SectionName c = SectionName.C;
        Purpose p = Purpose.FORMATIVE;
        expectIae(() -> new Reflection(null, "CS-352", "T", c, p, ok), "null id");
        expectIae(() -> new Reflection("", "CS-352", "T", c, p, ok), "empty id");
        expectIae(() -> new Reflection(" A1", "CS-352", "T", c, p, ok), "padded id");
        expectIae(() -> new Reflection("A1 ", "CS-352", "T", c, p, ok), "trailing-space id");
        expectIae(() -> new Reflection("A1", " ", "T", c, p, ok), "blank course");
        expectIae(() -> new Reflection("A1", "CS-352", null, c, p, ok), "null semester");
        expectIae(() -> new Reflection("A1", "CS-352", "T", null, p, ok), "null section");
        expectIae(() -> new Reflection("A1", "CS-352", "T", c, null, ok), "null purpose");
        expectIae(() -> new Reflection("A1", "CS-352", "T", c, p, null), "null schedule");
    }

    private static void verify(boolean condition, String what) {
        if (!condition) throw new AssertionError("ScheduleCheck: " + what);
    }

    private static void expectIae(Runnable action, String what) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        } catch (Throwable other) {
            throw new AssertionError("ScheduleCheck: " + what + " threw " + other, other);
        }
        throw new AssertionError("ScheduleCheck: " + what + " was accepted");
    }
}
