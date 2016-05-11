
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lamind10_attachment;
drop table if exists tl_lamind10_mindmap;
drop table if exists tl_lamind10_session;
drop table if exists tl_lamind10_user;
drop table if exists tl_lamind10_node;
drop table if exists tl_lamind10_request;

create table tl_lamind10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, mindmap_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lamind10_mindmap (uid bigint not null auto_increment, create_date datetime, update_date datetime, submission_deadline datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, multiuser_mode bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, export_content text, reflect_on_activity bit, reflect_instructions text, primary key (uid))ENGINE=InnoDB;
create table tl_lamind10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), mindmap_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lamind10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, mindmap_session_uid bigint, entry_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lamind10_node (node_id bigint not null auto_increment, unique_id bigint, parent_id bigint, node_text varchar(100), node_color varchar(6), session_id bigint, user_id bigint, mindmap_id bigint, primary key (node_id))ENGINE=InnoDB;
CREATE TABLE tl_lamind10_request (`uid` BIGINT(20) NOT NULL AUTO_INCREMENT, `unique_id` BIGINT(20) DEFAULT NULL, `global_id` BIGINT(20) DEFAULT NULL, `request_type` TINYINT(4) DEFAULT NULL, `node_id` BIGINT(20) DEFAULT NULL, `node_child_id` BIGINT(20) DEFAULT NULL, `user_id` BIGINT(20) DEFAULT NULL, `mindmap_id` BIGINT(20) DEFAULT NULL, PRIMARY KEY (`uid`), KEY `mindmap_id` (`mindmap_id`), KEY `user_id` (`user_id`), CONSTRAINT `tl_lamind10_request_fk` FOREIGN KEY (`mindmap_id`) REFERENCES `tl_lamind10_mindmap` (`uid`), CONSTRAINT `tl_lamind10_request_fk1` FOREIGN KEY (`user_id`) REFERENCES `tl_lamind10_user` (`uid`))ENGINE=InnoDB;

alter table tl_lamind10_attachment add index FK_NEW_972920762_12090F57FC940906 (mindmap_uid), add constraint FK_NEW_972920762_12090F57FC940906 foreign key (mindmap_uid) references tl_lamind10_mindmap (uid);
alter table tl_lamind10_session add index FK_NEW_972920762_B7C198E2FC940906 (mindmap_uid), add constraint FK_NEW_972920762_B7C198E2FC940906 foreign key (mindmap_uid) references tl_lamind10_mindmap (uid);
alter table tl_lamind10_user add index FK_NEW_972920762_CB8A58FFA3B0FADF (mindmap_session_uid), add constraint FK_NEW_972920762_CB8A58FFA3B0FADF foreign key (mindmap_session_uid) references tl_lamind10_session (uid);
alter table tl_lamind10_node add index MindmapNode_Parent_FK (parent_id), add constraint MindmapNode_Parent_FK foreign key (parent_id) references tl_lamind10_node (node_id);
alter table tl_lamind10_node add index MindmapNode_User_FK (user_id), add constraint MindmapNode_User_FK foreign key (user_id) references tl_lamind10_user (uid);
alter table tl_lamind10_node add index MindmapNode_Mindmap_FK (mindmap_id), add constraint MindmapNode_Mindmap_FK foreign key (mindmap_id) references tl_lamind10_mindmap (uid);

INSERT INTO tl_lamind10_mindmap (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	multiuser_mode,
	reflect_on_activity) 
VALUES(
	"Mindmap",
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
