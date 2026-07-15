package java_jdbc_project;

import java.util.Scanner;

public class Main {
    public static final String[] MENU = {
            "Exit",
            "Add Lecturer (INSERT)",
            "Add Committee (INSERT)",
            "Add Lecturer To A Committee (INSERT)",
            "Update chairman in committee (UPDATE)",
            "Remove Lecturer from committee (DELETE)",
            "Add Study Department (INSERT)",
            "Show The Average Salaries Of All Lecturers At The College",
            "Show The Average Salaries Of Lecturers In A Specific Department",
            "Display Details Of All Lecturers",
            "Display Details Of All Committees",
            "Add Lecturer To Study Department (UPDATE)",
            "Duplicate data of committee",
            "Compare between professors/doctors",
            "Compare between committees",
            "Search Lecturer by name (SEARCH)",
            "Update Lecturer wage (UPDATE)",
            "Delete Lecturer (DELETE)"
    };

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        runMenu(s);
        s.close();
    }

    public static void runMenu(Scanner s) {
        College college;
        try {
            college = College.load();
            if (college == null) {
                System.out.println("No college found in the database. Enter the name of your college: ");
                String collegeName = s.nextLine();
                college = College.create(collegeName);
            }
            System.out.println("Connected to college: " + college.getName());
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to the database. Check Database.java settings.");
            return;
        }

        int userChosen;
        do {
            userChosen = showMenu(s);
            switch (userChosen) {
                case 0:  System.out.println("Goodbye! (all data is already saved in the database)"); break;
                case 1:  addLecturerFlow(s, college); break;
                case 2:  addCommitteeFlow(s, college); break;
                case 3:  addLecturerToCommitteeFlow(s, college); break;
                case 4:  updateChairmanFlow(s, college); break;
                case 5:  removeLecturerFromCommitteeFlow(s, college); break;
                case 6:  addStudyDepartmentFlow(s, college); break;
                case 7:  showAverageOfSalariesFlow(college); break;
                case 8:  showAverageOfSalariesByDepartFlow(s, college); break;
                case 9:  displayLecturersFlow(college); break;
                case 10: displayCommitteesFlow(college); break;
                case 11: addLecturerToDepartmentFlow(s, college); break;
                case 12: duplicateCommitteeFlow(s, college); break;
                case 13: compareProDocFlow(s, college); break;
                case 14: compareCommitteesFlow(s, college); break;
                case 15: searchLecturerFlow(s, college); break;
                case 16: updateLecturerWageFlow(s, college); break;
                case 17: deleteLecturerFlow(s, college); break;
                default: System.out.println("Unexpected Value");
            }
        } while (userChosen != 0);
    }

    public static int showMenu(Scanner s) {
        System.out.println("\n====== Menu =======");
        for (int i = 0; i < MENU.length; i++) {
            System.out.println(i + ". " + MENU[i]);
        }
        System.out.println("Please enter your choice: ");
        return s.nextInt();
    }

    public static void addLecturerFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Add Lecturer Name: ");
            String lecturerName = s.nextLine();

            System.out.println("Add Lecturer's ID: ");
            String id = s.nextLine();

            System.out.println("Add the name of the degree:");
            String degreeName = s.nextLine();

            System.out.println("Add the level of the degree: ( 1 / 2 / Doctor / Professor ) ");
            String degree = s.nextLine();

            System.out.println("Add the wage: ");
            int wage = s.nextInt();
            s.nextLine();

            String[] articles = new String[0];
            String institution = "";
            if (degree.equals("Doctor") || degree.equals("Professor")) {
                articles = getArticles(s);
                if (degree.equals("Professor")) {
                    System.out.println("From which institution does the lecturer got the diploma? ");
                    institution = s.nextLine();
                }
            }
            college.addLecturer(lecturerName, degreeName, degree, wage, id, articles, institution);
            System.out.println("Lecturer added.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addCommitteeFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Add Committee Name: ");
            String committeeName = s.nextLine();

            System.out.println("Add the chairman's name: ");
            String chairman = s.nextLine();

            System.out.println("What level of degree should the committee accept? ( 1 / 2 / Doctor / Professor ) ");
            String levelOfDegree = s.nextLine();

            college.addCommittee(committeeName, chairman, levelOfDegree);
            System.out.println("Committee added.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addLecturerToCommitteeFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the lecturer you would like to add to the committee: ");
            String lecturerName = s.nextLine();

            System.out.println("Which committee would you like to add the lecturer to?");
            String committeeName = s.nextLine();

            college.addLecturerToCommittee(lecturerName, committeeName);
            System.out.println("Lecturer added to committee.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateChairmanFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("In which committee would you like to change the chairman? ");
            String committeeName = s.nextLine();

            System.out.println("Which lecturer would you like to put as a chairman? ");
            String chairmanName = s.nextLine();

            college.updateChairman(committeeName, chairmanName);
            System.out.println("Chairman updated.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeLecturerFromCommitteeFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("In which committee would you like to remove a lecturer?");
            String committeeName = s.nextLine();

            System.out.println("Which lecturer would you like to remove?");
            String lecturerName = s.nextLine();

            college.removeLecturerFromCommittee(committeeName, lecturerName);
            System.out.println("Lecturer removed from committee.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addStudyDepartmentFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Add Study Department Name: ");
            String studyDepartmentName = s.nextLine();

            college.addStudyDepartment(studyDepartmentName);
            System.out.println("Department added.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showAverageOfSalariesFlow(College college) {
        try {
            double average = college.showAverageOfSalaries();
            System.out.println("The average of salaries in the college is: " + average);
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showAverageOfSalariesByDepartFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Which department do you want to show average? ");
            String departmentName = s.nextLine();

            double average = college.showAverageOfSalariesByDepart(departmentName);
            System.out.println("The average of salaries in the " + departmentName +
                    " department is: " + average);
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void displayLecturersFlow(College college) {
        try {
            System.out.println(college.displayAllLecturers());
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void displayCommitteesFlow(College college) {
        try {
            System.out.println(college.displayAllCommittees());
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addLecturerToDepartmentFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the lecturer to add to a study department: ");
            String lecturerName = s.nextLine();

            System.out.println("To which department assign to?");
            String department = s.nextLine();

            college.addLecturerToDepartment(lecturerName, department);
            System.out.println("Lecturer assigned to department.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void duplicateCommitteeFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the committee you would like to copy:");
            String committeeName = s.nextLine();
            college.duplicateCommittee(committeeName);
            System.out.println("Committee duplicated as 'new-" + committeeName + "'.");
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

            int res = college.compareProDoc(first, second);
            if (res == 0) {
                System.out.println("They have the same amount of articles");
            } else if (res > 0) {
                System.out.println(first + " has more articles");
            } else {
                System.out.println(second + " has more articles");
            }
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void compareCommitteesFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the first committee: ");
            String first = s.nextLine();
            System.out.println("Enter the name of the second committee: ");
            String second = s.nextLine();

            int res = college.compareCommittees(first, second);
            if (res == 0) {
                System.out.println("They are the same size");
            } else if (res > 0) {
                System.out.println(first + " is bigger");
            } else {
                System.out.println(second + " is bigger");
            }
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void searchLecturerFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the lecturer to search: ");
            String lecturerName = s.nextLine();
            System.out.println(college.searchLecturerByName(lecturerName));
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateLecturerWageFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the lecturer: ");
            String lecturerName = s.nextLine();
            System.out.println("Enter the new wage: ");
            int wage = s.nextInt();
            s.nextLine();
            college.updateLecturerWage(lecturerName, wage);
            System.out.println("Wage updated.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteLecturerFlow(Scanner s, College college) {
        s.nextLine();
        try {
            System.out.println("Enter the name of the lecturer to delete: ");
            String lecturerName = s.nextLine();
            college.deleteLecturer(lecturerName);
            System.out.println("Lecturer deleted.");
        } catch (CollegeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String[] getArticles(Scanner s) {
        System.out.println("Enter articles written by the lecturer, separated by commas: ");
        return s.nextLine().split(",");
    }
}
