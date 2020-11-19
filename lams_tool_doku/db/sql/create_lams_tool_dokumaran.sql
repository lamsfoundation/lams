SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS tl_ladoku11_dokumaran;
DROP TABLE IF EXISTS tl_ladoku11_session;
DROP TABLE IF EXISTS tl_ladoku11_user;
DROP TABLE IF EXISTS tl_ladoku11_configuration;
CREATE TABLE tl_ladoku11_dokumaran (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished tinyint,
   description MEDIUMTEXT,
   instructions MEDIUMTEXT,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   show_chat tinyint,
   show_line_numbers tinyint,
   shared_pad_id varchar(255),
   use_select_leader_tool_ouput tinyint,
   allow_multiple_leaders tinyint,
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity smallint,
   time_limit integer DEFAULT 0,
   time_limit_launched_date datetime,
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_ladoku11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   dokumaran_uid bigint,
   session_id bigint,
   session_name varchar(250),
   group_leader_uid BIGINT,
   etherpad_group_id varchar(50),
   etherpad_read_only_id varchar(50),
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_ladoku11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   leader smallint,
   session_uid bigint,
   dokumaran_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_ladoku11_configuration (
	uid bigint not null auto_increment, 
	config_key varchar(30) unique, 
	config_value varchar(255), 
	primary key (uid)
)ENGINE=InnoDB;
ALTER TABLE tl_ladoku11_dokumaran ADD INDEX FK_NEW_680978081_89093BF758092FB (create_by), ADD CONSTRAINT FK_NEW_680978081_89093BF758092FB FOREIGN KEY (create_by) REFERENCES tl_ladoku11_user (uid)  ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_ladoku11_session ADD INDEX FK_NEW_680978081_24AA78C530E79035 (dokumaran_uid), ADD CONSTRAINT FK_NEW_680978081_24AA78C530E79035 FOREIGN KEY (dokumaran_uid) REFERENCES tl_ladoku11_dokumaran (uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE tl_ladoku11_session ADD UNIQUE (session_id);
ALTER TABLE tl_ladoku11_user ADD INDEX FK_NEW_680978081_30113BFCEC0D3147 (session_uid), ADD CONSTRAINT FK_NEW_680978081_30113BFCEC0D3147 FOREIGN KEY (session_uid) REFERENCES tl_ladoku11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE tl_ladoku11_user ADD INDEX FK_NEW_680978081_30113BFC309ED320 (dokumaran_uid), ADD CONSTRAINT FK_NEW_680978081_30113BFC309ED320 FOREIGN KEY (dokumaran_uid) REFERENCES tl_ladoku11_dokumaran (uid) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_ladoku11_user ADD INDEX idx_user_user_id (user_id);
ALTER TABLE tl_ladoku11_session ADD INDEX tl_ladoku11_session (group_leader_uid), ADD CONSTRAINT tl_ladoku11_session FOREIGN KEY (`group_leader_uid`) REFERENCES `tl_ladoku11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

INSERT INTO `tl_ladoku11_dokumaran` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `lock_on_finished`,`description`,
 `instructions`, `content_in_use`, `define_later`, `content_id`, `show_chat`, 
 `show_line_numbers`, `shared_pad_id`, `use_select_leader_tool_ouput`, `allow_multiple_leaders`, `reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'doKu','0','Instructions','Document',0,0,${default_content_id},0,0,NULL,0,0,0);
  
SET FOREIGN_KEY_CHECKS=1;