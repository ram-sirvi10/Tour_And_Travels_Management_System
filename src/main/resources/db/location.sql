 USE tourAndTravelManagementSystem;
 -- Country Table
CREATE TABLE countries (
    country_id INT PRIMARY KEY AUTO_INCREMENT,
    country_name VARCHAR(50) NOT NULL
);

-- State Table
CREATE TABLE states (
    state_id INT PRIMARY KEY AUTO_INCREMENT,
    state_name VARCHAR(50) NOT NULL,
    country_id INT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES countries(country_id)
);

-- City Table
CREATE TABLE cities (
    city_id INT PRIMARY KEY AUTO_INCREMENT,
    city_name VARCHAR(50) NOT NULL,
    state_id INT NOT NULL,
    FOREIGN KEY (state_id) REFERENCES states(state_id)
);

-- Area Table
CREATE TABLE areas (
    area_id INT PRIMARY KEY AUTO_INCREMENT,
    area_name VARCHAR(50) NOT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (city_id) REFERENCES cities(city_id)
);

-- Pincode Table
CREATE TABLE pincodes (
    pincode_id INT PRIMARY KEY AUTO_INCREMENT,
    pincode VARCHAR(10) NOT NULL,
    area_id INT NOT NULL,
    FOREIGN KEY (area_id) REFERENCES areas(area_id)
);

INSERT INTO countries (country_name) VALUES 
('India');

INSERT INTO states (state_name, country_id) VALUES 
('Uttar Pradesh', 1),
('Maharashtra', 1),
('Madhya Pradesh', 1),
('Tamil Nadu', 1),
('Karnataka', 1);

INSERT INTO cities (city_name, state_id) VALUES
-- Uttar Pradesh
('Lucknow', 1), ('Kanpur', 1), ('Varanasi', 1), ('Prayagraj', 1), ('Gorakhpur', 1),
-- Maharashtra
('Mumbai', 2), ('Pune', 2), ('Nagpur', 2), ('Nashik', 2), ('Aurangabad', 2),
-- Madhya Pradesh
('Indore', 3), ('Bhopal', 3), ('Gwalior', 3), ('Jabalpur', 3), ('Ujjain', 3),
-- Tamil Nadu
('Chennai', 4), ('Coimbatore', 4), ('Madurai', 4), ('Tiruchirappalli', 4), ('Salem', 4),
-- Karnataka
('Bengaluru', 5), ('Mysuru', 5), ('Mangalore', 5), ('Hubli', 5), ('Belgaum', 5);

INSERT INTO areas (area_name, city_id) VALUES
-- Lucknow
('Alambagh', 1), ('Indira Nagar', 1), ('Gomti Nagar', 1), ('Rajaji Puram', 1), ('Chinhat', 1),
-- Kanpur
('Kidwai Nagar', 2), ('Swaroop Nagar', 2), ('Kakadev', 2), ('Barra', 2), ('Kalyanpur', 2),
-- Mumbai
('Bandra', 6), ('Andheri', 6), ('Malad', 6), ('Vile Parle', 6), ('Kurla', 6),
-- Pune
('Shivaji Nagar', 7), ('Karve Nagar', 7), ('Wakad', 7), ('Bibwewadi', 7), ('Hadapsar', 7),
-- Indore
('Rajwada', 11), ('Vijay Nagar', 11), ('Chandan Nagar', 11), ('Kanchmilan', 11), ('Ban Ganga', 11);

INSERT INTO pincodes (pincode, area_id) VALUES
-- Lucknow
('226005', 1), ('226016', 2), ('226010', 3), ('226017', 4), ('226028', 5),
-- Kanpur
('208001', 6), ('208002', 7), ('208025', 8), ('208027', 9), ('208017', 10),
-- Mumbai
('400050', 11), ('400053', 12), ('400064', 13), ('400057', 14), ('400070', 15),
-- Pune
('411005', 16), ('411004', 17), ('411057', 18), ('411037', 19), ('411028', 20),
-- Indore
('452002', 21), ('452010', 22), ('452011', 23), ('452001', 24), ('452003', 25);


