// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// CompleteSale.java - Receipt page for completed sales. Records sales in database and displays sold items
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CompleteSale extends HttpServlet {
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
        
        // Header print
        out.println("<html><head>" +
        		"<title>Fabflix Complete Sale</title><head>" +
        		"<body bgcolor=\"FDF5E6\">" +
        		"<center><h1 align=\"center\">Fabflix: Complete Sale</h1>" +
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
           String query = "";
           
           // Fetch session and cart
           HashMap<String,MovieItem> cart;
           HttpSession session = request.getSession();
           synchronized(session){
	   			// If cart is null, create new cart
	   			cart = (HashMap<String,MovieItem>)session.getAttribute("cart");
   		   }
           
           out.println("Sale Complete! Customer Receipt:<br>");
           
           // For each movie in cart:
           Iterator<String> keyIterator = cart.keySet().iterator();
		   while(keyIterator.hasNext()){
				String key = keyIterator.next();
				// Format today's date
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date date = new Date();
				
				// Make sale for each quantity, record in database and show confirmation
				for(int i=0 ; i<cart.get(key).getQuantity() ; i++){
					query = "INSERT INTO sales (customer_id, movie_id, sale_date) " +
							"VALUES ('"+session.getAttribute("customerId")+"', '"+cart.get(key).getId()+"', '"+dateFormat.format(date)+"')";
					out.println("Movie id: "+cart.get(key).getId()+" SOLD TO Customer id: "+session.getAttribute("customerId")+" ON "+dateFormat.format(date)+"<br>");
				    statement.executeUpdate(query);
				}
		   }
		   // After completion of sale, empty cart and close resources
		   cart = new HashMap<String,MovieItem>();
		   session.setAttribute("cart", cart);
		   
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
      out.println("</center></html>");
      out.println("</html>");
      out.close();
		
	}

}
