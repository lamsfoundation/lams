
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lawiki10_attachment;
drop table if exists tl_lawiki10_session;
drop table if exists tl_lawiki10_user;
drop table if exists tl_lawiki10_wiki;
drop table if exists tl_lawiki10_wiki_page;
drop table if exists tl_lawiki10_wiki_page_content;
create table tl_lawiki10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, wiki_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lawiki10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), wiki_uid bigint, wiki_main_page_uid bigint, content_folder_id varchar(255), primary key (uid))ENGINE=InnoDB;
create table tl_lawiki10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, wiki_session_uid bigint, entry_uid bigint, wiki_edits integer, primary key (uid))ENGINE=InnoDB;
create table tl_lawiki10_wiki (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, allow_learner_create_pages bit, allow_learner_insert_links bit, allow_learner_attach_images bit, notify_updates bit, reflect_on_activity bit, reflect_instructions text, minimum_edits integer, maximum_edits integer, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, wiki_main_page_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lawiki10_wiki_page (
	uid bigint not null auto_increment, 
	wiki_uid bigint, 
	title varchar(255), 
	editable bit,
	deleted bit DEFAULT 0,
	wiki_current_content bigint, 
	added_by bigint, 
	wiki_session_uid bigint, 
	primary key (uid), 
	unique key wiki_unique_key (wiki_uid, title, wiki_session_uid)
)ENGINE=InnoDB;

create table tl_lawiki10_wiki_page_content (uid bigint not null auto_increment, wiki_page_uid bigint, body text, editor bigint, edit_date datetime, version bigint, primary key (uid))ENGINE=InnoDB;



alter table tl_lawiki10_attachment add index FK9406D87760B3B03B (wiki_uid), add constraint FK9406D87760B3B03B foreign key (wiki_uid) references tl_lawiki10_wiki (uid);
alter table tl_lawiki10_session add index FKF01D63C260B3B03B (wiki_uid), add constraint FKF01D63C260B3B03B foreign key (wiki_uid) references tl_lawiki10_wiki (uid);
alter table tl_lawiki10_session add index FKF01D63C2A3FF7EC0 (wiki_main_page_uid), add constraint FKF01D63C2A3FF7EC0 foreign key (wiki_main_page_uid) references tl_lawiki10_wiki_page (uid);
alter table tl_lawiki10_user add index FKED5D7A1FD8004954 (wiki_session_uid), add constraint FKED5D7A1FD8004954 foreign key (wiki_session_uid) references tl_lawiki10_session (uid);
alter table tl_lawiki10_wiki add index FKED5E3E04A3FF7EC0 (wiki_main_page_uid), add constraint FKED5E3E04A3FF7EC0 foreign key (wiki_main_page_uid) references tl_lawiki10_wiki_page (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_1 (wiki_session_uid), add constraint wiki_page_fk_1 foreign key (wiki_session_uid) references tl_lawiki10_session (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_2 (wiki_uid), add constraint wiki_page_fk_2 foreign key (wiki_uid) references tl_lawiki10_wiki (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_3 (added_by), add constraint wiki_page_fk_3 foreign key (added_by) references tl_lawiki10_user (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_4 (wiki_current_content), add constraint wiki_page_fk_4 foreign key (wiki_current_content) references tl_lawiki10_wiki_page_content (uid);
alter table tl_lawiki10_wiki_page_content add index FK528051242D44CCF8 (wiki_page_uid), add constraint FK528051242D44CCF8 foreign key (wiki_page_uid) references tl_lawiki10_wiki_page (uid);
alter table tl_lawiki10_wiki_page_content add index FK528051243233D952 (editor), add constraint FK528051243233D952 foreign key (editor) references tl_lawiki10_user (uid);

INSERT INTO tl_lawiki10_wiki 
(
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_learner_create_pages, 
	allow_learner_insert_links, 
	allow_learner_attach_images,
	notify_updates,
	reflect_on_activity, 
	reflect_instructions, 
	minimum_edits, 
	maximum_edits
) 
VALUES
(
	"Wiki",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	1,
	1,
	1,
	0,
	0,
	"",
	0,
	0
);

INSERT INTO tl_lawiki10_wiki_page 
(
	wiki_uid, 
	title, 
	editable
)
VALUES
(
	1,
	"Wiki Main Page",
	1
);

INSERT INTO tl_lawiki10_wiki_page_content 
(
	wiki_page_uid,
	body,
	version
)
VALUES
(
	1,
	"<div>Wiki Body</div>",
	0
);

UPDATE tl_lawiki10_wiki_page SET wiki_current_content=1 where uid=1;
UPDATE tl_lawiki10_wiki SET wiki_main_page_uid=1 where uid=1;


SET FOREIGN_KEY_CHECKS=1;
