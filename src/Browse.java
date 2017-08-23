
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.PreparedStatement;

/**
 * Servlet implementation class Browse
 */
public class Browse extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1317257330918795059L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Browse() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		String usr_email = (String) session.getAttribute("usr_email");
		if (usr_email == null) {
			response.sendRedirect("/Fabflix/");
			return;
		}
		
		String loginUser = "root";
		String loginPasswd = "252795";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		ArrayList<String> browse_options = new ArrayList<String>();

		response.setContentType("text/html"); // Response mime type
		try {
			Context initCtx = new InitialContext();
	        if (initCtx == null)
	            System.out.println("initCtx is NULL");

	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        if (envCtx == null)
	            System.out.println("envCtx is NULL");

	        // Look up our data source
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

	        // the following commented lines are direct connections without pooling
	        //Class.forName("org.gjt.mm.mysql.Driver");
	        //Class.forName("com.mysql.jdbc.Driver").newInstance();
	        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

	        if (ds == null)
	            System.out.println("ds is null.");

	        Connection dbcon = ds.getConnection();
	        if (dbcon == null)
	            System.out.println("dbcon is null.");
	        
			// Declare our statement

			String by = request.getParameter("BY");
			if (by.equals("genre")){
				String query = "SELECT name FROM genres";
			// String query = "SELECT * from movies where year = '2001'";
			// Perform the query
				PreparedStatement ps = dbcon.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					browse_options.add(rs.getString("name"));
				}
				rs.close();
				ps.close();
				request.setAttribute("browse_options", browse_options);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/genre_browse.jsp");
				dispatcher.forward(request, response);
			}
			else {
				String[] alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
				for (int i = 0; i < alphanumeric.length; i++){
					browse_options.add(alphanumeric[i]);
				}
				request.setAttribute("browse_options", browse_options);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/basic_browse.jsp");
				dispatcher.forward(request, response);
			}
			dbcon.close();
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException

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
