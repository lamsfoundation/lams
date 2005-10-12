create table tl_lafrum11_attachment (
   UUID bigint not null auto_increment,
   VERSION bigint,
   type varchar(255),
   NAME varchar(255),
   forum bigint,
   primary key (UUID)
);
create table tl_lafrum11_forum (
   UUID bigint not null auto_increment,
   CREATED datetime,
   UPDATED datetime,
   CREATEDBY bigint,
   TITLE varchar(255),
   ALLOWANNOMITY bit,
   FORCEOFFLINE bit,
   LOCKWHENFINISHED bit,
   INSTRUCTIONS varchar(255),
   ONLINEINSTRUCTIONS varchar(255),
   OFFLINEINSTRUCTIONS varchar(255),
   primary key (UUID)
);
create table tl_lafrum11_message (
   UUID bigint not null auto_increment,
   CREATED datetime,
   UPDATED datetime,
   CREATEDBY bigint,
   MODIFIEDBY bigint,
   SUBJECT varchar(255),
   BODY text,
   ISAUTHORED bit,
   ISANNONYMOUS bit,
   FORUM bigint,
   parent bigint,
   primary key (UUID)
);
alter table tl_lafrum11_attachment add index FK389AD9A29EAD680D (forum), add constraint FK389AD9A29EAD680D foreign key (forum) references tl_lafrum11_forum (UUID);
alter table tl_lafrum11_message add index FK4A6067E8F7440FBC (parent), add constraint FK4A6067E8F7440FBC foreign key (parent) references tl_lafrum11_message (UUID);
alter table tl_lafrum11_message add index FK4A6067E89EAD680D (FORUM), add constraint FK4A6067E89EAD680D foreign key (FORUM) references tl_lafrum11_forum (UUID);
