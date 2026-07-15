package java_oop_project;

import java.io.Serializable;
import java.util.ArrayList;

public class Committee implements Cloneable, Nameable, Serializable {
    private String name;
    private final String levelOfDegree;
    private ArrayList<Lecturer> lecturersInCommittee;
    private Lecturer chairman;

    public Committee(String name, Lecturer chairman, String levelOfDegree) {
        this.name = name;
        this.chairman = chairman;
        this.levelOfDegree = levelOfDegree;
        lecturersInCommittee = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<Lecturer> getLecturersInCommittee() {
        return lecturersInCommittee;
    }

    public Lecturer getChairman() {
        return chairman;
    }


    public void setChairman(Lecturer chairman) {
        this.chairman = chairman;
    }

    public String getLevelOfDegree() {
        return levelOfDegree;
    }

    public void setName(String name) {
        this.name = "new-" +name;
    }

    public StringBuilder showName(ArrayList<Lecturer> array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i)== null){
                continue;
            }
            sb.append(array.get(i).getName());
            if (i < array.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb;
    }

    public int sumOfArticles(){
        int res = 0;
        for (Lecturer lecturer : lecturersInCommittee) {
            if ((lecturer.getClass() == Doctor.class || lecturer.getClass() == Professor.class)) {
                Doctor doctor = (Doctor) lecturer;
                res += doctor.getNumOfArticles();
            }
        }
        return res;
    }

    @Override
    public Committee clone() throws CloneNotSupportedException {
        Committee committee = (Committee) super.clone();
        committee.chairman = chairman.clone();
        committee.lecturersInCommittee = new ArrayList<>();
        for (Lecturer lecturer : this.lecturersInCommittee) {
            committee.lecturersInCommittee.add(lecturer.clone());
        }
        return committee;
    }

    @Override
    public String toString() {
        return "{" +
                "Name: '" + name + '\'' +
                ", Lecturers In Committee: " + showName(lecturersInCommittee) +
                ", Number Of Lecturers In Committee: " + lecturersInCommittee.size() +
                ", Chairman: " + chairman.getName() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Committee committee) {
            return name.equals(committee.name);
        }
        return false;
    }

}