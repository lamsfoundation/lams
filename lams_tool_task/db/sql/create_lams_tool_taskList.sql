SET FOREIGN_KEY_CHECKS=0;

create table tl_latask10_tasklist (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   instructions MEDIUMTEXT,
   content_in_use TINYINT(1),
   define_later TINYINT(1),
   content_id bigint,
   lock_when_finished TINYINT(1),
   is_sequential_order TINYINT(1),
   minimum_number_tasks integer,
   allow_contribute_tasks TINYINT(1),
   is_monitor_verification_required TINYINT(1),
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity TINYINT(1), 
   submission_deadline datetime,
   primary key (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_latask10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status TINYINT(1),
   taskList_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK_NEW_174079138_24AA78C530E79035 FOREIGN KEY (taskList_uid)
   		REFERENCES tl_latask10_tasklist (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_latask10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished TINYINT(1),
   session_uid bigint,
   taskList_uid bigint,
   is_verified_by_monitor TINYINT(1),
   primary key (uid),
   CONSTRAINT FK_NEW_174079138_30113BFC309ED320 FOREIGN KEY (taskList_uid)
   		REFERENCES tl_latask10_tasklist (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_174079138_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_latask10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_latask10_tasklist_item (
   uid bigint not null auto_increment,
   sequence_id integer,
   description MEDIUMTEXT,
   init_item varchar(255),
   organization_xml text,
   title varchar(255),
   create_by bigint,
   create_date datetime,
   create_by_author TINYINT(1),
   is_required TINYINT(1),
   is_comments_allowed TINYINT(1),
   is_comments_required TINYINT(1),
   is_files_allowed TINYINT(1),
   is_files_required TINYINT(1),
   is_child_task TINYINT(1),
   parent_task_name varchar(255),
   taskList_uid bigint,
   session_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_174079138_F52D1F9330E79035 FOREIGN KEY (taskList_uid)
   		REFERENCES tl_latask10_tasklist (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_174079138_F52D1F93758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_latask10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_174079138_F52D1F93EC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_latask10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);
   
create table tl_latask10_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   taskList_item_uid bigint,
   user_uid bigint,
   complete TINYINT(1),
   session_id bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_174079138_693580A438BF8DFE FOREIGN KEY (taskList_item_uid)
   		REFERENCES tl_latask10_tasklist_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_174079138_693580A441F9365D FOREIGN KEY (user_uid)
   		REFERENCES tl_latask10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_latask10_item_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   taskList_item_uid bigint,
   create_by bigint,
   primary key (uid),
   CONSTRAINT FK_tl_latask10_item_attachment_1 FOREIGN KEY (taskList_item_uid)
   		REFERENCES tl_latask10_tasklist_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_latask10_item_attachment_2 FOREIGN KEY (create_by)
   		REFERENCES tl_latask10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

create table tl_latask10_item_comment (
   uid bigint not null auto_increment,
   comment MEDIUMTEXT,
   taskList_item_uid bigint,
   create_by bigint,
   create_date datetime,
   primary key (uid),
   CONSTRAINT FK_tl_latask10_item_comment_2 FOREIGN KEY (create_by)
   		REFERENCES tl_latask10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_latask10_item_comment_3 FOREIGN KEY (taskList_item_uid)
   		REFERENCES tl_latask10_tasklist_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_latask10_condition (
   condition_uid bigint not null auto_increment,
   sequence_id integer,
   taskList_uid bigint,
   name varchar(255),
   primary key (condition_uid),
   CONSTRAINT FK_tl_latask10_condition_1 FOREIGN KEY (taskList_uid)
   		REFERENCES tl_latask10_tasklist (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_latask10_condition_tl_item (
   uid bigint not null,
   condition_uid bigint not null,
   primary key (uid, condition_uid),
   CONSTRAINT FK_tl_latask10_tasklist_item_condition_1 FOREIGN KEY (condition_uid)
   		REFERENCES tl_latask10_condition (condition_uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_latask10_tasklist_item_condition_2 FOREIGN KEY (uid)
   		REFERENCES tl_latask10_tasklist_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_latask10_tasklist ADD CONSTRAINT FK_NEW_174079138_89093BF758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_latask10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;

   		
INSERT INTO tl_latask10_tasklist (uid, title,instructions,
	content_in_use, define_later, content_id, lock_when_finished, 
	minimum_number_tasks, is_sequential_order, allow_contribute_tasks, is_monitor_verification_required, 
	reflect_on_activity) VALUES
  (1,'Task List','Instructions ',0,0,${default_content_id},0,0,0,0,0,0);
  
INSERT INTO tl_latask10_tasklist_item (uid, sequence_id, title,  create_date, create_by_author, is_required, is_comments_allowed, is_comments_required, is_files_allowed, is_files_required, is_child_task, taskList_uid) VALUES 
  (1,1,'Task number 1',NOW(),1,0,0,0,0,0,0,1);
    
SET FOREIGN_KEY_CHECKS=1;