
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.PreparedStatement;

/**
 * Servlet implementation class AutoComplete
 */
@WebServlet("/AutoComplete")
public class AutoComplete extends HttpServlet {

	private static final long serialVersionUID = -1665956085312412676L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutoComplete() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		String usr_email = (String) session.getAttribute("usr_email");
		if (usr_email == null) {
			response.sendRedirect("/Fabflix/");
			return;
		}

		String loginUser = "root";
		String loginPasswd = "252795";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		response.setContentType("text/html");
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
	        
			String query = request.getParameter("query");
			if (!query.equals("")) {
				
				// modify our query
				String[] tokens = query.trim().split("\\s+");
				String boolexp = "";
				if (tokens.length == 1) {
					boolexp = String.format("%s*", tokens[0].toLowerCase());
				} else {
					for (int i = 0; i < tokens.length; i++) {
						if (i != tokens.length - 1) {
							boolexp += String.format("+*%s* ", tokens[i].toLowerCase());
						} else {
							boolexp += String.format("+%s*", tokens[i].toLowerCase());
						}
					}
				}
				
				// create our sql statement
				
				String sqlquery = String.format("select m.title from movies m where match(m.title) against ('%s' in boolean mode) limit 5;",
						boolexp);
				PreparedStatement ps = dbcon.prepareStatement(sqlquery);
				ResultSet rs = ps.executeQuery();
				
				// construct our output
				String suggestions = "";
				String urltemplate = "/Fabflix/servlet/AdvSearch?page=1&show=9&title=%s&year=&director=&fn=&ln=&SORT=&DESC=";
				while (rs.next()) {
					String title = rs.getString("title");
					String url = String.format(urltemplate, title);
					if (suggestions.equals("")) {
						suggestions += String.format("<a href='%s'>%s</a>", url, title);
					} else {
						suggestions += String.format("<br /><a href='%s'>%s</a>", url, title);
					}
				}
				
				// write the final response
				PrintWriter out = response.getWriter();
				out.println(suggestions);
			}
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
