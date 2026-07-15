package java_oop_project;

import java.io.*;

public class FileManager {

    public static void saveData(College college, String fileName){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(college);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static College loadData(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Data file not found. Initializing empty College.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            College college = (College) ois.readObject();
            System.out.println("Data loaded successfully.");
            return college;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return null;
        }
    }
}
