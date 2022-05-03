CREATE TABLE CANCELEDTRIP (
                              id int,
                              dayCanceled Date,
                              reason text,
                              CONSTRAINT CanceledTrip_FK_Trip FOREIGN KEY (id) REFERENCES TRIP(id) ON DELETE CASCADE,
                              CONSTRAINT CanceledTrip_PK PRIMARY KEY (id));