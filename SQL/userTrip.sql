CREATE TABLE USERTRIP (
                          id int,
                          username varchar(20),
                          CONSTRAINT UserTrip_PK PRIMARY KEY (id,username),
                          CONSTRAINT UserTrip_FK_Trip FOREIGN KEY (id) REFERENCES TRIP(id) ON DELETE CASCADE,
                          CONSTRAINT UserTrip_FK_User FOREIGN KEY (username)  REFERENCES USERS(userName)ON DELETE CASCADE
);