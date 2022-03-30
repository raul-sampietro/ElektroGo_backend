CREATE TABLE DRIVER (
	userName varchar(20),
	CONSTRAINT Driver_FK_User FOREIGN KEY (userName) REFERENCES Usuari(userName),
	CONSTRAINT Driver_PK PRIMARY KEY (userName));