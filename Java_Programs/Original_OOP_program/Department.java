package java_oop_project;

import java.io.Serializable;
import java.util.ArrayList;

public class Department implements Nameable, Serializable {
    private final String name;
    private final ArrayList<Lecturer> lecturersInDepartment;

    public Department(String name) {
        this.name = name;
        this.lecturersInDepartment = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<Lecturer> getLecturersInDepartment() {
        return lecturersInDepartment;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj instanceof Department department){
            return name.equals(department.name);
        }
        return false;
    }

}
