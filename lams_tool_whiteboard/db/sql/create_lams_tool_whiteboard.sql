SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS tl_lawhiteboard11_whiteboard;
DROP TABLE IF EXISTS tl_lawhiteboard11_session;
DROP TABLE IF EXISTS tl_lawhiteboard11_user;
CREATE TABLE tl_lawhiteboard11_whiteboard (
   uid bigint not null auto_increment,
   content_id bigint unique,
   source_wid varchar(64),
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished tinyint,
   instructions MEDIUMTEXT,
   content_in_use tinyint,
   define_later tinyint,
   use_select_leader_tool_ouput tinyint,
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity smallint,
   relative_time_limit smallint unsigned NOT NULL DEFAULT '0',
   absolute_time_limit datetime DEFAULT NULL,
   gallery_walk_enabled TINYINT NOT NULL DEFAULT 0,
   gallery_walk_read_only TINYINT NOT NULL DEFAULT 0,
   gallery_walk_started TINYINT NOT NULL DEFAULT 0,
   gallery_walk_finished TINYINT NOT NULL DEFAULT 0,
   gallery_walk_instructions TEXT,
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_lawhiteboard11_session (
   uid bigint not null auto_increment,
   whiteboard_uid bigint,
   session_id bigint,
   status integer,
   session_name varchar(250),
   group_leader_uid BIGINT,
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_lawhiteboard11_user (
   uid bigint not null auto_increment,
   session_uid bigint,
   whiteboard_uid bigint,
   user_id bigint,
   login_name varchar(255),
   last_name varchar(255),
   first_name varchar(255),
   session_finished smallint,
   time_limit_launched_date datetime DEFAULT NULL,
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_lawhiteboard11_time_limit (
	whiteboard_uid BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	adjustment SMALLINT NOT NULL DEFAULT 0,
	CONSTRAINT FK_tl_lawhiteboard11_time_limit_1 FOREIGN KEY (whiteboard_uid)
		REFERENCES tl_lawhiteboard11_whiteboard (uid) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_tl_lawhiteboard11_time_limit_2 FOREIGN KEY (user_id)
		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tl_lawhiteboard11_configuration (
  uid TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  config_key varchar(30) NOT NULL,
  config_value varchar(255),
  PRIMARY KEY (uid),
  UNIQUE KEY UQ_tl_lawhiteboard11_configuration_config_key (config_key)
) ENGINE=InnoDB;

ALTER TABLE tl_lawhiteboard11_whiteboard ADD CONSTRAINT FK_tl_lawhiteboard11_whiteboard_1 FOREIGN KEY (create_by) REFERENCES tl_lawhiteboard11_user (uid)  ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_lawhiteboard11_session ADD CONSTRAINT FK_tl_lawhiteboard11_session_2 FOREIGN KEY (whiteboard_uid) REFERENCES tl_lawhiteboard11_whiteboard (uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE tl_lawhiteboard11_session ADD UNIQUE UQ_tl_lawhiteboard11_session_session_id (session_id);
ALTER TABLE tl_lawhiteboard11_user ADD CONSTRAINT tl_lawhiteboard11_user_1 FOREIGN KEY (session_uid) REFERENCES tl_lawhiteboard11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE tl_lawhiteboard11_user ADD CONSTRAINT tl_lawhiteboard11_user_2 FOREIGN KEY (whiteboard_uid) REFERENCES tl_lawhiteboard11_whiteboard (uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE tl_lawhiteboard11_user ADD INDEX idx_user_user_id (user_id);
ALTER TABLE tl_lawhiteboard11_session ADD CONSTRAINT tl_lawhiteboard11_session_2 FOREIGN KEY (`group_leader_uid`) REFERENCES `tl_lawhiteboard11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

INSERT INTO `tl_lawhiteboard11_whiteboard` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `lock_on_finished`,
 `instructions`, `content_in_use`, `define_later`, `content_id`, `use_select_leader_tool_ouput`, `reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'Whiteboard','0','Instructions',0,0,${default_content_id},0,0);
  
INSERT INTO tl_lawhiteboard11_configuration (config_key, config_value) VALUES
('ServerURL', '[LAMS_SERVER_URL]:9003');
  
  
SET FOREIGN_KEY_CHECKS=1;