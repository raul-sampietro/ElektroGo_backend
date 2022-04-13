CREATE TABLE CHATS (
   sender varchar(20),
   receiver varchar(20),
   message varchar (280) NOT NULL,
   sentAt timestamp NOT NULL,
   CONSTRAINT Chats_PK PRIMARY KEY (sender,receiver,sentAt),
   CONSTRAINT Chats_FK_Sender FOREIGN KEY (sender) REFERENCES USERS(userName),
   CONSTRAINT Chats_FK_Receiver FOREIGN KEY (receiver) REFERENCES USERS(userName),
   CONSTRAINT Chats_Check_SenderNotReceiver CHECK (sender <> receiver)
);
