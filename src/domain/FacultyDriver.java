
package domain;


public final class FacultyDriver {

    private FacultyDriver() {
    }

    public static void main(String[] args) {

        System.out.println("=== A02 FacultyDriver ===");

        // Test both faculty types
        for (FacultyType type : FacultyType.values()) {
            switch (type) {
                case PERMANENT:
                    System.out.println("Permanent faculty: " + type);
                    break;

                case VISITING:
                    System.out.println("Visiting faculty: " + type);
                    break;
            }
        }

        // Test all three section names
        for (SectionName section : SectionName.values()) {
            switch (section) {
                case A:
                    System.out.println("Section A selected");
                    break;

                case B:
                    System.out.println("Section B selected");
                    break;

                case C:
                    System.out.println("Section C selected");
                    break;
            }
        }

        // Test an invalid faculty type
        try {
            FacultyType.valueOf("INVALID");
            System.out.println("Unexpected: INVALID was accepted");
        } catch (IllegalArgumentException e) {
            System.out.println(
                "Invalid faculty type: INVALID - not a valid FacultyType"
            );
        }

        System.out.println("FacultyDriver tests completed.");
    }
}
