DROP DATABASE IF EXISTS HOTELSYS;
CREATE DATABASE HOTELSYS;
USE HOTELSYS;

DROP TABLE IF EXISTS HOTEL;
CREATE TABLE HOTEL
(hotel_id INT NOT NULL auto_increment,
name VARCHAR(30) NOT NULL,
address VARCHAR(30) NOT NULL,
city VARCHAR(30) NOT NULL,
state VARCHAR(30) NOT NULL,
postal_code INT NOT NULL,
rating INT, 
PRIMARY KEY(hotel_id, name)
);

DROP TABLE IF EXISTS ROOM;
CREATE TABLE ROOM 
(room_id INT NOT NULL auto_increment,
room_number INT NOT NULL,
rate INT NOT NULL,
hotel_id INT REFERENCES HOTEL(hotel_id),
booking_id INT REFERENCES BOOKING(booking_id),
booked TINYINT(1),
PRIMARY KEY(room_id, room_number, rate)
);

DROP TABLE IF EXISTS TRANSACTION;
CREATE TABLE TRANSACTION 
(trans_id INT NOT NULL auto_increment,
Booking_id INT NOT NULL,
PRIMARY KEY(trans_id)
);
	
DROP TABLE IF EXISTS USER;
CREATE TABLE user(
user_id INT NOT NULL auto_increment, 
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
email VARCHAR(30) NOT NULL UNIQUE,
userPassword VARCHAR(30) NOT NULL,
PRIMARY KEY(user_id));

DROP TABLE IF EXISTS BOOKING;
CREATE TABLE booking(
booking_id INT NOT NULL auto_increment,
user_id int NOT NULL references user(user_id),
hotel_id int NOT NULL references hotel(hotel_id), 
room_id int NOT NULL references room(room_id),
date_booking_start datetime NOT NULL,
date_booking_end datetime NOT NULL,
updated_at date NOT NULL,
PRIMARY KEY(booking_id));

DROP TABLE IF EXISTS BOOKINGARCHIVE;
CREATE TABLE BOOKINGARCHIVE(
booking_id INT NOT NULL,
user_id int NOT NULL,
hotel_id int NOT NULL, 
room_id int NOT NULL,
date_booking_start datetime NOT NULL,
date_booking_end datetime NOT NULL,
updated_at date NOT NULL,
PRIMARY KEY(booking_id));

DELIMITER //
CREATE PROCEDURE archiveBookings(IN currentDate date)
BEGIN
INSERT INTO bookingarchive SELECT * FROM booking WHERE updated_at>currentDate;
DELETE FROM booking WHERE update_at>currentDate;
END //
DELIMITER ;

Delimiter //
Create trigger userDeleted
After delete on user 
For each row
Begin 
Delete from booking 
Where user_id = old.user_id;
End//
Delimiter ;

Delimiter //
create trigger updateID
AFTER update ON room
for each row
BEGIN
	update booking
	set room_id = new.room_id
	where room_id = old.room_id;
END//
Delimiter ;

LOCK TABLES `USER` WRITE;
INSERT INTO `USER` VALUES (1,'jon','wong','jonwong@email','jonwongpassword'),(2,'Vera','Wong','verawong@email.com','verawongpassword'),(3,'Steven','Louie','stevenlouie@email.com','stevenlouiepassword'),(4,'Rachel','Green','centralperk@email.com','rachellovesgunther'),(5,'Joey','Tribbiani','drakeramoray@dayofourlives.com','iloverachel'),(6,'Chandler','Bing','chanandlerbong@email.com','matthewperry'),(7,'Ross','Geller','fossils@nyu.edu','davidschwimmer'),(8,'Phoebe','Buffay','pbuffay@massage.com','lisakudrow'),(9,'Monica','Geller','mgeller@email.com','ihatesmoking'),(10,'Administrator','Account','admin@hotel.com','eleanor';
UNLOCK TABLES;

LOCK TABLES `room` WRITE;
INSERT INTO `room` (room_id, room_number, rate, hotel_id, booked) VALUES (1001,432,199,9001, 0),(1002,635,119,9001,0),(1003,639,129,9001,0),(1004,832,119,9001,0),(1005,519,99,9001,0),(1006,645,119,9001,0),(1014,923,299,9002,0),(1015,623,199,9002,0),(1016,532,99,9002,0),(1017,619,100,9002,0),(1018,719,149,9002,0),(1019,723,199,9002,0),(1020,523,139,9002,0),(1021,535,129,9003,0),(1022,735,99,9003,0),(1023,691,129,9003,0),(1024,681,99,9003,0);
UNLOCK TABLES;

LOCK TABLES `HOTEL` WRITE;
INSERT INTO `HOTEL` VALUES (9001,'Mariott San Jose','201 South 4th Street','San Jose','CA',95112,5),(9002,'Hilton San Jose','487 South 8th Street','San Jose','CA',95112,5),(9003,'Fairmont San Jose','170 S Market Street','San Jose','CA',95112,4);
UNLOCK TABLES;