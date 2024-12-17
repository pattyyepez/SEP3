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
('anne.andersen@gmail.com', 'Anne Andersen', '12345', 'owner_anne.jpg', '456789-0123', '+45 55667788', TRUE, 2);

INSERT INTO HouseOwner (owner_id, address, biography) VALUES
(8, 'Strandvej 10, Haderslev', 'Hi, I’m Niels. I’m a retired teacher who loves to travel frequently and meet new people. I’m looking for responsible sitters to help while I’m away.'),
(9, 'Nørrebro 45, Copenhagen', 'Hi, I’m Karen. I’m an architect who often travels for work and needs reliable sitters during my trips.'),
(10, 'Sønder Allé 12, Aarhus', 'Hi, I’m Henrik. I’m a software developer who travels regularly and values trustworthy sitters to take care of things while I’m gone.'),
(11, 'Lindevang 34, Ry', 'Hi, I’m Anne. I’m a nature photographer who frequently embarks on long assignments and seeks dependable sitters to assist during my absences.');


INSERT INTO HouseProfile (owner_id, title, description, address, region, city) VALUES
(8, 'Beachfront House', 'A spacious beach house with a serene view of the ocean and three turtles to care for. This home features four bedrooms, offering ample space for sitters to enjoy their stay. Additionally, it includes a private garage, making it convenient for those traveling with a vehicle.', 'Strandvej 10', 'Sønderjylland', 'Haderslev'),
(8, 'Urban Oasis', 'An apartment located in the heart of Copenhagen with a large terrace filled with indoor plants to maintain and a friendly dog. This modern home has two bedrooms, providing a comfortable and stylish stay. While there is no garage, the apartment is close to public transport.', 'Nørrebro 45', 'Hovedstaden', 'Copenhagen'),

(9, 'Countryside Getaway', 'A spacious countryside home surrounded by greenery, ideal for sitters who love nature and animals. The house includes three bedrooms, a private garage, and a large garden that requires maintenance. Two friendly dogs will keep you company during your stay.', 'Lindevang 34', 'Midtjylland', 'Ry'),
(9, 'City Getaway', 'A pet-friendly apartment located near Copenhagen’s main attractions, perfect for city lovers. It has two cozy bedrooms, a terrace with plants to maintain, and no garage. The apartment is ideal for sitters who enjoy vibrant urban life and caring for a dog and a fish.', 'Nørrebro 50', 'Hovedstaden', 'Copenhagen'),

(10, 'Sea Breeze Retreat', 'A beautiful home overlooking the sea in Skagen, perfect for sitters who enjoy coastal living. The house includes three bedrooms, a cozy garden, and a garage. A dog and fish require care during your stay.', 'Strandvej 15', 'Nordjylland', 'Skagen'),
(10, 'Cozy Aarhus Retreat', 'A charming and bright apartment in Aarhus with one playful cat. The home features one spacious bedroom, modern amenities, and plenty of on-street parking. It is located in a peaceful neighborhood with easy access to parks and public transport.', 'Sønder Allé 12', 'Midtjylland', 'Aarhus'),

(11, 'House with Garden', 'A tranquil home nestled in the countryside of Nyborg, perfect for garden, cat and dog enthusiasts. This cozy property has two bedrooms, a garage, and a large garden filled with flowers and greenery, including hydrangeas. Two pets live here and enjoy daily care.', 'Lindevang 40', 'Fyn', 'Nyborg'),
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
(1, '2024-10-01', '2024-10-10', 'Open'),
(1, '2025-02-01', '2025-02-10', 'Closed'),

(2, '2024-09-15', '2024-09-25', 'Open'),
(2, '2024-02-01', '2024-02-12', 'Closed'),

(3, '2024-08-01', '2024-08-10', 'Open'),
(3, '2025-02-05', '2025-02-15', 'Open'),

(4, '2024-07-01', '2024-07-10', 'Open'),
(4, '2025-02-10', '2025-02-18', 'Open'),

(5, '2024-06-01', '2024-06-10', 'Open'),
(5, '2025-02-01', '2025-02-09', 'Open'),

(6, '2024-11-10', '2024-11-20', 'Open'),
(6, '2025-02-05', '2025-02-14', 'Open'),

(7, '2024-10-15', '2024-10-25', 'Open'),
(7, '2025-02-01', '2025-02-08', 'Open'),

(8, '2024-09-20', '2024-09-30', 'Open'),
(8, '2025-02-10', '2025-02-18', 'Open'),

(1, '2025-02-20', '2025-03-01', 'Open'),
(2, '2025-03-05', '2025-03-15', 'Open');


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
(1, 1, 'I am experienced with exotic pets like turtles and would love to care for your home. My attention to detail ensures your pets and property will be in the best hands.', 'Pending', '2024-09-20 14:23:45'),
(1, 2, 'I have cared for aquatic pets before and would enjoy staying at your beachside house. I am also respectful of homeowners’ preferences and always leave properties spotless.', 'Pending', '2024-09-22 09:15:32'),
(1, 3, 'I am experienced with exotic pets like turtles and would love to care for your home. My attention to detail ensures your pets and property will be in the best hands.', 'Pending', '2024-09-22 11:45:20'),

(2, 3, 'I have previous experience with indoor plants and dogs, making me a great fit for your apartment. I enjoy caring for pets and ensuring plants are healthy and thriving.', 'Pending', '2024-09-15 08:12:18'),
(2, 5, 'I am skilled in managing indoor plants and caring for dogs. I have cared for similar apartments before and understand the importance of keeping both pets and plants healthy.', 'Confirmed', '2024-09-17 10:30:47'),

(3, 6, 'I specialize in rural properties and enjoy caring for dogs and gardens. I have maintained large outdoor spaces before and can handle any tasks related to gardening or pet care.', 'Pending', '2024-08-01 13:25:19'),
(3, 4, 'I am a responsible sitter with experience maintaining large gardens and caring for pets. I take pride in ensuring pets are comfortable and gardens are well-maintained.', 'Pending', '2024-08-05 14:18:36'),

(4, 6, 'I love the vibrant city life and can ensure your pets and plants are well cared for. My approach focuses on providing attentive care and maintaining the property’s cleanliness.', 'Pending', '2024-07-03 09:45:12'),
(4, 5, 'I have experience in caring for small dogs and plants, making me an excellent fit for your apartment. I would ensure everything is handled and that your home is left in good condition.', 'Confirmed', '2024-07-01 11:20:50'),

(5, 3, 'I have previously cared for dogs and fish in coastal homes and would be delighted to assist. I am thorough in my approach and ensure every task is completed to the highest standard.', 'Pending', '2024-06-01 17:35:27'),
(5, 2, 'I enjoy seaside environments and have experience maintaining gardens and caring for pets. I bring a proactive approach to all my tasks and strive to exceed homeowners’ expectations.', 'Pending', '2024-06-05 19:22:48'),

(6, 7, 'I am a cat lover with previous experience in urban apartments. Your property sounds ideal. ', 'Pending', '2024-11-10 16:42:31'),
(6, 4, 'I have cared for cats in Aarhus before and am familiar with the area. I enjoy spending time with animals and ensure they feel comfortable and loved during their owners’ absences.', 'Pending', '2024-11-12 14:10:59'),

(7, 6, 'I am skilled in maintaining gardens and caring for both dogs and cats. I would enjoy staying at your lovely home. My experience includes handling similar responsibilities in rural homes.', 'Pending', '2024-10-15 10:15:45'),
(7, 5, 'I have experience managing properties like yours and can provide excellent care for your pets and garden.', 'Pending', '2024-10-18 11:45:21'),

(8, 1, 'I have cared for cats and gardens in similar coastal homes before. I would ensure everything is perfect during your absence.', 'Pending', '2024-09-20 08:18:15'),
(8, 3, 'I am a responsible sitter with experience in maintaining gardens and caring for cats. I understand the importance of providing a safe and loving environment for pets.', 'Pending', '2024-09-22 12:25:50'),

(17, 5, 'I am experienced with mountain properties and can ensure your cabin is well-maintained. I love nature and hiking, making me an excellent fit for your retreat.', 'Pending', '2025-02-15 13:45:20'),
(17, 2, 'I have previous experience caring for dogs in lakeside properties. I would ensure your pets and property are well taken care of during your absence.', 'Pending', '2025-03-01 16:30:40'),
(18, 6, 'I am experienced with exotic pets like turtles and would love to care for your home. My attention to detail ensures your pets and property will be in the best hands.', 'Pending', '2025-03-01 18:22:55');


INSERT INTO House_review (profile_id, sitter_id, rating, comments, date) VALUES
(1, 1, 4, 'The house was spacious and well-maintained. Taking care of the turtles was a delightful experience.', '2024-10-15 14:23:45'),
(1, 3, 4, 'The hosts were incredibly kind and welcoming. Their house was cozy, and the turtles were a joy to care for.', '2024-10-15 09:12:30'),
(1, 6, 5, 'The hosts were very accommodating and left detailed instructions. The house was clean, and the turtles were easy to care for.', '2024-10-15 16:45:12'),
(1, 7, 4, 'The hosts were very thoughtful and communicative. The modern house was lovely with incredible views.', '2024-10-15 11:20:50'),
(2, 3, 4, 'The apartment was cozy and the dog was very friendly. The plants were easy to care for.', '2024-09-30 08:15:37'),
(3, 6, 4, 'The countryside home was peaceful, and the dogs were a joy to look after. The garden required some effort but was rewarding.', '2024-08-12 13:07:59'),
(4, 6, 5, 'The urban apartment was modern and conveniently located. The dog was playful and easy to care for.', '2024-07-18 10:32:45'),
(5, 3, 4, 'The coastal home had a relaxing atmosphere. Taking care of the dog and the fish was straightforward and enjoyable.', '2024-06-12 18:25:18'),
(6, 7, 4, 'The apartment in Aarhus was clean and comfortable. The cat was independent but enjoyed company.', '2024-11-22 14:42:05'),
(7, 6, 4, 'The rural house was serene, and the garden was beautiful to work in. The pets were affectionate.', '2024-10-28 09:30:22'),
(8, 1, 5, 'The house in Nyborg had a lovely garden and the cats were delightful to care for.', '2024-09-28 17:18:33');

INSERT INTO Sitter_review (owner_id, sitter_id, rating, comments, date) VALUES
(8, 1, 5, 'Jonas was excellent with our turtles and kept the house tidy. Would love to host him again.', '2024-10-15 15:24:16'),
(9, 3, 5, 'Emilie was wonderful with our dog and plants. She left the apartment spotless!', '2024-09-30 11:12:47'),
(10, 6, 5, 'Line did an amazing job with our garden and dogs. Everything was perfect.', '2024-08-12 13:35:25'),
(9, 6, 5, 'Line was fantastic, very attentive to details, and left everything in perfect order.', '2024-07-18 16:48:55'),
(10, 3, 5, 'Emilie was fantastic, taking great care of the dog and fish. Highly recommend!', '2024-06-12 19:20:40'),
(10, 7, 5, 'Mads took great care of our cat and was very attentive. Would host him again!', '2024-11-22 10:15:32'),
(11, 6, 5, 'Line maintained our garden beautifully and took excellent care of our pets.', '2024-10-28 12:30:17'),
(11, 1, 5, 'Jonas was excellent with our cats and garden. Would highly recommend!', '2024-09-28 16:45:55');


INSERT INTO Report (reporting_id, reported_id, admin_id, comments, status) VALUES
-- (1, 5, 1, 'Sitter was not responsive.', 'Pending'),
(2, 6, 2, 'House was left untidy.', 'Resolved');

INSERT INTO Report (reporting_id, reported_id, admin_id, comments) VALUES
(1, 5, 1, 'Sitter was not responsive.');

