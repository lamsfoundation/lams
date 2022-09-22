SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tl_lascrt11_answer_log;

CREATE TABLE tl_lascrt11_answer_log (
  uid bigint NOT NULL AUTO_INCREMENT,
  access_date datetime DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY sessionIdIndex (session_id),
  CONSTRAINT FK_tl_lascrt11_answer_log_1 FOREIGN KEY (uid) REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT sessionIdIndex FOREIGN KEY (session_id) REFERENCES tl_lascrt11_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lascrt11_burning_que_like;

CREATE TABLE tl_lascrt11_burning_que_like (
  uid bigint NOT NULL AUTO_INCREMENT,
  burning_question_uid bigint DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY FK_burning_que_uid (burning_question_uid),
  CONSTRAINT FK_burning_que_uid FOREIGN KEY (burning_question_uid) REFERENCES tl_lascrt11_burning_question (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lascrt11_burning_question;

CREATE TABLE tl_lascrt11_burning_question (
  uid bigint NOT NULL AUTO_INCREMENT,
  access_date datetime DEFAULT NULL,
  scratchie_item_uid bigint DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  question mediumtext,
  general_question tinyint(1) DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY FK_NEW_610529188_693580A438BF8DF2 (scratchie_item_uid),
  KEY sessionIdIndex2 (session_id),
  CONSTRAINT FK_NEW_610529188_693580A438BF8DF2 FOREIGN KEY (scratchie_item_uid) REFERENCES tl_lascrt11_scratchie_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT sessionIdIndex2 FOREIGN KEY (session_id) REFERENCES tl_lascrt11_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lascrt11_configuration;

CREATE TABLE tl_lascrt11_configuration (
  uid bigint NOT NULL AUTO_INCREMENT,
  config_key varchar(30) DEFAULT NULL,
  config_value varchar(255) DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

DROP TABLE IF EXISTS tl_lascrt11_scratchie;

CREATE TABLE tl_lascrt11_scratchie (
  uid bigint NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  create_by bigint DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  instructions mediumtext,
  define_later tinyint(1) DEFAULT NULL,
  content_id bigint DEFAULT NULL,
  reflect_instructions mediumtext,
  reflect_on_activity tinyint(1) DEFAULT NULL,
  submission_deadline datetime DEFAULT NULL,
  burning_questions_enabled tinyint(1) DEFAULT '1',
  discussion_sentiment_enabled tinyint DEFAULT '0',
  question_etherpad_enabled tinyint(1) DEFAULT '0',
  relative_time_limit smallint unsigned NOT NULL DEFAULT '0',
  absolute_time_limit datetime DEFAULT NULL,
  double_click tinyint DEFAULT '0',
  shuffle_items tinyint(1) DEFAULT '0',
  confidence_levels_activity_uiid int DEFAULT NULL,
  confidence_levels_anonymous tinyint DEFAULT '0',
  preset_marks varchar(255) DEFAULT NULL,
  show_scratchies_in_results tinyint(1) DEFAULT '1',
  activity_uuid_providing_vsa_answers int DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY content_id (content_id)
);

DROP TABLE IF EXISTS tl_lascrt11_scratchie_item;

CREATE TABLE tl_lascrt11_scratchie_item (
  uid bigint NOT NULL AUTO_INCREMENT,
  scratchie_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY FK_NEW_610529188_F52D1F9330E79035 (scratchie_uid),
  CONSTRAINT FK_NEW_610529188_F52D1F9330E79035 FOREIGN KEY (scratchie_uid) REFERENCES tl_lascrt11_scratchie (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lascrt11_session;


CREATE TABLE tl_lascrt11_session (
  uid bigint NOT NULL AUTO_INCREMENT,
  session_end_date datetime DEFAULT NULL,
  session_start_date datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  scratchie_uid bigint DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  session_name varchar(250) DEFAULT NULL,
  group_leader_uid bigint DEFAULT NULL,
  mark int DEFAULT '0',
  scratching_finished tinyint(1) DEFAULT '0',
  time_limit_launched_date datetime DEFAULT NULL,
  time_limit_adjustment smallint DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY session_id_UNIQUE (session_id),
  KEY FK_NEW_610529188_24AA78C530E79035 (scratchie_uid),
  KEY FK_lalead11_session1 (group_leader_uid),
  CONSTRAINT FK_lalead11_session1 FOREIGN KEY (group_leader_uid) REFERENCES tl_lascrt11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT FK_NEW_610529188_24AA78C530E79035 FOREIGN KEY (scratchie_uid) REFERENCES tl_lascrt11_scratchie (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lascrt11_user;

CREATE TABLE tl_lascrt11_user (
  uid bigint NOT NULL AUTO_INCREMENT,
  user_id bigint DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  login_name varchar(255) DEFAULT NULL,
  session_finished tinyint(1) DEFAULT NULL,
  scratching_finished tinyint(1) DEFAULT NULL,
  session_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY user_id (user_id,session_uid),
  KEY userIdIndex (user_id),
  KEY FK_NEW_610529188_30113BFCEC0D3147 (session_uid),
  CONSTRAINT FK_NEW_610529188_30113BFCEC0D3147 FOREIGN KEY (session_uid) REFERENCES tl_lascrt11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);
   		
INSERT INTO tl_lascrt11_scratchie (uid,title,
 instructions, define_later, content_id, reflect_on_activity) VALUES
  (1,'Scratchie','Scenario explanation ',0,${default_content_id},0);
 
INSERT INTO lams_qb_tool_question (qb_question_uid, tool_content_id, answer_required, display_order) VALUES (2, ${default_content_id},0,1);

INSERT INTO tl_lascrt11_scratchie_item VALUES (LAST_INSERT_ID(), 1);
  
INSERT INTO tl_lascrt11_configuration (config_key, config_value) VALUES
  ('isEnabledExtraPointOption',	'true');
INSERT INTO tl_lascrt11_configuration (config_key, config_value) VALUES
  ('presetMarks', '4,2,1,0');
INSERT INTO tl_lascrt11_configuration (config_key, config_value) VALUES
  ('hideTitles','false');

SET FOREIGN_KEY_CHECKS=1;