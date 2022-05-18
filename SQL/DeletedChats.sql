CREATE TABLE DELETEDCHATS (
      UserA varchar(20),
      UserB varchar(20),
      CONSTRAINT DeletedChats_PK PRIMARY KEY (UserA, UserB),
      CONSTRAINT NotSameUser CHECK (UserA <> UserB));