SET FOREIGN_KEY_CHECKS=0;

create table tl_lasbmt11_content (
   content_id bigint not null,
   title varchar(255) not null,
   instruction text,
   define_later TINYINT(1),
   content_in_use TINYINT(1),
   lock_on_finished TINYINT(1),
   reflect_instructions TEXT, 
   reflect_on_activity TINYINT(1), 
   limit_upload TINYINT(1), 
   limit_upload_number integer,
   created datetime, 
   created_by bigint, 
   updated datetime,
   submission_deadline datetime,
   mark_release_notify TINYINT(1) DEFAULT 0,
   file_submit_notify TINYINT(1) DEFAULT 0,
   primary key (content_id)
);

create table tl_lasbmt11_session (
   session_id bigint not null,
   status TINYINT(1),
   content_id bigint,
   session_name varchar(250),
   primary key (session_id),
   CONSTRAINT FKEC8C77C9785A173A FOREIGN KEY (content_id)
   		REFERENCES tl_lasbmt11_content (content_id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasbmt11_user (
	uid bigint not null auto_increment, 
	user_id integer, 
	finished TINYINT(1), 
	session_id bigint, 
	first_name varchar(255), 
	login_name varchar(255), 
	last_name varchar(255), 
	content_id bigint, 
	primary key (uid),
	KEY session_id (session_id),
    CONSTRAINT tl_lasbmt11_user_ibfk_1 FOREIGN KEY (session_id)
    		REFERENCES tl_lasbmt11_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasbmt11_submission_details (
   submission_id bigint not null auto_increment,
   filePath varchar(250),
   fileDescription text,
   date_of_submission datetime,
   uuid bigint,
   version_id bigint,
   session_id bigint,
   learner_id bigint,
   removed tinyint(1) NOT NULL DEFAULT 0,
   PRIMARY KEY (submission_id),
   CONSTRAINT FK1411A53C93C861A FOREIGN KEY (session_id)
   		REFERENCES tl_lasbmt11_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK1411A53CFFD5A38B FOREIGN KEY (learner_id)
   		REFERENCES tl_lasbmt11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasbmt11_report (
   report_id bigint not null auto_increment,
   comments text,
   marks float,
   date_marks_released datetime,
   mark_file_uuid bigint,
   mark_file_version_id bigint,
   mark_file_name varchar(255),
   primary key (report_id),
   CONSTRAINT tl_lasbmt11_report_ibfk_1 FOREIGN KEY (report_id)
   		REFERENCES tl_lasbmt11_submission_details (submission_id) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_lasbmt11_content ADD CONSTRAINT FKAEF329AC172BC670 FOREIGN KEY (created_by)
   		REFERENCES tl_lasbmt11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
   		

INSERT INTO tl_lasbmt11_content (content_id,title,instruction,define_later,content_in_use,lock_on_finished,reflect_on_activity,limit_upload,limit_upload_number) 
values(${default_content_id},"Submit Files","Instructions",0,0,0,0,0,1);

SET FOREIGN_KEY_CHECKS=1;