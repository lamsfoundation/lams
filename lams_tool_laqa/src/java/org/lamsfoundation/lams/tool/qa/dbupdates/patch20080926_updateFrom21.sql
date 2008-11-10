-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------


-- SQL statements to update from LAMS 2.1/2.1.1
CREATE TABLE tl_laqa11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT QaConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT QaConditionToQaContent FOREIGN KEY (content_uid)
                  REFERENCES tl_laqa11_content(uid) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_condition_questions (
 	   condition_id BIGINT(20)
 	 , question_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,question_uid)
	 , CONSTRAINT QaConditionQuestionToQaCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_laqa11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT QaConditionQuestionToQaQuestion FOREIGN KEY (question_uid)
                  REFERENCES tl_laqa11_que_content(uid) ON DELETE CASCADE ON UPDATE CASCADE	
)TYPE=InnoDB;

UPDATE lams_tool SET supports_outputs=1 WHERE tool_signature='laqa11';

-- Condition copies created for branch activity entries won't have links to any existing Q&A contents.
ALTER TABLE tl_laqa11_que_content MODIFY COLUMN qa_content_id BIGINT(20);

SET FOREIGN_KEY_CHECKS=0;

-- insert scripts for the wizard tables
create table tl_laqa11_configuration (uid bigint not null auto_increment, config_key varchar(30) unique, config_value varchar(255), primary key (uid))TYPE=InnoDB;
create table tl_laqa11_wizard_category (uid bigint not null auto_increment, title varchar(255) not null, primary key (uid))TYPE=InnoDB;
create table tl_laqa11_wizard_cognitive_skill (uid bigint not null auto_increment, title varchar(255) not null, category_uid bigint, primary key (uid))TYPE=InnoDB;
create table tl_laqa11_wizard_question (uid bigint not null auto_increment, cognitive_skill_uid bigint, title text not null, primary key (uid))TYPE=InnoDB;
alter table tl_laqa11_wizard_cognitive_skill add index FK3BA4132BCBB0DC8D (category_uid), add constraint FK3BA4132BCBB0DC8D foreign key (category_uid) references tl_laqa11_wizard_category (uid);
alter table tl_laqa11_wizard_question add index FKAF08A0C7EFF77FD4 (cognitive_skill_uid), add constraint FKAF08A0C7EFF77FD4 foreign key (cognitive_skill_uid) references tl_laqa11_wizard_cognitive_skill (uid);

-- Inserting the required config item into the config table
INSERT INTO tl_laqa11_configuration (
	config_key, 
	config_value
)
VALUES(
	"enableQaWizard",
	"false"
);
SET FOREIGN_KEY_CHECKS=1;

-- updating q&a to have an admin page for the qa wizard
UPDATE lams_tool SET admin_url='tool/laqa11/laqa11admin.do' WHERE tool_signature='laqa11';


-- Dumps for qaWizard default content

-- Wizard categories
INSERT INTO `tl_laqa11_wizard_category` VALUES (1,'Data%20Collection'),(2,'Data%20organization'),(3,'Data%20analysis');

-- Wizard cognitive skills
INSERT INTO `tl_laqa11_wizard_cognitive_skill` VALUES (1,'Recognition',1),(2,'Observation',1),(3,'Recall',1),(4,'Ordering',2),(5,'Classification',2),(6,'Hierarchy',2),(7,'Comparison',2),(8,'Clarification',3),(9,'Pattern%20recognition',3),(10,'Distinction%20between%20facts%20%26%20opinions/judgments',3),(11,'Analysis%20of%20basic%20parts',3),(12,'Flush%20out%20relationships',3);

-- Wizard questions
INSERT INTO `tl_laqa11_wizard_question` VALUES (1,1,'Please%20identify%u2026'),(2,2,'What%20do%20you%20observe%20...'),(3,2,'What%20do%20you%20see%u2026%3F'),(4,3,'Give%20the%20definition%20of%u2026'),(5,3,'What%20is%20the%20meaning%20of%u2026%3F'),(6,3,'What%20has%20been%20said%20by%u2026%20about%u2026%3F'),(7,4,'Find%20a%20criterion%20to%20order%20these%u2026'),(8,4,'Order%20these%u2026'),(9,4,'Find%20the%205%20biggest/smallest%20%u2026'),(10,5,'Group%20these%20elements%20using%20the%20following%20criteria%u2026'),(11,5,'Find%20the%20pros%20and%20cons%20of%20these%u2026solutions'),(12,5,'Find%205%20%20advantages%20and%203%20disadvantages%20%20of%20%u2026'),(13,6,'Arrange%20these%u2026by%20the%20following%20criterion%u2026'),(14,6,'Sort%20these%u2026%20by%20the%20following%20criterion%u2026'),(15,6,'Put%20these%u2026in%20an%20ascending/descending%20sequence%u2026'),(16,7,'Compare%20these%u2026%20using%20the%20following%20criteria%u2026'),(17,7,'Find%20the%20pros%20and%20cons%20of%u2026'),(18,7,'Find%20both%20the%20similarities%20and%20the%20differences%20between..%20and%u2026'),(19,8,'What%20do%20we%20mean%20by%u2026%3F'),(20,8,'Provide%20an%20example%20to%20clarify%u2026'),(21,8,'When%20does%20this%u2026have%20a%20meaning%u2026%3F'),(22,9,'Is%20there%20any%20pattern%20that%20is%20repeated%20in%u2026%3F'),(23,9,'Find%20out%20what%20is%20common%20in%u2026'),(24,9,'Find%20out%20what%20is%20similar%20in%u2026'),(25,10,'Clarify%20whether%20this%20%u2026describes%20a%20fact%20or%20if%20it%20is%20a%20personal%20opinion'),(26,10,'Provide%20more%20than%20one%20view%20about%20the%20following%u2026'),(27,10,'Provide%20logical%20arguments%20to%20support%20the%20following%20statement'),(28,11,'Identify%20the%20parts/units/characteristics%20of%u2026'),(29,11,'Define%20the%20data%20given%20and%20the%20objectives%20of%u2026.'),(30,11,'Refer%20to%20the%20appropriate%20phases%20of%20the%20solution%20plan%20for%u2026'),(31,12,'Distinguish%20possible%20relationships%20among%20the%20data%u2026'),(32,12,'Is%20there%20any%20reason%20that%20affects%20this%20phenomenon%u2026%3F'),(33,12,'Is%20there%20any%20structure%20within%u2026%3F');

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
