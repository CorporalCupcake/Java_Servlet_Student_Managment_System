package com.ameen.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	@Resource(name="jdbc/JdbcChadDarby")
	private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//Declaring all our SQL elements
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			//Get a connection to the database
			connection = dataSource.getConnection();
			
			//Create an SQL statement
			String sql = "SELECT * FROM Students";
			
			statement = connection.createStatement();
			
			//Execute the statement
			resultSet = statement.executeQuery(sql);
			
			//Process the result set
			while(resultSet.next()) {
				String email = resultSet.getString("email");
				out.println(email);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
