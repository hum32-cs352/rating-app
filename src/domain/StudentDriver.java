package domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public final class StudentDriver {

    private static int rejected = 0;

    private StudentDriver() { }

    public static void main(String[] args) {
        // Read input: three lines, each with nextLine()
        Scanner input = new Scanner(System.in);
        String seat = input.nextLine();
        String name = input.nextLine();
        String label = input.nextLine();
        SectionName sectionName = SectionName.valueOf(label.trim().toUpperCase(Locale.ROOT));

        System.out.println("=== Hum32@CS352 | A02 StudentDriver ===");

        // ---- Identity: six valid Students ----
        int countBefore = Student.getCreatedCount();
        Student original = new Student(seat, name, sectionName);
        Student sameSeat = new Student(seat, "Different Name", SectionName.B);
        Student viaCopyCtor = new Student(original);
        Student viaClone = original.clone();
        Student viaShallow = original.shallowCopy();
        Student viaDeep = original.deepCopy();
        int countAfterValid = Student.getCreatedCount();

        System.out.println("Student: " + original);

        check(original.getSeatNo().equals(seat)
                && original.getName().equals(name)
                && original.getSectionName() == sectionName,
                "Input preserved exactly");

        Object nothing = null;
        check(original.equals(sameSeat) && sameSeat.equals(original)
                && original.hashCode() == sameSeat.hashCode()
                && !original.equals(nothing),
                "Same seat: equals and hashCode agree");

        Set<Student> set = new HashSet<>();
        set.add(original);
        set.add(sameSeat);
        check(set.size() == 1, "HashSet size after duplicate: 1");

        Student[] copies = { viaCopyCtor, viaClone, viaShallow, viaDeep };
        boolean copiesOk = true;
        for (Student c : copies) {
            copiesOk = copiesOk
                    && c != original
                    && c.equals(original)
                    && c.hashCode() == original.hashCode()
                    && c.getSeatNo().equals(original.getSeatNo())
                    && c.getName().equals(original.getName())
                    && c.getSectionName() == original.getSectionName();
        }
        copiesOk = copiesOk
                && viaCopyCtor != viaClone && viaClone != viaShallow
                && viaShallow != viaDeep && viaCopyCtor != viaDeep;
        check(copiesOk, "Copy/clone objects are distinct and equal");

        // ---- Invalid Student inputs ----
        int rejectedBefore = rejected;
        expectThrows(NullPointerException.class,
                () -> new Student(null, "Name", SectionName.A), "null seat");
        expectThrows(NullPointerException.class,
                () -> new Student("S1", null, SectionName.A), "null name");
        expectThrows(NullPointerException.class,
                () -> new Student("S1", "Name", null), "null section");
        expectThrows(NullPointerException.class,
                () -> new Student((Student) null), "null copy source");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("", "Name", SectionName.A), "empty seat");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("   ", "Name", SectionName.A), "blank seat");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "", SectionName.A), "empty name");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "   ", SectionName.A), "blank name");
        check(rejected - rejectedBefore == 8, "Invalid Student inputs rejected");

        int countAfterInvalid = Student.getCreatedCount();
        check((countAfterValid - countBefore) == 6 && (countAfterInvalid - countAfterValid) == 0,
                "Student counter: +6; failed attempts: +0");

        // ---- Enrolment number (version 2) ----
        // Fictional enrolment numbers only.
        Student noEnrol = new Student("B26000000002", "No Enrol", SectionName.B);
        Student nullEnrol = new Student("B26000000003", "Null Enrol", SectionName.A, null);
        Student withEnrol = new Student("B26000000004", "With Enrol", SectionName.C, "0012345");
        check(noEnrol.getEnrolmentNo() == null
                && nullEnrol.getEnrolmentNo() == null
                && withEnrol.getEnrolmentNo() != null,
                "Old constructor and null enrolment store null; new constructor stores a value");

        String leadingZeros = "0012345";
        String innerSpaces = "26 CS 0042";
        Student exact1 = new Student("B26000000005", "Zero Lead", SectionName.A, leadingZeros);
        Student exact2 = new Student("B26000000006", "Inner Space", SectionName.B, innerSpaces);
        check(exact1.getEnrolmentNo().equals("0012345")
                && exact1.getEnrolmentNo().length() == 7
                && exact2.getEnrolmentNo().equals("26 CS 0042"),
                "Enrolment text preserved exactly (leading zeros kept)");

        int countBeforeBadEnrol = Student.getCreatedCount();
        int rejectedBeforeEnrol = rejected;
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "Name", SectionName.A, ""), "empty enrolment");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "Name", SectionName.A, "   "), "blank enrolment");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "Name", SectionName.A, " 0012345"), "leading space");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "Name", SectionName.A, "0012345 "), "trailing space");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "Name", SectionName.A, "\t0012345"), "leading tab");
        expectThrows(IllegalArgumentException.class,
                () -> new Student("S1", "Name", SectionName.A, "0012345\n"), "trailing newline");
        check(rejected - rejectedBeforeEnrol == 6
                && Student.getCreatedCount() == countBeforeBadEnrol,
                "Missing enrolment accepted; blank/whitespace rejected; failed attempts: +0");

        Student[] enrolCopies = {
                new Student(withEnrol), withEnrol.clone(),
                withEnrol.shallowCopy(), withEnrol.deepCopy() };
        Student[] nullCopies = {
                new Student(noEnrol), noEnrol.clone(),
                noEnrol.shallowCopy(), noEnrol.deepCopy() };
        boolean allKept = true;
        for (Student c : enrolCopies) {
            allKept = allKept && c != withEnrol && "0012345".equals(c.getEnrolmentNo());
        }
        for (Student c : nullCopies) {
            allKept = allKept && c != noEnrol && c.getEnrolmentNo() == null;
        }
        check(allKept, "Enrolment preserved by all four copy routes");

        Student seatA1 = new Student("B26000000009", "First Name", SectionName.A, "0000001");
        Student seatA2 = new Student("B26000000009", "Second Name", SectionName.B, "0000002");
        Student seatB = new Student("B26000000010", "First Name", SectionName.A, "0000001");
        check(seatA1.equals(seatA2) && seatA2.equals(seatA1)
                && seatA1.hashCode() == seatA2.hashCode()
                && !seatA1.equals(seatB) && !seatB.equals(seatA1),
                "Equality uses seat only: same seat equal; different seats unequal");

        // ---- Array copies ----
        int[] ints = { 1, 2, 3 };
        int[] intsCopy = ints.clone();
        intsCopy[0] = 99;
        Student[] students = { original, sameSeat };
        Student[] studentsClone = students.clone();
        Student[] studentsCopyOf = Arrays.copyOf(students, students.length);
        check(ints != intsCopy && ints[0] == 1 && intsCopy[0] == 99
                && students != studentsClone && students != studentsCopyOf
                && studentsClone != studentsCopyOf
                && studentsClone[0] == students[0] && studentsClone[1] == students[1]
                && studentsCopyOf[0] == students[0] && studentsCopyOf[1] == students[1],
                "Array copies: new arrays; shared Student references");

        // ---- Teaching relationships ----
        Faculty humera = new Faculty("HUMERA_TARIQ", "Humera Tariq",
                "Assistant Professor", FacultyType.PERMANENT);
        Faculty visitor = new Faculty("DEMO_VISITING", "Visiting Demo",
                "Visiting Lecturer", FacultyType.VISITING);

        Section sectionA = new Section("CS-352", 2026, 2, SectionName.A);
        Section sectionB = new Section("CS-352", 2026, 2, SectionName.B);
        Section sectionC = new Section("CS-352", 2026, 2, SectionName.C);

        sectionA.assignFaculty(humera);
        sectionB.assignFaculty(humera);
        check(sectionA.hasFaculty() && sectionB.hasFaculty()
                && sectionA.getFaculty() == humera
                && sectionB.getFaculty() == humera
                && sectionA.getFaculty() == sectionB.getFaculty(),
                "Sections A and B share one Faculty");

        check(!sectionC.hasFaculty() && sectionC.getFaculty() == null,
                "Section C begins unassigned (test state)");

        Office office = new Office(original, sectionC);
        ArrayList<Office> offices = new ArrayList<>();
        offices.add(office);
        check(offices.size() == 1
                && offices.get(0) == office
                && office.getStudent() == original
                && office.getSection() == sectionC,
                "Office keeps Student/Section references");

        sectionC.assignFaculty(humera);
        check(office.getSection().hasFaculty()
                && office.getSection().getFaculty() == humera,
                "Assigning C is visible through its Office record");

        sectionC.assignFaculty(visitor);
        boolean visitorVisible = office.getSection().getFaculty() == visitor
                && visitor.getFacultyType() == FacultyType.VISITING;
        boolean abUnchangedDuring = sectionA.getFaculty() == humera
                && sectionB.getFaculty() == humera;
        sectionC.assignFaculty(humera);
        boolean restored = office.getSection().getFaculty() == humera;
        boolean abUnchangedAfter = sectionA.getFaculty() == humera
                && sectionB.getFaculty() == humera;
        check(visitorVisible && abUnchangedDuring && restored && abUnchangedAfter,
                "Replacing C does not change A or B");

        boolean noCollections = true;
        for (Class<?> type : new Class<?>[] { Student.class, Faculty.class,
                Section.class, Office.class }) {
            for (Field field : type.getDeclaredFields()) {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    noCollections = false;
                }
            }
        }
        check(noCollections && offices.get(0) == office,
                "ArrayList<Office> is held in the driver only");

        // ---- Invalid Faculty, Section and Office inputs ----
        rejectedBefore = rejected;
        expectThrows(NullPointerException.class,
                () -> new Faculty(null, "N", "D", FacultyType.PERMANENT), "null facultyId");
        expectThrows(NullPointerException.class,
                () -> new Faculty("F", null, "D", FacultyType.PERMANENT), "null faculty name");
        expectThrows(NullPointerException.class,
                () -> new Faculty("F", "N", null, FacultyType.PERMANENT), "null designation");
        expectThrows(NullPointerException.class,
                () -> new Faculty("F", "N", "D", null), "null facultyType");
        expectThrows(IllegalArgumentException.class,
                () -> new Faculty(" ", "N", "D", FacultyType.PERMANENT), "blank facultyId");
        expectThrows(IllegalArgumentException.class,
                () -> new Faculty("F", " ", "D", FacultyType.PERMANENT), "blank faculty name");
        expectThrows(IllegalArgumentException.class,
                () -> new Faculty("F", "N", " ", FacultyType.PERMANENT), "blank designation");

        expectThrows(NullPointerException.class,
                () -> new Section(null, 2026, 2, SectionName.A), "null courseCode");
        expectThrows(NullPointerException.class,
                () -> new Section("CS-352", 2026, 2, null), "null section label");
        expectThrows(IllegalArgumentException.class,
                () -> new Section(" ", 2026, 2, SectionName.A), "blank courseCode");
        expectThrows(IllegalArgumentException.class,
                () -> new Section("CS-352", 0, 2, SectionName.A), "zero year");
        expectThrows(IllegalArgumentException.class,
                () -> new Section("CS-352", -1, 2, SectionName.A), "negative year");
        expectThrows(IllegalArgumentException.class,
                () -> new Section("CS-352", 2026, 0, SectionName.A), "zero semester");
        expectThrows(IllegalArgumentException.class,
                () -> new Section("CS-352", 2026, -1, SectionName.A), "negative semester");
        expectThrows(NullPointerException.class,
                () -> sectionA.assignFaculty(null), "assignFaculty(null)");

        expectThrows(NullPointerException.class,
                () -> new Office(null, sectionC), "null Office student");
        expectThrows(NullPointerException.class,
                () -> new Office(original, null), "null Office section");

        check(rejected - rejectedBefore == 17 && sectionA.getFaculty() == humera,
                "Invalid Faculty, Section and Office inputs rejected");

        System.out.println("A02 READY | Identity + teaching relationships verified");
    }

    /** Throws AssertionError when the condition is false; prints a PASS line otherwise. */
    private static void check(boolean condition, String label) {
        if (!condition) {
            throw new AssertionError("[FAIL] " + label);
        }
        System.out.println("[PASS] " + label);
    }

    /** Passes only if the action throws the expected exception type; anything else fails. */
    private static void expectThrows(Class<? extends Throwable> expected,
                                     Runnable action, String what) {
        try {
            action.run();
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                rejected++;
                return;
            }
            throw new AssertionError("[FAIL] " + what + ": expected "
                    + expected.getSimpleName() + " but got " + t, t);
        }
        throw new AssertionError("[FAIL] " + what + ": expected "
                + expected.getSimpleName() + " but nothing was thrown");
    }
}