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
				
				String hotel_name = null;
				String hotel_address = null;
				String hotel_city = null;
				String hotel_state = null;

				while(!((input.equals("L")) || (input.equals("l")))) {
					if (input.equals("H") || input.equals("h")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[C]reate hotel [S]how all hotels [B]est selling hotel [D]elete worst selling hotel");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("C") || input.equals("c")) {
							//createHotel	
							System.out.print("Enter hotel id: ");
							int hotelId = reader.nextInt();
							System.out.print("Enter hotel name: ");
							hotel_name = reader.nextLine();
							System.out.print("Enter hotel address: ");
							hotel_address = reader.nextLine();
							System.out.print("Enter hotel city: ");
							hotel_city = reader.nextLine();
							System.out.print("Enter hotel state: ");
							hotel_state = reader.nextLine();
							System.out.print("Enter postal code: ");
							int hotel_postal_code = reader.nextInt();
							System.out.print("Enter hotel rating: ");
							int hotel_rating = reader.nextInt();
							
							String sql = "SELECT * FROM HOTEL WHERE hotel_id ='" + hotelId + "'";
							rs = stmt.executeQuery(sql);
							
							if (rs.next()) {
								System.out.println("Hotel already exists..");
							}
							else {
								sql = "INSERT INTO HOTEL (hotel_id, name, address, city, state, postal_code, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setInt(1, hotelId);
								pStmt.setString(2, hotel_name);
								pStmt.setString(3, hotel_address);
								pStmt.setString(4, hotel_city);
								pStmt.setString(5, hotel_state);
								pStmt.setInt(6, hotel_postal_code);
								pStmt.setInt(7, hotel_rating);
								pStmt.addBatch();
								pStmt.executeBatch();
								
								System.out.println("Hotel created successfully...");
							}
						
						} else if (input.equals("S") || input.equals("s")) {
							//getAllHotels
							System.out.println("Here is a list of all hotels..");
							String sql = "SELECT * FROM HOTEL ORDER BY hotel_id";
							rs = stmt.executeQuery(sql);
							
							System.out.println("Hotel ID | Name | Address | City | State | Postal Code | Rating");
							while (rs.next()) {
								System.out.println(rs.getInt("hotel_id") + " | " + rs.getString("name") + " | " + rs.getString("address") + " | " + rs.getString("city") + " | " + rs.getString("state") + " | " + 
										rs.getInt("postal_code") + " | " + rs.getInt("rating"));
							}
							
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
							System.out.print("Enter room id: ");
							int roomId = reader.nextInt();
							System.out.print("Enter room number: ");
							int roomNumber = reader.nextInt();
							System.out.print("Enter room rating: ");
							int roomRate = reader.nextInt();
							System.out.print("Enter hotel id: ");
							int hotel_id = reader.nextInt();
							
							String sql = "SELECT * FROM HOTEL WHERE hotel_id ='" + hotel_id + "'"; 
							rs = stmt.executeQuery(sql);
							
							if (rs.next()) {
								sql = "INSERT INTO room (room_id, room_number, rate, hotel_id, booked) VALUES (?, ?, ?, ?, ?)";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setInt(1, roomId);
								pStmt.setInt(2, roomNumber);
								pStmt.setInt(3, roomRate);
								pStmt.setInt(4, hotel_id);
								pStmt.setInt(5, 0);
								pStmt.addBatch();
								pStmt.executeBatch();
								
								System.out.println("Room created successfully..");
							}
							else {
								System.out.println("Hotel doesn't exist, room cannot be created..");
							}
							
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
							System.out.print("Enter hotel id: ");
							int hotelId = reader.nextInt();

							String sql = "SELECT name, address, city, state, postal_code, rating FROM HOTEL WHERE hotel_id='" + hotelId + "'";
							rs = stmt.executeQuery(sql);
							
							if (rs.next()) {
								System.out.println("Name | Address | City | State | Postal Code | Rating");
								System.out.println(rs.getString("name") + " | " + rs.getString("address") + " | " + rs.getString("city") + " | " +
										rs.getString("state") + " | " + rs.getInt("postal_code") + " | " + rs.getInt("rating"));
							}
							else {
								System.out.println("No such hotel..");
							}
							
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
							System.out.print("Enter hotel id: ");
							int hotelId = reader.nextInt();
							System.out.print("Enter room id: ");
							int roomId = reader.nextInt();
							System.out.print("Enter new starting date: ");
							int start_date = reader.nextInt();
							System.out.print("Enter new end date: ");
							int end_date = reader.nextInt();
							
							String sql = "UPDATE booking, user SET date_booking_start = ? and date_booking_end = ? WHERE booking.user_id = user.user_id and hotel_id = ? and room_id = ?";
							pStmt = conn.prepareStatement(sql);
							
							pStmt.setInt(1, start_date);
							pStmt.setInt(2, end_date);
							pStmt.setInt(3, hotelId);
							pStmt.setInt(4, roomId);
							pStmt.addBatch();
							pStmt.executeBatch();
							
							System.out.println("Booking updated successfully..");
							
						} else if (input.equals("S") || input.equals("s")) {
							//showAllReservations
							System.out.println("Here are all your reservations..");
							String sql = "SELECT * FROM user JOIN booking where user.user_id = booking.user_id";
							rs = stmt.executeQuery(sql);
							
							System.out.println("First name | Last name | Booking ID | Booking start date | Booking end date");
							while (rs.next()) {
								System.out.println(rs.getString("first_name") + " | " + rs.getString("last_name") + " | " + rs.getInt("booking_id") +
										" | " + rs.getDate("date_booking_start") + " | " + rs.getDate("date_booking_end"));
							}
							
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
