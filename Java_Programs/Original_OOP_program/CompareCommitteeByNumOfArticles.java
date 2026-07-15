package java_oop_project;

import java.util.Comparator;

public class CompareCommitteeByNumOfArticles implements Comparator <Committee> {
    @Override
    public int compare(Committee o1, Committee o2) {
        return o1.sumOfArticles() - o2.sumOfArticles();
    }
}
