import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Purchase extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6459543349790159326L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Purchase() {
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
			// Create a session object if it is already not created.
			@SuppressWarnings("unchecked")
			HashMap<String,Integer> cart = (HashMap<String,Integer>) session.getAttribute("cart");
			@SuppressWarnings("unchecked")
			HashMap<String,String> ids = (HashMap<String,String>) session.getAttribute("ids");

			String title = request.getParameter("title");
			String copies = request.getParameter("copies");
			String id = request.getParameter("id");
			
			Integer val = (Integer) cart.get(title);
            
			// If the movie is already in their cart and they set the quantity to 0
			// then delete the movie from their cart
			if (val != null) {
				if (String.valueOf(copies).equals("0")) {
                	cart.remove(title);
                	ids.remove(title);
				}
				
				// If it isn't set to 0, simply overwrite the quantity with the new value
				else {
					cart.put(title, Integer.parseInt(copies));
					
					// We only want to insert the id if the movie is not already in the cart
					// HashMap AND if the id was supplied from the movie listing links
					Integer numCopies = (Integer) cart.get(title);
					if (numCopies != null && id != null) {
		                ids.put(title, id);
		            }
				}
            }
			
			// If the movie isn't already in their cart, add it in
			else {
				cart.put(title, Integer.parseInt(copies));
				ids.put(title, id);
			}
			
			session.setAttribute("cart", cart);
			session.setAttribute("ids", ids);
			
			response.sendRedirect(request.getHeader("referer"));
		} 
		
//		catch (SQLException ex) {
//			while (ex != null) {
//				System.out.println("SQL Exception:  " + ex.getMessage());
//				ex = ex.getNextException();
//			} // end while
//		} // end catch SQLException

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
