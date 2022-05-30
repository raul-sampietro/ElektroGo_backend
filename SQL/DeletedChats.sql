CREATE TABLE DELETEDCHATS (
      UserA varchar(20),
      UserB varchar(20),
      CONSTRAINT DeletedChats_PK PRIMARY KEY (UserA, UserB),
      CONSTRAINT DeletedChats_FK_Sender FOREIGN KEY (UserA) REFERENCES USERS(userName),
      CONSTRAINT DeletedChats_FK_Receiver FOREIGN KEY (UserB) REFERENCES USERS(userName),
      CONSTRAINT NotSameUser CHECK (UserA <> UserB));