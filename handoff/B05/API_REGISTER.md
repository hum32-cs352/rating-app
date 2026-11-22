API Register - Group B05 Reflection Module

Package: domain.reflection

1. Class SelfReflection

Public constructors:

public SelfReflection(Map<ReflectionQuestion, String> answers, String remarks): Initializes a reflection record after validating that neither the map nor the remarks are null, and that all answers comply with their question's choices.

Public methods:

public String getRemarks(): Returns the remarks string.
public String getAnswer(ReflectionQuestion question): Returns the answer string for a given reflection question.

2. Enum ReflectionQuestion

Constants:

STUDY_PATTERN, CONSULTATIONS, KEY_ISSUE, READING_HABIT, CODING_STRENGTH, EXPECTED_MIDTERM_GRADE

Public methods:

public String getLabel(): Returns the display label for the question.
public List<String> getChoices(): Returns the list of valid choices for the question.
public boolean isAllowed(String answer): Checks whether an answer matches one of the allowed choices.

3. Class ReflectionMatrixView (implements Matrix<String>)

Public constructors:

public ReflectionMatrixView(String courseCode, String termId, List<String> seatNos, List<StudentPortfolio> portfolios): Initializes the matrix view, validating course code, term ID, roster seat numbers and matching student portfolios.

Public methods:

public int rows(): Returns the number of rows (seats).
public int cols(): Returns the number of columns (questions).
public List<String> getRowIds(): Returns an unmodifiable list of row identifiers (seat numbers).
public List<String> getColumnIds(): Returns an unmodifiable list of column identifiers (question labels).
public String get(int row, int col): Returns the answer at the given row and column.
public int getAnsweredCount(): Returns the total number of answered questions across the matrix.
public boolean isRowComplete(int row): Checks whether all questions for a row are answered.
public List<ReflectionQuestion> getUnanswerdQuestions(int row): Returns an unmodifiable list of unanswered questions for a row.

4. Class StudentPortfolio

Public constructors:

public StudentPortfolio(String seatNo, String courseCode, String termId, String sourceRef, SelfReflection reflection): Initializes a portfolio entry after rejecting null or blank inputs.

Public methods:

public String getSeatNo(): Returns the seat number.
public String getCourseCode(): Returns the course code.
public String getTermId(): Returns the term identifier.
public String getSourceRef(): Returns the source reference string.
public SelfReflection getReflection(): Returns the associated SelfReflection object.

5. Class ReflectionDriver

Public constructors:

None.

Public methods:

public static void main(String[] args): Entry point for the interactive demo and local checks.

6. Interface Matrix<T>

Public methods (public by default in an interface):

int rows(): Returns the row count.
int cols(): Returns the column count.
List<String> getRowIds(): Returns row identifiers.
List<String> getColumnIds(): Returns column identifiers.
T get(int row, int col): Returns the element at the given coordinates.