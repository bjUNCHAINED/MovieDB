// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// LoginScreen.java - Login authenticator, screens unauthorized users from entering the main site
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginScreen extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http POST
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    	doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	// Connect to database
    	String loginUser = "testuser";
        String loginPasswd = "testpass";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type
        HttpSession session = request.getSession();
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();        
        
        try
        {
           //Class.forName("org.gjt.mm.mysql.Driver");
           Class.forName("com.mysql.jdbc.Driver").newInstance();

           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
           Statement statement = dbcon.createStatement();

           String email = request.getParameter("email");
           String password = request.getParameter("password");
           String query = "SELECT * FROM customers WHERE email ='"+email+"' AND password ='"+password+"'";

           // Perform the authentication query, redirect user as necessary
           ResultSet rs = statement.executeQuery(query);
           
           if(rs.next()){
        	   session.setAttribute("customerId", rs.getString("id"));
        	   response.sendRedirect("/Fabflix/main.html");
           }
           else{
        	   response.sendRedirect("/Fabflix/servlet/Login?log=f");
           }
           
           rs.close();
           statement.close();
           dbcon.close();
       }
        
       catch (SQLException ex) {
           while (ex != null) {
                 System.out.println ("SQL Exception:  " + ex.getMessage ());
                 ex = ex.getNextException ();
             }
       }

       catch(java.lang.Exception ex) {
             out.println("<HTML>" +
                         "<HEAD><TITLE>" +
                         "MovieDB: Error" +
                         "</TITLE></HEAD>\n<BODY>" +
                         "<P>SQL error in doPost: " +
                         ex.getMessage() + "</P></BODY></HTML>");
             return;
      }
      out.close();
        
    }
}