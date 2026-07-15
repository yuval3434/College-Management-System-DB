package java_oop_project;

import java.util.ArrayList;

public class Utils {

    public static Nameable getObjectByName(String name, ArrayList<Nameable> array) {
        for (Nameable item : array) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public static int findIndex(Object item ,ArrayList<?> array){
        for (int i = 0; i < array.size() ; i++) {
            if (array.get(i).equals(item)) {
                return i;
            }
        }
        return -1;
    }

}
