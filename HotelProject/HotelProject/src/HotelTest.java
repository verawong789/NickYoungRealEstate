import java.sql.*;
import java.text.SimpleDateFormat;
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
			boolean deleted = false;
			
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
							outerloop1:
								while(reader.hasNextLine()) {
									hotel_name = reader.nextLine();
									if(!hotel_name.equals("")){
										break outerloop1;
									}
								}
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
						System.out.println("[S]how all users s[H]ow users with recent bookings [C]reate room [U]pdate room");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						if (input.equals("S") || input.equals("s")) {
							//getAllUsers
						} else if (input.equals("H") || input.equals("h")) {
							//shows all users with recent bookings, even users with no bookings
							String sql = "SELECT * from user u left join booking b on u.user_id=b.user_id;";
							rs = stmt.executeQuery(sql);
							
							System.out.println("User ID | Booking ID | Hotel ID | Room ID | Booking start date | Booking end date");
							while (rs.next()) {
								System.out.println(rs.getInt("user_id") + " | " + rs.getInt("booking_id") + " | " + rs.getInt("hotel_id") + " | " + rs.getInt("room_id") +
										" | " + rs.getDate("date_booking_start") + " | " + rs.getDate("date_booking_end"));
							}
						} else if (input.equals("C") || input.equals("c")) {
							//createRoom
							System.out.print("Enter room id: ");
							int roomId = reader.nextInt();
							System.out.print("Enter room number: ");
							int roomNumber = reader.nextInt();
							System.out.print("Enter room rate: ");
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
							System.out.println("Reset: [N]ame [E]mail [P]assword");
							System.out.println("Enter letter within a pair of brackets to select option");
							input = reader.nextLine();
							
							if(input.toLowerCase().equals("n")) {
								System.out.print("First name: ");
								firstName = reader.nextLine();
								System.out.print("Last name: ");
								lastName = reader.nextLine();
								
								String sql = "UPDATE user SET first_name=?, last_name=? WHERE user_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setString(1, firstName);
								pStmt.setString(2, lastName);
								pStmt.setInt(3, loggedInUserID);
							} else if(input.toLowerCase().equals("e")) {
								System.out.print("Email: ");
								userEmail = reader.nextLine();
								
								String sql = "UPDATE user SET email=? WHERE user_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setString(1, userEmail);
								pStmt.setInt(2, loggedInUserID);
							} else if(input.toLowerCase().equals("p")) {
								System.out.print("Password: ");
								userPassword = reader.nextLine();
								
								String sql = "UPDATE user SET password=? WHERE user_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setString(1, userPassword);
								pStmt.setInt(2, loggedInUserID);
							}
							pStmt.executeUpdate();
							
						} else if (input.equals("D") || input.equals("d")) {
							//deleteUser
							System.out.println("Are you sure you want to delete account for " + userEmail + "?   [Y]es [N]o");
							System.out.println("Enter letter within a pair of brackets to select option");
							input = reader.nextLine();
							
							if (input.toLowerCase().equals("y")) {
								String sql = "DELETE FROM user WHERE user_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setInt(1, loggedInUserID);
								pStmt.executeUpdate();
								
								sql = "SELECT * FROM user WHERE user_id='" + loggedInUserID + "';";
								rs = stmt.executeQuery(sql);
								boolean val = rs.next();
								if(val==false) {
									System.out.println("Account successfully deleted for " + userEmail + ".");
									deleted = true;
								}
								else {
									System.out.println("Deletion unsuccesful");
								}
							}
							//if "n", just keep going
							System.out.println();
						}
					} else if (input.equals("M") || input.equals("m")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[M]ake reservation		Search [H]otel		Search available [R]ooms");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						wrongAction:
						if (input.equals("M") || input.equals("m")) {
							//bookRoom
							System.out.print("Enter the hotel ID you want: ");
							while(!reader.hasNextInt()) reader.next();
							int hotelID = reader.nextInt();
							System.out.print("Enter the room ID you want: ");
							while(!reader.hasNextInt()) reader.next();
							int roomID=reader.nextInt();
							System.out.println("Enter start date in the format YYYY-MM-DD HH:MM:SS ");
							String date1 = null;
							outerloop1:
							while(reader.hasNextLine()) {
								date1 = reader.nextLine();
								if(!date1.equals("")){
									break outerloop1;
								}
							}
							SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							java.util.Date startDate = format1.parse(date1);
							System.out.println("Enter end date in the format YYYY-MM-DD HH:MM:SS ");
							String date2 = null;
							outerloop2:
							while(reader.hasNextLine()) {
								date2 = reader.nextLine();
								if(!date2.equals("")) {
									break outerloop2;
								}
							}
							SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							java.util.Date endDate = format2.parse(date2);//String sql = "INSERT into booking(user_id, hotel_id, room_id, start, end, updated_at) values ("+ 
								//loggedInUserID +","+room_id+","+start_date+","+"end_date" +", CONVERT(date, getDate())";
							
							java.util.Date currentDate = new java.util.Date();
							
							//check if booked
							int booked = -1;
							String sql = "SELECT booked FROM room WHERE room_id='"+roomID+"' and hotel_id='"+hotelID+"';";
							rs = stmt.executeQuery(sql);
							
							while (rs.next())
							{ 
								booked = rs.getInt("booked"); 
							}
							if(booked==-1) {
								System.out.println("This room does not exist");
								System.out.println();
								break wrongAction;
							}
							else if(booked==1) {
								System.out.println("Cannot make reservation for a taken room");
								System.out.println();
								break wrongAction;
							}
							
							//insert into booking
							sql = "INSERT INTO booking (user_id, hotel_id, room_id, date_booking_start, date_booking_end, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
							pStmt = conn.prepareStatement(sql);
							
							pStmt.setInt(1, loggedInUserID);
							pStmt.setInt(2, hotelID);
							pStmt.setInt(3, roomID);
							Timestamp startDateSql = new java.sql.Timestamp(startDate.getTime());
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String sDS = df.format(startDateSql);
							pStmt.setString(4, sDS);
							Timestamp endDateSql = new java.sql.Timestamp(endDate.getTime());
							String eDS = df.format(endDateSql);
							pStmt.setString(5, eDS);
							pStmt.setDate(6, new java.sql.Date(currentDate.getTime()));
							pStmt.addBatch();
							pStmt.executeBatch();
							
							//get booking_id
							int bookingID = 0;
							sql = "SELECT booking_id FROM booking WHERE user_id='"+loggedInUserID+"' and hotel_id='"+hotelID+"' and room_id='"+roomID+ "'and date_booking_start='"+sDS+"' and date_booking_end='"+eDS+"';";
							rs = stmt.executeQuery(sql);
							
							while (rs.next())
							{ 
								bookingID = rs.getInt("booking_id"); 
							}
							
							//insert into transaction 
							sql = "INSERT INTO transaction (booking_id) VALUES (?)";
							pStmt = conn.prepareStatement(sql);
							
							pStmt.setInt(1, bookingID);
							pStmt.executeUpdate();
							
							//update room table
							
							sql = "UPDATE room SET booking_id=?, booked=? WHERE room_id=?";
							pStmt = conn.prepareStatement(sql);
							
							pStmt.setInt(1, bookingID);
							pStmt.setInt(2, 1);
							pStmt.setInt(3, roomID);
							pStmt.executeUpdate();
							
							System.out.println("Reservation at hotel ID " + hotelID + ", room ID " + roomID + " from " +sDS+ " to " +eDS+ " was successfully booked!");
							System.out.println();
							
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
							System.out.println("HOTEL NAME | HOTEL ID | RATING | LOCATION | ROOM ID | ROOM NUMBER | PRICE");
							while (rs.next())
							{ System.out.println(rs.getString("name") + " | " + rs.getInt("hotel_id") + " | " + rs.getInt("rating") + " | " + rs.getString("city") + "," +rs.getString("state")+
									" | " + rs.getInt("room_id") + " | " + rs.getInt("room_number") + " | $" + rs.getInt("rate")); }
							
						}
					} else if (input.equals("C") || input.equals("c")) {
						System.out.println("Select one of the following options: ");
						System.out.println("[C]ancel reservation  c[H]ange reservation  [S]how all reservations  [M]ost visited city");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
						
						wrongAction2:
						if (input.equals("C") || input.equals("c")) {
							//cancelRoom
							System.out.println("Enter the hotel ID of the reservation you would like to cancel: ");
							int hotelID = reader.nextInt();
							System.out.println("Enter the room ID: ");
							int roomID = reader.nextInt();
							
							//select information of reservation to be deleted
							int bookingID = 0;
							String hotelName = null;
							String startDate = null;
							String endDate = null;
							String sql = "SELECT booking_id, name, date_booking_start,date_booking_end FROM booking b JOIN hotel h ON b.hotel_id=h.hotel_id WHERE user_id='"+loggedInUserID+"' AND b.hotel_id='"+hotelID+"' and room_id='"+roomID+"';";
							rs = stmt.executeQuery(sql);
							
							while(rs.next()) {
								bookingID = rs.getInt("booking_id");
								hotelName = rs.getString("name");
								startDate = rs.getString("date_booking_start");
								endDate = rs.getString("date_booking_end");
							}
							if(bookingID==0) {
								System.out.println("This reservation cannot be found. ");
								System.out.println();
								break wrongAction2;
							}
							
							System.out.println("Are you sure you want to delete reservation at " + hotelName + " from "+startDate+" to "+endDate+"?   [Y]es [N]o");
							System.out.println("Enter letter within a pair of brackets to select option");							
							outerloop1:
								while(reader.hasNextLine()) {
									input = reader.nextLine();
									if(!input.equals("")){
										break outerloop1;
									}
								}
							
							//if yes, delete
							if (input.toLowerCase().equals("y")) {
								//delete from booking
								sql = "DELETE FROM booking WHERE booking_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setInt(1, bookingID);
								pStmt.executeUpdate();
								
								//delete from transaction
								sql = "DELETE FROM transaction WHERE booking_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setInt(1, bookingID);
								pStmt.executeUpdate();
								
								//update room
								sql = "UPDATE room SET booking_id=null, booked=0 WHERE room_id=?";
								pStmt = conn.prepareStatement(sql);
								
								pStmt.setInt(1, roomID);
								pStmt.executeUpdate();
								
								System.out.println("Reservation deleted.");
							}
							
							//if n, keep going
							System.out.println();

						} else if (input.equals("H") || input.equals("h")) {
							//changeReservation
							System.out.print("Enter hotel id: ");
							int hotelId = reader.nextInt();
							System.out.print("Enter room id: ");
							int roomId = reader.nextInt();
							System.out.print("Enter new starting date in the format YYYY-MM-DD HH:MM:SS ");
							String start_date = null;
							outerloop1:
							while(reader.hasNextLine()) {
								start_date = reader.nextLine();
								if(!start_date.equals("")){
									break outerloop1;
								}
							}
							System.out.print("Enter new end date in the format YYYY-MM-DD HH:MM:SS ");
							String end_date = reader.nextLine();
							
							String sql = "UPDATE booking, user SET date_booking_start = ?, date_booking_end = ? WHERE booking.user_id = user.user_id and hotel_id = ? and room_id = ?";
							pStmt = conn.prepareStatement(sql);
							
							pStmt.setString(1, start_date);
							pStmt.setString(2, end_date);
							pStmt.setInt(3, hotelId);
							pStmt.setInt(4, roomId);
							pStmt.addBatch();
							pStmt.executeBatch();
							
							System.out.println("Booking updated successfully..");
							
						} else if (input.equals("S") || input.equals("s")) {
							//showAllReservations
							System.out.println("Here are all your reservations..");
							String sql = "SELECT * FROM user JOIN booking b JOIN hotel h ON b.hotel_id=h.hotel_id where user.user_id = b.user_id";
							rs = stmt.executeQuery(sql);
							
							System.out.println("First name | Last name | Hotel | City | Booking start date | Booking end date");
							while (rs.next()) {
								System.out.println(rs.getString("first_name") + " | " + rs.getString("last_name") + " | " + rs.getString("name") + " | " + rs.getString("city") +
										" | " + rs.getDate("date_booking_start") + " | " + rs.getDate("date_booking_end"));
							}
							
						} else if (input.equals("M") || input.equals("m")) {
							//getMostVistitedCity
							String city = null;
							String sql = "select city from (SELECT * FROM booking UNION SELECT * FROM bookingarchive) as a JOIN hotel o ON a.hotel_id=o.hotel_id WHERE user_id='"+loggedInUserID+"' group by city having count(city) = (SELECT max(count) FROM (SELECT count(city) count FROM (SELECT * FROM booking UNION SELECT * FROM bookingarchive) as b JOIN hotel h ON b.hotel_id=h.hotel_id WHERE user_id='"+loggedInUserID+"' group by city) as x);";
							rs = stmt.executeQuery(sql);
							
							while(rs.next()) {
								city = rs.getString("city");
							}
							
							if(city.equals(null)) {
								System.out.println("You have not made any reservations yet.");
							}
							else {
								System.out.println("Your most visited city is " + city + ".");
							}
							System.out.println();
						}
					}
					if(deleted == false) {
						System.out.println("Select one of the following options: ");
						System.out.println("[A]ccount [M]ake reservation [C]urrent reservation [L]og out");
						System.out.println("Enter letter within a pair of brackets to select option");
						input = reader.nextLine();
					}
					else {
						input = "l";
					}
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
