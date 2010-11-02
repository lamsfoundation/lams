alter table tl_eueadv10_attachment drop foreign key FK_NEW_1226715514_1E7009430E79035;
alter table tl_eueadv10_item_instruction drop foreign key FK_NEW_1226715514_A5665013980570ED;
alter table tl_eueadv10_item_log drop foreign key FK_NEW_1226715514_63195BC938BF8DFE;
alter table tl_eueadv10_item_log drop foreign key FK_NEW_1226715514_63195BC941F9365D;
alter table tl_eueadv10_eadventure drop foreign key FK_NEW_1226715514_89093BF758092FB;
alter table tl_eueadv10_eadventure_item drop foreign key FK_NEW_1226715514_F52D1F9330E79035;
alter table tl_eueadv10_eadventure_item drop foreign key FK_NEW_1226715514_F52D1F93EC0D3147;
alter table tl_eueadv10_eadventure_item drop foreign key FK_NEW_1226715514_F52D1F93758092FB;
alter table tl_eueadv10_session drop foreign key FK_NEW_1226715514_24AA78C530E79035;
alter table tl_eueadv10_user drop foreign key FK_NEW_1226715514_30113BFC30E79035;
alter table tl_eueadv10_user drop foreign key FK_NEW_1226715514_30113BFCEC0D3147;
drop table if exists tl_eueadv10_attachment;
drop table if exists tl_eueadv10_item_instruction;
drop table if exists tl_eueadv10_item_log;
drop table if exists tl_eueadv10_eadventure;
drop table if exists tl_eueadv10_eadventure_item;
drop table if exists tl_eueadv10_session;
drop table if exists tl_eueadv10_user;
create table tl_eueadv10_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   eadventure_uid bigint,
   primary key (uid)
);
create table tl_eueadv10_eadventure (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   lock_on_finished tinyint,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   allow_add_files tinyint,
   allow_add_urls tinyint,
   mini_view_eadventure_number integer,
   allow_auto_run tinyint,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint,
   assigment_submit_notify tinyint DEFAULT 0,
   ims_schema varchar(255),
   organization_xml text,
   file_uuid bigint,
   file_version_id bigint,
   file_name varchar(255),
   complete tinyint,
   session_uid bigint,
   primary key (uid)
);
create table tl_eueadv10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   eadventure_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
);
create table tl_eueadv10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   eadventure_uid bigint,
   primary key (uid)
);
alter table tl_eueadv10_attachment add index FK_NEW_1226715514_1E7009430E79035 (eadventure_uid), add constraint FK_NEW_1226715514_1E7009430E79035 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_eadventure add index FK_NEW_1226715514_89093BF758092FB (create_by), add constraint FK_NEW_1226715514_89093BF758092FB foreign key (create_by) references tl_eueadv10_user (uid);
alter table tl_eueadv10_eadventure add index FK_NEW_1226715514_F52D1F93EC0D3147 (session_uid), add constraint FK_NEW_1226715514_F52D1F93EC0D3147 foreign key (session_uid) references tl_eueadv10_session (uid);
alter table tl_eueadv10_session add index FK_NEW_1226715514_24AA78C530E79035 (eadventure_uid), add constraint FK_NEW_1226715514_24AA78C530E79035 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_user add index FK_NEW_1226715514_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1226715514_30113BFCEC0D3147 foreign key (session_uid) references tl_eueadv10_session (uid);
alter table tl_eueadv10_user add index FK_NEW_1226715514_30113BFC309ED320 (eadventure_uid), add constraint FK_NEW_1226715514_30113BFC309ED320 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
