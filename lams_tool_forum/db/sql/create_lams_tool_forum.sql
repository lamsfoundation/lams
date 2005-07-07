create table tl_lafrum11_attachment (
   id bigint not null,
   UUID bigint,
   VERSION bigint,
   FORUM bigint,
   primary key (id)
);
create table tl_lafrum11_forum (
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
create table tl_lafrum11_genericentity (
   ID bigint not null auto_increment,
   CREATED datetime,
   UPDATED datetime,
   CREATEDBY bigint,
   MODIFIEDBY bigint,
   primary key (ID)
);
create table tl_lafrum11_message (
   id bigint not null,
   SUBJECT varchar(255),
   BODY text,
   ISAUTHORED bit,
   ISANNONYMOUS bit,
   FORUM bigint,
   PARENT bigint,
   primary key (id)
);
alter table tl_lafrum11_attachment add index FK389AD9A23FF9501 (FORUM), add constraint FK389AD9A23FF9501 foreign key (FORUM) references tl_lafrum11_forum (id);
alter table tl_lafrum11_attachment add index FK389AD9A2D1B (id), add constraint FK389AD9A2D1B foreign key (id) references tl_lafrum11_genericentity (ID);
alter table tl_lafrum11_forum add index FK87917942D1B (id), add constraint FK87917942D1B foreign key (id) references tl_lafrum11_genericentity (ID);
alter table tl_lafrum11_message add index FK4A6067E83FF9501 (FORUM), add constraint FK4A6067E83FF9501 foreign key (FORUM) references tl_lafrum11_forum (id);
alter table tl_lafrum11_message add index FK4A6067E88C3DFCAA (PARENT), add constraint FK4A6067E88C3DFCAA foreign key (PARENT) references tl_lafrum11_message (id);
alter table tl_lafrum11_message add index FK4A6067E8D1B (id), add constraint FK4A6067E8D1B foreign key (id) references tl_lafrum11_genericentity (ID);
