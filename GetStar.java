// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// GetMovie.java - Star information display page. Fetches related information, including movies that the star was in
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

/**
 * Servlet implementation class GetStar
 */
public class GetStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Connect to database
    	String loginUser = "testuser";
        String loginPasswd = "testpass";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Fabflix Star Page</title><head>" +
        		"<body bgcolor=\"#FDF5E6\">" +
        		"<h1 align=\"center\">Fabflix: Star Page</h1>" +
        		"<center>" +
        		"<a href=\"/Fabflix/main.html\">Back to Browse</a> | "+
        		"<a href=\"/Fabflix/search.html\">Search For a Movie</a> | "+
        		"<a href=\"/Fabflix/servlet/Cart\">My Cart</a> | " +
        		"<a href=\"/Fabflix/servlet/LogOut\">Log Out</a><br>");
        
        try
        {
           // Setup
           Class.forName("com.mysql.jdbc.Driver").newInstance();
           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           Statement statement = dbcon.createStatement();
           Statement statement2 = dbcon.createStatement();

           String id = request.getParameter("id");
           
           String query = "SELECT * FROM stars WHERE id ='"+id+"'";

           // Perform the authentication query
           ResultSet rs = statement.executeQuery(query);
           ResultSet rs2; // ResultSet for individual movie
           
           // Create and print table
           out.println("<br><TABLE border>" +
        		"<tr>" +
                "<td>ID</td>" +
                "<td>Star Name</td>" +
                "<td>Date of Birth</td>" +
                "<td>Starred in</td>" +
                "<td>Picture</td>" +
                "</tr>");
           
           rs.next();
           String m_ID = rs.getString("id");
           String m_FN = rs.getString("first_name");
           String m_LN = rs.getString("last_name");
           String m_DB = rs.getString("dob");
           String m_SI = "";
           String m_PC = rs.getString("photo_url");
           
           // Search for movies that the star was in and display them
           query = "SELECT b.id, b.title FROM (SELECT movie_id FROM stars_in_movies WHERE star_id = '"+m_ID+
        		   "') a INNER JOIN (SELECT * FROM movies) b ON a.movie_id = b.id";
           rs2 = statement2.executeQuery(query);
           while(rs2.next()){
        	   m_SI += "<a href=\"/Fabflix/servlet/GetMovie?id="+rs2.getString("id")+"\">" + rs2.getString("title") + "</a>, ";
           }
           
           out.println("<tr>" +
                       "<td>" + m_ID + "</td>" +
                       "<td>" + m_FN + " " + m_LN + "</td>" +
                       "<td>" + m_DB + "</td>" +
                       "<td>" + m_SI + "</td>" +
                       "<td><img src=\"" + m_PC + "\" alt=\"Image not available\"></td>" +
                       "</tr>" +
                       "</TABLE>" + 
                       "<a href=\"javascript:history.go(-1)\">Go back</a>" + 
        		   	   "</center></html>");
      
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
	
	// Use http GET
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
