SET FOREIGN_KEY_CHECKS=0;

create table tl_lascrt11_scratchie (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   instructions MEDIUMTEXT,
   define_later TINYINT(1),
   content_id bigint,
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity TINYINT(1),
   extra_point TINYINT(1),
   submission_deadline datetime,
   burning_questions_enabled TINYINT(1) DEFAULT 1,
   question_etherpad_enabled TINYINT(1) DEFAULT 0,
   time_limit int(11) DEFAULT 0,
   shuffle_items TINYINT(1) DEFAULT 0,
   confidence_levels_activity_uiid INT(11),
   preset_marks varchar(255),
   show_scratchies_in_results TINYINT(1) DEFAULT 1,
   activity_uuid_providing_vsa_answers INT(11),
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_lascrt11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status TINYINT(1),
   scratchie_uid bigint,
   session_id bigint,
   session_name varchar(250),
   group_leader_uid bigint(20),
   mark int(11) DEFAULT 0,
   scratching_finished TINYINT(1) DEFAULT 0,
   time_limit_launched_date datetime,
   PRIMARY KEY (uid),
   UNIQUE KEY session_id_UNIQUE (session_id),
   CONSTRAINT FK_NEW_610529188_24AA78C530E79035 FOREIGN KEY (scratchie_uid)
   		REFERENCES tl_lascrt11_scratchie (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lascrt11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished TINYINT(1),
   scratching_finished TINYINT(1),
   session_uid bigint,
   primary key (uid),
   UNIQUE KEY user_id (user_id,session_uid),
   KEY userIdIndex (user_id),
   CONSTRAINT FK_NEW_610529188_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_lascrt11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);
  
create table tl_lascrt11_scratchie_item (
   uid bigint not null auto_increment,
   scratchie_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_610529188_F52D1F9330E79035 FOREIGN KEY (scratchie_uid)
   		REFERENCES tl_lascrt11_scratchie (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lascrt11_answer_log (
   uid bigint not null auto_increment,
   access_date datetime,
   session_id bigint,
   primary key (uid),
   CONSTRAINT sessionIdIndex FOREIGN KEY (session_id)
   		REFERENCES tl_lascrt11_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE
);

 CREATE TABLE tl_lascrt11_burning_question (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  access_date datetime,
  scratchie_item_uid bigint(20),
  session_id bigint(20),
  question mediumtext,
  general_question tinyint(1),
  PRIMARY KEY (uid),
  CONSTRAINT FK_NEW_610529188_693580A438BF8DF2 FOREIGN KEY (scratchie_item_uid)
  		REFERENCES tl_lascrt11_scratchie_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT sessionIdIndex2 FOREIGN KEY (session_id) 
  		REFERENCES tl_lascrt11_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lascrt11_burning_que_like (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  burning_question_uid bigint(20),
  session_id bigint(20),
  PRIMARY KEY (uid),
  KEY FK_burning_que_uid (burning_question_uid),
  CONSTRAINT FK_burning_que_uid FOREIGN KEY (burning_question_uid)
  		REFERENCES tl_lascrt11_burning_question (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lascrt11_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key varchar(30),
  config_value varchar(255),
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);


ALTER TABLE tl_lascrt11_session ADD CONSTRAINT FK_lalead11_session1 FOREIGN KEY (group_leader_uid)
   		REFERENCES tl_lascrt11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;

-- data for content table
INSERT INTO tl_lascrt11_scratchie (uid,title, instructions, define_later, content_id, reflect_on_activity, extra_point) VALUES
  (1,'Scratchie','Scenario explanation ',0,${default_content_id},0, 0);
  
-- data for QB question table
SET @max_question_id = COALESCE((SELECT MAX(question_id) FROM lams_qb_question),0) + 1;
INSERT INTO `lams_qb_question` (`type`,`question_id`,`version`,`create_date`,`name`,`description`,`max_mark`,`content_folder_id`)
	VALUES (1,@max_question_id,1,NOW(),'Question Title','Question Description',1,'01234567-89ab-cdef-0123-4567890abcde');
INSERT INTO lams_sequence_generator(lams_qb_question_question_id) VALUES (@max_question_id);
SET @qb_question_uid = (SELECT MAX(uid) FROM lams_qb_question);
INSERT INTO `lams_qb_option` (`qb_question_uid`,`display_order`,`name`,`max_mark`) VALUES (@qb_question_uid,1,'Question Answer A',1);
INSERT INTO `lams_qb_option` (`qb_question_uid`,`display_order`,`name`,`max_mark`) VALUES (@qb_question_uid,2,'Question Answer B',0);
INSERT INTO `lams_qb_option` (`qb_question_uid`,`display_order`,`name`,`max_mark`) VALUES (@qb_question_uid,3,'Question Answer C',0);
INSERT INTO `lams_qb_option` (`qb_question_uid`,`display_order`,`name`,`max_mark`) VALUES (@qb_question_uid,4,'Question Answer D',0);
INSERT INTO lams_qb_collection_question	SELECT 1, @max_question_id;

-- data for tool question tables
INSERT INTO lams_qb_tool_question (qb_question_uid,tool_content_id,display_order) VALUES (@qb_question_uid,${default_content_id},1);
INSERT INTO tl_lascrt11_scratchie_item (uid,scratchie_uid) VALUES ((SELECT MAX(tool_question_uid) FROM lams_qb_tool_question),1);

  

INSERT INTO tl_lascrt11_configuration (config_key, config_value) VALUES
  ('isEnabledExtraPointOption',	'true');
INSERT INTO tl_lascrt11_configuration (config_key, config_value) VALUES
  ('presetMarks', '4,2,1,0');
INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES 
  ('hideTitles', 'false');
  
SET FOREIGN_KEY_CHECKS=1;