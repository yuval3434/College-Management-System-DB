package java_jdbc_project;

import java.sql.*;

public class College {

    private final int collegeId;
    private final String name;

    private College(int collegeId, String name) {
        this.collegeId = collegeId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static College load() throws CollegeException {
        String sql = "SELECT college_id, name FROM college ORDER BY college_id LIMIT 1";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new College(rs.getInt("college_id"), rs.getString("name"));
            }
            return null;
        } catch (SQLException e) {
            throw new CollegeException("Could not load college: " + e.getMessage());
        }
    }

    public static College create(String name) throws CollegeException {
        String sql = "INSERT INTO college (name) VALUES (?) RETURNING college_id";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new College(rs.getInt("college_id"), name);
            }
        } catch (SQLException e) {
            throw new CollegeException("Could not create college: " + e.getMessage());
        }
    }

    private String findLecturerIdByName(Connection con, String name) throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement("SELECT lecturer_id FROM lecturer WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    private Integer findCommitteeIdByName(Connection con, String name) throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement("SELECT committee_id FROM committee WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }

    private Integer findDepartmentIdByName(Connection con, String name) throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement("SELECT department_id FROM department WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }

    private String getLecturerDegree(Connection con, String lecturerId) throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement("SELECT degree_level FROM lecturer WHERE lecturer_id = ?")) {
            ps.setString(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public void addLecturer(String lecturerName, String degreeName, String degree, int wage,
                            String id, String[] articles, String institution) throws CollegeException {
        if (wage < 0) {
            throw new InputException("wage is under 0");
        }
        if (!degree.equals("1") && !degree.equals("2")
                && !degree.equals("Doctor") && !degree.equals("Professor")) {
            throw new InputException("Invalid level of degree");
        }

        String insertLecturer =
                "INSERT INTO lecturer " +
                "(lecturer_id, name, name_of_degree, degree_level, wage, institution) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String insertArticle =
                "INSERT INTO article (title, lecturer_id) VALUES (?, ?)";

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                if (findLecturerIdByName(con, lecturerName) != null) {
                    throw new ExistanceException("Lecturer already exist");
                }

                try (PreparedStatement ps = con.prepareStatement(insertLecturer)) {
                    ps.setString(1, id);
                    ps.setString(2, lecturerName);
                    ps.setString(3, degreeName);
                    ps.setString(4, degree);
                    ps.setInt(5, wage);
                    if (degree.equals("Professor") && institution != null && !institution.isEmpty()) {
                        ps.setString(6, institution);
                    } else {
                        ps.setNull(6, Types.VARCHAR);
                    }
                    ps.executeUpdate();
                }

                if ((degree.equals("Doctor") || degree.equals("Professor")) && articles != null) {
                    try (PreparedStatement ps = con.prepareStatement(insertArticle)) {
                        for (String article : articles) {
                            if (article != null && !article.trim().isEmpty()) {
                                ps.setString(1, article.trim());
                                ps.setString(2, id);
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
                }

                con.commit();
            } catch (SQLException | CollegeException e) {
                con.rollback();
                throw e instanceof CollegeException ? (CollegeException) e
                        : new CollegeException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void addStudyDepartment(String departmentName) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            if (findDepartmentIdByName(con, departmentName) != null) {
                throw new ExistanceException("The study department is already exist");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO department (name, college_id) VALUES (?, ?)")) {
                ps.setString(1, departmentName);
                ps.setInt(2, collegeId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void addCommittee(String committeeName, String chairmanName, String level)
            throws CollegeException {
        if (!level.equals("1") && !level.equals("2")
                && !level.equals("Doctor") && !level.equals("Professor")) {
            throw new InputException("Invalid level of degree");
        }
        try (Connection con = Database.getConnection()) {
            if (findCommitteeIdByName(con, committeeName) != null) {
                throw new ExistanceException("Committee already exist");
            }
            String chairmanId = findLecturerIdByName(con, chairmanName);
            if (chairmanId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            String degree = getLecturerDegree(con, chairmanId);
            if (degree.equals("1") || degree.equals("2")) {
                throw new ConditionsExceptions("The lecturer does not meet the conditions");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO committee (name, level_of_degree, chairman_id) " +
                    "VALUES (?, ?, ?)")) {
                ps.setString(1, committeeName);
                ps.setString(2, level);
                ps.setString(3, chairmanId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void addLecturerToCommittee(String lecturerName, String committeeName)
            throws CollegeException {
        try (Connection con = Database.getConnection()) {
            String lecturerId = findLecturerIdByName(con, lecturerName);
            if (lecturerId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            Integer committeeId = findCommitteeIdByName(con, committeeName);
            if (committeeId == null) {
                throw new NotExistException("The Committees Does Not exist");
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT 1 FROM committee WHERE committee_id = ? AND chairman_id = ?")) {
                ps.setInt(1, committeeId);
                ps.setString(2, lecturerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new ConditionsExceptions("The lecturer is the chairman");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT 1 FROM committee_member WHERE committee_id = ? AND lecturer_id = ?")) {
                ps.setInt(1, committeeId);
                ps.setString(2, lecturerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new ExistanceException("Lecturer is already in this committee");
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO committee_member (committee_id, lecturer_id) VALUES (?, ?)")) {
                ps.setInt(1, committeeId);
                ps.setString(2, lecturerId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ConditionsExceptions(e.getMessage());
        }
    }

    public void updateChairman(String committeeName, String chairmanName) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            Integer committeeId = findCommitteeIdByName(con, committeeName);
            if (committeeId == null) {
                throw new NotExistException("The Committees Does Not exist");
            }
            String chairmanId = findLecturerIdByName(con, chairmanName);
            if (chairmanId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            String degree = getLecturerDegree(con, chairmanId);
            if (degree.equals("1") || degree.equals("2")) {
                throw new ConditionsExceptions("The lecturer does not meet the conditions");
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE committee SET chairman_id = ? WHERE committee_id = ?")) {
                ps.setString(1, chairmanId);
                ps.setInt(2, committeeId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM committee_member WHERE committee_id = ? AND lecturer_id = ?")) {
                ps.setInt(1, committeeId);
                ps.setString(2, chairmanId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void addLecturerToDepartment(String lecturerName, String departmentName)
            throws CollegeException {
        try (Connection con = Database.getConnection()) {
            String lecturerId = findLecturerIdByName(con, lecturerName);
            if (lecturerId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            Integer departmentId = findDepartmentIdByName(con, departmentName);
            if (departmentId == null) {
                throw new NotExistException("The study department does not exist");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE lecturer SET department_id = ? WHERE lecturer_id = ?")) {
                ps.setInt(1, departmentId);
                ps.setString(2, lecturerId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void updateLecturerWage(String lecturerName, int newWage) throws CollegeException {
        if (newWage < 0) {
            throw new InputException("wage is under 0");
        }
        try (Connection con = Database.getConnection()) {
            String lecturerId = findLecturerIdByName(con, lecturerName);
            if (lecturerId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE lecturer SET wage = ? WHERE lecturer_id = ?")) {
                ps.setInt(1, newWage);
                ps.setString(2, lecturerId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void removeLecturerFromCommittee(String committeeName, String lecturerName)
            throws CollegeException {
        try (Connection con = Database.getConnection()) {
            Integer committeeId = findCommitteeIdByName(con, committeeName);
            if (committeeId == null) {
                throw new NotExistException("The Committees Does Not exist");
            }
            String lecturerId = findLecturerIdByName(con, lecturerName);
            if (lecturerId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM committee_member WHERE committee_id = ? AND lecturer_id = ?")) {
                ps.setInt(1, committeeId);
                ps.setString(2, lecturerId);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new NotInCommitteeException();
                }
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public void deleteLecturer(String lecturerName) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            String lecturerId = findLecturerIdByName(con, lecturerName);
            if (lecturerId == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT 1 FROM committee WHERE chairman_id = ?")) {
                ps.setString(1, lecturerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new ConditionsExceptions(
                                "Cannot delete a lecturer who is a committee chairman");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM lecturer WHERE lecturer_id = ?")) {
                ps.setString(1, lecturerId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public double showAverageOfSalaries() throws CollegeException {
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT AVG(wage) AS avg_wage, COUNT(*) AS cnt FROM lecturer");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            if (rs.getInt("cnt") == 0) {
                throw new ExistanceException("There are no lecturers");
            }
            return rs.getDouble("avg_wage");
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public double showAverageOfSalariesByDepart(String departmentName) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            Integer departmentId = findDepartmentIdByName(con, departmentName);
            if (departmentId == null) {
                throw new ExistanceException("The study department does not exist");
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT AVG(wage) AS avg_wage, COUNT(*) AS cnt " +
                    "FROM lecturer WHERE department_id = ?")) {
                ps.setInt(1, departmentId);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt("cnt") == 0) {
                        throw new EmptyDepartmentException(
                                "There are no lecturers in that department");
                    }
                    return rs.getDouble("avg_wage");
                }
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    public String displayAllLecturers() throws CollegeException {
        StringBuilder sb = new StringBuilder();
        String sql =
                "SELECT l.lecturer_id, l.name, l.name_of_degree, l.degree_level, l.wage, " +
                "       l.institution, d.name AS dept_name " +
                "FROM lecturer l " +
                "LEFT JOIN department d ON l.department_id = d.department_id " +
                "ORDER BY l.name";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String lecturerId = rs.getString("lecturer_id");
                sb.append("Name: '").append(rs.getString("name")).append('\'')
                  .append(", Id: ").append(lecturerId)
                  .append(", Name Of Degree: '").append(rs.getString("name_of_degree")).append('\'')
                  .append(", Level Of Degree: '").append(rs.getString("degree_level")).append('\'')
                  .append(", Wage: ").append(rs.getInt("wage"));
                String dept = rs.getString("dept_name");
                sb.append(", Department: '").append(dept == null ? "No Department" : dept).append('\'');
                sb.append(", Committees: ").append(committeesOfLecturer(con, lecturerId));

                String degree = rs.getString("degree_level");
                if (degree.equals("Doctor") || degree.equals("Professor")) {
                    sb.append(", Articles: ").append(articlesOfLecturer(con, lecturerId));
                }
                if (degree.equals("Professor")) {
                    sb.append(", Institution: ").append(rs.getString("institution"));
                }
                sb.append('\n');
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
        return sb.length() == 0 ? "No lecturers." : sb.toString();
    }

    public String displayAllCommittees() throws CollegeException {
        StringBuilder sb = new StringBuilder();
        String sql =
                "SELECT c.committee_id, c.name, c.level_of_degree, l.name AS chairman_name " +
                "FROM committee c " +
                "JOIN lecturer l ON c.chairman_id = l.lecturer_id " +
                "ORDER BY c.name";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int committeeId = rs.getInt("committee_id");
                String members = membersOfCommittee(con, committeeId);
                sb.append("{Name: '").append(rs.getString("name")).append('\'')
                  .append(", Level: '").append(rs.getString("level_of_degree")).append('\'')
                  .append(", Chairman: ").append(rs.getString("chairman_name"))
                  .append(", Members: ").append(members)
                  .append("}\n");
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
        return sb.length() == 0 ? "No committees." : sb.toString();
    }

    public String searchLecturerByName(String lecturerName) throws CollegeException {
        String sql =
                "SELECT l.lecturer_id, l.name, l.name_of_degree, l.degree_level, l.wage, " +
                "       l.institution, d.name AS dept_name " +
                "FROM lecturer l " +
                "LEFT JOIN department d ON l.department_id = d.department_id " +
                "WHERE l.name = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lecturerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new NotExistException("The Lecturer Does Not exist");
                }
                String lecturerId = rs.getString("lecturer_id");
                StringBuilder sb = new StringBuilder();
                sb.append("Name: '").append(rs.getString("name")).append('\'')
                  .append(", Id: ").append(lecturerId)
                  .append(", Name Of Degree: '").append(rs.getString("name_of_degree")).append('\'')
                  .append(", Level Of Degree: '").append(rs.getString("degree_level")).append('\'')
                  .append(", Wage: ").append(rs.getInt("wage"));
                String dept = rs.getString("dept_name");
                sb.append(", Department: '").append(dept == null ? "No Department" : dept).append('\'');
                sb.append(", Committees: ").append(committeesOfLecturer(con, lecturerId));
                String degree = rs.getString("degree_level");
                if (degree.equals("Doctor") || degree.equals("Professor")) {
                    sb.append(", Articles: ").append(articlesOfLecturer(con, lecturerId));
                }
                if (degree.equals("Professor")) {
                    sb.append(", Institution: ").append(rs.getString("institution"));
                }
                return sb.toString();
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    private String committeesOfLecturer(Connection con, String lecturerId) throws SQLException {
        StringBuilder sb = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT c.name FROM committee c " +
                "JOIN committee_member cm ON c.committee_id = cm.committee_id " +
                "WHERE cm.lecturer_id = ? ORDER BY c.name")) {
            ps.setString(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(", ");
                    sb.append(rs.getString(1));
                    first = false;
                }
            }
        }
        return sb.append("]").toString();
    }

    private String articlesOfLecturer(Connection con, String lecturerId) throws SQLException {
        StringBuilder sb = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT title FROM article WHERE lecturer_id = ? ORDER BY article_id")) {
            ps.setString(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(", ");
                    sb.append(rs.getString(1));
                    first = false;
                }
            }
        }
        return sb.append("]").toString();
    }

    private String membersOfCommittee(Connection con, int committeeId) throws SQLException {
        StringBuilder sb = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT l.name FROM lecturer l " +
                "JOIN committee_member cm ON l.lecturer_id = cm.lecturer_id " +
                "WHERE cm.committee_id = ? ORDER BY l.name")) {
            ps.setInt(1, committeeId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(", ");
                    sb.append(rs.getString(1));
                    first = false;
                }
            }
        }
        return sb.append("]").toString();
    }

    public void duplicateCommittee(String committeeName) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                Integer committeeId = findCommitteeIdByName(con, committeeName);
                if (committeeId == null) {
                    throw new NotExistException("Committee do not exist");
                }
                String level, chairmanId;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT level_of_degree, chairman_id FROM committee WHERE committee_id = ?")) {
                    ps.setInt(1, committeeId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        level = rs.getString("level_of_degree");
                        chairmanId = rs.getString("chairman_id");
                    }
                }
                int newId;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO committee (name, level_of_degree, chairman_id) " +
                        "VALUES (?, ?, ?) RETURNING committee_id")) {
                    ps.setString(1, "new-" + committeeName);
                    ps.setString(2, level);
                    ps.setString(3, chairmanId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        newId = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO committee_member (committee_id, lecturer_id) " +
                        "SELECT ?, lecturer_id FROM committee_member WHERE committee_id = ?")) {
                    ps.setInt(1, newId);
                    ps.setInt(2, committeeId);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException | CollegeException e) {
                con.rollback();
                throw e instanceof CollegeException ? (CollegeException) e
                        : new CollegeException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    private int articleCount(Connection con, String lecturerId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM article WHERE lecturer_id = ?")) {
            ps.setString(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public int compareProDoc(String first, String second) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            String id1 = findLecturerIdByName(con, first);
            String id2 = findLecturerIdByName(con, second);
            if (id1 == null || id2 == null) {
                throw new NotExistException("The Lecturer Does Not exist");
            }
            return Integer.compare(articleCount(con, id1), articleCount(con, id2));
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }

    private int committeeMemberCount(Connection con, int committeeId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM committee_member WHERE committee_id = ?")) {
            ps.setInt(1, committeeId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private int committeeArticleSum(Connection con, int committeeId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM article a " +
                "JOIN committee_member cm ON a.lecturer_id = cm.lecturer_id " +
                "WHERE cm.committee_id = ?")) {
            ps.setInt(1, committeeId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public int compareCommittees(String first, String second) throws CollegeException {
        try (Connection con = Database.getConnection()) {
            Integer id1 = findCommitteeIdByName(con, first);
            Integer id2 = findCommitteeIdByName(con, second);
            if (id1 == null || id2 == null) {
                throw new NotExistException("The committee Does Not exist");
            }
            int res = Integer.compare(committeeMemberCount(con, id1), committeeMemberCount(con, id2));
            if (res == 0) {
                res = Integer.compare(committeeArticleSum(con, id1), committeeArticleSum(con, id2));
            }
            return res;
        } catch (SQLException e) {
            throw new CollegeException(e.getMessage());
        }
    }
}
