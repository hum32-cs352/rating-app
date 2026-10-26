package domain.reflection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public final class ReflectionDriver {

    private static final String COURSE = "CS-352";
    private static final String TERM = "2026-Semester-II";
    private static final String SOURCE_REF =
            "CS352_rubric_portfolio.pdf, p.1; practice choices";
    private static final String NOT_ANSWERED = "Not answered";

    private ReflectionDriver() {
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        // ---- seat number ----
        String seatNo;
        if (args.length == 0) {
            System.out.println("Enter seat number:");
            seatNo = keyboard.nextLine();
        } else if (args.length == 1) {
            seatNo = args[0];
        } else {
            throw new IllegalArgumentException("Provide only one seat number");
        }

        if (seatNo == null || seatNo.isBlank() || !seatNo.equals(seatNo.trim())) {
            throw new IllegalArgumentException(
                    "Invalid seat number (blank or padded with spaces)");
        }

        // ---- ask each question ----
        Map<ReflectionQuestion, String> answers = new HashMap<>();
        for (ReflectionQuestion question : ReflectionQuestion.values()) {
            String chosen = askQuestion(keyboard, question);
            if (chosen != null) {
                answers.put(question, chosen);
            }
        }

        System.out.println("Remarks (press Enter to leave empty):");
        String remarks = keyboard.nextLine();

        // ---- build objects ----
        SelfReflection reflection = new SelfReflection(answers, remarks);
        StudentPortfolio portfolio =
                new StudentPortfolio(seatNo, COURSE, TERM, SOURCE_REF, reflection);

        List<String> roster = List.of(seatNo);
        List<StudentPortfolio> portfolios = List.of(portfolio);

        Matrix<String> matrix = new ReflectionMatrixView(COURSE, TERM, roster, portfolios);

        // ---- print results ----
        System.out.println();
        System.out.println("=== B05 Local Reflection Matrix ===");
        System.out.println("Practice answers - not an official student submission");
        System.out.println("Seat: " + matrix.getRowIds().get(0)
                + " | Course: " + COURSE + " | Term: " + TERM);
        System.out.println("Rows: " + matrix.rows() + " | Columns: " + matrix.cols());

        List<String> headings = matrix.getColumnIds();
        for (int col = 0; col < matrix.cols(); col++) {
            String answer = matrix.get(0, col);
            if (answer == null) {
                answer = NOT_ANSWERED;
            }
            System.out.println(headings.get(col) + ": " + answer);
        }

        if (runChecks(matrix, seatNo, answers)) {
            System.out.println("[PASS] All required local checks");
            System.out.println("B05 LOCAL READY | Reflection matrix verified");
        }
    }

    /**
     * Shows a question with numbered choices and returns the chosen answer,
     * or null if the user presses Enter to skip. Asks again on invalid input.
     */
    private static String askQuestion(Scanner keyboard, ReflectionQuestion question) {
        List<String> choices = question.getChoices();

        while (true) {
            System.out.println();
            System.out.println(question.getLabel() + ":");
            for (int i = 0; i < choices.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + choices.get(i));
            }
            System.out.println("Enter a number (or press Enter to skip):");

            String line = keyboard.nextLine().trim();
            if (line.isEmpty()) {
                return null;
            }

            try {
                int number = Integer.parseInt(line);
                if (number >= 1 && number <= choices.size()) {
                    return choices.get(number - 1);
                }
            } catch (NumberFormatException e) {
                // fall through to the error message below
            }
            System.out.println("Invalid choice. Enter a number from 1 to "
                    + choices.size() + ".");
        }
    }

    /** Runs the local checks. Prints a [FAIL] line for each one that fails. */
    private static boolean runChecks(Matrix<String> matrix, String seatNo,
                                     Map<ReflectionQuestion, String> answers) {
        boolean ok = true;
        ReflectionQuestion[] questions = ReflectionQuestion.values();

        // Exact roster
        if (matrix.rows() != 1 || !matrix.getRowIds().equals(List.of(seatNo))) {
            System.out.println("[FAIL] Roster is not exactly the entered seat");
            ok = false;
        }

        // Six columns in enum order
        boolean columnsOk = matrix.cols() == questions.length
                && matrix.getColumnIds().size() == questions.length;
        for (int c = 0; columnsOk && c < questions.length; c++) {
            if (!questions[c].getLabel().equals(matrix.getColumnIds().get(c))) {
                columnsOk = false;
            }
        }
        if (!columnsOk) {
            System.out.println("[FAIL] Columns are not the six questions in enum order");
            ok = false;
        }

        // Each cell: chosen answer read back, absent answer -> null
        for (int c = 0; c < questions.length; c++) {
            String expected = answers.get(questions[c]);
            if (!Objects.equals(expected, matrix.get(0, c))) {
                System.out.println("[FAIL] Wrong value for " + questions[c].getLabel());
                ok = false;
            }
        }

        // Labels and row IDs must be unmodifiable
        if (!isUnmodifiable(matrix.getRowIds()) || !isUnmodifiable(matrix.getColumnIds())) {
            System.out.println("[FAIL] Row IDs or column IDs can be modified");
            ok = false;
        }

        // Invalid indexes must be rejected
        if (!throwsOutOfBounds(matrix, -1, 0) || !throwsOutOfBounds(matrix, 1, 0)
                || !throwsOutOfBounds(matrix, 0, -1)
                || !throwsOutOfBounds(matrix, 0, questions.length)) {
            System.out.println("[FAIL] An invalid index did not throw IndexOutOfBoundsException");
            ok = false;
        }

        return ok;
    }

    private static boolean isUnmodifiable(List<String> list) {
        try {
            list.add("x");
            return false;
        } catch (UnsupportedOperationException e) {
            return true;
        }
    }

    private static boolean throwsOutOfBounds(Matrix<String> matrix, int row, int col) {
        try {
            matrix.get(row, col);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }
}