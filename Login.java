// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014

// Login menu, where users input login information
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        String heading = "Header";
        
        //Check for incorrect login
        String log = request.getParameter("log");
        if (log == null) {
        	heading = "Welcome, Newcomer";
        } else {
        	heading = "Incorrect Login. Please Try again<br></center>";
        }
        
        out.println("<html><head>" +
        		"<title>Fabflix Login Page:</title><head>" +
        		"<body bgcolor=\"FDF5E6\">" +
        		"<h1 align=\"center\">Welcome to Fabflix! Please log in:</h1>" +
        		"<h2 align=\"center\">"+heading+"</h2>" +
        		"<form action=\"/Fabflix/servlet/LoginScreen\" method=\"post\">" +
        		"<center>E-Mail: <input type=\"text\" name=\"email\"><br>" +
        		"Password: <input type=\"password\" name=\"password\"><br>" +
        		"<input type=\"submit\" value=\"Login\"></center>" +
        		"</form></body></html>");
	}
	
	// Use http GET
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
