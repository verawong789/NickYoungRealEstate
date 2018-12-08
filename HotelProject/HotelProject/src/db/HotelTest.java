package db;
import java.sql.*;
import java.util.Scanner;


public class HotelTest {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	//static final String DB_URL = "jdbc:mysql://localhost/singaporeHotels";
	static final String DB_URL = "jdbc:mysql://localhost/hotelsys";

	//  Database credentials
	static final String USER = "root";
	//static final String PASS = "barney96";
	static final String PASS = "10fruits";
	   
	public static void main(String[] args) {
		Connection conn = null;
		try{
			//Open a connection
			System.out.println("Connecting to database: " + DB_URL);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			ResultSet rs = null;
			Statement stmt = conn.createStatement();
			PreparedStatement pStmt = null;
			
			int admin = 0;
			
			System.out.println("Welcome to Nick Young's Hotels");
			// menu
			System.out.println("Select one of the following options: ");
			System.out.println("[C]reate account [S]ign in");
			System.out.println("Enter letter within a pair of brackets to select option");
			Scanner reader = new Scanner(System.in);
			String input = reader.nextLine();
			
			int loggedInUserID = 999;
			String firstName = null;
			String lastName = null;
			String userEmail = null;
			String userPassword = null;

			// menu action
			if (input.equals("C") || input.equals("c")) {
				//createUser 
				System.out.print("First name: ");
				firstName = reader.nextLine();
				System.out.print("Last name: ");
				lastName = reader.nextLine();
				System.out.print("Email: ");
				userEmail = reader.nextLine();
				System.out.print("Password: ");
				userPassword = reader.nextLine();
				
				String sql = "INSERT INTO user (first_name, last_name, email, password) VALUES (?,?,?,?)";
				pStmt= conn.prepareStatement(sql);
				
				pStmt.setString(1, firstName);
				pStmt.setString(2, lastName);
				pStmt.setString(3, userEmail);
				pStmt.setString(4, userPassword);
				pStmt.addBatch();
				pStmt.executeBatch();
				
				sql = "SELECT user_id FROM user WHERE email='"+userEmail+"' and password='"+userPassword+"';";
				rs = stmt.executeQuery(sql);
				while (rs.next())
				{ 
					loggedInUserID = rs.getInt("user_id"); 
				}
				
				
				if (loggedInUserID != 999 ) {System.out.println("Create Account Successful! Welcome " + firstName + " " + lastName + "!");}
				else {System.out.println("Account creation failed"); reader.close(); return; }
				
			} else if (input.equals("S") || input.equals("s")) {
				//signInUser
				System.out.print("Email: ");
				userEmail = reader.nextLine();
				System.out.print("Password: ");
				userPassword = reader.nextLine();
				
				String sql = "SELECT first_name, last_name,user_id FROM user WHERE email='"+userEmail+"' and password='"+userPassword+"';"; 
				rs = stmt.executeQuery(sql);
				
				while (rs.next())
				{ 
					loggedInUserID = rs.getInt("user_id"); 
					firstName = rs.getString("first_name");
					lastName = rs.getString("last_name");
				}
				
				if (loggedInUserID != 999 ) {System.out.println("Sign in Successful! Welcome " + firstName + " " + lastName + "!");}
				else {System.out.println("Incorrect Credentials."); reader.close(); return; }
			}
			System.out.println();
			
			//check if admin account
			if((userEmail.equals("admin@hotel.com")) && (userPassword.equals("eleanor"))) admin=1; 

			
			if(admin==1) {
				System.out.println("Select one of the following options: ");
				System.out.println("[H]otel management [O]ther [L]og out");
				System.out.println("Enter letter within a pair of brackets to select option");
				input = reader.nextLine();
				
				while(!((input.equals("L")) || (input.equals("l")))) {
					if (input.equals("H") || input.equals("h")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[C]reate hotel [S]how all hotels [B]est selling hotel [D]elete worst selling hotel");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("C") || input.equals("c")) {
							//createHotel
						} else if (input.equals("S") || input.equals("s")) {
							//getAllHotels
						} else if (input.equals("B") || input.equals("b")) {
							//getBestSellingHotel
						} else if (input.equals("D") || input.equals("d")) {
							//deleteWorstSellingHotel
						}
					} else if (input.equals("O") || input.equals("o")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[S]how all users [C]reate room [U]pdate room");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("S") || input.equals("s")) {
							//getAllUsers
						} else if (input.equals("C") || input.equals("c")) {
							//createRoom
						} else if (input.equals("U") || input.equals("u")) {
							//updateRoom
						}
					}
					System.out.println("Select one of the following options: ");
					System.out.println("[H]otel management [O]ther [L]og out");
					System.out.println("Enter letter within a pair of brackets to select option");
					input = reader.nextLine();
				}
			}
			else{
				System.out.println("Select one of the following options: ");
				System.out.println("[A]ccount [M]ake reservation [C]urrent reservation [L]og out");
				System.out.println("Enter letter within a pair of brackets to select option");
				input = reader.nextLine();
				
				while(!((input.equals("L")) || (input.equals("l")))) {
					if (input.equals("A") || input.equals("a")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[U]pdate account [D]elete account");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("U") || input.equals("u")) {
							//updateUser

						} else if (input.equals("D") || input.equals("d")) {
							//deleteUser

						}
					} else if (input.equals("M") || input.equals("m")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[M]ake reservation		Search [H]otel		Search available [R]oom");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("M") || input.equals("m")) {
							//bookRoom
							System.out.print("Enter the room ID you want: ");
//							int room_id = reader.ne
							int room_id=123;
//							System.out.println("Enter start date YYYY-MM-DD: ");
							String start_date = reader.nextLine();
//							System.out.println("Enter end date YYYY-MM-DD: ");
//							String end_date = reader.nextLine();
							//String sql = "INSERT into booking(user_id, hotel_id, room_id, start, end, updated_at) values ("+ loggedInUserID +","+room_id+","+start_date+","+"end_date" +", CONVERT(date, getDate())";
//							System.out.println(sql);
						} else if (input.equals("H") || input.equals("h")) {
							//searchHotel

						} else if (input.equals("R") || input.equals("r")) {
							//searchAvailableRooms
							System.out.println("Here are all the rooms you can book:");
							String sql = "SELECT * FROM room,hotel WHERE room.hotel_id = hotel.hotel_id";
							rs = stmt.executeQuery(sql);
							System.out.println("HOTEL NAME | ADDRESS | ROOM ID | ROOM NUMBER | PRICE");
							while (rs.next())
							{ System.out.println(rs.getString("name") + " | " + rs.getString("address")+","+rs.getString("city") + 
									" | " + rs.getInt("room_id") + " | " + rs.getInt("room_number") + " | $" + rs.getInt("rate")); }
							
						}
					} else if (input.equals("C") || input.equals("c")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[C]ancel reservation  c[H]ange reservation  [S]how all reservations  [M]ost visited city");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("C") || input.equals("c")) {
							//cancelRoom

						} else if (input.equals("H") || input.equals("h")) {
							//changeReservation

						} else if (input.equals("S") || input.equals("s")) {
							//showAllReservations
							
						} else if (input.equals("M") || input.equals("m")) {
							//getMostVistitedCity
							
						}
					}
					System.out.println("Select one of the following options: ");
					System.out.println("[A]ccount [M]ake reservation [C]urrent reservation [L]og out");
					System.out.println("Enter letter within a pair of brackets to select option");
					input = reader.nextLine();
				}
			}
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
