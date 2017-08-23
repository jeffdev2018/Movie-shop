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
import javax.sql.DataSource;

import java.sql.PreparedStatement;

/**
 * Servlet implementation class MobileSearch
 */
@WebServlet("/MobileSearch")
public class MobileSearch extends HttpServlet {
       
	private static final long serialVersionUID = 1330736353614350856L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSearch() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser = "root";
		String loginPasswd = "252795";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		response.setContentType("text/html"); // Response mime type
		
		String query = request.getParameter("search");
		
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
	        
			if (!query.equals("")) {
				
				// modify our query
				String[] query_list = query.trim().split("\\s+");
				String mod_query = "";
				if (query_list.length == 1) {
					mod_query = String.format("%s*", query_list[0].toLowerCase());
				} else {
					for (int i = 0; i < query_list.length; i++) {
						if (i != query_list.length - 1) {
							mod_query += String.format("+*%s* ", query_list[i].toLowerCase());
						} else {
							mod_query += String.format("+%s*", query_list[i].toLowerCase());
						}
					}
				}
				
				// create our sql statements
				
				String sqlquery = String.format("select m.title from movies m where match(m.title) against ('%s' in boolean mode) limit 4 offset ", mod_query);
				String sqlquery2 = String.format("select count(*) from movies m where match(m.title) against ('%s' in boolean mode);", mod_query);
				int page = Integer.parseInt(request.getParameter("page"));
				String offset = String.valueOf((page - 1) * 4);
				sqlquery += offset + ";";
				
				PreparedStatement ps = dbcon.prepareStatement(sqlquery);
				PreparedStatement ps2 = dbcon.prepareStatement(sqlquery2);
				
				// get our results
				ResultSet rs = ps.executeQuery(sqlquery);
				ResultSet rs2 = ps2.executeQuery(sqlquery2);
				
				int max = 0;
				while (rs2.next()) {
					max = rs2.getInt(1);
				}
				
				// write results out
				String results = String.format("%d;", max);
				while (rs.next()) {
					String title = rs.getString("title");
					results += title + ";";
				}
				PrintWriter out = response.getWriter();
				out.println(results);
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
