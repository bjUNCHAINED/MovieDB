// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// Search.java - Movie Search Engine, supports searching by title, year, director, genres, featured stars, or any combination of listed
// 				 categories. Also handles sorting by title or year, and pagination.
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

public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Connect to database
    	String loginUser = "testuser";
        String loginPasswd = "testpass";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        boolean multiSearch = false;
        boolean multiSearch2 = false;

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Fabflix Search Results</title><head>" +
        		"<body bgcolor=\"#FDF5E6\">" +
        		"<h1 align=\"center\">Fabflix: Search Results</h1>" +
        		"<center>" +
        		"<a href=\"/Fabflix/main.html\">Back to Browse</a> | "+
        		"<a href=\"/Fabflix/search.html\">Search For a Movie</a> | "+
        		"<a href=\"/Fabflix/servlet/Cart\">My Cart</a> | " +
        		"<a href=\"/Fabflix/servlet/LogOut\">Log Out</a><br>");
        
        try
        {
           //Class.forName("org.gjt.mm.mysql.Driver");
           Class.forName("com.mysql.jdbc.Driver").newInstance();

           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
           Statement statement = dbcon.createStatement();
           Statement statement2 = dbcon.createStatement();
           
           String type = request.getParameter("type");
           
           String title = "";
           String year = "";
           String director = "";
           String genre = "";
           String firstName = "";
           String lastName = "";
           String sortBy = request.getParameter("sortBy");
           String page = request.getParameter("page");
           String rpp = request.getParameter("rpp");
           
           if (type.equals("search")){
        	   title = request.getParameter("title");
               year = request.getParameter("year");
               director = request.getParameter("director");
               firstName = request.getParameter("firstName");
               lastName = request.getParameter("lastName");
           }
           else if (type.equals("genre")){
        	   genre = request.getParameter("genre");
           }
           else if (type.equals("title")){
        	   title = request.getParameter("title");
           }
           
           String sort = "";							// sort to go in SQL
           
           if(page == null) page = "0";
           if(rpp == null) rpp = "5";
           
           String element = Integer.toString(Integer.parseInt(page)*Integer.parseInt(rpp));
           
           // Parse sortBy into SQL ORDER BY
           if(sortBy == null || sortBy.equals("tAsc")){
        	   sortBy = "tAsc";
        	   sort = "title ASC";
           }
           else if(sortBy.equals("tDsc")) sort = "title DESC";
           else if(sortBy.equals("yAsc")) sort = "year ASC";
           else if(sortBy.equals("yDsc")) sort = "year DESC";
           
           
           // Sort functions, selected depending on type of search selected
           if (type.equals("search")){
        	   out.println("Sort by : " +
           		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+"&sortBy=tAsc"+"&page="+page+"&rpp="+rpp+"\">Title (asc)</a> | " +
           		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+"&sortBy=tDsc"+"&page="+page+"&rpp="+rpp+"\">Title (dsc)</a> | " +
           		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+"&sortBy=yAsc"+"&page="+page+"&rpp="+rpp+"\">Year (asc)</a> | " +
           		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+"&sortBy=yDsc"+"&page="+page+"&rpp="+rpp+"\">Year (dsc)</a>");
           }
           else if (type.equals("genre")){
        	   out.println("Sort by : " +
                "<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+"&sortBy=tAsc&page="+page+"&rpp="+rpp+"\">Title (asc)</a> | " +
                "<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+"&sortBy=tDsc&page="+page+"&rpp="+rpp+"\">Title (dsc)</a> | " +
                "<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+"&sortBy=yAsc&page="+page+"&rpp="+rpp+"\">Year (asc)</a> | " +
                "<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+"&sortBy=yDsc&page="+page+"&rpp="+rpp+"\">Year (dsc)</a>");
           }
           else if (type.equals("title")){
        	   out.println("Sort by : " +
                "<a href=\"/Fabflix/servlet/Search?type=title&title="+title+"&sortBy=tAsc&page="+page+"&rpp="+rpp+"\">Title (asc)</a> | " +
                "<a href=\"/Fabflix/servlet/Search?type=title&title="+title+"&sortBy=tDsc&page="+page+"&rpp="+rpp+"\">Title (dsc)</a> | " +
                "<a href=\"/Fabflix/servlet/Search?type=title&title="+title+"&sortBy=yAsc&page="+page+"&rpp="+rpp+"\">Year (asc)</a> | " +
                "<a href=\"/Fabflix/servlet/Search?type=title&title="+title+"&sortBy=yDsc&page="+page+"&rpp="+rpp+"\">Year (dsc)</a>");
           }
           
           String query = "SELECT * FROM movies ";
           String query2 = "SELECT movie_id FROM stars INNER JOIN stars_in_movies ON stars.id = stars_in_movies.star_id ";
           
           // Directly search movies
           if(!title.equals("")){
        	   if(type.equals("search"))
        		   query += "WHERE title LIKE '%"+title+"%'";
        	   else if(type.equals("title"))
        		   query += "WHERE title LIKE '"+title+"%'";
        	   multiSearch = true;
           }
           if(!year.equals("")){
        	   if(multiSearch == true){
        		   query += " AND";
        	   }
        	   else {
        		   multiSearch = true;
        		   query += " WHERE";
        	   }
        	   query += " year = '"+year+"'";
           }
           if(!director.equals("")){
        	   if(multiSearch == true){
        		   query += " AND";
        	   }
        	   else {
        		   multiSearch = true;
        		   query += " WHERE";
        	   }
        	   query += " director LIKE '%"+director+"%'";
           }
           if(!genre.equals("")){
        	   multiSearch = true;
        	   query = "SELECT * FROM (SELECT movie_id FROM genres_in_movies WHERE genre_id = '"+genre+
        			   "') a INNER JOIN (SELECT * FROM movies) b ON a.movie_id = b.id ";
           }
           
           // Search Stars with first and last name
           if(!firstName.equals("")){
        	   query2 += "AND stars.first_Name LIKE '%"+firstName+"%'";
        	   multiSearch2 = true;
           }           
           if(!lastName.equals("")){
        	   query2 += "AND stars.last_Name LIKE '%"+lastName+"%'";
        	   multiSearch2 = true;
           }
           
           // Combine Movies and Stars query
           if(multiSearch){
        	   if(multiSearch2){
        		   query = "SELECT * from ("+query+") a INNER JOIN ("+query2+") b ON a.id = b.movie_id ORDER BY "+sort+" LIMIT "+element+", "+rpp;
        	   }
        	   else{
        		   query += "ORDER BY "+sort+" LIMIT "+element+", "+rpp;
        	   }
           }
           else{
        	   if(multiSearch2){
        		   query = "SELECT * from ("+query+") a INNER JOIN ("+query2+") b ON a.id = b.movie_id ORDER BY "+sort+" LIMIT "+element+", "+rpp;
        	   }
        	   else {
        		   query = "";
        	   }
           }
           
           // Perform the authentication query
           ResultSet rs = statement.executeQuery(query);
           ResultSet rs2; // ResultSet for individual movie

           // Create and print table
           out.println("<TABLE border>" +
        		"<tr>" +
                "<td>ID</td>" +
                "<td>Title</td>" +
                "<td>Year</td>" +
                "<td>Director</td>" +
                "<td>Banner Image</td>" +
                "<td>Genre</td>" +
                "<td>Stars</td>" +
                "<td>Add to Cart</td>"+
                "</tr>");
           
           while(rs.next()){
        	   String m_ID = rs.getString("id");
               String m_TT = rs.getString("title");
               String m_YR = rs.getString("year");
               String m_DR = rs.getString("director");
               String m_BU = rs.getString("banner_url");
               String m_GN = "";
               String m_ST = "";
               
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
                           "<td><a href=\"/Fabflix/servlet/Cart?add=add&qty=1&mid=" + m_ID + "\">Add to Cart</td>" +
                           "</tr>");
           }
           
           out.println("</TABLE>");
           
           // Pagination for different types of searches
           if(type.equals("search")){
        	   if (page.equals("0")){
        		   out.println("<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)+1)+"&rpp="+rpp+"\">Next Page</a> | " + 
		        		   "Previous Page");
        	   }
        	   else{
		           out.println("<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)+1)+"&rpp="+rpp+"\">Next Page</a> | " + 
		        		   "<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)-1)+"&rpp="+rpp+"\">Previous Page</a>");
        	   }
	           
	           out.println("Results Per Page: " +
	        		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=5\">5</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=10\">10</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=15\">15</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=20\">20</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=search&title="+title+"&year="+year+"&director="+director+"&firstName="+firstName+"&lastName="+lastName+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=25\">25</a>");
           }
           else if(type.equals("genre")){
        	   if (page.equals("0")){
        		   out.println("<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)+1)+"&rpp="+rpp+"\">Next Page</a> | " + 
		        		   "Previous Page");
        	   }
        	   else{
	        	   out.println("<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)+1)+"&rpp="+rpp+"\">Next Page</a> | " + 
		        		   "<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)-1)+"&rpp="+rpp+"\">Previous Page</a>");
        	   }
	           
	           out.println("Results Per Page: " +
	        		"<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=5\">5</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=10\">10</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=15\">15</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=20\">20</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=genre&genre="+genre+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=25\">25</a> | ");
           }
           else if(type.equals("title")){
        	   if (page.equals("0")){
        		   out.println("<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)+1)+"&rpp="+rpp+"\">Next Page</a> | " + 
		        		   "Previous Page");
        	   }
        	   else{
	        	   out.println("<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)+1)+"&rpp="+rpp+"\">Next Page</a> | " + 
		        		   "<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
		        		   "&sortBy="+sortBy+"&page="+(Integer.parseInt(page)-1)+"&rpp="+rpp+"\">Previous Page</a>");
        	   }
	           
	           out.println("Results Per Page: " +
	        		"<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=5\">5</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=10\">10</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=15\">15</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=20\">20</a> | " +
	        		"<a href=\"/Fabflix/servlet/Search?type=title&title="+title+
	        			"&sortBy="+sortBy+"&page="+page+"&rpp=25\">25</a> | ");
           }
           
           out.println("</center></html>");
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
