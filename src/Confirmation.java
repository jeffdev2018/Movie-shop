
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Confirmation extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5762914719318035966L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Confirmation() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		String usr_email = (String) session.getAttribute("usr_email");
		if (usr_email == null) {
			response.sendRedirect("/Fabflix/");
			return;
		}

		response.setContentType("text/html"); // Response mime type
		try {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/confirmation.jsp");
			dispatcher.forward(request, response);
			
			// Create a session object if it is already not created.
			// create an empty cart for reset
			HashMap<String, Integer> new_cart = new HashMap<String, Integer>();
			session.setAttribute("cart", new_cart);
		} 

		catch (java.lang.Exception ex) {
			System.out.println("Java Exception: " + ex);
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
