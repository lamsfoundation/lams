
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lapixl10_attachment;
drop table if exists tl_lapixl10_pixlr;
drop table if exists tl_lapixl10_session;
drop table if exists tl_lapixl10_user;
drop table if exists tl_lapixl10_configuration;
create table tl_lapixl10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, pixlr_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lapixl10_pixlr (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, reflect_on_activity bit, allow_view_others_images bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, image_file_name text, image_width bigint, image_height bigint, reflect_instructions text, primary key (uid))ENGINE=InnoDB;
create table tl_lapixl10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), pixlr_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lapixl10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, pixlr_session_uid bigint, entry_uid bigint, image_file_name text, imageHeight bigint, imageWidth bigint, imageHidden bit, primary key (uid))ENGINE=InnoDB;
create table tl_lapixl10_configuration (uid bigint not null auto_increment, config_key varchar(30) unique, config_value text, primary key (uid))ENGINE=InnoDB;
alter table tl_lapixl10_attachment add index FK951F889ACB8ADA74 (pixlr_uid), add constraint FK951F889ACB8ADA74 foreign key (pixlr_uid) references tl_lapixl10_pixlr (uid);
alter table tl_lapixl10_session add index FKE5C05E7FCB8ADA74 (pixlr_uid), add constraint FKE5C05E7FCB8ADA74 foreign key (pixlr_uid) references tl_lapixl10_pixlr (uid);
alter table tl_lapixl10_user add index FK9A39C08236E23005 (pixlr_session_uid), add constraint FK9A39C08236E23005 foreign key (pixlr_session_uid) references tl_lapixl10_session (uid);

-- Inserting the required config item into the config table
-- Currently supported languages for pixlr
INSERT INTO tl_lapixl10_configuration (
	config_key, 
	config_value
)
VALUES(
	"LanguageCSV",
	"en,es,fr,de,it,ru,ro,zh-cn,pt-br,sv,pl,th"
);


INSERT INTO tl_lapixl10_pixlr (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity,
	allow_view_others_images,
	image_width, 
	image_height,
	image_file_name
	
) 
VALUES(
	"Pixlr",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	0,
	1,
	400,
	450,
	"blank.jpg"
);

