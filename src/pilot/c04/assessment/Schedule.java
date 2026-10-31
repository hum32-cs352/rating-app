package pilot.c04.assessment;

import java.time.LocalDateTime;

/** Start and end of an assessment, in Karachi local time. */
public final class Schedule {
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;

    public Schedule(LocalDateTime startsAt, LocalDateTime endsAt) {
        if (startsAt == null || endsAt == null) {
            throw new IllegalArgumentException("startsAt and endsAt are required");
        }
        if (!endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException("endsAt must be strictly after startsAt");
        }
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
}
