package java_oop_project;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static final String[] MENU = {
            "Exit Menu And Save Data",
            "Add Lecturer",
            "Add Committee",
            "Add Lecturer To A Committee",
            "Update chairman in committee",
            "Remove Lecturer from committee",
            "Add Study Department",
            "Show The Average Salaries Of All Lecturers At The College",
            "Show The Average Salaries Of All Lecturers At The College In A Specific Department",
            "Display Details Of All Lecturers",
            "Display Details Of All Committees",
            "Add Lecturer To Study Department",
            "Duplicate data of committee",
            "Compare between professors/doctors",
            "Compare between committees"
    };

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        runMenu(s);
        s.close();
    }

    public static void runMenu(Scanner s){
        String fileName = "collegeData.bin";
        College college = FileManager.loadData(fileName);
        if (college == null){
            System.out.println("Enter the name of your college: ");
            String collegeName = s.nextLine();
            ArrayList<Lecturer> lecturers = new ArrayList<>();
            ArrayList<Committee> committees = new ArrayList<>();
            ArrayList<Department> studyDepartments = new ArrayList<>();
            college = new College(collegeName, lecturers,committees,studyDepartments);
        }


        int userChosen;
        do {
            userChosen = showMenu(s);
            switch (userChosen) {
                case 0 -> FileManager.saveData(college,"collegeData.bin");
                case 1 -> addLecturerFlow(s,college);
                case 2 -> addCommitteeFlow(s,college);
                case 3 -> addLecturerToCommitteeFlow(s,college);
                case 4 -> updateChairmanFlow(s,college);
                case 5 -> removeLecturerFromCommitteeFlow(s,college);
                case 6 -> addStudyDepartmentFlow(s,college);
                case 7 -> showAverageOfSalariesFlow(college);
                case 8 -> showAverageOfSalariesByDepartFlow(s,college);
                case 9 -> System.out.println(showDetails(college.getLecturers()));
                case 10 -> System.out.println(showDetails(college.getCommittees()));
                case 11 -> addLecturerToDepartmentFlow(s,college);
                case 12 -> duplicateCommitteeFlow(s,college);
                case 13 -> compareProDocFlow(s,college);
                case 14 -> compareCommitteesFlow(s,college);
                default -> System.out.println("Unexpected Value");
            }
        } while (userChosen != 0);
    }

    public static void compareCommitteesFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the first committee: ");
            String first = s.nextLine();
            System.out.println("Enter the name of the second committee: ");
            String second = s.nextLine();

            int res = college.compareCommittees(first,second);

            if (res == 0){
                System.out.println("They Are the same size");
            } else if (res > 0) {
                System.out.println( first + " is bigger");
            } else {
                System.out.println( second + " is bigger");
            }
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void compareProDocFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the first professor/doctor: ");
            String first = s.nextLine();
            System.out.println("Enter the name of the second professor/doctor: ");
            String second = s.nextLine();

            int res = college.compareProDoc(first,second);

            if (res == 0){
                System.out.println("They have the same amount of articles");
            } else if (res > 0) {
                System.out.println( first + " has more articles");
            } else {
                System.out.println( second + " has more articles");
            }
        } catch (CollegeException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addLecturerFlow(Scanner s, College college){
        s.nextLine();
        while (true) {
            try {
                System.out.println("Add Lecturer Name: ");
                String lecturerName = s.nextLine();

                System.out.println("Add Lecturer's ID: ");
                String id = s.nextLine();

                System.out.println("Add the name of the degree:");
                String degreeName = s.nextLine();

                System.out.println("Add the level of the degree: ( 1 / 2 / Doctor / Professor) ");
                String degree = s.nextLine();

                System.out.println("Add the wage: ");
                int wage = s.nextInt();
                s.nextLine();

                String[] articles = new String[0];
                String institution = "";

                if (degree.equals("Doctor") || degree.equals("Professor")){
                    articles = getArticles(s);
                    if (degree.equals("Professor")){
                        System.out.println("From which institution does the lecturer got the diploma? ");
                        institution = s.nextLine();
                    }
                }
                college.addLecturer(lecturerName, degreeName, degree, wage, id,articles,institution);
                break;
            } catch (CollegeException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void addCommitteeFlow(Scanner s, College college){
        s.nextLine();
            try {
                System.out.println("Add Committee Name: ");
                String committeeName = s.nextLine();

                System.out.println("Add the chairman's name: ");
                String chairman = s.nextLine();

                System.out.println("What level of degree should the department accept? ( 1 / 2 / Doctor / Professor ) ");
                String levelOfDegree = s.nextLine();

                college.addCommittee(committeeName, chairman, levelOfDegree);
            } catch (CollegeException e) {
                System.out.println(e.getMessage());
            }
    }

    public static void addLecturerToCommitteeFlow(Scanner s, College college) {
        s.nextLine();
            try {
                System.out.println("Enter The Name Of The Lecturer You Would Like You Add To The Committee: ");
                String lecturerName = s.nextLine();

                System.out.println("Which Committee You Would Like To Add The Lecturer To?");
                String committeeName = s.nextLine();

                college.addLecturerToCommittee(lecturerName, committeeName);
            } catch (CollegeException e){
                System.out.println(e.getMessage());
            }

    }

    public static void updateChairmanFlow(Scanner s, College college){
        s.nextLine();
            try {
                System.out.println("In which committee would you like to change the chairman? ");
                String committeeName = s.nextLine();

                System.out.println("Which lecturer would you like to put as a chairman? ");
                String chairmanName = s.nextLine();

                college.updateChairman(committeeName, chairmanName);
            } catch (CollegeException e){
                System.out.println(e.getMessage());
            }


    }

    public static void removeLecturerFromCommitteeFlow(Scanner s, College college){
        s.nextLine();
            try {
                System.out.println("In which committee would you like to remove a lecturer?");
                String committeeName = s.nextLine();

                System.out.println("Which lecturer would you like to remove?");
                String lecturerName = s.nextLine();

                college.removeLecturerFromCommittee(committeeName, lecturerName);

            }catch (CollegeException e){
                System.out.println(e.getMessage());
            }
    }

    public static void addStudyDepartmentFlow(Scanner s, College college) {
        s.nextLine();
        while (true){
            try {
                System.out.println("Add Study Department Name: ");
                String studyDepartmentName = s.nextLine();

                college.addStudyDepartment(studyDepartmentName);
                break;
            }catch (CollegeException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void showAverageOfSalariesFlow(College college) {
        try {
            double average = college.showAverageOfSalaries();
                System.out.println("The average of salaries in the college is: ");
                System.out.println(average);
        } catch (CollegeException e){
            System.out.println(e.getMessage());
        }
    }

    public static void showAverageOfSalariesByDepartFlow(Scanner s, College college) {
        double average;
        s.nextLine();
        try {
            System.out.println("Which department do you want to show average? ");
            String departmentName = s.nextLine();

            average = college.showAverageOfSalariesByDepart(departmentName);
            System.out.println("The average of salaries in the " + departmentName + " department is: ");
            System.out.println(average);

        } catch (CollegeException e){
            System.out.println(e.getMessage());
        }
    }

    public static int showMenu(Scanner s){
        System.out.println("\n====== Menu =======");
        for (int i = 0; i < MENU.length; i++) {
            System.out.println(i + ". " + MENU[i]);
        }
        System.out.println("Please enter your chose : ");
        return s.nextInt();
    }

    public static StringBuilder showDetails(ArrayList<?> array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.size(); i++) {
            sb.append(array.get(i));
            if (i< array.size() - 1){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb;
    }

    public static void addLecturerToDepartmentFlow(Scanner s , College college){
        s.nextLine();
        while (true){
            try {
                System.out.println("Enter The Name Of The Lecturer You Would Like You Add To The Study Department: ");
                String lecturerName = s.nextLine();

                System.out.println("To which department assign to?");
                String department = s.nextLine();

                college.addLecturerToDepartment(lecturerName,department);
                break;
            }catch (CollegeException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static String[] getArticles(Scanner s) {
        System.out.println("enter articles written by the Doctor, seperated by commas: ");
        return s.nextLine().split(",");
    }

    public static void duplicateCommitteeFlow(Scanner s, College college){
        s.nextLine();
        try {
            System.out.println("Enter the name of the committee you would like to copy the data:");
            String committeeName = s.nextLine();
            college.duplicateCommittee(committeeName);
        } catch (CollegeException | CloneNotSupportedException e){
            System.out.println(e.getMessage());
        }
    }
}
