SET FOREIGN_KEY_CHECKS=0;
create table tl_larsrc11_resource (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished TINYINT(1),
   instructions MEDIUMTEXT,
   content_in_use TINYINT(1),
   define_later TINYINT(1),
   content_id bigint,
   allow_add_files TINYINT(1),
   allow_add_urls TINYINT(1),
   mini_view_resource_number integer,
   allow_auto_run TINYINT(1),
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity TINYINT(1),
   assigment_submit_notify TINYINT(1) DEFAULT 0,
   file_upload_notify TINYINT(1) DEFAULT 0,
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_larsrc11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   resource_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK24AA78C530E79035 FOREIGN KEY (resource_uid)
   		REFERENCES tl_larsrc11_resource (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_larsrc11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished TINYINT(1),
   session_uid bigint,
   resource_uid bigint,
   primary key (uid),
   KEY idx_user_user_id (user_id),
   CONSTRAINT FK30113BFC309ED320 FOREIGN KEY (resource_uid)
   		REFERENCES tl_larsrc11_resource (uid) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT FK30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_larsrc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_larsrc11_resource_item (
   uid bigint not null auto_increment,
   file_uuid bigint,
   file_version_id bigint,
   description varchar(255),
   ims_schema varchar(255),
   init_item varchar(255),
   organization_xml text,
   title varchar(255),
   url text,
   create_by bigint,
   create_date datetime,
   create_by_author tinyint(1),
   is_hide tinyint(1),
   item_type smallint,
   file_type varchar(255),
   file_name varchar(255),
   open_url_new_window tinyint(1),
   resource_uid bigint,
   session_uid bigint,
   order_id integer,
   is_allow_rating TINYINT(1) default 0,
   is_allow_comments TINYINT(1) default 0,
   primary key (uid),
   CONSTRAINT FKF52D1F9330E79035 FOREIGN KEY (resource_uid)
   		REFERENCES tl_larsrc11_resource (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FKF52D1F93758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_larsrc11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FKF52D1F93EC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_larsrc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_larsrc11_item_instruction (
   uid bigint not null auto_increment,
   description MEDIUMTEXT,
   sequence_id integer,
   item_uid bigint,
   primary key (uid),
   CONSTRAINT FKA5665013980570ED FOREIGN KEY (item_uid)
   		REFERENCES tl_larsrc11_resource_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_larsrc11_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   complete_date datetime,
   resource_item_uid bigint,
   user_uid bigint,
   complete tinyint(1),
   session_id bigint,
   primary key (uid),
   KEY idx_item_log_session_id (session_id),
   CONSTRAINT FK693580A438BF8DFE FOREIGN KEY (resource_item_uid)
   		REFERENCES tl_larsrc11_resource_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK693580A441F9365D FOREIGN KEY (user_uid)
   		REFERENCES tl_larsrc11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_larsrc11_resource ADD CONSTRAINT FK89093BF758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_larsrc11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
   		
   		
INSERT INTO tl_larsrc11_resource (uid, title, lock_on_finished,
 instructions, content_in_use, define_later, content_id, allow_add_files, 
 allow_add_urls, mini_view_resource_number, allow_auto_run,reflect_on_activity) VALUES
  (1,'Resources','0','Instructions ',0,0,${default_content_id},0,0,0,0,0);
  
INSERT INTO tl_larsrc11_resource_item (uid, title, url,create_date, create_by_author, is_hide, item_type, open_url_new_window, resource_uid,order_id) VALUES 
  (1,'Web Search','http://www.google.com ',NOW(),1,0,1,1,1,1);
INSERT INTO tl_larsrc11_item_instruction (uid, description, sequence_id, item_uid) VALUES 
  (1,'Use Google to search the web',0,1);
    
SET FOREIGN_KEY_CHECKS=1;