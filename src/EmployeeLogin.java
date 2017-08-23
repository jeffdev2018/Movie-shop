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

public class EmployeeLogin extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6459543349790159326L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmployeeLogin() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String loginUser = "root";
		String loginPasswd = "252795";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		response.setContentType("text/html"); // Response mime type

		String email = request.getParameter("usr");
		String pwd = request.getParameter("pwd");

		try {
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
		        
				// Login authentication

				String loginCheck = "SELECT * FROM employees c WHERE email = ? AND password=?;";
				PreparedStatement loginSt = dbcon.prepareStatement(loginCheck);
				loginSt.setString(1, email);
				loginSt.setString(2, pwd);
				ResultSet loginRs = loginSt.executeQuery();

				if (loginRs.next()) {
					HttpSession session = request.getSession(true);
					session.setAttribute("emp_email", email);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/servlet/_Dashboard");
					dispatcher.forward(request, response);
				} else {
					request.setAttribute("error", "Your email or password is invalid, please try again");
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/_dashboard");
					dispatcher.forward(request, response);
				}

				loginRs.close();
				loginSt.close();
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

		} catch (Exception e) {
			e.printStackTrace();
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
