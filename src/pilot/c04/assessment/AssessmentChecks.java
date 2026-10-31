package pilot.c04.assessment;

/** Runs all five checks. Prints PASS only after a check returns; exits 1 on any failure. */
public final class AssessmentChecks {
    private AssessmentChecks() { }

    public static void main(String[] args) {
        try {
            ScheduleCheck.run();
            System.out.println("PASS ScheduleCheck");
            ReflectionCheck.run();
            System.out.println("PASS ReflectionCheck");
            RecordCheck.run();
            System.out.println("PASS RecordCheck");
            LinkCheck.run();
            System.out.println("PASS LinkCheck");
            MatrixCheck.run();
            System.out.println("PASS MatrixCheck");
            System.out.println("ALL CHECKS PASSED");
        } catch (Throwable t) {
            System.out.println("FAIL: " + t);
            System.exit(1);
        }
    }
}
