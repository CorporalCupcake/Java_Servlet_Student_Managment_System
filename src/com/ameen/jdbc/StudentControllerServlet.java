package com.ameen.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServer
 */
@WebServlet(name = "StudentControllerServlet", urlPatterns = { "/StudentControllerServlet" })
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDBUtil dbUtil;
	
	@Resource(name = "jdbc/JdbcChadDarby")
	private DataSource dataSource;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		dbUtil = new StudentDBUtil(dataSource);
		
		try {
			
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}



	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			String theCommand = request.getParameter("command");
			
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			switch(theCommand) {
			
			case "LIST":
				listStudents(request, response);
				break;
				
			case "ADD":
				addStudent(request, response);
				break;
				
			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
							
			default:
				listStudents(request, response);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			// read student info from form data
			int id = Integer.parseInt(request.getParameter("studentID"));
			String firstName = request.getParameter("fname");
			String lastName = request.getParameter("lname");
			String email = request.getParameter("email");
			
			// create a new student object
			Student theStudent = new Student(id, firstName, lastName, email);
			
			// perform update on database
			dbUtil.updateStudent(theStudent);
			
			// send them back to the "list students" page
			listStudents(request, response);
			
		}
	
	
	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

			// read student id from form data
			String theStudentId = request.getParameter("studentID");
			
			// get student from database (db util)
			Student theStudent = dbUtil.getStudent(theStudentId);
			
			// place student in the request attribute
			request.setAttribute("THE_STUDENT", theStudent);
			
			// send to jsp page: update-student-form.jsp
			RequestDispatcher dispatcher = 
					request.getRequestDispatcher("/update-student-form.jsp");
			dispatcher.forward(request, response);		
		}
	
	private void addStudent(HttpServletRequest request, HttpServletResponse response) {
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(fname, lname, email);
		
		dbUtil.addStudent(theStudent);
		
		try {
			listStudents(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}





	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Student> studentList = dbUtil.getStudents();
		
		request.setAttribute("STUDENT_LIST", studentList);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		
		dispatcher.forward(request, response);
	}

}
