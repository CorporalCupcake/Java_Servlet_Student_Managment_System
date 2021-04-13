package com.ameen.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


/**
 * class StudentDBUtil
 * 
 * This class contains all the function we would need to connect to the Student Database and 
 * parse it.
 * 
 * @author Ameen Ayub
 *
 */
public class StudentDBUtil {

	/* -------- Fields -------- */
	private DataSource dataSource;

	/* -------- Constructor -------- */
	public StudentDBUtil(DataSource datasource) {
		this.dataSource = datasource;
	}
	
	/* -------- Utility Functions -------- */
	
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();   // doesn't really close it ... just puts back in connection pool
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * List<Student> getStudents()
	 * 
	 * This function connects to the DB and creates a List of Students.
	 * 
	 * @return List of type Student
	 * @throws Exception
	 */
	public List<Student> getStudents() throws Exception {
		
		List<Student> students = new ArrayList<>();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			
			/*Make the DB use our DB*/
			
			
			
			/* Get a connection to the database */
			connection = dataSource.getConnection();
			
			/* Create an SQL statement */
			String sql = "SELECT * FROM Students ORDER BY lname DESC;";
			
			statement = connection.createStatement();
			
			/* Execute the statement */
			statement.executeQuery("USE JdbcChadDarby;");
			
			resultSet = statement.executeQuery(sql);
			
			/* Process the result set */
			while(resultSet.next()) {
				Student tempStudent = new Student( 
											resultSet.getInt("id"),
											resultSet.getString("fname"),
											resultSet.getString("lname"),
											resultSet.getString("email")
												);
				
				students.add(tempStudent);
						
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			
		} finally {
			
			/* We close a our connection, statement and result set */
			try {
				if(resultSet != null)
					resultSet.close();
				
				if(connection != null)
					connection.close(); /* Makes the connection available for someone else to use */
				
				if(statement != null)
					statement.close();
				
			} catch (Exception exception) {
				exception.printStackTrace();
				
			}
		}
		
		return students;
	}

	public void addStudent(Student theStudent) {
		Connection myConn = null;
		

		PreparedStatement myStmt = null;
		
		
		try {
			
			myConn = dataSource.getConnection();
			
			String sql = "INSERT INTO JdbcChadDarby.Students (fname, lname, email) VALUES (?,?,?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, theStudent.getFname());
			myStmt.setString(2, theStudent.getLname());
			myStmt.setString(3, theStudent.getEmail());
			
			myStmt.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				myConn.close();
				myStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public Student getStudent(String theStudentId) throws Exception {

		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "select * from student where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("fname");
				String lastName = myRs.getString("lname");
				String email = myRs.getString("email");
				
				// use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + studentId);
			}				
			
			return theStudent;
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFname());
			myStmt.setString(2, theStudent.getLname());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}
	
	
}
