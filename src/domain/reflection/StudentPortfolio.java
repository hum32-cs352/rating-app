package domain.reflection;
public final class StudentPortfolio{
    private final String seatNo;
    private final String courseCode;
    private final String termId;
    private final String sourceRef;
    private final SelfReflection reflection;

    public StudentPortfolio(String seatNo, String courseCode, String termId, String sourceRef, SelfReflection reflection){
        if (seatNo == null || seatNo.isBlank()|| !seatNo.equals(seatNo.trim())){ //checks and reject null reflection, blank or whitespace-padded identifiers
            throw new IllegalArgumentException("Invalid seat no.");
        }
        if (courseCode == null || courseCode.isBlank() || !courseCode.equals(courseCode.trim())){
            throw new IllegalArgumentException("Invalid courseCode.");
        }
        if (termId == null || termId.isBlank() || !termId.equals(termId.trim())){
            throw new IllegalArgumentException("Invalid termId.");
        }
        if (sourceRef == null || sourceRef.isBlank()|| !sourceRef.equals(sourceRef.trim())){
            throw new IllegalArgumentException("Invalid sourceRef.");
        }
        if (reflection == null) {
            throw new IllegalArgumentException("Reflection cannot be null");
        }
        this.seatNo = seatNo;
        this.courseCode = courseCode;
        this.termId = termId;
        this.sourceRef = sourceRef;
        this.reflection = reflection;

    }
    public String getSeatNo(){
        return seatNo;
    }
    public String getCourseCode(){
        return courseCode;
    }
    public String getTermId(){
        return termId;
    }
    public String getSourceRef(){
        return sourceRef;
    }
    public SelfReflection getReflection(){
        return reflection;
    }
}