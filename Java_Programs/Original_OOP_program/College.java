package java_oop_project;


import java.io.Serializable;
import java.util.ArrayList;
import static java_oop_project.Utils.*;


public class College implements Serializable {
    private final String name;
    private final ArrayList<Lecturer> lecturers;
    private final ArrayList<Committee> committees;
    private final ArrayList<Department> studyDepartments;

    public College(String name, ArrayList<Lecturer> lecturers, ArrayList<Committee> committees, ArrayList<Department> studyDepartments) {
        this.name = name;
        this.lecturers = lecturers;
        this.committees = committees;
        this.studyDepartments = studyDepartments;
    }

    public ArrayList<Lecturer> getLecturers() {
        return lecturers;
    }

    public ArrayList<Committee> getCommittees() {
        return committees;
    }

    public void addLecturerToCommittee(String lecturerName , String committeeName) throws CollegeException {

            Lecturer lecturer =  (Lecturer) getObjectByName(lecturerName, new ArrayList<>(lecturers));
            Committee committee =  (Committee) getObjectByName(committeeName, new ArrayList<>(committees));


            if (!lecturers.contains(lecturer)) {
                throw new NotExistException("The Lecturer Does Not exist");
            }

            if (!committees.contains(committee)) {
                throw new NotExistException("The Committees Does Not exist");
            }

            if (committee == null || committee.getLecturersInCommittee().contains(lecturer)) {
                throw new ExistanceException("Lecturer is already in this committee");
            }

            if (committee.getChairman() == lecturer) {
                throw new ConditionsExceptions("The lecturer is the chairman");
            }

            if (lecturer == null || !lecturer.getDegree().equals(committee.getLevelOfDegree())) {
                throw new ConditionsExceptions("There is mismatch between the degree levels");
            }

            committee.getLecturersInCommittee().add(lecturer);


    }

    public void removeLecturerFromCommittee(String committeeName, String lecturerName) throws CollegeException{
        Lecturer lecturer =  (Lecturer) getObjectByName(lecturerName, new ArrayList<>(lecturers));
        Committee committee =  (Committee) getObjectByName(committeeName, new ArrayList<>(committees));

        if (committee == null || !committees.contains(committee)) {
            throw new NotExistException("The Committees Does Not exist");
        }

        if(lecturer == null || !lecturers.contains(lecturer)){
            throw new NotExistException("The Lecturer Does Not exist");
        }

        ArrayList<Lecturer> lecturersInCommittee = committee.getLecturersInCommittee();
        int indexToRemove = findIndex(lecturer,lecturersInCommittee);

        if (indexToRemove == -1) {
            throw new NotInCommitteeException();
        }

        lecturersInCommittee.remove(indexToRemove);

        ArrayList<Committee> committeesBelongs = lecturer.getCommitteesBelongs();
        int indexToRemove2 = findIndex(committee,committeesBelongs);

        committeesBelongs.remove(indexToRemove2);
    }

    public void updateChairman(String committeeName, String chairmanName) throws CollegeException {
        Committee committee =  (Committee) getObjectByName(committeeName, new ArrayList<>(committees));
        Lecturer chairman =  (Lecturer) getObjectByName(chairmanName, new ArrayList<>(lecturers));
        if(committee == null || !committees.contains(committee)){
            throw new NotExistException("The Committees Does Not exist");
        }

        if(chairman == null || !lecturers.contains(chairman)){
            throw new NotExistException("The Lecturer Does Not exist");
        }

        if (chairman.getDegree().equals("1") || chairman.getDegree().equals("2")) {
            throw new ConditionsExceptions("The lecturer does not meet the conditions");
        }

        if (committee.getChairman() == chairman) {
           throw new ConditionsExceptions("The lecturer is already the chairman");
        }

        committee.setChairman(chairman);

        if (committee.getLecturersInCommittee().contains(chairman)) {
            removeLecturerFromCommittee(committeeName,chairmanName);
        }

    }

    public double showAverageOfSalariesByDepart(String departmentName) throws CollegeException {
        Department department =  (Department) getObjectByName(departmentName, new ArrayList<>(studyDepartments));

        if (department == null || !studyDepartments.contains(department)) {
                throw new ExistanceException("The study department does not exist");
        }
        ArrayList<Lecturer> lecturersInDepartment = department.getLecturersInDepartment();

        if (lecturersInDepartment.isEmpty()) {
            throw new EmptyDepartmentException("There are no lecturers in that department");
        }

        int sum = 0;


        for (Lecturer lecturer : lecturersInDepartment) {
            sum += lecturer.getWage();
        }


        return (double) sum / lecturersInDepartment.size();
    }

    public double showAverageOfSalaries() throws CollegeException {
        int sum = 0;
        if (lecturers.isEmpty()){
            throw new ExistanceException("There are no lecturers");}
        for (Lecturer lecturer : lecturers) {
            sum += lecturer.getWage();
        }
        return (double) sum / lecturers.size();
    }

    public void addLecturer(String lecturerName, String degreeName, String degree,int wage,String id, String[] articles, String institution) throws CollegeException {
        Lecturer lecturer =  (Lecturer) getObjectByName(lecturerName, new ArrayList<>(lecturers));

        if (lecturers.contains(lecturer)) {
            throw new ExistanceException("Lecturer already exist");
        }

        if (wage < 0){
            throw new InputException("wage is under 0");
        }

        if (!degree.equals("1") && !degree.equals("2") && !degree.equals("Doctor") && !degree.equals("Professor")) {
            throw new InputException("Invalid level of degree");
        }

        if (degree.equals("1") || degree.equals("2")){
            Lecturer newLecturer = new Lecturer(lecturerName, id, degreeName,degree,wage);
            lecturers.add(newLecturer);
        } else {
            if (degree.equals("Doctor")){
                Lecturer newDoctor = new Doctor(lecturerName,id, degreeName, degree, wage,articles);
                lecturers.add(newDoctor);
            } else {
               Lecturer newProfessor =  new Professor(lecturerName,id, degreeName, degree, wage,articles,institution);
                lecturers.add(newProfessor);
            }
        }

    }

    public void addStudyDepartment(String studyDepartmentName) throws CollegeException {
        Department studyDepartment =  (Department) getObjectByName(studyDepartmentName, new ArrayList<>(studyDepartments));
            if (studyDepartments.contains(studyDepartment)) {
                throw new ExistanceException("The study department is already exist");
            } else {
                Department newDepartment = new Department(studyDepartmentName);
                studyDepartments.add(newDepartment);
            }
    }

    public void addCommittee(String committeeName, String chairmanName, String level ) throws CollegeException{
        Committee committee =  (Committee) getObjectByName(committeeName, new ArrayList<>(committees));
        Lecturer chairman =  (Lecturer) getObjectByName(chairmanName, new ArrayList<>(lecturers));

        if (committees.contains(committee)) {
            throw new ExistanceException("Committee already exist");
        }

        if( chairman == null || !lecturers.contains(chairman)) {
            throw new NotExistException("The Lecturer Does Not exist");
        }

        if (!level.equals("1") && !level.equals("2") && !level.equals("Doctor") && !level.equals("Professor")) {
            throw new InputException("Invalid level of degree");
        }

        String levelOfDegree = chairman.getDegree();
        if (levelOfDegree.equals("1") || levelOfDegree.equals("2")) {
            throw new ConditionsExceptions("The lecturer does not meet the conditions");
        }

        Committee newCommittee = new Committee(committeeName,chairman,level);
        committees.add(newCommittee);

    }

    public void addLecturerToDepartment(String lecturerName, String departmentName) throws CollegeException{
        Lecturer lecturer =  (Lecturer) getObjectByName(lecturerName, new ArrayList<>(lecturers));
        Department department =  (Department) getObjectByName(departmentName, new ArrayList<>(studyDepartments));

        if (lecturer == null || !lecturers.contains(lecturer)){
            throw new NotExistException("The Lecturer Does Not exist");
        }

        if (department == null || !studyDepartments.contains(department)){
            throw new NotExistException("The study department does not exist");
        }

        ArrayList<Lecturer> lecturerInDepartment = department.getLecturersInDepartment();

        if (lecturerInDepartment.contains(lecturer)) {
            throw new ExistanceException("Lecturer is already in this department");
        }


        if (lecturer.getDepartment() != null) {
            Department currentDepartment = lecturer.getDepartment();
            ArrayList<Lecturer> lecturersInCurrentDepartment = currentDepartment.getLecturersInDepartment();

            int indexToRemove = findIndex(lecturer, lecturersInCurrentDepartment);
            lecturersInCurrentDepartment.remove(indexToRemove);

        }

        lecturer.setDepartment(department);
    }

    public void duplicateCommittee(String committeeName) throws CollegeException, CloneNotSupportedException {
        Committee committee =  (Committee) getObjectByName(committeeName, new ArrayList<>(committees));
        if (committee == null || !committees.contains(committee)) {
            throw new NotExistException("Committee do not exist");
        }

        Committee newCommittee = committee.clone();
        newCommittee.setName(committee.getName());
        committees.add(newCommittee);
    }

    public int compareProDoc(String first, String second) throws CollegeException {
        Doctor doctor1 = (Doctor) getObjectByName(first, new ArrayList<>(lecturers));
        Doctor doctor2 = (Doctor) getObjectByName(second, new ArrayList<>(lecturers));

        if (doctor1 == null || !lecturers.contains(doctor1)) {
            throw new NotExistException("The Lecturer Does Not exist");
        }

        if (doctor2 == null || !lecturers.contains(doctor2)) {
            throw new NotExistException("The Lecturer Does Not exist");
        }

        return doctor1.compareTo(doctor2);



    }

    public int compareCommittees(String first, String second) throws CollegeException {
      Committee committee1 = (Committee) getObjectByName(first, new ArrayList<>(committees));
      Committee committee2 = (Committee) getObjectByName(second, new ArrayList<>(committees));

        if (committee1 == null || !committees.contains(committee1)) {
            throw new NotExistException("The committee Does Not exist");
        }

        if (committee2 == null || !committees.contains(committee2)) {
            throw new NotExistException("The committee Does Not exist");
        }

        CompareCommitteeByNumOfLecturers byNum = new CompareCommitteeByNumOfLecturers();
        int res = byNum.compare(committee1,committee2);
        if (res == 0){
            CompareCommitteeByNumOfArticles byArticles = new CompareCommitteeByNumOfArticles();
            res = byArticles.compare(committee1,committee2);
        }
        return res;
    }
}





