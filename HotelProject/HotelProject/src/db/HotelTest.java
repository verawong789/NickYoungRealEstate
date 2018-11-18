package db;
import java.sql.*;

public class HotelTest {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/library";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "default$";
	   
	public static void main(String[] args) {
		Connection conn = null;
		try{
			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
}
