DROP DATABASE  IF EXISTS housePalBD;
CREATE DATABASE housePalBD;

DROP SCHEMA IF EXISTS housePal CASCADE;
CREATE SCHEMA housePal;
SET search_path TO housePal;  --fix or ask

CREATE TABLE Admin (
    admin_id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    profile_picture VARCHAR(255),
    CPR VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    isVerified BOOLEAN DEFAULT FALSE,
    admin_id INT DEFAULT NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL
);


CREATE TABLE HouseOwner (
    owner_id SERIAL PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    biography VARCHAR(1000) NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES Users(id) ON DELETE CASCADE
);



CREATE TABLE House_profile (
    profile_id SERIAL PRIMARY KEY,
    owner_id INT NOT NULL,
    description VARCHAR(1000) NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES houseowner(owner_id) ON DELETE CASCADE
);

CREATE TABLE House_pictures (
    profile_id INT NOT NULL,
    picture VARCHAR(255),
    FOREIGN KEY (profile_id) REFERENCES House_profile(profile_id) ON DELETE CASCADE
);

CREATE TABLE House_listing (
    listing_id SERIAL PRIMARY KEY,
    profile_id INT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('Open', 'Closed', 'Unavailable')),
    FOREIGN KEY (profile_id) REFERENCES House_profile(profile_id) ON DELETE CASCADE
);

CREATE TABLE Chores (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE Rules (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE Amenities (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE Skills (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE House_rules (
    profile_id INT NOT NULL,
    rule_id INT NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES House_profile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (rule_id) REFERENCES Rules(id) ON DELETE CASCADE
);

CREATE TABLE House_chores (
    profile_id INT NOT NULL,
    chore_id INT NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES House_profile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (chore_id) REFERENCES Chores(id) ON DELETE CASCADE
);

CREATE TABLE House_amenities (
    profile_id INT NOT NULL,
    amenity_id INT NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES House_profile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (amenity_id) REFERENCES Amenities(id) ON DELETE CASCADE
);


CREATE TABLE House_sitter (
    sitter_id INT PRIMARY KEY,
    past_experience VARCHAR(1000) NOT NULL,
    biography VARCHAR(1000) NOT NULL,
    FOREIGN KEY (sitter_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE Sitter_pictures (
    sitter_id INT NOT NULL,
    picture VARCHAR(255),
    FOREIGN KEY (sitter_id) REFERENCES House_sitter(sitter_id) ON DELETE CASCADE
);

CREATE TABLE Sitter_skills (
    sitter_id INT NOT NULL,
    skill_id INT NOT NULL,
    FOREIGN KEY (sitter_id) REFERENCES House_sitter(sitter_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES Skills(id) ON DELETE CASCADE
);


CREATE TABLE Application (
    application_id SERIAL PRIMARY KEY,
    listing_id INT NOT NULL,
    sitter_id INT NOT NULL,
    message VARCHAR(10000) NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('Pending', 'Approved', 'Rejected')),
    date DATE NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES House_listing(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (sitter_id) REFERENCES House_sitter(sitter_id) ON DELETE CASCADE
);

CREATE TABLE House_review (
    id SERIAL PRIMARY KEY,
    listing_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(1000) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES House_listing(listing_id) ON DELETE CASCADE
);

CREATE TABLE Sitter_review (
    id SERIAL PRIMARY KEY,
    listing_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(1000) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES House_listing(listing_id) ON DELETE CASCADE
);

CREATE TABLE Report (
    report_id SERIAL PRIMARY KEY,
    listing_id INT DEFAULT NULL,
    sitter_id INT DEFAULT NULL,
    admin_id INT DEFAULT NULL,
    comments VARCHAR(1000) NOT NULL,
    status VARCHAR(10) DEFAULT 'Pending',
    FOREIGN KEY (listing_id) REFERENCES House_listing(listing_id) ON DELETE SET NULL,
    FOREIGN KEY (sitter_id) REFERENCES House_sitter(sitter_id) ON DELETE SET NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL
);


---DUMMY DATA

INSERT INTO Admin (email, password) VALUES
('admin1@housepal.com', '12345'),
('admin2@housepal.com', '12345');

INSERT INTO Users (email, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES
('niels.jensen@gmail.com', '12345', 'profile1.jpg', '123456-7890', '+45 12345678', TRUE, 1),
('karen.sorensen@gmail.com', '12345', 'profile2.jpg', '234567-8901', '+45 23456789', TRUE, 1),
('kristian.jensen@gmail.com', '12345', 'profile5.jpg', '777777-1111', '+45 34567790', FALSE, NULL),
('lene.moller@gmail.com', '12345', 'profile6.jpg', '452229-2223', '+45 45678901', TRUE, 2),
('mikkel.pedersen@gmail.com', '12345', 'profile3.jpg', '345678-9012', '+45 34567890', FALSE, NULL),
('anne.larsen@gmail.com', '12345', 'profile4.jpg', '456789-0123', '+45 45678901', TRUE, 2);

INSERT INTO HouseOwner (address, biography) VALUES
('123 Nyhavn, Copenhagen', 'Loves traveling and pets. Looking for responsible sitters.'),
('45 Amagerbrogade, Copenhagen', 'Looking for someone to take care of plants and garden.'),
('78 Aarhusvej, Aarhus', 'We often travel, need sitters to care for our dogs.'),
('101 Odensegade, Odense', 'Cats lover, need help when on business trips.');


INSERT INTO House_profile (owner_id, description) VALUES
(1, 'Beautiful house with a garden in central Copenhagen.'),
(2, 'Charming apartment near the beach with a large terrace.'),
(3, 'Spacious villa in Aarhus with a backyard. Perfect for dog lovers.'),
(4, 'Modern house in Odense. Cozy and perfect for cat lovers.');

INSERT INTO House_pictures (profile_id, picture) VALUES
(1, 'house1.jpg'),
(1, 'house11.jpg'),
(1, 'house111.jpg'),
(2, 'house2.jpg'),
(2, 'house22.jpg'),
(2, 'house222.jpg'),
(3, 'house3.jpg'),
(3, 'house33.jpg'),
(3, 'house333.jpg'),
(4, 'house4.jpg'),
(4, 'house44.jpg'),
(4, 'house444.jpg');


INSERT INTO House_listing (profile_id, startDate, endDate, status) VALUES
(1, '2024-11-01', '2024-11-30', 'Open'),
(2, '2024-12-01', '2024-12-15', 'Open'),
(3, '2024-10-01', '2024-10-15', 'Closed'),
(4, '2024-12-20', '2025-01-05', 'Open');
INSERT INTO Chores (type) VALUES
('Water plants'),
('Feed pets'),
('Take out trash'),
('Mow the lawn');
INSERT INTO Rules (type) VALUES
('No smoking'),
('No parties'),
('Keep noise to a minimum');
INSERT INTO Amenities (type) VALUES
('Wi-Fi'),
('Washing machine'),
('Dishwasher'),
('Parking');
INSERT INTO Skills (type) VALUES
('Pet care'),
('Gardening'),
('Cleaning'),
('Home maintenance');
INSERT INTO House_rules (profile_id, rule_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 1),
(4, 2);
INSERT INTO House_chores (profile_id, chore_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 2),
(4, 3);
INSERT INTO House_amenities (profile_id, amenity_id) VALUES
(1, 1),
(1, 3),
(2, 1),
(2, 4),
(3, 1),
(4, 2);
INSERT INTO House_sitter (sitter_id, past_experience, biography) VALUES
(5, 'Previous experience caring for dogs and cats.', 'Animal lover, available for short-term house sits.'),
(6, 'Skilled in plant care and pet care.', 'Looking for long-term sitting opportunities in Denmark.');
INSERT INTO Sitter_pictures (sitter_id, picture) VALUES
(5, 'sitter5.jpg'),
(5, 'sitter55.jpg'),
(5, 'sitter555.jpg'),
(6, 'sitter6.jpg'),
(6, 'sitter66.jpg'),
(6, 'sitter666.jpg');
INSERT INTO Sitter_skills (sitter_id, skill_id) VALUES
(5, 1),
(5, 2),
(5, 3),
(6, 1),
(6, 2),
(6, 4);
INSERT INTO Application (listing_id, sitter_id, message, status, date) VALUES
(1, 5, 'I am available and love taking care of pets!', 'Pending', '2024-10-22'),
(2, 6, 'Experienced sitter available for your house and garden.', 'Approved', '2024-10-23');
INSERT INTO House_review (listing_id, rating, comments, date) VALUES
(1, 5, 'Beautiful house, great experience!', '2024-10-20'),
(2, 4, 'Lovely home, perfect location.', '2024-10-21');
INSERT INTO Sitter_review (listing_id, rating, comments, date) VALUES
(1, 5, 'Excellent sitter, took great care of our pets.', '2024-10-22'),
(2, 4, 'Very reliable and responsible.', '2024-10-21');
INSERT INTO Report (listing_id, sitter_id, admin_id, comments, status) VALUES
(1, 5, 1, 'Sitter was not responsive.', 'Pending'),
(2, 6, 2, 'House was left untidy.', 'Resolved');
