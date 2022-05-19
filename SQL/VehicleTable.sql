CREATE TABLE VEHICLE(
brand varchar(100) NOT NULL,
model varchar(100)NOT NULL,
numberPlate varchar(100) NOT NULL,
drivingRange varchar(100) NOT NULL,
fabricationYear integer NOT NULL,
seats integer NOT NULL,
imageId varchar(100),
CONSTRAINT Vehicle_PK PRIMARY KEY (numberPlate)
);
ALTER TABLE VEHICLE ADD COLUMN verification varchar(10) DEFAULT 'pending';