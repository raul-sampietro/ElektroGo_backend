
CREATE TABLE REPORT (
	userWhoReports varchar(100),
	reportedUser varchar(100),
	reason varchar(500),
	CONSTRAINT Rating_PK PRIMARY KEY(userWhoReports, reportedUser),
	CONSTRAINT Rating_FK_userWhoReports FOREIGN KEY (userWhoReports) REFERENCES USERS(username),
	CONSTRAINT Rating_FK_reportedUser FOREIGN KEY (reportedUser) REFERENCES USERS(username)
);