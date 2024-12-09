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
    name VARCHAR(50) NOT NULL,
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

CREATE TABLE HouseProfile (
    profile_id SERIAL PRIMARY KEY,
    owner_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    address VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES houseowner(owner_id) ON DELETE CASCADE
);

CREATE TABLE House_pictures (
    profile_id INT NOT NULL,
    picture VARCHAR(255),
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, picture)
);

CREATE TABLE House_listing (
    listing_id SERIAL PRIMARY KEY,
    profile_id INT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('Open', 'Closed', 'Unavailable')),
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE
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
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (rule_id) REFERENCES Rules(id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, rule_id)
);

CREATE TABLE House_chores (
    profile_id INT NOT NULL,
    chore_id INT NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (chore_id) REFERENCES Chores(id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, chore_id)
);

CREATE TABLE House_amenities (
    profile_id INT NOT NULL,
    amenity_id INT NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (amenity_id) REFERENCES Amenities(id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, amenity_id)
);

CREATE TABLE HouseSitter (
    sitter_id INT PRIMARY KEY,
    past_experience VARCHAR(1000) NOT NULL,
    biography VARCHAR(1000) NOT NULL,
    FOREIGN KEY (sitter_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE Sitter_pictures (
    sitter_id INT NOT NULL,
    picture VARCHAR(255),
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE CASCADE,
    PRIMARY KEY (sitter_id, picture)
);

CREATE TABLE Sitter_skills (
    sitter_id INT NOT NULL,
    skill_id INT NOT NULL,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES Skills(id) ON DELETE CASCADE,
    PRIMARY KEY (sitter_id, skill_id)
);

CREATE TABLE Application (
    listing_id INT NOT NULL,
    sitter_id INT NOT NULL,
    message VARCHAR(10000) NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('Pending', 'Approved', 'Confirmed', 'Rejected', 'Canceled')),
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES House_listing(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE CASCADE,
    PRIMARY KEY (listing_id, sitter_id)
);

CREATE TABLE House_review (
    profile_id INT NOT NULL,
    sitter_id INT NOT NULL,

    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(1000) NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES HouseProfile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE CASCADE,
    PRIMARY KEY (profile_id, sitter_id)
);

CREATE TABLE Sitter_review (
    owner_id INT NOT NULL,
    sitter_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(1000) NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES HouseOwner(owner_id) ON DELETE CASCADE,
    FOREIGN KEY (sitter_id) REFERENCES HouseSitter(sitter_id) ON DELETE CASCADE,
    PRIMARY KEY (owner_id, sitter_id)
);

CREATE TABLE Report (
    report_id SERIAL PRIMARY KEY,
    reporting_id INT DEFAULT NULL,
    reported_id INT DEFAULT NULL,
    admin_id INT DEFAULT NULL,
    comments VARCHAR(1000) NOT NULL,
    status VARCHAR(10) DEFAULT 'Pending',
    date TIMESTAMP,
    FOREIGN KEY (reporting_id) REFERENCES Users(id) ON DELETE SET NULL,
    FOREIGN KEY (reported_id) REFERENCES Users(id) ON DELETE SET NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ON DELETE SET NULL
);

---DUMMY DATA

INSERT INTO Admin (email, password) VALUES
('admin1@housepal.com', '12345'),
('admin2@housepal.com', '12345');

INSERT INTO Users (email, name, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES
('jonas.hansen@gmail.com', 'Jonas Hansen', '12345', 'sitter_jonas.jpg', '123456-7890', '+45 12345678', TRUE, 1),
('sophie.madsen@gmail.com', 'Sophie Madsen', '12345', 'sitter_sophie.jpg', '234567-8901', '+45 23456789', TRUE, 1),
('emilie.pedersen@gmail.com', 'Emilie Pedersen', '12345', 'sitter_emilie.jpg', '345678-9012', '+45 34567890', TRUE, 1),
('kasper.andersen@gmail.com', 'Kasper Andersen', '12345', 'sitter_kasper.jpg', '456789-0123', '+45 45678901', TRUE, 2),
('camille.larsen@gmail.com', 'Camille Larsen', '12345', 'sitter_camille.jpg', '567890-1234', '+45 56789012', TRUE, 2),
('line.jensen@gmail.com', 'Line Jensen', '12345', 'sitter_line.jpg', '678901-2345', '+45 67890123', TRUE, 2),
('mads.nielsen@gmail.com', 'Mads Nielsen', '12345', 'sitter_mads.jpg', '789012-3456', '+45 78901234', TRUE, 2);

INSERT INTO HouseSitter (sitter_id, past_experience, biography) VALUES
(1, 'I have cared for pets like dogs and cats during vacations and maintained gardens in both urban and rural homes.', 'Hi, I’m Jonas! I love animals and traveling. I enjoy helping homeowners feel secure by taking care of their pets and homes.'),
(2, 'I have experience maintaining indoor plants and managing outdoor gardens. I’ve also cared for small pets.', 'Hi, I’m Sophie! I’m passionate about nature and ensuring that every home I stay in looks beautiful and well-kept.'),
(3, 'I have specialized in caring for exotic pets like turtles and fish, and I’m skilled in maintaining aquariums and ponds.', 'Hi, I’m Emilie! I’m a certified pet trainer who loves working with unique animals while exploring new places.'),
(4, 'I’ve taken care of large gardens and ensured lawns, flowers, and vegetables are in perfect condition.', 'Hi, I’m Kasper! I’m a responsible traveler who enjoys helping homeowners maintain their homes and pets while they’re away.'),
(5, 'I have managed urban apartments with pets, including dogs and cats, and I have a talent for caring for indoor plants.', 'Hi, I’m Camille! I love city life and am skilled in keeping apartments clean and pets happy during your trips.'),
(6, 'I’ve cared for rural homes with pets and large gardens. I’m experienced in both short-term and long-term stays.', 'Hi, I’m Line! I’m passionate about sustainable living and helping homeowners keep their spaces beautiful and their pets loved.'),
(7, 'I’m experienced in taking care of dogs and maintaining beautiful flower gardens like hydrangeas and rose bushes.', 'Hi, I’m Mads! I’m an animal lover and gardener who enjoys making sure both pets and gardens are thriving.');

INSERT INTO Users (email, name, password, profile_picture, CPR, phone, isVerified, admin_id) VALUES
('niels.jensen@gmail.com', 'Niels Jensen', '12345', 'owner_niels.jpg', '123456-7890', '+45 22334455', TRUE, 1),
('karen.sorensen@gmail.com', 'Karen Sorensen', '12345', 'owner_karen.jpg', '234567-8901', '+45 33445566', TRUE, 1),
('henrik.madsen@gmail.com', 'Henrik Madsen', '12345', 'owner_henrik.jpg', '345678-9012', '+45 44556677', TRUE, 1),
('anne.andersen@gmail.com', 'Lotte Andersen', '12345', 'owner_anne.jpg', '456789-0123', '+45 55667788', TRUE, 2);

INSERT INTO HouseOwner (owner_id, address, biography) VALUES
(8, 'Strandvej 10, Haderslev', 'Hi, I’m Niels. I’m a retired teacher who loves to travel frequently and meet new people. I’m looking for responsible sitters to help while I’m away.'),
(9, 'Nørrebro 45, Copenhagen', 'Hi, I’m Karen. I’m an architect who often travels for work and needs reliable sitters during my trips.'),
(10, 'Sønder Allé 12, Aarhus', 'Hi, I’m Henrik. I’m a software developer who travels regularly and values trustworthy sitters to take care of things while I’m gone.'),
(11, 'Lindevang 34, Ry', 'Hi, I’m Lotte. I’m a nature photographer who frequently embarks on long assignments and seeks dependable sitters to assist during my absences.');


INSERT INTO HouseProfile (owner_id, title, description, address, region, city) VALUES
(8, 'Beachfront House', 'A spacious beach house with a serene view of the ocean and three turtles to care for. This home features four bedrooms, offering ample space for sitters to enjoy their stay. Additionally, it includes a private garage, making it convenient for those traveling with a vehicle.', 'Strandvej 10', 'Sønderjylland', 'Haderslev'),
(8, 'Urban Oasis', 'An apartment located in the heart of Copenhagen with a large terrace filled with indoor plants to maintain and a friendly dog. This modern home has two bedrooms, providing a comfortable and stylish stay. While there is no garage, the apartment is close to public transport.', 'Nørrebro 45', 'Hovedstaden', 'Copenhagen'),

(9, 'Countryside Getaway', 'A spacious countryside home surrounded by greenery, ideal for sitters who love nature and animals. The house includes three bedrooms, a private garage, and a large garden that requires maintenance. Two friendly dogs will keep you company during your stay.', 'Lindevang 34', 'Midtjylland', 'Ry'),
(9, 'City Getaway', 'A pet-friendly apartment located near Copenhagen’s main attractions, perfect for city lovers. It has two cozy bedrooms, a terrace with plants to maintain, and no garage. The apartment is ideal for sitters who enjoy vibrant urban life and caring for a dog and a fish.', 'Nørrebro 50', 'Hovedstaden', 'Copenhagen'),

(10, 'Sea Breeze Retreat', 'A beautiful home overlooking the sea in Skagen, perfect for sitters who enjoy coastal living. The house includes three bedrooms, a cozy garden, and a garage. A dog and fish require care during your stay.', 'Strandvej 15', 'Nordjylland', 'Skagen'),
(10, 'Cozy Aarhus Retreat', 'A charming and bright apartment in Aarhus with one playful cat. The home features one spacious bedroom, modern amenities, and plenty of on-street parking. It is located in a peaceful neighborhood with easy access to parks and public transport.', 'Sønder Allé 12', 'Midtjylland', 'Aarhus'),

(11, 'House with Garden in Nyborg', 'A tranquil home nestled in the countryside of Nyborg, perfect for garden, cat and dog enthusiasts. This cozy property has two bedrooms, a garage, and a large garden filled with flowers and greenery, including hydrangeas. Two pets live here and enjoy daily care.', 'Lindevang 40', 'Fyn', 'Nyborg'),
(11, 'House in Skagen', 'A house in Skagen with a large patio, a small garden filled with hydrangeas, and two friendly cats. The property has two bedrooms and a warm, inviting atmosphere, making it perfect for cat lovers.', 'Havnegade 20', 'Nordjylland', 'Skagen');



INSERT INTO House_pictures (profile_id, picture) VALUES
(1, 'haderslev1.jpg'),
(1, 'haderslev2.jpg'),
(1, 'haderslev3.jpg'),
(1, 'haderslev4.jpg'),
(1, 'haderslev5.jpg'),
(1, 'haderslev6.jpg'),
(1, 'haderslev7.jpg'),

(2, 'Cope1.jpg'),
(2, 'Cope2.jpg'),
(2, 'Cope3.jpg'),
(2, 'Cope4.jpg'),
(2, 'Cope5.jpg'),
(2, 'Cope6.jpg'),

(3, 'ry1.jpg'),
(3, 'ry2.jpg'),
(3, 'ry3.jpg'),
(3, 'ry4.jpg'),
(3, 'ry5.jpg'),
(3, 'ry6.jpg'),

(4, 'copen1.jpg'),
(4, 'copen2.jpg'),
(4, 'copen3.jpg'),
(4, 'copen4.jpg'),
(4, 'copen5.jpg'),
(4, 'copen6.jpg'),
(4, 'copen7.jpg'),
(4, 'copen8.jpg'),

(5, 'skagen1.jpg'),
(5, 'skagen2.jpg'),
(5, 'skagen3.jpg'),
(5, 'skagen4.jpg'),
(5, 'skagen5.jpg'),
(5, 'skagen6.jpg'),
(5, 'skagen7.jpg'),

(6, 'aarhus1.jpg'),
(6, 'aarhus2.jpg'),
(6, 'aarhus3.jpg'),
(6, 'aarhus4.jpg'),

(7, 'nyborg1.jpg'),
(7, 'nyborg2.jpg'),
(7, 'nyborg3.jpg'),
(7, 'nyborg4.jpg'),
(7, 'nyborg5.jpg'),

(8, 'ska1.jpg'),
(8, 'ska2.jpg'),
(8, 'ska3.jpg'),
(8, 'ska4.jpg'),
(8, 'ska5.jpg')

;

INSERT INTO House_listing (profile_id, startDate, endDate, status) VALUES
(1, '2024-10-01', '2024-10-10', 'Closed'),
(1, '2025-02-01', '2025-02-10', 'Open'),

(2, '2024-09-15', '2024-09-25', 'Closed'),
(2, '2025-02-01', '2025-02-12', 'Open'),

(3, '2024-08-01', '2024-08-10', 'Closed'),
(3, '2025-02-05', '2025-02-15', 'Open'),

(4, '2024-07-01', '2024-07-10', 'Closed'),
(4, '2025-02-10', '2025-02-18', 'Open'),

(5, '2024-06-01', '2024-06-10', 'Closed'),
(5, '2025-02-01', '2025-02-09', 'Open'),

(6, '2024-11-10', '2024-11-20', 'Closed'),
(6, '2025-02-05', '2025-02-14', 'Open'),

(7, '2024-10-15', '2024-10-25', 'Closed'),
(7, '2025-02-01', '2025-02-08', 'Open'),

(8, '2024-09-20', '2024-09-30', 'Closed'),
(8, '2025-02-10', '2025-02-18', 'Open');


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
(2, 2),
(3, 3),
(4, 1),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(1, 2),
(2, 1),
(3, 2),
(4, 3),
(5, 1);

INSERT INTO House_chores (profile_id, chore_id) VALUES
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(2, 3),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(4, 1),
(4, 2),
(4, 3),
(5, 1),
(5, 2),
(5, 3),
(6, 2),
(6, 3),
(7, 1),
(7, 2),
(7, 3),
(7, 4),
(8, 1),
(8, 2);


INSERT INTO House_amenities (profile_id, amenity_id) VALUES
(1, 1),
(1, 3),
(1, 4),
(2, 1),
(2, 2),
(2, 4),
(3, 1),
(3, 3),
(4, 1),
(4, 4),
(5, 1),
(6, 1),
(6, 2),
(7, 1),
(7, 2),
(7, 4),
(8, 1),
(8, 2),
(8, 4);


INSERT INTO Sitter_pictures (sitter_id, picture) VALUES
(1, 'sitter_jonas1.jpg'),(1, 'sitter_jonas2.jpg'),(1, 'sitter_jonas3.jpg'),(1, 'sitter_jonas4.jpg'),(1, 'sitter_jonas5.jpg'),
(2, 'sitter_sophie1.jpg'),(2, 'sitter_sophie2.jpg'),(2, 'sitter_sophie3.jpg'),(2, 'sitter_sophie4.jpg'),
(3, 'sitter_emilie1.jpg.jpg'),(3, 'sitter_emilie2.jpg.jpg'),(3, 'sitter_emilie3.jpg.jpg'),
(4, 'sitter_kasper1.jpg'),(4, 'sitter_kasper2.jpg'),(4, 'sitter_kasper3.jpg'),
(5, 'sitter_camille1.jpg'),(5, 'sitter_camille2.jpg'),(5, 'sitter_camille3.jpg'),(5, 'sitter_camille4.jpg'),
(6, 'sitter_line1.jpg'),(6, 'sitter_line2.jpg'),(6, 'sitter_line3.jpg'),(6, 'sitter_line4.jpg'),
(7, 'sitter_mads1.jpg'),(7, 'sitter_mads2.jpg'),(7, 'sitter_mads3.jpg');

INSERT INTO Sitter_skills (sitter_id, skill_id) VALUES
(1, 1),
(1, 2),
(1, 4),
(2, 2),
(2, 4),
(3, 1),
(3, 4),
(4, 2),
(4, 3),
(4, 4),
(5, 1),
(5, 3),
(6, 1),
(6, 2),
(6, 4),
(7, 1),
(7, 2),
(7, 3);
INSERT INTO Application (listing_id, sitter_id, message, status, date) VALUES
(1, 1, 'I am experienced with exotic pets like turtles and would love to care for your home. My attention to detail ensures your pets and property will be in the best hands. I understand the specific needs of turtles and have previously managed similar responsibilities in a coastal home. I would be happy to maintain your property and keep you updated throughout your trip.', 'Confirmed', '2024-09-20'),
(1, 2, 'I have cared for aquatic pets before and would enjoy staying at your beachside house. I will ensure everything is well-maintained. My prior experience includes caring for fish and amphibians, so I am familiar with the requirements of marine animals. I am also respectful of homeowners’ preferences and always leave properties spotless.', 'Rejected', '2024-09-22'),

(2, 3, 'I have previous experience with indoor plants and dogs, making me a great fit for your apartment. I enjoy caring for pets and ensuring plants are healthy and thriving. My attention to detail and love for animals make me confident I can provide the best care for your home. I also prioritize communication and will keep you updated on your dog and plants regularly.', 'Confirmed', '2024-09-15'),
(2, 5, 'I am skilled in managing indoor plants and caring for dogs. Your home matches my expertise. I have cared for similar apartments before and understand the importance of keeping both pets and plants healthy. Additionally, I am very organized and ensure the home is left in pristine condition after the stay.', 'Rejected', '2024-09-17'),

(3, 6, 'I specialize in rural properties and enjoy caring for dogs and gardens. Your property aligns perfectly with my skills. I have maintained large outdoor spaces before and can handle any tasks related to gardening or pet care. My prior hosts have praised my dedication and attention to detail, and I would love to bring the same level of care to your home.', 'Confirmed', '2024-08-01'),
(3, 4, 'I am a responsible sitter with experience maintaining large gardens and caring for pets. I take pride in ensuring pets are comfortable and gardens are well-maintained during homeowners’ absences. I am happy to accommodate any specific requests and am always prompt with updates about the property.', 'Rejected', '2024-08-05'),

(4, 6, 'I love the vibrant city life and can ensure your pets and plants are well cared for. I have managed similar apartments before and understand the needs of indoor pets and plants. My approach focuses on providing attentive care and maintaining the property’s cleanliness and organization. I am confident I can meet your expectations and make your return stress-free.', 'Confirmed', '2024-07-03'),
(4, 5, 'I have experience in caring for small dogs and plants, making me an excellent fit for your city apartment. I enjoy staying in urban environments and am familiar with the responsibilities of caring for pets and plants in such settings. I would ensure everything is handled professionally and that your home is left in excellent condition.', 'Rejected', '2024-07-01'),

(5, 3, 'I have previously cared for dogs and fish in coastal homes and would be delighted to assist. My experience includes feeding and maintaining aquariums, as well as handling the daily routines of dogs. I am thorough in my approach and ensure every task is completed to the highest standard. I also prioritize communication to keep homeowners informed during their absence.', 'Confirmed', '2024-06-01'),
(5, 2, 'I enjoy seaside environments and have experience maintaining gardens and caring for pets. I am familiar with the unique challenges of coastal properties and ensure homes are left in excellent condition. I bring a proactive approach to all my tasks and strive to exceed homeowners’ expectations.', 'Rejected', '2024-06-05'),

(6, 7, 'I am a cat lover with previous experience in urban apartments. Your property sounds ideal. I am confident I can provide the care and attention your cat deserves, as well as maintain the cleanliness of the apartment. My focus is on ensuring pets are happy and that the home is left exactly as the homeowner expects.', 'Confirmed', '2024-11-10'),
(6, 4, 'I have cared for cats in Aarhus before and am familiar with the area. I enjoy spending time with animals and always ensure they feel comfortable and loved during their owners’ absences. I also make it a point to respect homeowners’ preferences and keep them updated regularly.', 'Rejected', '2024-11-12'),

(7, 6, 'I am skilled in maintaining gardens and caring for both dogs and cats. I would enjoy staying at your lovely home. My experience includes handling similar responsibilities in rural homes, and I pride myself on my attention to detail. I ensure pets are cared for and gardens are kept in excellent condition throughout the stay.', 'Confirmed', '2024-10-15'),
(7, 5, 'I have experience managing properties like yours and can provide excellent care for your pets and garden. I understand the unique requirements of maintaining both indoor and outdoor spaces and am happy to handle all assigned tasks with diligence and care.', 'Rejected', '2024-10-18'),

(8, 1, 'I have cared for cats and gardens in similar coastal homes before. I would ensure everything is perfect during your absence. I am familiar with the specific needs of cats and enjoy maintaining gardens to keep them vibrant and healthy. I also prioritize clear communication to ensure homeowners feel confident while away.', 'Confirmed', '2024-09-20'),
(8, 3, 'I am a responsible sitter with experience in maintaining gardens and caring for cats. I understand the importance of providing a safe and loving environment for pets and ensuring properties are well-maintained. I am confident I can exceed your expectations during the stay.', 'Rejected', '2024-09-22');

INSERT INTO House_review (profile_id, sitter_id, rating, comments, date) VALUES
(1, 1, 5, 'The turtles were well cared for, and the house was left spotless. Great sitter!', '2024-10-15'),
(2, 3, 5, 'Emilie was fantastic! The dog and plants were happy and well cared for.', '2024-09-30'),
(3, 6, 5, 'Line was amazing with the garden and dogs. Everything was in perfect condition.', '2024-08-12'),
(4, 6, 5, 'Line was excellent and left everything in perfect condition. Great sitter!', '2024-07-18'),
(5, 3, 5, 'Emilie took wonderful care of the dog and fish. Highly recommend her!', '2024-06-12'),
(6, 7, 5, 'Mads was perfect with our cat and left the apartment in immaculate condition.', '2024-11-22'),
(7, 6, 5, 'Line was fantastic with the garden and pets. Everything was flawless.', '2024-10-28'),
(8, 1, 5, 'Jonas was great with our cats and garden. Highly recommend him!', '2024-09-28');

INSERT INTO Sitter_review (owner_id, sitter_id, rating, comments, date) VALUES
(8, 1, 5, 'Jonas was excellent with our turtles and kept the house tidy. Would love to host him again.', '2024-10-15'),
(9, 3, 5, 'Emilie was wonderful with our dog and plants. She left the apartment spotless!', '2024-09-30'),
(10, 6, 5, 'Line did an amazing job with our garden and dogs. Everything was perfect.', '2024-08-12'),
(9, 6, 5, 'Line was fantastic, very attentive to details, and left everything in perfect order.', '2024-07-18'),
(10, 3, 5, 'Emilie was fantastic, taking great care of the dog and fish. Highly recommend!', '2024-06-12'),
(10, 7, 5, 'Mads took great care of our cat and was very attentive. Would host him again!', '2024-11-22'),
(11, 6, 5, 'Line maintained our garden beautifully and took excellent care of our pets.', '2024-10-28'),
(11, 1, 5, 'Jonas was excellent with our cats and garden. Would highly recommend!', '2024-09-28');

INSERT INTO Report (reporting_id, reported_id, admin_id, comments, status) VALUES
-- (1, 5, 1, 'Sitter was not responsive.', 'Pending'),
(2, 6, 2, 'House was left untidy.', 'Resolved');

INSERT INTO Report (reporting_id, reported_id, admin_id, comments) VALUES
(1, 5, 1, 'Sitter was not responsive.');