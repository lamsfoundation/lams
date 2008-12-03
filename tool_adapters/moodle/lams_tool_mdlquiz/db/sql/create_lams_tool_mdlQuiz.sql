-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_mdquiz10_configuration;
drop table if exists tl_mdquiz10_mdlquiz;
drop table if exists tl_mdquiz10_session;
drop table if exists tl_mdquiz10_user;
create table tl_mdquiz10_configuration (uid bigint not null auto_increment, config_key varchar(30) unique, config_value varchar(255), primary key (uid))TYPE=InnoDB;
create table tl_mdquiz10_mdlquiz (uid bigint not null auto_increment, create_date datetime, update_date datetime, define_later bit, content_in_use bit, run_offline bit, tool_content_id bigint, ext_tool_content_id bigint, ext_user_name varchar(255), ext_course_id varchar(255), ext_section varchar(255), primary key (uid))TYPE=InnoDB;
create table tl_mdquiz10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, ext_session_id bigint, session_name varchar(250), mdlQuiz_uid bigint, primary key (uid))TYPE=InnoDB;
create table tl_mdquiz10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, mdlquiz_session_uid bigint, entry_uid bigint, primary key (uid))TYPE=InnoDB;
alter table tl_mdquiz10_session add index FKmdquiz10_session (mdlQuiz_uid), add constraint FKmdquiz10_session foreign key (mdlQuiz_uid) references tl_mdquiz10_mdlquiz (uid);
alter table tl_mdquiz10_user add index FKmdquiz10_user (mdlquiz_session_uid), add constraint FKmdquiz10_user foreign key (mdlquiz_session_uid) references tl_mdquiz10_session (uid);

INSERT INTO tl_mdquiz10_configuration (
	config_key, 
	config_value
)
VALUES(
	"toolAdapterServlet",
	"http://localhost:8000/lams2conf/tooladapter_mdquiz10"
);

INSERT INTO tl_mdquiz10_configuration (
	config_key, 
	config_value
)
VALUES(
	"extServerUrl",
	"http://localhost/moodle19/"
);

INSERT INTO tl_mdquiz10_mdlquiz (
	tool_content_id,
	ext_tool_content_id,
	run_offline,
	content_in_use,
	define_later
) 
VALUES(
	0,
	null,
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;
