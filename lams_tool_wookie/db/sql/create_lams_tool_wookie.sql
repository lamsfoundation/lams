
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lawook10_attachment;
drop table if exists tl_lawook10_wookie;
drop table if exists tl_lawook10_session;
drop table if exists tl_lawook10_user;
drop table if exists tl_lawook10_configuration;
create table tl_lawook10_attachment (
	uid bigint not null auto_increment, 
	file_version_id bigint, 
	file_type varchar(255), 
	file_name varchar(255), 
	file_uuid bigint, 
	create_date datetime, 
	wookie_uid bigint, 
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lawook10_wookie (
	uid bigint not null auto_increment, 
	create_date datetime, 
	update_date datetime, 
	create_by bigint, 
	title varchar(255), 
	instructions text, 
	run_offline bit, 
	lock_on_finished bit, 
	reflect_on_activity bit, 
	online_instructions text, 
	offline_instructions text,
	content_in_use bit, 
	define_later bit, 
	tool_content_id bigint, 
	reflect_instructions text, 
	widget_identifier varchar(511),
	widget_author_url varchar(511),
	widget_maximise bit,
	widget_width integer,
	widget_height integer,
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lawook10_session (
	uid bigint not null auto_increment, 
	session_end_date datetime, 
	session_start_date datetime, 
	status integer, 
	session_id bigint, 
	session_name varchar(250), 
	wookie_uid bigint, 
	widget_shared_data_key varchar(63),
	widget_height integer,
	widget_width integer,
	widget_maximise bit,
	widget_identifier varchar(511),
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lawook10_user (
	uid bigint not null auto_increment, 
	user_id bigint, 
	last_name varchar(255),
	login_name varchar(255), 
	first_name varchar(255), 
	finishedActivity bit, 
	wookie_session_uid bigint, 
	entry_uid bigint, 
	user_widget_url varchar(511),
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lawook10_configuration (
	uid bigint not null auto_increment, 
	config_key varchar(30) unique, 
	config_value text, primary key (uid)
)ENGINE=InnoDB;

alter table tl_lawook10_attachment add index IN_WOOKIE_ATTACH (wookie_uid), add constraint FK_WOOKIE_ATTACH foreign key (wookie_uid) references tl_lawook10_wookie (uid);
alter table tl_lawook10_session add index IN_WOOKIE_SESSION (wookie_uid), add constraint FK_WOOKIE_SESSION foreign key (wookie_uid) references tl_lawook10_wookie (uid);
alter table tl_lawook10_user add index IN_WOOKIE_USER (wookie_session_uid), add constraint FK_WOOKIE_USER foreign key (wookie_session_uid) references tl_lawook10_session (uid);

-- Inserting the required config item into the config table
-- Currently supported languages for wookie
INSERT INTO tl_lawook10_configuration (
	config_key, 
	config_value
)
VALUES(
	"APIKEY",
	"TEST"
);

INSERT INTO tl_lawook10_configuration (
	config_key, 
	config_value
)
VALUES(
	"WOOKIE_SERVER_URL",
	"http://localhost:8180/wookie/"
);


INSERT INTO tl_lawook10_wookie (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity
) 
VALUES(
	"Wookie",
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

