// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// Checkout.java - Verifies inputed credit card information to confirm checkout
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckoutAuthenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// Use http POST
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Connect to database
    	String loginUser = "testuser";
        String loginPasswd = "testpass";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();        
        
        try
        {
           //Class.forName("org.gjt.mm.mysql.Driver");
           Class.forName("com.mysql.jdbc.Driver").newInstance();

           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
           Statement statement = dbcon.createStatement();

           String creditCard = request.getParameter("creditCard");
           String expDate = request.getParameter("expDate");
           String firstName = request.getParameter("firstName");
           String lastName = request.getParameter("lastName");
           
           String query = "SELECT * FROM creditcards WHERE id='"+creditCard+"' AND first_name='"+firstName+"' AND last_name='"+lastName+"' AND expiration='"+expDate+"'";

           // Perform the authentication query
           ResultSet rs = statement.executeQuery(query);
           
           // If valid credit card information, display completed sale confirmation page
           if(rs.next()){
        	   response.sendRedirect("/Fabflix/servlet/CompleteSale");
           }
           // If not redirect to checkout page and prompt the user to retry
           else{
        	   response.sendRedirect("/Fabflix/servlet/Checkout?&check=f");
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
