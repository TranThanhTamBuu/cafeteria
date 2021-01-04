

CREATE TABLE Category
(
    ID tinyint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name varchar(50) UNIQUE,

    FULLTEXT(Name)
);

CREATE TABLE SpecificType
(
    ID tinyint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name varchar(50) UNIQUE,

    FULLTEXT(Name)
);

CREATE TABLE Menu
(
    ID tinyint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Type char(1),
    CategoryID tinyint,
    SpecificTypeID tinyint,
    Name varchar(50),
    Price mediumint,
    Discount decimal(2,2),
    
    FULLTEXT(Name),
    FOREIGN KEY (CategoryID) REFERENCES Category(ID),
    FOREIGN KEY (SpecificTypeID) REFERENCES SpecificType(ID)
);

CREATE TABLE Combo
(
    ComboID tinyint,
    DishID tinyint,
    Quantity tinyint,
    

    PRIMARY KEY (ComboID, DishID),
    FOREIGN KEY (ComboID) REFERENCES Menu(ID),
    FOREIGN KEY (DishID) REFERENCES Menu(ID)
);

CREATE TABLE Payment
(
    ID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    DatePayment datetime,
    Note varchar(200),
    TotalAmount mediumint
);

CREATE TABLE SpecificPayment
(
    PaymentID int,
    FoodID tinyint,
    Quantity tinyint,
    
    PRIMARY KEY (PaymentID, FoodID),
    FOREIGN KEY (PaymentID) REFERENCES Payment(ID),
    FOREIGN KEY (FoodID) REFERENCES Menu(ID)
);

CREATE TABLE Unit
(
    ID tinyint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name varchar(50)
);


CREATE TABLE Cost
(
    ID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Type char(1),
    DateCost Date,
    Description varchar(50),
    Quantity decimal(5,2),
    UnitID tinyint,
    TotalAmount mediumint,
    
    FULLTEXT(Description),
    FOREIGN KEY (UnitID) REFERENCES Unit(ID)
);

INSERT INTO Category VALUES (null, 'Cuisine');
INSERT INTO Category VALUES (null, 'Baverage');
INSERT INTO Category VALUES (null, 'Mixed');

INSERT INTO SpecificType VALUES (null, 'Chicken');
INSERT INTO SpecificType VALUES (null, 'Korean dish');
INSERT INTO SpecificType VALUES (null, 'Salad & fries');
INSERT INTO SpecificType VALUES (null, 'Alcohol');
INSERT INTO SpecificType VALUES (null, 'Soft drink');
INSERT INTO SpecificType VALUES (null, 'Group 2-3');
INSERT INTO SpecificType VALUES (null, 'Group 4-6');

INSERT INTO Menu VALUES (null, 'D', 1, 1, 'Snow cheese chicken', 165000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 1, 'Sweet-spicy fried chicken', 160000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 1, 'Spicy grilled chicken', 150000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 1, 'Galic grilled chicken', 150000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 1, 'Spicy cream cheese boneless chicken', 300000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 1, 'Cream cheese boneless chicken', 280000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 2, 'Sausage & seafood stew', 280000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 2, 'Sausage & chicken stew', 280000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 2, 'Sausage & baccon stew', 260000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 2, 'Tteok-bokki hotpot', 260000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 2, 'Beef mixed rice', 140000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 2, 'Seafood mixed rice', 140000, 0.0);

INSERT INTO Menu VALUES (null, 'D', 1, 3, 'Snow cheese fries', 70000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 3, 'Honey fries', 70000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 3, 'Seafood salad', 120000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 3, 'Cajun chicken salad', 120000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 3, 'Cream cheese fruit salad', 120000, 0.0);

INSERT INTO Menu VALUES (null, 'D', 1, 4, 'Saporo', 35000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 4, 'Heineken', 35000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 4, 'Soju', 120000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 4, 'Makgeolli', 160000, 0.0);

INSERT INTO Menu VALUES (null, 'D', 1, 5, 'Pepsi', 20000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 5, '100 Plus', 20000, 0.0);
INSERT INTO Menu VALUES (null, 'D', 1, 5, 'Aquafina', 10000, 0.0);

INSERT INTO Menu VALUES (null, 'C', 1, 6, '#CheeseTeam', 4390000, 0.0);
INSERT INTO Menu VALUES (null, 'C', 1, 6, '#SpicyTeam', 2990000, 0.0);
INSERT INTO Menu VALUES (null, 'C', 1, 6, '#SweetTeam', 3090000, 0.0);
INSERT INTO Menu VALUES (null, 'C', 1, 6, 'Traditional 1', 319000, 0.0);

INSERT INTO Menu VALUES (null, 'C', 1, 7, 'Traditional 2', 699000, 0.0);

INSERT INTO Combo VALUES (25, 6, 1);
INSERT INTO Combo VALUES (25, 13, 1);
INSERT INTO Combo VALUES (25, 17, 1);

INSERT INTO Combo VALUES (26, 3, 1);
INSERT INTO Combo VALUES (26, 14, 1);
INSERT INTO Combo VALUES (26, 17, 1);

INSERT INTO Combo VALUES (27, 2, 1);
INSERT INTO Combo VALUES (27, 14, 1);
INSERT INTO Combo VALUES (27, 17, 1);

INSERT INTO Combo VALUES (28, 4, 1);
INSERT INTO Combo VALUES (28, 11, 1);
INSERT INTO Combo VALUES (28, 22, 2);

INSERT INTO Combo VALUES (29, 10, 1);
INSERT INTO Combo VALUES (29, 13, 1);
INSERT INTO Combo VALUES (29, 2, 1);
INSERT INTO Combo VALUES (29, 3, 1);
INSERT INTO Combo VALUES (29, 22, 4);

INSERT INTO Unit VALUES (null, 'Kilogram');
INSERT INTO Unit VALUES (null, 'Gram');
INSERT INTO Unit VALUES (null, 'Salary');
INSERT INTO Unit VALUES (null, 'Bill');

INSERT INTO Cost VALUES (null, 'G', '2020-11-23', 'Beef', 10.5, 1, 5000000);
INSERT INTO Cost VALUES (null, 'O', '2020-11-23', 'Electricity bill', 1, 4, 1500000);
INSERT INTO Cost VALUES (null, 'O', '2020-11-23', 'Manager: Tran Thanh Tam', 1, 3, 1500000);
INSERT INTO Cost VALUES (null, 'G', '2020-11-23', 'Galic', 300, 2, 60000);
INSERT INTO Cost VALUES (null, 'O', '2020-11-24', 'Water bill', 1, 4, 700000);
INSERT INTO Cost VALUES (null, 'G', '2020-11-24', 'Cheese', 5.5, 1, 500000);