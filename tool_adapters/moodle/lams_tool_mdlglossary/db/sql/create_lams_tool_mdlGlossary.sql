-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_mdglos10_mdlglossary;
drop table if exists tl_mdglos10_session;
drop table if exists tl_mdglos10_user;
create table tl_mdglos10_mdlglossary (uid bigint not null auto_increment, create_date datetime, update_date datetime, define_later bit, content_in_use bit, run_offline bit, tool_content_id bigint, ext_tool_content_id bigint, ext_user_name varchar(255), ext_lms_id varchar(255), ext_course_id varchar(255), ext_section varchar(255), primary key (uid))TYPE=InnoDB;
create table tl_mdglos10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, ext_session_id bigint, session_name varchar(250), mdlglossary_uid bigint, primary key (uid))TYPE=InnoDB;
create table tl_mdglos10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, mdlglossary_session_uid bigint, entry_uid bigint, primary key (uid))TYPE=InnoDB;
alter table tl_mdglos10_session add index FKGLOSBA4AC37B89669C81 (mdlglossary_uid), add constraint FKGLOSBA4AC37B89669C81 foreign key (mdlglossary_uid) references tl_mdglos10_mdlglossary (uid);
alter table tl_mdglos10_user add index FKGLOS1B25DD0618E034DA (mdlglossary_session_uid), add constraint FKGLOS1B25DD0618E034DA foreign key (mdlglossary_session_uid) references tl_mdglos10_session (uid);

INSERT INTO tl_mdglos10_mdlglossary (
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
