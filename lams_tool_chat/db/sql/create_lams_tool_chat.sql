
 
SET FOREIGN_KEY_CHECKS=0;

drop table if exists tl_lachat11_attachment;
drop table if exists tl_lachat11_chat;
drop table if exists tl_lachat11_message;
drop table if exists tl_lachat11_session;
drop table if exists tl_lachat11_user;
drop table if exists tl_lachat11_conditions;

create table tl_lachat11_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, chat_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lachat11_chat (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, reflect_on_activity bit, reflect_instructions text, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, filtering_enabled bit, filter_keywords text, submission_deadline datetime default null, primary key (uid))ENGINE=InnoDB;
create table tl_lachat11_message (uid bigint not null auto_increment, chat_session_uid bigint not null, from_user_uid bigint, to_user_uid bigint, type varchar(255), body text, send_date datetime, hidden bit, primary key (uid))ENGINE=InnoDB;
create table tl_lachat11_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), chat_uid bigint, jabber_room varchar(250), room_created bit, primary key (uid))ENGINE=InnoDB;
create table tl_lachat11_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), jabber_id varchar(255), finishedActivity bit, jabber_nickname varchar(255), chat_session_uid bigint, primary key (uid))ENGINE=InnoDB;

CREATE TABLE tl_lachat11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT ChatConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ChatConditionToChat FOREIGN KEY (content_uid)
                  REFERENCES tl_lachat11_chat(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

alter table tl_lachat11_attachment add index FK9ED6CB2E1A3926E3 (chat_uid), add constraint FK9ED6CB2E1A3926E3 foreign key (chat_uid) references tl_lachat11_chat (uid);
alter table tl_lachat11_message add index FKCC08C1DC2AF61E05 (to_user_uid), add constraint FKCC08C1DC2AF61E05 foreign key (to_user_uid) references tl_lachat11_user (uid);
alter table tl_lachat11_message add index FKCC08C1DC9C8469FC (chat_session_uid), add constraint FKCC08C1DC9C8469FC foreign key (chat_session_uid) references tl_lachat11_session (uid);
alter table tl_lachat11_message add index FKCC08C1DCCF3BF9B6 (from_user_uid), add constraint FKCC08C1DCCF3BF9B6 foreign key (from_user_uid) references tl_lachat11_user (uid);
alter table tl_lachat11_session add index FK96E446B1A3926E3 (chat_uid), add constraint FK96E446B1A3926E3 foreign key (chat_uid) references tl_lachat11_chat (uid);
alter table tl_lachat11_user add index FK4EB82169C8469FC (chat_session_uid), add constraint FK4EB82169C8469FC foreign key (chat_session_uid) references tl_lachat11_session (uid);

INSERT INTO tl_lachat11_chat (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	filtering_enabled,
	content_in_use,
	define_later,
	reflect_on_activity) 
VALUES(
	"Chat",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;