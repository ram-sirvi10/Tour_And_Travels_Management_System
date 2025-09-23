CREATE DATABASE tourAndTravelManagementSystem;
USE tourAndTravelManagementSystem;

-- USERS
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL,
    user_email VARCHAR(50) UNIQUE NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_role VARCHAR(10) DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE,
    is_delete BOOLEAN DEFAULT FALSE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- COMPANIES
CREATE TABLE travelAgency (
    agency_id INT AUTO_INCREMENT PRIMARY KEY,
    agency_name VARCHAR(150) NOT NULL,
    owner_name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pincode VARCHAR(10),
    registration_number VARCHAR(50),      
    password VARCHAR(255) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
     is_active BOOLEAN DEFAULT TRUE,
     is_delete BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TRAVEL PACKAGES
CREATE TABLE travel_packages (
    package_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    agency_id int not null,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    location VARCHAR(255),
    duration INT ,
    is_active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (agency_id) REFERENCES travelAgency(agency_id) ON DELETE CASCADE
);

-- BOOKINGS
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('CONFIRMED', 'PENDING', 'CANCELLED') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES travel_packages(package_id) ON DELETE CASCADE
);

-- PAYMENTS
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('SUCCESSFUL', 'FAILED') DEFAULT 'FAILED',
    amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);
-- TOURISTS
CREATE TABLE travelers (
    traveler_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    age int not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);
-- add new column(no_of_travellers) in booking table 
ALTER TABLE bookings
ADD COLUMN no_of_travellers INT NOT NULL DEFAULT 1;

-- drop unique constraints in tables  
ALTER TABLE users DROP INDEX user_email;
ALTER TABLE travelAgency DROP INDEX email;

-- add column for image url 
ALTER TABLE users
ADD COLUMN imageurl VARCHAR(200) ;
