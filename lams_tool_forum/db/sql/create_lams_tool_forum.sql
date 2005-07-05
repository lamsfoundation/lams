create table ATTACHMENT (
   id bigint not null,
   UUID bigint,
   TYPE bit,
   NAME varchar(255),
   FORUM bigint,
   primary key (id)
);
create table FORUM (
   id bigint not null,
   TITLE varchar(255),
   ALLOWANNOMITY bit,
   FORCEOFFLINE bit,
   LOCKWHENFINISHED bit,
   INSTRUCTIONS varchar(255),
   ONLINEINSTRUCTIONS varchar(255),
   OFFLINEINSTRUCTIONS varchar(255),
   primary key (id)
);
create table GENERICENTITY (
   ID bigint not null auto_increment,
   CREATED datetime,
   UPDATED datetime,
   CREATEDBY bigint,
   MODIFIEDBY bigint,
   primary key (ID)
);
create table MESSAGE (
   id bigint not null,
   SUBJECT varchar(255),
   BODY text,
   ISAUTHORED bit,
   ISANNONYMOUS bit,
   FORUM bigint,
   PARENT bigint,
   primary key (id)
);
alter table ATTACHMENT add index FKA7E145233FF9501 (FORUM), add constraint FKA7E145233FF9501 foreign key (FORUM) references FORUM (id);
alter table ATTACHMENT add index FKA7E14523D1B (id), add constraint FKA7E14523D1B foreign key (id) references GENERICENTITY (ID);
alter table FORUM add index FK3FF9501D1B (id), add constraint FK3FF9501D1B foreign key (id) references GENERICENTITY (ID);
alter table MESSAGE add index FK63B68BE78C3DFCAA (PARENT), add constraint FK63B68BE78C3DFCAA foreign key (PARENT) references MESSAGE (id);
alter table MESSAGE add index FK63B68BE73FF9501 (FORUM), add constraint FK63B68BE73FF9501 foreign key (FORUM) references FORUM (id);
alter table MESSAGE add index FK63B68BE7D1B (id), add constraint FK63B68BE7D1B foreign key (id) references GENERICENTITY (ID);