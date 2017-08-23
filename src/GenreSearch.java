
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class GenreSearch extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -899112192747390612L;

	/**
	 * 
	 */
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenreSearch() {
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
		int max = 0;
		int showings = 0;
		
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
			Statement statement = dbcon.createStatement();
			Statement statement2 = dbcon.createStatement();

			String query = "select t1.id, t1.title, genres, year, director, banner_url, trailer_url, fn, ln "
					+ "from (SELECT m.id, title, group_concat(name separator ',') as genres FROM movies m , genres g, genres_in_movies gim "
					+ "WHERE m.id = gim.movie_id AND gim.genre_id = g.id GROUP BY m.title, m.id) "
					+ "t1 join (SELECT m.id, title, year, director, banner_url, trailer_url, GROUP_CONCAT(first_name separator ',') as fn, GROUP_CONCAT(last_name separator ',') as ln "
					+ "FROM movies m , stars s, stars_in_movies sm, genres g, genres_in_movies gim "
					+ "WHERE s.id = sm.star_id AND sm.movie_id = m.id AND m.id = gim.movie_id AND gim.genre_id = g.id AND g.name = '" + request.getParameter("tag")
					+ "' GROUP BY m.title, m.id) t2 on t1.title=t2.title";
			String total = "select COUNT(*) "
					+ "from (SELECT m.id, title, group_concat(name separator ',') as genres FROM movies m , genres g, genres_in_movies gim "
					+ "WHERE m.id = gim.movie_id AND gim.genre_id = g.id GROUP BY m.title, m.id) "
					+ "t1 join (SELECT m.id, title, year, director, banner_url, trailer_url, GROUP_CONCAT(first_name separator ',') as fn, GROUP_CONCAT(last_name separator ',') as ln "
					+ "FROM movies m , stars s, stars_in_movies sm, genres g, genres_in_movies gim "
					+ "WHERE s.id = sm.star_id AND sm.movie_id = m.id AND m.id = gim.movie_id AND gim.genre_id = g.id AND g.name = '" + request.getParameter("tag")
					+ "' GROUP BY m.title, m.id) t2 on t1.title=t2.title";
			
			// Perform the query
			String sort_type = request.getParameter("SORT");
			String desc = request.getParameter("DESC");
			if (sort_type != null && sort_type != "") {
				query = query + " ORDER BY ";
				if (sort_type.equals("title")){
					query = query + "t1.";
				}
				query = query + sort_type;
				if (desc != null && desc != "" && desc.equals("true")) {
					query = query + " DESC";
				}
			}
			query = query + " LIMIT " + request.getParameter("show") + " OFFSET ";
			showings = Integer.valueOf(request.getParameter("show"));

			int page = Integer.parseInt(request.getParameter("page"));
			String offset = String.valueOf((page - 1) * showings);
			query = query + offset + ";";
			
			PreparedStatement ps = dbcon.prepareStatement(query);
			PreparedStatement ps2 = dbcon.prepareStatement(total);
			
			ResultSet rs = ps.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			
			while(rs2.next()) {
				max = rs2.getInt(1);
			}
			
			while (rs.next()) {
				HashMap<String, String> movie = new HashMap<String, String>();
				String[] fns = rs.getString("fn").split(",");
				String[] lns = rs.getString("ln").split(",");
				if (fns[0].equals("none")){
					fns[0] = "";
				}
				String names = fns[0] + ":" + lns[0];
				for (int i = 1; i < fns.length; i++){
					names += "," + fns[i] + ":" + lns[i];
				}
				String title = rs.getString("title");
				Integer copies = (Integer) cart.get(title);
	            if (copies == null) {
	                movie.put("copies", String.valueOf(0));
	            }
	            else {
	                movie.put("copies", String.valueOf(copies));
	            }
				movie.put("names", names);
				movie.put("id", rs.getString("id"));
				movie.put("genres", rs.getString("genres"));
				movie.put("title", title);
				movie.put("year", rs.getString("year"));
				movie.put("director", rs.getString("director"));
				movie.put("banner_url", rs.getString("banner_url"));
				movie.put("trailer_url", rs.getString("trailer_url"));
				movie_results.add(movie);
			}
			
			rs.close();
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
		int maxPage = max / showings;
		if ((max % showings) != 0)
			maxPage += 1;
		
		int prevPage = Integer.parseInt(request.getParameter("page")) - 1;
		int nextPage = Integer.parseInt(request.getParameter("page")) + 1;
		
		request.setAttribute("type", "GenreSearch");
		request.setAttribute("page", request.getParameter("page"));
		request.setAttribute("show", request.getParameter("show"));
		request.setAttribute("prev", String.valueOf(prevPage));
		request.setAttribute("maxPage", maxPage);
		request.setAttribute("next", nextPage);
		request.setAttribute("tag", request.getParameter("tag"));
		request.setAttribute("sort", request.getParameter("SORT"));
		request.setAttribute("desc", request.getParameter("DESC"));
		request.setAttribute("movie_results", movie_results);

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/showmovies.jsp");
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
