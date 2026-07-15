package java_oop_project;

public class CollegeException extends Exception {
    private static final String PRE_MESSAGE = "Error: ";
    public CollegeException(String message) {
        super(PRE_MESSAGE + message);
    }
}
