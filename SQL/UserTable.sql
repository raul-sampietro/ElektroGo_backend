CREATE TABLE Usuari (
	userName varchar(20),
	mail varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	CONSTRAINT User_PK PRIMARY KEY (userName));