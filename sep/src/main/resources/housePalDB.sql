CREATE DATABASE housePalBD;

DROP SCHEMA IF EXISTS housePal CASCADE;
CREATE SCHEMA housePal;
SET search_path TO housePal;   --ARREGLAR O PREGUNTAR

CREATE TABLE HouseOwner (
    owner_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    isVerified BOOLEAN DEFAULT FALSE
);

CREATE TABLE HouseSitter (
    sitter_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    experience TEXT,
    skills TEXT,
    email VARCHAR(100) NOT NULL UNIQUE,
    isVerified BOOLEAN DEFAULT FALSE
);

CREATE TABLE Admin (
    admin_id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE HouseProfile (
    profile_id SERIAL PRIMARY KEY,
    owner_id INT NOT NULL,
    description TEXT,
    chores TEXT,
    rules TEXT,
    amenities TEXT,
    FOREIGN KEY (owner_id) REFERENCES HouseOwner(owner_id) ON DELETE CASCADE
);

CREATE TABLE HouseListing (
    listing_id SERIAL PRIMARY KEY,
    profile_id INT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    status VARCHAR(15) NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE,
    CONSTRAINT listing_status_check CHECK (status IN ('Open', 'Closed', 'Unavailable'))
);

CREATE TABLE Application (
    application_id SERIAL PRIMARY KEY,
    listing_id INT NOT NULL,
    sitter_id INT NOT NULL,
    message TEXT,
    status VARCHAR(10) NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES HouseListing(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE CASCADE,
    CONSTRAINT application_status_check CHECK (status IN ('Pending', 'Approved', 'Rejected'))
);

CREATE TABLE Review (
    review_id SERIAL PRIMARY KEY,
    review_type VARCHAR(15) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    listing_id INT DEFAULT NULL,
    sitter_id INT DEFAULT NULL,
    FOREIGN KEY (listing_id) REFERENCES HouseListing(listing_id) ON DELETE SET NULL,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE SET NULL
    --CONSTRAINT review_type_check CHECK (Review.review_type IN ('SitterReview', 'OwnerReview'))
);

CREATE TABLE Report (
    report_id SERIAL PRIMARY KEY,
    report_type VARCHAR(20) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(10) NOT NULL,
    listing_id INT DEFAULT NULL,
    sitter_id INT DEFAULT NULL,
    admin_id INT DEFAULT NULL,
    FOREIGN KEY (listing_id) REFERENCES HouseListing(listing_id) ON DELETE SET NULL,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE SET NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL,
    CONSTRAINT report_status_check CHECK (status IN ('Pending', 'Resolved'))
    --CONSTRAINT report_type_check CHECK (report_type IN ('SitterReport', 'OwnerReport'))

);

---DUMMY DATA
INSERT INTO HouseOwner (name, address, phone, email, isVerified) VALUES
('Anders Hansen', 'Vesterbrogade 50, 1620 Copenhagen', '45-12345678', 'anders.hansen@gmail.com', TRUE),
('Marie Jensen', 'Nørrebrogade 78, 2200 Copenhagen N', '45-87654321', 'marie.jensen@gmail.com', TRUE),
('Mads Nielsen', 'Øster Allé 15, 2100 Copenhagen Ø', '45-23456789', 'mads.nielsen@gmail.com', FALSE),
('Emma Christensen', 'Amagerbrogade 120, 2300 Copenhagen S', '45-11223344', 'emma.christensen@gmail.com', TRUE),
('Søren Larsen', 'Falkoner Allé 12, 2000 Frederiksberg', '45-55667788', 'soren.larsen@gmail.com', FALSE);

INSERT INTO HouseSitter (name, experience, skills, email, isVerified) VALUES
('Emilie Larsen', '2 years of experience in pet sitting and gardening', 'Pet care, Gardening', 'emilie.larsen@gmail.com', TRUE),
('Kasper Pedersen', '1 year of experience, focused on cleaning and pet care', 'Cleaning, Pet care', 'kasper.pedersen@gmail.com', TRUE),
('Sofie Sørensen', 'New to house sitting, eager to learn', 'Basic cleaning, Plant care', 'sofie.sorensen@gmail.com', FALSE),
('Frederik Rasmussen', 'Experienced with large pets and house maintenance', 'Pet care, General maintenance', 'frederik.rasmussen@gmail.com', TRUE),
('Lise Møller', '5 years of experience with house sitting and pet care', 'Pet care, Cleaning', 'lise.moller@gmail.com', TRUE);

INSERT INTO Admin (email) VALUES
('admin1@housepal.dk'),
('admin2@housepal.dk'),
('admin3@housepal.dk');

INSERT INTO HouseProfile (owner_id, description, chores, rules, amenities) VALUES
(1, '3-bedroom house with a garden and 2 cats', 'Feed the cats, Water the plants, Clean the house', 'No smoking, No parties', 'WiFi, Garage, Garden'),
(2, 'Modern apartment, perfect for single house sitters', 'Water the plants, Collect the mail', 'No pets, No loud noise after 10PM', 'Air conditioner, WiFi, Parking spot'),
(3, 'Vacation house near the beach, great for summer', 'Feed the dog, Clean the pool, Water the plants', 'No pets allowed, No overnight guests', 'Swimming pool, WiFi, Garden, Garage'),
(4, 'Cozy cottage in the countryside', 'Take care of the chickens, Water the vegetable garden', 'No smoking, No loud noise', 'Fireplace, WiFi, Large garden'),
(5, 'Spacious house with a pool and gym', 'Clean the pool, Take care of the gym, Feed the fish', 'No pets, No parties', 'WiFi, Pool, Gym');

INSERT INTO HouseListing (profile_id, startDate, endDate, status) VALUES
(1, '2024-06-01', '2024-06-15', 'Open'),
(2, '2024-07-01', '2024-07-10', 'Open'),
(3, '2024-08-01', '2024-08-20', 'Closed'),
(4, '2024-09-01', '2024-09-30', 'Open'),
(5, '2024-10-01', '2024-10-15', 'Unavailable');

INSERT INTO HouseListing (profile_id, startDate, endDate, status) VALUES
(1, '2024-06-01', '2024-06-15', 'Open'),
(2, '2024-07-01', '2024-07-10', 'Open'),
(3, '2024-08-01', '2024-08-20', 'Closed'),
(4, '2024-09-01', '2024-09-30', 'Open'),
(5, '2024-10-01', '2024-10-15', 'Unavailable');

INSERT INTO Application (listing_id, sitter_id, message, status) VALUES
(1, 1, 'I have experience with cats and can take care of your home.', 'Pending'),
(2, 2, 'I can water the plants and collect the mail during your absence.', 'Approved'),
(3, 3, 'I would love to take care of the beach house.', 'Rejected'),
(4, 4, 'I am experienced with chickens and gardening, would love to help!', 'Pending'),
(5, 5, 'I can maintain the pool and gym, and take care of the fish.', 'Pending');

INSERT INTO Review (review_type, rating, comments, listing_id, sitter_id) VALUES
('HouseReview', 5, 'Emilie was fantastic, took great care of the cats and the house was spotless.', 1, 1),
('SitterReview', 4, 'The apartment was well-maintained, but some instructions were unclear.', 2, 2),
('HouseReview', 2, 'Sofie did not follow all the rules, and the dog was not well cared for.', 3, 3),
('SitterReview', 5, 'Frederik did an excellent job, everything was in perfect condition when I returned.', 4, 4),
('HouseReview', 4, 'Lise took good care of the fish and maintained the gym and pool well.', 5, 5);

INSERT INTO Report (report_type, reason, status, listing_id, sitter_id, admin_id) VALUES
('HouseOwnerReport', 'The sitter did not water the plants as instructed.', 'Pending', 1, 3, 1),
('HouseSitterReport', 'The owner did not provide clear instructions for pet care.', 'Resolved', 2, 2, 2),
('HouseOwnerReport', 'The sitter broke the no-smoking rule.', 'Pending', 3, 5, 3),
('HouseSitterReport', 'The house was not in a clean state upon arrival.', 'Resolved', 4, 1, 2);
