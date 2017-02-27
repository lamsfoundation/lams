CREATE TABLE tl_laqa11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , qa_content_id BIGINT(20) NOT NULL
     , title TEXT
     , instructions TEXT
     , creation_date DATETIME
     , update_date DATETIME
     , submission_deadline DATETIME
     , reflect TINYINT(1) NOT NULL DEFAULT 0
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , allow_rate_answers TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , reflectionSubject TEXT
     , lockWhenFinished TINYINT(1) NOT NULL DEFAULT 1
     , showOtherAnswers TINYINT(1) NOT NULL DEFAULT 1
     , allow_rich_editor TINYINT(1) NOT NULL DEFAULT 0
     , use_select_leader_tool_ouput tinyint(1) NOT NULL DEFAULT 0
     , notify_response_submit tinyint(1) NOT NULL DEFAULT 0
     , minimum_rates int(11) DEFAULT 0
     , maximum_rates int(11) DEFAULT 0
     , no_reedit_allowed tinyint(1) NOT NULL DEFAULT 0
     , show_other_answers_after_deadline tinyint(1) NOT NULL DEFAULT 0
     ,  PRIMARY KEY (uid)
     ,  KEY tl_laqa11_content_qa_content_id (qa_content_id)
);

CREATE TABLE tl_laqa11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , qa_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_name VARCHAR(100)
     , session_status VARCHAR(100)
     , qa_content_id BIGINT(20) NOT NULL
     , qa_group_leader_uid bigint(20)
     , PRIMARY KEY (uid)
     , UNIQUE KEY qa_session_id (qa_session_id)
     , KEY qa_content_id (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (qa_content_id)
     		REFERENCES tl_laqa11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_laqa11_que_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , username VARCHAR(255)
     , responseFinalized TINYINT(1) NOT NULL DEFAULT 0
     , qa_session_id BIGINT(20) NOT NULL
     , fullname VARCHAR(255)
     , learnerFinished TINYINT(1) NOT NULL
     , PRIMARY KEY (uid)
     , UNIQUE KEY que_usr_id (que_usr_id,qa_session_id)
     , INDEX (qa_session_id)
     , CONSTRAINT FK_tl_laqa11_que_usr_1 FOREIGN KEY (qa_session_id)
                  REFERENCES tl_laqa11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_laqa11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question TEXT
     , feedback TEXT
     , display_order INT(5) DEFAULT 1
     , qa_content_id BIGINT(20)
     , answer_required TINYINT(1) NOT NULL DEFAULT 0
     , min_words_limit int(11) DEFAULT 0
     , PRIMARY KEY (uid)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (qa_content_id)
                  REFERENCES tl_laqa11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_laqa11_usr_resp (
       response_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , answer TEXT
     , time_zone VARCHAR(255)
     , attempt_time DATETIME
     , que_usr_id BIGINT(20) NOT NULL
     , qa_que_content_id BIGINT(20)
     , visible TINYINT(1) NOT NULL DEFAULT 1
     , answer_autosaved text
     , PRIMARY KEY (response_id)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_3 FOREIGN KEY (que_usr_id)
                  REFERENCES tl_laqa11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (qa_que_content_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_2 FOREIGN KEY (qa_que_content_id)
                  REFERENCES tl_laqa11_que_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_laqa11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT QaConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT QaConditionToQaContent FOREIGN KEY (content_uid)
                  REFERENCES tl_laqa11_content(uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_laqa11_condition_questions (
 	   condition_id BIGINT(20)
 	 , question_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,question_uid)
	 , CONSTRAINT QaConditionQuestionToQaCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_laqa11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT QaConditionQuestionToQaQuestion FOREIGN KEY (question_uid)
                  REFERENCES tl_laqa11_que_content(uid) ON DELETE CASCADE ON UPDATE CASCADE	
);

CREATE TABLE tl_laqa11_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key varchar(30),
  config_value varchar(255),
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

CREATE TABLE tl_laqa11_wizard_category (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  PRIMARY KEY (uid)
);

CREATE TABLE tl_laqa11_wizard_cognitive_skill (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  category_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK3BA4132BCBB0DC8D FOREIGN KEY (category_uid)
  		REFERENCES tl_laqa11_wizard_category (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_laqa11_wizard_question (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  cognitive_skill_uid bigint(20),
  title mediumtext NOT NULL,
  PRIMARY KEY (uid),
  CONSTRAINT FKAF08A0C7EFF77FD4 FOREIGN KEY (cognitive_skill_uid)
  		REFERENCES tl_laqa11_wizard_cognitive_skill (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_laqa11_session ADD CONSTRAINT FK_laqa11_session1 FOREIGN KEY (qa_group_leader_uid)
		REFERENCES tl_laqa11_que_usr (uid) ON DELETE SET NULL ON UPDATE CASCADE;


-- data for content table
INSERT INTO tl_laqa11_content (qa_content_id, title, instructions, creation_date, lockWhenFinished)  VALUES (${default_content_id}, 'Q&A', 'Instructions', NOW() , 0);

-- data for content questions table
INSERT INTO tl_laqa11_que_content (question, display_order, qa_content_id) VALUES ('Sample Question 1?',1,1);

-- Inserting the required config item into the config table
INSERT INTO tl_laqa11_configuration (
	config_key, 
	config_value
)
VALUES(
	"enableQaWizard",
	"false"
);

-- Default content for the qa wizard

-- Wizard categories
INSERT INTO tl_laqa11_wizard_category VALUES (1,'Data%20Collection'),(2,'Data%20Organization'),(3,'Data%20Analysis'),(4,'Data%20Transcendence');

-- Wizard cognitive skills
INSERT INTO tl_laqa11_wizard_cognitive_skill VALUES (1,'2.%20Recognition',1),(2,'1.%20Observation',1),(3,'3.%20Recall',1),(4,'Ordering',2),(5,'Comparison',2),(6,'Hierarchy',2),(7,'Classification',2),(8,'Distinction%20between%20facts%20%26%20opinions/judgments',3),(9,'Analysis%20of%20basic%20parts',3),(10,'Clarification',3),(11,'Flush%20out%20relationships',3),(12,'Pattern%20recognition',3),(13,'Error%20and%20conflict%20detection',4),(14,'Summary',4),(15,'Reflection',4),(16,'Hypothesis',4),(17,'Prediction',4),(18,'Explanation',4),(19,'Empathy',4),(20,'Knowledge%20Organization',4),(21,'Application%20and%20Improvement',4),(22,'Assessment/Evaluation',4),(23,'Verification',4),(24,'Conclusion',4);

-- Wizard questions
INSERT INTO tl_laqa11_wizard_question VALUES (1,1,'Please%20identify%u2026'),(2,2,'What%20do%20you%20observe%20...'),(3,2,'What%20do%20you%20see%u2026%3F'),(4,3,'Give%20the%20definition%20of%u2026'),(5,3,'What%20is%20the%20meaning%20of%u2026%3F'),(6,3,'What%20has%20been%20said%20by%u2026%20about%u2026%3F'),(7,4,'Find%20a%20criterion%20to%20order%20these%u2026'),(8,4,'Order%20these%u2026'),(9,4,'Find%20the%205%20biggest/smallest%20%u2026'),(10,5,'Compare%20these%u2026%20using%20the%20following%20criteria%u2026'),(11,5,'Find%20the%20pros%20and%20cons%20of%u2026'),(12,5,'Find%20both%20the%20similarities%20and%20the%20differences%20between..%20and%u2026'),(13,6,'Arrange%20these%u2026by%20the%20following%20criterion%u2026'),(14,6,'Sort%20these%u2026%20by%20the%20following%20criterion%u2026'),(15,6,'Put%20these%u2026in%20an%20ascending/descending%20sequence%u2026'),(16,7,'Group%20these%20elements%20using%20the%20following%20criteria%u2026'),(17,7,'Find%20the%20pros%20and%20cons%20of%20these%u2026solutions'),(18,7,'Find%205%20%20advantages%20and%203%20disadvantages%20%20of%20%u2026'),(19,8,'Clarify%20whether%20this%20%u2026describes%20a%20fact%20or%20if%20it%20is%20a%20personal%20opinion'),(20,8,'Provide%20more%20than%20one%20view%20about%20the%20following%u2026'),(21,8,'Provide%20logical%20arguments%20to%20support%20the%20following%20statement'),(22,9,'Identify%20the%20parts/units/characteristics%20of%u2026'),(23,9,'Define%20the%20data%20given%20and%20the%20objectives%20of%u2026.'),(24,9,'Refer%20to%20the%20appropriate%20phases%20of%20the%20solution%20plan%20for%u2026'),(25,10,'What%20do%20we%20mean%20by%u2026%3F'),(26,10,'Provide%20an%20example%20to%20clarify%u2026'),(27,10,'When%20does%20this%u2026have%20a%20meaning%u2026%3F'),(28,11,'Distinguish%20possible%20relationships%20among%20the%20data%u2026'),(29,11,'Is%20there%20any%20reason%20that%20affects%20this%20phenomenon%u2026%3F'),(30,11,'Is%20there%20any%20structure%20within%u2026%3F'),(31,12,'Is%20there%20any%20pattern%20that%20is%20repeated%20in%u2026%3F'),(32,12,'Find%20out%20what%20is%20common%20in%u2026'),(33,12,'Find%20out%20what%20is%20similar%20in%u2026'),(34,13,'Point%20out%20the%20mistakes%20of/in%u2026'),(35,13,'Are%20there%20any%20contradictions%20in%u2026%3F'),(36,13,'What%20is%20missing%20in%u2026%3F'),(37,14,'Form%20an%20abstract%20of%20%u2026%28number%29%20words%20to%20describe%u2026'),(38,14,'Which%20are%20the%20main%20points%20of%20%u2026'),(39,14,'Which%20are%20the%20top%205%20essential%20points%20of%20%u2026'),(40,15,'What%20have%20you%20learnt%20about%u2026%3F'),(41,15,'Are%20there%20any%20points%20that%20you%u2026%3F'),(42,16,'If%20%u2026%20then%u2026'),(43,17,'Could%20you%20make%20any%20predictions%20about%u2026%3F'),(44,17,'If%20we%20do%u2026then%20what%20will%20happen%3F'),(45,17,'In%20the%20case%20of%u2026find%20out%u2026'),(46,18,'Please%20explain%20how%20the%20meaning%20of%u2026'),(47,18,'Please%20explain%20why%20the%20meaning%20of/%u2026'),(48,18,'Please%20explain%20what%20the%20meaning%20of%20%u2026'),(49,18,'Please%20explain%20in%20your%20own%20words%u2026'),(50,18,'Please%20explain%20in%20your%20own%20words%u2026'),(51,19,'Could%20you%20accept%20the%20role%20of%u2026%3F'),(52,19,'What%20are%20the%20possible%20arguments%20of%20others%20for%u2026%3F'),(53,19,'What%20would%20be%20your%20answer%20if%20you%20were%20in%20the%20position%20of%u2026%3F'),(54,20,'Make%20a%20hierarchical%20tree%20to%20describe%u2026'),(55,20,'Can%20you%20form%20a%20sequential%20structure%20of%u2026%3F'),(56,20,'Characterize%20it%20according%20to%20the%20following%20criteria%u2026'),(57,21,'How%20can%20you%20improve%20this%u2026in%20order%20to%u2026%3F'),(58,21,'How%20can%20you%20apply%20this%u2026in%20order%20to%u2026%3F'),(59,22,'Assess%20this%u2026'),(60,22,'Evaluate%20the%u2026'),(61,22,'What%20kind%20of%20criteria%20can%20you%20use%20to%20evaluate%20the%u2026%3F'),(62,23,'Please%20verify%20that%u2026'),(63,23,'Please%20confirm%20that%u2026'),(64,24,'Based%20on%20this%u2026what%20do%20you%20conclude%3F'),(65,24,'Based%20on%20these%u2026what%20do%20you%20conclude%3F'),(66,24,'Are%20there%20any%20exceptions%20to%u2026%3F'),(67,24,'Are%20there%20any%20weak%20points%20in%u2026%3F');

SET FOREIGN_KEY_CHECKS=1;