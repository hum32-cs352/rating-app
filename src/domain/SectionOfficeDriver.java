package domain;

import java.util.ArrayList;

/**
 * A02 PR#2 - Muhammad Arqam
 * Driver that demonstrates Section/Faculty assignment and Office record behaviour.
 *
 * Creates Sections A and B sharing one Faculty, Section C with no Faculty,
 * and an Office referencing a Student and Section C. Assigns, replaces and
 * restores C's faculty, checking that the Office sees each change and that
 * A and B remain unchanged. Throws AssertionError on any failed check.
 */
public class SectionOfficeDriver {

    // ---- tiny assert helpers ---------------------------------------------

    private static void check(boolean condition, String label) {
        if (!condition) {
            throw new AssertionError("FAIL: " + label);
        }
        System.out.println("[PASS] " + label);
    }

    // ---- main ------------------------------------------------------------

    public static void main(String[] args) {

        System.out.println("===> Hum32@CS352 | A02 SectionOfficeDriver");

        // 1. Shared Faculty (Humera Tariq) and temporary visiting Faculty
        Faculty humera = new Faculty(
                "HUMERA_TARIQ",
                "Humera Tariq",
                "Assistant Professor",
                FacultyType.PERMANENT);

        Faculty visitor = new Faculty(
                "DEMO_VISITING",
                "Visiting Demo",
                "Visiting Lecturer",
                FacultyType.VISITING);

        // 2. Sections A, B, C for CS-352, 2026, semester 2
        Section sectionA = new Section("CS-352", 2026, 2, SectionName.A);
        Section sectionB = new Section("CS-352", 2026, 2, SectionName.B);
        Section sectionC = new Section("CS-352", 2026, 2, SectionName.C);

        // 3. Test setup: A and B share one Faculty; C stays unassigned
        sectionA.assignFaculty(humera);
        sectionB.assignFaculty(humera);

        check(sectionA.getFaculty() == humera,
                "Section A references shared Faculty");
        check(sectionB.getFaculty() == humera,
                "Section B references shared Faculty");
        check(sectionA.getFaculty() == sectionB.getFaculty(),
                "Sections A and B share the same Faculty object");
        check(!sectionC.hasFaculty(),
                "Section C begins unassigned (test state)");

        // 4. Create a Student and Office referencing C
        Student student = new Student(
                "B26000000001",
                "Ali Demo",
                SectionName.A);   // student's roster label; may differ from Section label

        Office office = new Office(student, sectionC);

        check(office.getStudent() == student,
                "Office keeps Student reference");
        check(office.getSection() == sectionC,
                "Office keeps Section reference");
        check(office.getSection().getFaculty() == null,
                "Office sees C unassigned at start");

        // 5. Assign C's faculty -> Office should now see Humera
        sectionC.assignFaculty(humera);
        check(office.getSection().getFaculty() == humera,
                "Assigning C is visible through its Office record");
        check(sectionA.getFaculty() == humera,
                "Assigning C did not change A");
        check(sectionB.getFaculty() == humera,
                "Assigning C did not change B");

        // 6. Replace C's faculty with the visitor
        sectionC.assignFaculty(visitor);
        check(office.getSection().getFaculty() == visitor,
                "Replacing C is visible through its Office record");
        check(sectionA.getFaculty() == humera,
                "Replacing C does not change A");
        check(sectionB.getFaculty() == humera,
                "Replacing C does not change B");

        // 7. Restore C's faculty
        sectionC.assignFaculty(humera);
        check(office.getSection().getFaculty() == humera,
                "Restoring C is visible through its Office record");
        check(sectionA.getFaculty() == humera,
                "Restoring C does not change A");
        check(sectionB.getFaculty() == humera,
                "Restoring C does not change B");

        // 8. Guard: replacing C with null must throw and leave state intact
        boolean nullRejected = false;
        try {
            sectionC.assignFaculty(null);
        } catch (NullPointerException expected) {
            nullRejected = true;
        }
        check(nullRejected,
                "assignFaculty(null) rejected");
        check(office.getSection().getFaculty() == humera,
                "C still points to Humera after rejected null assignment");

        // 9. ArrayList<Office> held in the driver only
        ArrayList<Office> records = new ArrayList<>();
        records.add(office);
        check(records.size() == 1,
                "ArrayList<Office> is held in the driver only");
        check(records.get(0).getSection() == sectionC,
                "Driver record references the same Section C");

        // 10. Print the objects via their toString (classes do no I/O)
        System.out.println("--- Objects ---");
        System.out.println("Student : " + student);
        System.out.println("Faculty : " + humera);
        System.out.println("Visitor : " + visitor);
        System.out.println("SectionA: " + sectionA);
        System.out.println("SectionB: " + sectionB);
        System.out.println("SectionC: " + sectionC);
        System.out.println("Office  : " + office);

        System.out.println("A02 SectionOffice READY | shared-reference checks passed");
    }
}