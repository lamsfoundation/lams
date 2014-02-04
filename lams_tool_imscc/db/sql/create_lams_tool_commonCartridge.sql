SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_laimsc11_attachment;
drop table if exists tl_laimsc11_item_instruction;
drop table if exists tl_laimsc11_commoncartridge;
drop table if exists tl_laimsc11_commoncartridge_item;
drop table if exists tl_laimsc11_commoncartridge_item_visit_log;
drop table if exists tl_laimsc11_session;
drop table if exists tl_laimsc11_user;
drop table if exists tl_laimsc11_configuration;

create table tl_laimsc11_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   commonCartridge_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_item_instruction (
   uid bigint not null auto_increment,
   description varchar(255),
   sequence_id integer,
   item_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_commoncartridge (
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
   mini_view_commonCartridge_number integer,
   allow_auto_run tinyint,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_commoncartridge_item (
   uid bigint not null auto_increment,
   file_uuid bigint,
   file_version_id bigint,
   description varchar(255),
   ims_schema varchar(255),
   init_item varchar(255),
   organization_xml text,
   title varchar(255),
   url text,
   launch_url varchar(255),
   secure_launch_url varchar(255),
   tool_key varchar(255),
   tool_secret varchar(255),
   custom_str text,
   button_text varchar(255),
   frame_height integer,
   create_by bigint,
   create_date datetime,
   create_by_author tinyint,
   is_hide tinyint,
   item_type smallint,
   file_type varchar(255),
   file_name varchar(255),
   open_url_new_window tinyint,
   commonCartridge_uid bigint,
   session_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   commonCartridge_item_uid bigint,
   user_uid bigint,
   complete tinyint,
   session_id bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   commonCartridge_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   commonCartridge_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimsc11_configuration (
   uid bigint not null auto_increment,
   config_key varchar(30) unique,
   config_value varchar(255),
   primary key (uid)
)ENGINE=InnoDB;

alter table tl_laimsc11_attachment add index FK_NEW_1279208528_1E7009430E79035 (commonCartridge_uid), add constraint FK_NEW_1279208528_1E7009430E79035 foreign key (commonCartridge_uid) references tl_laimsc11_commoncartridge (uid);
alter table tl_laimsc11_item_instruction add index FK_NEW_1279208528_A5665013980570ED (item_uid), add constraint FK_NEW_1279208528_A5665013980570ED foreign key (item_uid) references tl_laimsc11_commoncartridge_item (uid);
alter table tl_laimsc11_commoncartridge add index FK_NEW_1279208528_89093BF758092FB (create_by), add constraint FK_NEW_1279208528_89093BF758092FB foreign key (create_by) references tl_laimsc11_user (uid);
alter table tl_laimsc11_commoncartridge_item add index FK_NEW_1279208528_F52D1F93758092FB (create_by), add constraint FK_NEW_1279208528_F52D1F93758092FB foreign key (create_by) references tl_laimsc11_user (uid);
alter table tl_laimsc11_commoncartridge_item add index FK_NEW_1279208528_F52D1F9330E79035 (commonCartridge_uid), add constraint FK_NEW_1279208528_F52D1F9330E79035 foreign key (commonCartridge_uid) references tl_laimsc11_commoncartridge (uid);
alter table tl_laimsc11_commoncartridge_item add index FK_NEW_1279208528_F52D1F93EC0D3147 (session_uid), add constraint FK_NEW_1279208528_F52D1F93EC0D3147 foreign key (session_uid) references tl_laimsc11_session (uid);
alter table tl_laimsc11_item_log add index FK_NEW_1279208528_693580A438BF8DFE (commonCartridge_item_uid), add constraint FK_NEW_1279208528_693580A438BF8DFE foreign key (commonCartridge_item_uid) references tl_laimsc11_commoncartridge_item (uid);
alter table tl_laimsc11_item_log add index FK_NEW_1279208528_693580A441F9365D (user_uid), add constraint FK_NEW_1279208528_693580A441F9365D foreign key (user_uid) references tl_laimsc11_user (uid);
alter table tl_laimsc11_session add index FK_NEW_1279208528_24AA78C530E79035 (commonCartridge_uid), add constraint FK_NEW_1279208528_24AA78C530E79035 foreign key (commonCartridge_uid) references tl_laimsc11_commoncartridge (uid);
alter table tl_laimsc11_user add index FK_NEW_1279208528_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1279208528_30113BFCEC0D3147 foreign key (session_uid) references tl_laimsc11_session (uid);
alter table tl_laimsc11_user add index FK_NEW_1279208528_30113BFC309ED320 (commonCartridge_uid), add constraint FK_NEW_1279208528_30113BFC309ED320 foreign key (commonCartridge_uid) references tl_laimsc11_commoncartridge (uid);



INSERT INTO `tl_laimsc11_commoncartridge` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, `lock_on_finished`,
 `instructions`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`,
 `mini_view_commonCartridge_number`, `allow_auto_run`,`reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'CommonCartridge','0','0','Instructions ',null,null,0,0,${default_content_id},0,0,0);
  
INSERT INTO `tl_laimsc11_commoncartridge_item` (`uid`, `file_uuid`, `file_version_id`, `description`, `ims_schema`, `init_item`, `organization_xml`, `title`, `url`, `create_by`, `create_date`, `create_by_author`, `is_hide`, `item_type`, `file_type`, `file_name`, `open_url_new_window`, `commonCartridge_uid`, `session_uid`, `frame_height`) VALUES 
  (1,NULL,NULL,NULL,NULL,NULL,NULL,'Web Search','http://www.google.com ',null,NOW(),1,0,1,NULL,NULL,0,1,NULL, 100);
  
INSERT INTO `tl_laimsc11_configuration` (`config_key`, `config_value`) VALUES
  ('allowExposeUserName',	'true');
INSERT INTO `tl_laimsc11_configuration` (`config_key`, `config_value`) VALUES
  ('allowExposeUserEmail', 'true');  
    
SET FOREIGN_KEY_CHECKS=1;
