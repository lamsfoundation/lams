SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tl_ladoku11_dokumaran;

CREATE TABLE tl_ladoku11_dokumaran (
  uid bigint NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  create_by bigint DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  lock_on_finished tinyint DEFAULT NULL,
  `description` mediumtext,
  instructions mediumtext,
  content_in_use tinyint DEFAULT NULL,
  define_later tinyint DEFAULT NULL,
  content_id bigint DEFAULT NULL,
  show_chat tinyint DEFAULT NULL,
  show_line_numbers tinyint DEFAULT NULL,
  shared_pad_id varchar(255) DEFAULT NULL,
  use_select_leader_tool_ouput tinyint DEFAULT NULL,
  allow_multiple_leaders tinyint DEFAULT NULL,
  reflect_instructions mediumtext,
  reflect_on_activity smallint DEFAULT NULL,
  relative_time_limit smallint unsigned NOT NULL DEFAULT '0',
  absolute_time_limit datetime DEFAULT NULL,
  gallery_walk_enabled tinyint NOT NULL DEFAULT '0',
  gallery_walk_read_only tinyint NOT NULL DEFAULT '0',
  gallery_walk_started tinyint NOT NULL DEFAULT '0',
  gallery_walk_finished tinyint NOT NULL DEFAULT '0',
  gallery_walk_edit_enabled tinyint NOT NULL DEFAULT '0',
  gallery_walk_instructions text,
  PRIMARY KEY (uid),
  UNIQUE KEY content_id (content_id),
  KEY FK_NEW_680978081_89093BF758092FB (create_by),
  CONSTRAINT FK_NEW_680978081_89093BF758092FB FOREIGN KEY (create_by) REFERENCES tl_ladoku11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_ladoku11_session;

CREATE TABLE tl_ladoku11_session (
  uid bigint NOT NULL AUTO_INCREMENT,
  session_end_date datetime DEFAULT NULL,
  session_start_date datetime DEFAULT NULL,
  `status` int DEFAULT NULL,
  dokumaran_uid bigint DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  session_name varchar(250) DEFAULT NULL,
  group_leader_uid bigint DEFAULT NULL,
  etherpad_group_id varchar(50) DEFAULT NULL,
  etherpad_read_only_id varchar(50) DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  KEY FK_NEW_680978081_24AA78C530E79035 (dokumaran_uid),
  KEY tl_ladoku11_session (group_leader_uid),
  CONSTRAINT FK_NEW_680978081_24AA78C530E79035 FOREIGN KEY (dokumaran_uid) REFERENCES tl_ladoku11_dokumaran (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT tl_ladoku11_session FOREIGN KEY (group_leader_uid) REFERENCES tl_ladoku11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_ladoku11_time_limit;

CREATE TABLE tl_ladoku11_time_limit (
  dokumaran_uid bigint NOT NULL,
  user_id bigint NOT NULL,
  adjustment smallint NOT NULL DEFAULT '0',
  KEY FK_tl_ladoku11_time_limit_1 (dokumaran_uid),
  KEY FK_tl_ladoku11_time_limit_2 (user_id),
  CONSTRAINT FK_tl_ladoku11_time_limit_1 FOREIGN KEY (dokumaran_uid) REFERENCES tl_ladoku11_dokumaran (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_ladoku11_time_limit_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_ladoku11_user;

CREATE TABLE tl_ladoku11_user (
  uid bigint NOT NULL AUTO_INCREMENT,
  user_id bigint DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  login_name varchar(255) DEFAULT NULL,
  session_finished smallint DEFAULT NULL,
  leader smallint DEFAULT NULL,
  session_uid bigint DEFAULT NULL,
  dokumaran_uid bigint DEFAULT NULL,
  time_limit_launched_date datetime DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY FK_NEW_680978081_30113BFCEC0D3147 (session_uid),
  KEY FK_NEW_680978081_30113BFC309ED320 (dokumaran_uid),
  KEY idx_user_user_id (user_id),
  CONSTRAINT FK_NEW_680978081_30113BFC309ED320 FOREIGN KEY (dokumaran_uid) REFERENCES tl_ladoku11_dokumaran (uid) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT FK_NEW_680978081_30113BFCEC0D3147 FOREIGN KEY (session_uid) REFERENCES tl_ladoku11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO `tl_ladoku11_dokumaran` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `lock_on_finished`,`description`,
 `instructions`, `content_in_use`, `define_later`, `content_id`, `show_chat`, 
 `show_line_numbers`, `shared_pad_id`, `use_select_leader_tool_ouput`, `allow_multiple_leaders`, `reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'doKu','0','Instructions','Document',0,0,${default_content_id},0,0,NULL,0,0,0);
  
SET FOREIGN_KEY_CHECKS=1;