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


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
