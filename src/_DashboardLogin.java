
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class _DashboardLogin extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6459543349790159326L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public _DashboardLogin() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html"); // Response mime type
		try {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/_dashboard.jsp");
			dispatcher.forward(request, response);
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
