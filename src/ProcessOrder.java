import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

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

public class ProcessOrder extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6459543349790159326L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessOrder() {
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

		response.setContentType("text/html"); // Response mime type
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> cart = (HashMap<String, Integer>) session.getAttribute("cart");
			@SuppressWarnings("unchecked")
			HashMap<String, String> ids = (HashMap<String, String>) session.getAttribute("ids");
			
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
			String ccID = request.getParameter("ccID");
			String ccExp = request.getParameter("ccExp");

			String query = "SELECT EXISTS (SELECT * FROM creditcards " + "WHERE id='" + ccID + "' and first_name='" + fn
					+ "' and last_name='" + ln + "' and expiration='" + ccExp + "') as exist;";

			PreparedStatement ps = dbcon.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String ex = rs.getString("exist");
				if (ex.equals("0")) {
					request.setAttribute("error", "Your provided information is invalid, please try again");
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/checkout.jsp");
					dispatcher.forward(request, response);
				} else if (ex.equals("1")) {
					String cid = (String) session.getAttribute("usr_id");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String sale_date = sdf.format(new Date());
					for (Entry<String, Integer> entry : cart.entrySet()) {
						String title = entry.getKey();
						Integer copies = entry.getValue();
						String mid = ids.get(title);
						
						// do something with key and/or tab
						for (int i = 0; i < copies; i++){
							Statement s = dbcon.createStatement();
							String q = " INSERT INTO sales(customer_id, movie_id, sale_date) "
									+ "VALUES('" + cid + "', '" + mid + "', '" + sale_date + "');";
							s.executeUpdate(q);
							s.close();
						}
					}
					response.sendRedirect("/Fabflix/servlet/Confirmation");
				}
			}
			rs.close();
			ps.close();
			dbcon.close();
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception: " + ex.getMessage());
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
