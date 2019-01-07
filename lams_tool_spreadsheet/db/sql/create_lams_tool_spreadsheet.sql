SET FOREIGN_KEY_CHECKS=0;


create table tl_lasprd10_spreadsheet_mark (
   uid bigint not null auto_increment,
   marks FLOAT,
   comments MEDIUMTEXT,
   date_marks_released datetime,
   primary key (uid)
);

create table tl_lasprd10_spreadsheet (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   is_learner_allowed_to_save TINYINT(1),
   is_marking_enabled TINYINT(1),
   lock_on_finished TINYINT(1),
   instructions MEDIUMTEXT,
   code text,
   content_in_use TINYINT(1),
   define_later TINYINT(1),
   content_id bigint,
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity TINYINT(1), 
   primary key (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_lasprd10_user_modified_spreadsheet (
   uid bigint not null auto_increment,
   user_modified_spreadsheet text,
   mark_id bigint,
   primary key (uid),
   CONSTRAINT FK_tl_lasprd10_user_modified_spreadsheet_1 FOREIGN KEY (mark_id)
   		REFERENCES tl_lasprd10_spreadsheet_mark (uid) ON DELETE SET NULL ON UPDATE NO ACTION
);

create table tl_lasprd10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status TINYINT(1),
   spreadsheet_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK_NEW_2065267438_24AA78C530E79035 FOREIGN KEY (spreadsheet_uid)
  		REFERENCES tl_lasprd10_spreadsheet (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasprd10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished TINYINT(1),
   session_uid bigint,
   spreadsheet_uid bigint,
   user_modified_spreadsheet_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_2065267438_30113BFC309ED320 FOREIGN KEY (spreadsheet_uid)
		REFERENCES tl_lasprd10_spreadsheet (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_2065267438_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_lasprd10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_2065267438_693580A441F9365D FOREIGN KEY (user_modified_spreadsheet_uid)
   		REFERENCES tl_lasprd10_user_modified_spreadsheet (uid) ON DELETE SET NULL ON UPDATE NO ACTION
);


ALTER TABLE tl_lasprd10_spreadsheet ADD CONSTRAINT FK_NEW_2065267438_89093BF758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_lasprd10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
   		
   		
INSERT INTO tl_lasprd10_spreadsheet (uid, title, is_learner_allowed_to_save, 
  is_marking_enabled, lock_on_finished, instructions, code,content_in_use, define_later, 
  content_id, reflect_on_activity) VALUES
  (1,'Spreadsheet','1','0','0','Instructions ','',0,0,${default_content_id},0);
  
SET FOREIGN_KEY_CHECKS=1;