package java_oop_project;
import java.util.Arrays;


public class Doctor extends Lecturer implements Comparable <Doctor>{
    private final String[] articles;
    private int numOfArticles;


    public Doctor(String name, String id, String nameOfDegree, String degree, int wage, String[] articles) {
        super(name, id, nameOfDegree, degree, wage);
        this.articles = articles;
        setNumOfArticles();
    }

    public void setNumOfArticles() {
        for (String article : articles) {
            if (article != null) {
                this.numOfArticles++;
            }
        }
    }

    public int getNumOfArticles() {
        return numOfArticles;
    }

    @Override
    public String toString() {
        return super.toString() +
                " ,articles:" + Arrays.toString(articles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj instanceof Doctor doctor){
            return super.equals(doctor);
        }
        return false;
    }

    @Override
    public int compareTo(Doctor other) {
        return Integer.compare(numOfArticles,other.numOfArticles);
    }
}
