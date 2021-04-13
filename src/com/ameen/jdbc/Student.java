package com.ameen.jdbc;

public class Student {

	//--------Fields--------
	private int id;
	private String fname;
	private String lname;
	private String email;
	
	
	//--------Constructors--------
	public Student(int id, String fname, String lname, String email) {
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
	}
	
	public Student(String fname, String lname, String email) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
	}

	//--------To String Method--------
	@Override
	public String toString() {
		return ("Student ID: " + this.getId() + "\n" + 
				"Student Name: " + this.getFname() + " " + getLname() + "\n" +
				"Student E-mail Address: " + this.getEmail()
		);
	}
	
	//--------Getters and Setters--------
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
