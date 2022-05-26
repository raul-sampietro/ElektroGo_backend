CREATE TABLE TRIP(
                     id int NOT NULL AUTO_INCREMENT ,
                     startDate Date,
                     startTime Time,
                     offeredSeats integer,
                     occupiedSeats integer,
                     restrictions text,
                     details text,
                     CancelDate Date,
                     vehicleNumberPlate varchar(100),
                     origin varchar(1000),
                     destination varchar(1000),
                     LatitudeOrigin varchar(100),
                     LongitudeOrigin varchar(100),
                     LatitudeDestination varchar(100),
                     LongitudeDestination varchar(100),
                     username varchar(20),
                     CONSTRAINT Trip_PK PRIMARY KEY (id),
                     CONSTRAINT Trip_FK_Driver FOREIGN KEY (username) REFERENCES DRIVER(userName) ON DELETE CASCADE,
                     CONSTRAINT Trip_FK_Vehicle FOREIGN KEY (vehicleNumberPlate) REFERENCES VEHICLE(numberPlate)ON DELETE CASCADE,
                     UNIQUE(username, startTime, startDate)
);

ALTER TABLE TRIP ADD state varchar(15) DEFAULT ('current');
