SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lantbk11_notebook (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  allow_rich_editor bit(1),
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  submission_deadline datetime,
  force_response bit(1) DEFAULT b'0',
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lantbk11_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  notebook_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FKB7C198E2FC940906 FOREIGN KEY (notebook_uid)
  		REFERENCES tl_lantbk11_notebook (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lantbk11_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  notebook_session_uid bigint(20),
  entry_uid bigint(20),
  teachers_comment mediumtext,
  PRIMARY KEY (uid),
  CONSTRAINT FKCB8A58FFA3B0FADF FOREIGN KEY (notebook_session_uid)
  		REFERENCES tl_lantbk11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lantbk11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT NotebookConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT NotebookConditionToNotebook FOREIGN KEY (content_uid)
                  REFERENCES tl_lantbk11_notebook(uid) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO tl_lantbk11_notebook (
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_rich_editor) 
VALUES(
	"Notebook",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;