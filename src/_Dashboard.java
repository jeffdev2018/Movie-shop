
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class _Dashboard extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6459543349790159326L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public _Dashboard() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		String emp_email = (String) session.getAttribute("emp_email");
		if (emp_email == null) {
			response.sendRedirect("/Fabflix/_dashboard");
			return;
		}
		
		String loginUser = "root";
		String loginPasswd = "252795";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		response.setContentType("text/html"); // Response mime type
		String metadata = "";
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

			String method = request.getParameter("method");
			if (method == null) {
				method = "";
			}

			String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='moviedb' ";
			PreparedStatement ps = dbcon.prepareStatement(query);
			ResultSet result = ps.executeQuery();

			while (result.next()) {
				String r = result.getString(1);
				metadata += "<b>Table name:</b> " + r + "<br>";

				query = "SHOW COLUMNS FROM " + r;
				PreparedStatement ps2 = (PreparedStatement) dbcon.prepareStatement(query);
				ResultSet result2 = ps2.executeQuery();
				while (result2.next()) {

					metadata += result2.getString(1) + " " + result2.getString(2) + "<br>";
				}

				metadata += "<br>";
				result2.close();
				ps2.close();
			}
			result.close();
			ps.close();

			request.setAttribute("metadata", metadata);

			if (method.equals("insertstar")) {
				String fn = request.getParameter("fn");
				String ln = request.getParameter("ln");
				if (fn != "" && ln != "") {
					String query2 = "INSERT INTO stars(first_name, last_name) VALUES(" + modify_name_query(fn, ln)
							+ ")";
					// System.out.println(query2);
					PreparedStatement insert = (PreparedStatement) dbcon.prepareStatement(query2);
					int retID = insert.executeUpdate();
					request.setAttribute("error", "Sucess adding a new star");
					insert.close();
				} else {
					request.setAttribute("error", "Error adding a new star");
				}
			} else if (method.equals("addmovie")) {
				String title = request.getParameter("title");
				String director = request.getParameter("dir");
				String year = request.getParameter("year");
				String genre = request.getParameter("genre");
				String burl = request.getParameter("banner_url");
				String turl = request.getParameter("trailer_url");
				String fn = request.getParameter("fn");
				String ln = request.getParameter("ln");
				if (title != "" && director != "" && year != "" && genre != "" && (fn != "" || ln != "")) {
					String query3 = String.format("CALL add_movie(%s);",
							modify_add_movie_params(title, year, director, burl, turl, fn, ln, genre));
					// System.out.println(query3);
					PreparedStatement insert2 = (PreparedStatement) dbcon.prepareStatement(query3);
					int retID = insert2.executeUpdate();
					request.setAttribute("error", "Success adding a new movie");
					insert2.close();
				} else {
					request.setAttribute("error", "Error adding a new movie");
				}
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

		// // redirect back to dashboard
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/employee-dashboard.jsp");
		dispatcher.forward(request, response);

		// response.sendRedirect(request.getHeader("referer"));

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

	public String modify_name_query(String fn, String ln) {
		String result = "";
		if (!fn.isEmpty() && !ln.isEmpty()) {
			result += "'" + fn + "', '" + ln + "'";
		} else {
			if (!fn.isEmpty()) {
				result += "'' , '" + fn + "'";
			} else {
				result += "'' , '" + ln + "'";
			}
		}
		return result;
	}

	public String modify_add_movie_params(String title, String year, String director, String burl, String turl,
			String fn, String ln, String genre) {
		String result = "";
		String name = modify_name_query(fn, ln);
		result = String.format("'%s', %s, '%s', '%s', '%s', %s, '%s'", title, year, director, burl, turl, name, genre);
		return result;
	}
}
