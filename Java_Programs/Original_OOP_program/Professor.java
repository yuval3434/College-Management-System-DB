package java_oop_project;

public class Professor extends Doctor{
    private final String institution;

    public Professor(String name, String id, String nameOfDegree, String degree, int wage, String[] articles, String institution) {
        super(name, id, nameOfDegree, degree, wage, articles);
        this.institution = institution;
    }

    @Override
    public String toString() {
        return super.toString() + " ,institution: " + institution;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj instanceof Professor professor){
            return super.equals(professor);
        }
        return false;
    }
}
