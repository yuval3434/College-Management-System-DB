# **College Management System \- Database Integration 🎓**

## **About The Project**

This project represents the architectural evolution of a Java Object-Oriented application. Originally built using in-memory data structures and local file storage, the system was refactored and upgraded to use a **robust, fully normalized PostgreSQL relational database**.

The primary goal of this project was to demonstrate database design, data integrity, and backend integration using **JDBC**.

## **🚀 Key Features**

* **Database Migration:** Successfully replaced local array/file storage with a persistent relational database.  
* **Normalized Architecture:** Designed a 3NF (Third Normal Form) database schema to eliminate data redundancy and ensure data integrity.  
* **Full CRUD Operations:** Java backend supports Create, Read, Update, and Delete operations directly against the database via JDBC.  
* **Complex SQL Queries:** Implemented over 10 meaningful SQL queries, utilizing JOINs, GROUP BY, aggregations, and subqueries.  
* **Data Integrity:** Enforced strict Primary Keys, Foreign Keys, and constraints across 5 interrelated entities.  
* **\[Optional \- if you did the bonus\] Database Triggers:** Implemented SQL triggers to automate backend logic directly within the DB layer.

## **🛠️ Tech Stack**

* **Language:** Java (JDK 17+)  
* **Database:** PostgreSQL  
* **Connectivity:** JDBC (Java Database Connectivity)  
* **Design/Modeling:** ERD & Relational Schema Design

## **📊 Database Architecture (ERD)**

The database was carefully planned and normalized into 5 entities to handle College Departments, Lecturers, Committees, and Articles.

*Note the handling of the Many-to-Many relationship between Committees and Lecturers via the Committee\_Members junction table.*

*(👈 **Note to developer:** Export your ERD image from draw.io, name it erd.png, upload it to your repo, and it will appear here\!)*

## **💡 Highlighted Code: Advanced SQL Integration**

Instead of iterating through arrays in Java, operations are now optimized at the database level. Here is an example of calculating the average salary of lecturers in a specific department using SQL Aggregation:

// Example: Fetching Average Salary by Department securely using PreparedStatement  
public double showAverageOfSalariesByDepart(String departmentName) throws SQLException {  
    String query \= "SELECT AVG(wage) AS avg\_salary " \+  
                   "FROM LECTURER l " \+  
                   "JOIN DEPARTMENT d ON l.department\_id \= d.department\_id " \+  
                   "WHERE d.name \= ?";  
                     
    try (Connection conn \= DatabaseManager.getConnection();  
         PreparedStatement pstmt \= conn.prepareStatement(query)) {  
           
        pstmt.setString(1, departmentName);  
        ResultSet rs \= pstmt.executeQuery();  
          
        if (rs.next()) {  
            return rs.getDouble("avg\_salary");  
        }  
    }  
    return 0.0;  
}

## **⚙️ Setup and Installation**

1. **Clone the repository:**  
   git clone https://github.com/yuval3434/College-Management-System-DB.git

2. **Database Setup:**  
   * Open pgAdmin or your preferred PostgreSQL client.  
   * Run the database\_setup.sql script provided in the repository to create the schema, tables, and insert dummy data.  
3. **Configure Database Credentials:**  
   * Update the DB connection string, username, and password in the Utils.java (or your DB Manager class).  
4. **Run the Application:**  
   * Run Main.java in your preferred IDE (IntelliJ / Eclipse).
