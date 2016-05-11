
 
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS tl_lantbk11_conditions;
drop table if exists tl_lantbk11_attachment;
drop table if exists tl_lantbk11_notebook;
drop table if exists tl_lantbk11_session;
drop table if exists tl_lantbk11_user;
create table tl_lantbk11_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, notebook_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lantbk11_notebook (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, allow_rich_editor bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, submission_deadline datetime DEFAULT NULL, primary key (uid))ENGINE=InnoDB;
create table tl_lantbk11_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), notebook_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lantbk11_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, notebook_session_uid bigint, entry_uid bigint, primary key (uid))ENGINE=InnoDB;
alter table tl_lantbk11_attachment add index FK12090F57FC940906 (notebook_uid), add constraint FK12090F57FC940906 foreign key (notebook_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_session add index FKB7C198E2FC940906 (notebook_uid), add constraint FKB7C198E2FC940906 foreign key (notebook_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_user add index FKCB8A58FFA3B0FADF (notebook_session_uid), add constraint FKCB8A58FFA3B0FADF foreign key (notebook_session_uid) references tl_lantbk11_session (uid);

INSERT INTO tl_lantbk11_notebook (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_rich_editor) 
VALUES(
	"Notebook",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	0
);

CREATE TABLE tl_lantbk11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT NotebookConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT NotebookConditionToNotebook FOREIGN KEY (content_uid)
                  REFERENCES tl_lantbk11_notebook(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;