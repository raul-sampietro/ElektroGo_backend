CREATE TABLE DRIVERVEHICLE (
nPVehicle varchar(100),
userDriver varchar(20),
CONSTRAINT DriverVehicle_PK PRIMARY KEY (nPVehicle,userDriver),
CONSTRAINT DriverVehicle_FK_Vehicle FOREIGN KEY (nPVehicle) REFERENCES VEHICLE(numberPlate),
CONSTRAINT DriverVehicle_FK_Driver FOREIGN KEY (userDriver)  REFERENCES DRIVER(userName)
);
