// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// Checkout.java - Credit card information to be verified is entered here
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        // Print header
        out.println("<html><head>" +
        		"<title>Fabflix Checkout Page</title><head>" +
        		"<body bgcolor=\"FDF5E6\">" +
        		"<center><h1 align=\"center\">Fabflix: Checkout Page</h1>" +
        		"<a href=\"/Fabflix/main.html\">Back to Browse</a> | "+
        		"<a href=\"/Fabflix/search.html\">Search For a Movie</a> | "+
        		"<a href=\"/Fabflix/servlet/Cart\">My Cart</a> | " +
        		"<a href=\"/Fabflix/servlet/LogOut\">Log Out</a><br>");
        
        // If sent here from invalid credit card information, display retry message
        if(request.getParameter("check")!=null){
        	out.println("<h3>Incorrect credit card info. Please retry.</h3>");
        }
        
        // Print forms
        out.println("<form action=\"/Fabflix/servlet/CheckoutAuthenticate\" method=\"post\">" +
        		"Credit Card Number: <input type=\"text\" name=\"creditCard\"><br>" +
        		"Expiration Date: <input type=\"text\" name=\"expDate\"><br>" +
        		"First Name: <input type=\"text\" name=\"firstName\"><br>" +
        		"Last Name: <input type=\"text\" name=\"lastName\"><br>" +
        		"<input type=\"submit\" value=\"Checkout\"><br>" +
        		"<a href=\"javascript:history.go(-1)\">Go back</a></center>" +
        		"</form></body></html>");
	}
	
	// Use http GET
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
