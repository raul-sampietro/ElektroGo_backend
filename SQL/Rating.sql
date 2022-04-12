CREATE TABLE RATING (
	userWhoRates varchar(100),
	ratedUser	 varchar(100),
	points		 integer NOT NULL,
	comment		 varchar(500),
	CONSTRAINT PK_RATING PRIMARY KEY(userWhoRates, ratedUser),
	CONSTRAINT FK_userWhoRates FOREIGN KEY (userWhoRates) REFERENCES USERS(userName),
	CONSTRAINT FK_ratedUser FOREIGN KEY (ratedUser) REFERENCES USERS(userName)
	);
	