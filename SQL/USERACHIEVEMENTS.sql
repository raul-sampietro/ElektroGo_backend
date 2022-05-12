CREATE TABLE USERACHIEVEMENTS (
 username varchar(100),
 achievement varchar(50),
 points integer,
 CONSTRAINT UserAchivements_PK PRIMARY KEY(username, achievement),
 CONSTRAINT User_FK FOREIGN KEY (username) REFERENCES USERS(username),
 CONSTRAINT Achiev_FK FOREIGN KEY (achievement) REFERENCES ACHIEVEMENTS(name));