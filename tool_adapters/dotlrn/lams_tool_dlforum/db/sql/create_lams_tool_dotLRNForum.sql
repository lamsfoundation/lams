-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_dlfrum10_dotlrnforum;
drop table if exists tl_dlfrum10_session;
drop table if exists tl_dlfrum10_user;
drop table if exists tl_dlfrum10_configuration;
create table tl_dlfrum10_dotlrnforum (uid bigint not null auto_increment, create_date datetime, update_date datetime, define_later bit, content_in_use bit, run_offline bit, tool_content_id bigint, ext_tool_content_id bigint, ext_user_name varchar(255), ext_course_id varchar(255), ext_course_url varchar(255), primary key (uid))TYPE=InnoDB;
create table tl_dlfrum10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, ext_session_id bigint, session_name varchar(250), dotLRNForum_uid bigint, primary key (uid))TYPE=InnoDB;
create table tl_dlfrum10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, dotlrnforum_session_uid bigint, entry_uid bigint, primary key (uid))TYPE=InnoDB;
create table tl_dlfrum10_configuration (uid bigint not null auto_increment, config_key varchar(30) unique, config_value varchar(255), primary key (uid))TYPE=InnoDB;
alter table tl_dlfrum10_session add index FKC3601D49E66F3387 (dotLRNForum_uid), add constraint FKC3601D49E66F3387 foreign key (dotLRNForum_uid) references tl_dlfrum10_dotlrnforum (uid);
alter table tl_dlfrum10_user add index FKEB7D85F89CB14C64 (dotlrnforum_session_uid), add constraint FKEB7D85F89CB14C64 foreign key (dotlrnforum_session_uid) references tl_dlfrum10_session (uid);
alter table tl_dlfrum10_user add index FKEB7D85F8C1134AEC (dotLRNForum_session_uid), add constraint FKEB7D85F8C1134AEC foreign key (dotLRNForum_session_uid) references tl_dlfrum10_session (uid);

INSERT INTO tl_dlfrum10_configuration (
	config_key, 
	config_value
)
VALUES(
	"toolAdapterServlet",
	"http://localhost:8000/lams2conf/tooladapter_dlfrum10"
);

INSERT INTO tl_dlfrum10_dotlrnforum (
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
