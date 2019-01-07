SET FOREIGN_KEY_CHECKS=0;

create table tl_laimsc11_commoncartridge (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   instructions MEDIUMTEXT,
   content_in_use tinyint(1),
   define_later tinyint(1),
   content_id bigint,
   mini_view_commonCartridge_number integer,
   allow_auto_run tinyint(1),
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity tinyint(1),
   primary key (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_laimsc11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   commonCartridge_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK_NEW_1279208528_24AA78C530E79035 FOREIGN KEY (commonCartridge_uid)
   		REFERENCES tl_laimsc11_commoncartridge (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimsc11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished tinyint(1),
   session_uid bigint,
   commonCartridge_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_1279208528_30113BFC309ED320 FOREIGN KEY (commonCartridge_uid)
   		REFERENCES tl_laimsc11_commoncartridge (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1279208528_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_laimsc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

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
   create_by_author tinyint(1),
   is_hide tinyint(1),
   item_type smallint,
   file_type varchar(255),
   file_name varchar(255),
   open_url_new_window tinyint(1),
   commonCartridge_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_1279208528_F52D1F9330E79035 FOREIGN KEY (commonCartridge_uid)
   		REFERENCES tl_laimsc11_commoncartridge (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1279208528_F52D1F93758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_laimsc11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

create table tl_laimsc11_item_instruction (
   uid bigint not null auto_increment,
   description varchar(255),
   sequence_id integer,
   item_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_1279208528_A5665013980570ED FOREIGN KEY (item_uid)
   		REFERENCES tl_laimsc11_commoncartridge_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimsc11_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   commonCartridge_item_uid bigint,
   user_uid bigint,
   complete tinyint(1),
   session_id bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_1279208528_693580A438BF8DFE FOREIGN KEY (commonCartridge_item_uid)
   		REFERENCES tl_laimsc11_commoncartridge_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1279208528_693580A441F9365D FOREIGN KEY (user_uid)
   		REFERENCES tl_laimsc11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimsc11_configuration (
   uid bigint not null auto_increment,
   config_key varchar(30) unique,
   config_value varchar(255),
   primary key (uid)
);


ALTER TABLE tl_laimsc11_commoncartridge ADD CONSTRAINT FK_NEW_1279208528_89093BF758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_laimsc11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;

   		
INSERT INTO tl_laimsc11_commoncartridge (uid, title,
 instructions, content_in_use, define_later, content_id,
 mini_view_commonCartridge_number, allow_auto_run,reflect_on_activity) VALUES
  (1,'CommonCartridge','Instructions ',0,0,${default_content_id},0,0,0);
  
INSERT INTO tl_laimsc11_commoncartridge_item (uid, title, url, create_date, create_by_author, is_hide, item_type,open_url_new_window, commonCartridge_uid,frame_height) VALUES 
  (1,'Web Search','http://www.google.com ',NOW(),1,0,1,0,1,100);
  
INSERT INTO tl_laimsc11_configuration (config_key, config_value) VALUES
  ('allowExposeUserName',	'true');
INSERT INTO tl_laimsc11_configuration (config_key, config_value) VALUES
  ('allowExposeUserEmail', 'true');  
    
SET FOREIGN_KEY_CHECKS=1;