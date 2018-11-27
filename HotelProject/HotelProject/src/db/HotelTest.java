package db;
import java.sql.*;
import java.util.Scanner;

public class HotelTest {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/singaporeHotels";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "barney96";
	   
	public static void main(String[] args) {
		Connection conn = null;
		try{
			//Open a connection
			System.out.println("Connecting to database: " + DB_URL);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			ResultSet rs = null;
			
			Scanner reader = new Scanner(System.in);
			System.out.print("Email: ");
			String userEmail = reader.nextLine();
			System.out.print("Password: ");
			String userPassword = reader.nextLine();
			
			Statement stmt = conn.createStatement();
			String sql = "SELECT first_name, last_name,user_id FROM user WHERE email='"+userEmail+"' and password='"+userPassword+"';";
			rs = stmt.executeQuery(sql);
			
			int loggedInUserID = 999;
			String first_name = null;
			String last_name = null;
			while (rs.next())
			{ 
				loggedInUserID = rs.getInt("user_id"); 
				first_name = rs.getString("first_name");
				last_name = rs.getString("last_name");
			}
			
			if (loggedInUserID != 999 ) {System.out.println("Sign in Successful! Welcome " + first_name + " " + last_name + "!");}
			else {System.out.println("Incorrect Credentials."); reader.close(); return; }
			
			System.out.println();
			System.out.println("Here are all the rooms you can book:");
			sql = "SELECT * FROM room,hotel WHERE room.hotel_id = hotel.hotel_id";
			rs = stmt.executeQuery(sql);
			System.out.println("HOTEL NAME | ADDRESS | ROOM ID | ROOM NUMBER | PRICE");
			while (rs.next())
			{ System.out.println(rs.getString("name") + " | " + rs.getString("address")+","+rs.getString("city") + 
					" | " + rs.getInt("room_id") + " | " + rs.getInt("room_number") + " | $" + rs.getInt("rate")); }
			
			System.out.print("Enter the room ID you want: ");
//			int room_id = reader.ne
			int room_id=123;
//			System.out.println("Enter start date YYYY-MM-DD: ");
			String start_date = reader.nextLine();
//			System.out.println("Enter end date YYYY-MM-DD: ");
//			String end_date = reader.nextLine();
			sql = "INSERT into booking(user_id, hotel_id, room_id, start, end, updated_at) values ("+
					loggedInUserID +","+room_id+","+start_date+","+"end_date" +", CONVERT(date, getDate())";
//			System.out.println(sql);
			
		
			reader.close();
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			try{
				if(conn!=null) {
					System.out.println("closing connection");
					conn.close();
				}
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
}
