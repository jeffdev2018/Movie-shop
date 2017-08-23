import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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

public class StarSearch extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8861813466224779700L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StarSearch() {
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

		String loginUser = "root";
		String loginPasswd = "252795";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		ArrayList<HashMap<String, String>> movie_results = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> star = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		HashMap<String,Integer> cart = (HashMap<String,Integer>) session.getAttribute("cart");
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
			
			String fn = request.getParameter("fn");
			String ln = request.getParameter("ln");
			String query1 = "SELECT t1.id, t1.title, year, director, banner_url, trailer_url, fn, ln, genres "
					+ "FROM (SELECT m.id, title, year, director, banner_url, trailer_url, GROUP_CONCAT(first_name separator ',') as fn, GROUP_CONCAT(last_name separator ',') as ln "
					+ "FROM movies m, stars s, stars_in_movies sm WHERE s.id = sm.star_id AND sm.movie_id = m.id  AND m.title in "
					+ "(SELECT title FROM movies m , stars s, stars_in_movies sm "
					+ "WHERE s.id = sm.star_id AND sm.movie_id = m.id  AND s.first_name LIKE '" + fn
					+ "' AND s.last_name LIKE '" + ln + "' GROUP BY m.title, m.id) GROUP BY m.title, m.id) "
					+ "t1 left join (SELECT m.id, title, group_concat(name separator ',') as genres "
					+ "FROM movies m , genres g, genres_in_movies gim WHERE m.id = gim.movie_id AND gim.genre_id = g.id GROUP BY m.title, m.id) "
					+ "t2 on t1.title=t2.title;";

			// Perform the query
			PreparedStatement ps = dbcon.prepareStatement(query1);
			ResultSet rs1 = ps.executeQuery();

			while (rs1.next()) {
				HashMap<String, String> movie = new HashMap<String, String>();
				String[] fns = rs1.getString("fn").split(",");
				String[] lns = rs1.getString("ln").split(",");
				if (fns[0].equals("none")) {
					fns[0] = "";
				}
				String names = fns[0] + ":" + lns[0];
				for (int i = 1; i < fns.length; i++) {
					names += "," + fns[i] + ":" + lns[i];
				}
				String title = rs1.getString("title");
				Integer copies = (Integer) cart.get(title);
	            if (copies == null) {
	                movie.put("copies", String.valueOf(0));
	            }
	            else {
	                movie.put("copies", String.valueOf(copies));
	            }
				movie.put("names", names);
				movie.put("id", rs1.getString("id"));
				movie.put("genres", rs1.getString("genres"));
				movie.put("title", title);
				movie.put("year", rs1.getString("year"));
				movie.put("director", rs1.getString("director"));
				movie.put("banner_url", rs1.getString("banner_url"));
				movie.put("trailer_url", rs1.getString("trailer_url"));
				movie_results.add(movie);
			}
			rs1.close();

			String query2 = "SELECT * FROM stars WHERE first_name = '" + fn + "' AND last_name = '" + ln + "';";
			PreparedStatement ps2 = dbcon.prepareStatement(query2);
			ResultSet rs2 = ps2.executeQuery();

			while (rs2.next()) {
				star.put("id", rs2.getString("id"));
				star.put("fn", rs2.getString("first_name"));
				star.put("ln", rs2.getString("last_name"));
				star.put("dob", rs2.getString("dob"));
				star.put("photo_url", rs2.getString("photo_url"));
			}
			rs2.close();

			ps.close();
			ps2.close();
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
		request.setAttribute("movie_results", movie_results);
		request.setAttribute("star_info", star);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/showstar.jsp");
		dispatcher.forward(request, response);
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
