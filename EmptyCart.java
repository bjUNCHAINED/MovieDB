// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014

// EmptyCart.java - Empties cart
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmptyCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		HashMap<String,MovieItem> cart = new HashMap<String,MovieItem>();
		
		session.setAttribute("cart", cart);
		
		response.sendRedirect("/Fabflix/servlet/Cart");
	}
	
	// Use http GET
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
