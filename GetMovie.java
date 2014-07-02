// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// GetMovie.java - Movie information display page. Fetches related information, including genres and stars featured
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

public class GetMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Connect to database
    	String loginUser = "testuser";
        String loginPasswd = "testpass";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Fabflix Movie Page</title><head>" +
        		"<body bgcolor=\"#FDF5E6\">" +
        		"<h1 align=\"center\">Fabflix: Movie Page</h1>" +
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
           
           String query = "SELECT * FROM movies WHERE id ='"+id+"'";

           // Perform the authentication query
           ResultSet rs = statement.executeQuery(query);
           ResultSet rs2; // ResultSet for individual movie
           
           // Create and print table
           out.println("<br><TABLE border>" +
        		"<tr>" +
                "<td>ID</td>" +
                "<td>Title</td>" +
                "<td>Year</td>" +
                "<td>Director</td>" +
                "<td>Banner Image</td>" +
                "<td>Genre</td>" +
                "<td>Stars</td>" +
                "<td>Trailer</td>" +
                "<td>Add to Cart</td>" +
                "</tr>");
           
           rs.next();
           String m_ID = rs.getString("id");
           String m_TT = rs.getString("title");
           String m_YR = rs.getString("year");
           String m_DR = rs.getString("director");
           String m_BU = rs.getString("banner_url");
           String m_GN = "";
           String m_ST = "";
           String m_TR = rs.getString("trailer_url");
           
           // Search for genres of movie
           query = "SELECT name FROM (SELECT genre_id FROM genres_in_movies WHERE movie_id = '"+m_ID+
        		   "') a INNER JOIN (SELECT * FROM genres) b ON a.genre_id = b.id";
           rs2 = statement2.executeQuery(query);
           while(rs2.next()){
        	   m_GN += rs2.getString("name")+", ";
           }
           
           // Search for stars of movie
           query = "SELECT first_name, last_name FROM (SELECT star_id FROM stars_in_movies WHERE movie_id = '"+m_ID+
        		   "') a INNER JOIN (SELECT * FROM stars) b ON a.star_id = b.id";
           rs2 = statement2.executeQuery(query);
           
           // For each star, create a query for the id and hyperlink it
           while(rs2.next()){
        	   Statement statement3 = dbcon.createStatement();
        	   query = "SELECT id FROM stars WHERE first_name = '"+rs2.getString("first_name")+"' AND last_name = '"+rs2.getString("last_name")+"'";
        	   ResultSet rs3 = statement3.executeQuery(query);
        	   rs3.next();
        	   m_ST += "<a href=\"/Fabflix/servlet/GetStar?id="+rs3.getString("id")+"\">"+rs2.getString("first_name")+" "+rs2.getString("last_name")+"</a>, ";
           }
           
           out.println("<tr>" +
                       "<td>" + m_ID + "</td>" +
                       "<td><a href=\"/Fabflix/servlet/GetMovie?id="+m_ID+"\">" + m_TT + "</a></td>" +
                       "<td>" + m_YR + "</td>" +
                       "<td>" + m_DR + "</td>" +
                       "<td><img src=\"" + m_BU + "\" alt=\"Image not available\"></td>" +
                       "<td>" + m_GN + "</td>" +
                       "<td>" + m_ST + "</td>" +
                       "<td>" + m_TR + "</td>" +
                       "<td><a href=\"/Fabflix/servlet/Cart?add=add&qty=1&mid=" + m_ID + "\">Add to Cart</td>" +
                       "</tr>");
       
           out.println("</TABLE>" + 
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
