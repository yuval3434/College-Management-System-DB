package java_oop_project;

import java.util.Comparator;

public class CompareCommitteeByNumOfLecturers implements Comparator <Committee> {
    @Override
    public int compare(Committee o1, Committee o2) {
        return o1.getLecturersInCommittee().size() - o2.getLecturersInCommittee().size();
    }
}
