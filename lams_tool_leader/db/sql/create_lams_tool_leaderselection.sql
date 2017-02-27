SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lalead11_leaderselection (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lalead11_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  leaderselection_uid bigint(20),
  group_leader_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FK_NEW_1316293302_B7C198E2FC940906 FOREIGN KEY (leaderselection_uid)
  		REFERENCES tl_lalead11_leaderselection (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lalead11_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  leaderselection_session_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK_NEW_1316293302_CB8A58FFA3B0FADF FOREIGN KEY (leaderselection_session_uid)
  		REFERENCES tl_lalead11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_lalead11_session ADD CONSTRAINT FK_lalead11_1 FOREIGN KEY (group_leader_uid)
  		REFERENCES tl_lalead11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
  		

INSERT INTO tl_lalead11_leaderselection (
	title,
	instructions,
	tool_content_id,
	content_in_use,
	define_later) 
VALUES(
	"Leader Selection",
	"Instructions",
	${default_content_id},
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;