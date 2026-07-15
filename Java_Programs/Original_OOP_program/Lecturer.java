package java_oop_project;
import java.io.Serializable;
import java.util.ArrayList;

public class Lecturer implements Cloneable, Nameable, Serializable {
    private final String name;
    private final String id;
    private final String nameOfDegree;
    private final String degree;
    private final int wage;
    private Department department;
    private final ArrayList<Committee> committeesBelongs;

    public Lecturer(String name, String id, String nameOfDegree, String degree, int wage) {
        this.name = name;
        this.id = id;
        this.nameOfDegree = nameOfDegree;
        this.degree = degree;
        this.wage = wage;
        committeesBelongs = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }

    public ArrayList<Committee> getCommitteesBelongs() {
        return committeesBelongs;
    }

    public Department getDepartment() {
        return department;
    }


    public int getWage() {
        return wage;
    }


    public void setDepartment(Department department) {
        this.department = department;
    }

    public StringBuilder showName(ArrayList<Committee> array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.size(); i++) {
            sb.append(array.get(i).getName());
            if (i< array.size() - 1){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb;
    }

    @Override
    protected Lecturer clone() throws CloneNotSupportedException {
        return (Lecturer) super.clone();
    }

    @Override
    public String toString() {
        String deptName = (department == null) ? "No Department" : department.getName();

        StringBuilder sb = new StringBuilder();
                sb.append("Name: '").append(name).append('\'')
                .append(", Id: ").append(id)
                .append(", Name Of Degree: '").append(nameOfDegree).append('\'')
                .append(", Level Of Degree: '").append(degree).append('\'')
                .append(", Wage: ").append(wage)
                .append(", Department: '").append(deptName).append('\'')
                .append(", Committees belongs to: ").append(showName(committeesBelongs));


        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj instanceof Lecturer lecturer){
            return name.equals(lecturer.name);
        }
        return false;
    }
}
