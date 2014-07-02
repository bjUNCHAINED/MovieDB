// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// Cart.java - Displays cart contents, allows the user to change quantities and displays costs
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Connect to database
    	String loginUser = "testuser";
        String loginPasswd = "testpass";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // Session/Printwriter set up
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		HashMap<String,MovieItem> cart;
		
		String docType = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
		
		// Request parameters
		String add = request.getParameter("add");
		String mid = request.getParameter("mid");
		String qty = request.getParameter("qty");
		int qty2 = 0;
		if(qty!=null)
			qty2 = Integer.parseInt(qty);
		
		// Cart Variables
		MovieItem newItem;
		double totalCost = 0.0;
		
		synchronized(session){
			// If cart is null, create new cart
			cart = (HashMap<String,MovieItem>)session.getAttribute("cart");
			if(cart == null){
				cart = new HashMap<String,MovieItem>();
				session.setAttribute("cart", cart);
			}
		}
		
		// Take care of add/update/remove request
		synchronized(cart){
			if(add!=null){
				if(add.equals("add")){
					if(cart.containsKey(mid))
						cart.get(mid).setQuantity(cart.get(mid).getQuantity()+qty2);
					else{
						newItem = new MovieItem(mid, qty2);
						cart.put(mid, newItem);
					}
				}
				else if(add.equals("update")){
					if(qty2<=0)
						cart.remove(mid);
					else cart.get(mid).setQuantity(qty2);
				
				}
				else if(add.equals("remove")){
					cart.remove(mid);
				}
			}
		}
		
		// Print out header
		out.println(docType +
				"<html><head>" +
        		"<title>Fabflix Cart Page:</title><head>" +
        		"<body bgcolor=\"FDF5E6\">" +
        		"<h1 align=\"center\">Fabflix: Cart Page</h1>" +
        		"<center>" +
        		"<a href=\"/Fabflix/main.html\">Back to Browse</a> | "+
        		"<a href=\"/Fabflix/search.html\">Search For a Movie</a> | "+
        		"<a href=\"/Fabflix/servlet/Cart\">My Cart</a> | "+
        		"<a href=\"/Fabflix/servlet/LogOut\">Log Out</a><br>");
		
		// Create database connection to get movie titles from id's while printing out cart
		try{
			// Setup
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	        Statement statement = dbcon.createStatement();
	       
			// Print out cart
			synchronized(cart){
				if(cart.size() == 0){
					out.println("Cart is empty<br>");
				}
				else {
					out.println("<TABLE border>" +
		        		"<tr>" +
		                "<td>Movie Title</td>" +
		                "<td>Price</td>" +
		                "<td>Qty</td>" +
		                "<td>Options</td>" +
		                "</tr>");
					
					
					// Iterator goes through each movie, printing out information and options. Also update total cost
					Iterator<String> keyIterator = cart.keySet().iterator();
					while(keyIterator.hasNext()){
						
						String key = keyIterator.next();
						String query = "SELECT title FROM movies WHERE id='"+key+"'";
	        
						ResultSet rs = statement.executeQuery(query);
						rs.next();
						String title = rs.getString("title");
						
						// Update total cost
						totalCost += 9.99*cart.get(key).getQuantity();
						
						out.println("<tr>" +
								"<td><a href=\"/Fabflix/servlet/GetMovie?id="+cart.get(key).getId()+"\">"+title+"</td>" +
				                "<td>$9.99</td>" +
				                "<td>"+cart.get(key).getQuantity()+"</td>" +
				                "<td><form action=\"/Fabflix/servlet/Cart\" method=\"get\">" +
				                "Quantity: <input type=\"text\" name=\"qty\"><br>" +
				                "<input type=\"hidden\" name=\"add\" value=\"update\">"+
				                "<input type=\"hidden\" name=\"mid\" value=\""+cart.get(key).getId()+"\">"+
				                "<input type=\"submit\" value=\"Update\"></form>" +
				                "<a href=\"/Fabflix/servlet/Cart?add=remove&mid="+cart.get(key).getId()+"\">Remove</a></td>" +
				                "</tr>");
					}
					out.println("</TABLE>" +
							"TOTAL COST: $" +totalCost+" | " +
							"<a href=\"/Fabflix/servlet/EmptyCart\">Empty Cart</a> | "+
							"<a href=\"/Fabflix/servlet/Checkout\">Checkout</a><br>");
				}
			}
			
			out.println("<a href=\"javascript:history.go(-1)\">Go back</a>");
			out.println("</center>");
	        out.println("</html>");
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
		doGet(request,response);
	}

}
