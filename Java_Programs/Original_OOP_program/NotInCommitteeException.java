package java_oop_project;

public class NotInCommitteeException extends CollegeException {
    private static final String message = "Lecturer is not in the committee";
    public NotInCommitteeException() {super(message);
    }
}
