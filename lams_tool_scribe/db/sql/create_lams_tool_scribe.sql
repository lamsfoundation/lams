SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lascrb11_scribe (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  auto_select_scribe bit(1),
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  aggregated_reports bit(1) DEFAULT b'0',
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lascrb11_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  scribe_uid bigint(20),
  appointed_scribe_uid bigint(20),
  force_complete bit(1),
  report_submitted bit(1),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FK89732793B3FA1495 FOREIGN KEY (scribe_uid)
  		REFERENCES tl_lascrb11_scribe (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lascrb11_heading (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  heading mediumtext,
  scribe_uid bigint(20),
  display_order int(11),
  PRIMARY KEY (uid),
  CONSTRAINT FK428A22FFB3FA1495 FOREIGN KEY (scribe_uid)
  		REFERENCES tl_lascrb11_scribe (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lascrb11_report_entry (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  entry_text mediumtext,
  scribe_heading_uid bigint(20),
  scribe_session_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK5439FACA1C266FAE FOREIGN KEY (scribe_session_uid)
  		REFERENCES tl_lascrb11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK5439FACAEA50D086 FOREIGN KEY (scribe_heading_uid)
  		REFERENCES tl_lascrb11_heading (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lascrb11_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  scribe_session_uid bigint(20),
  report_approved bit(1),
  started_activity bit(1),
  PRIMARY KEY (uid),
  CONSTRAINT FK187DAFEE1C266FAE FOREIGN KEY (scribe_session_uid)
  		REFERENCES tl_lascrb11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_lascrb11_session ADD CONSTRAINT FK89732793E46919FF FOREIGN KEY (appointed_scribe_uid)
  		REFERENCES tl_lascrb11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;

  		
INSERT INTO tl_lascrb11_scribe (
	uid,
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity,
	auto_select_scribe,
	aggregated_reports) 
VALUES(
	1,
	"Scribe",
	"Instructions",
	${default_content_id},
	1,
	0,
	0,
	0,
	1,
	0
);

INSERT INTO tl_lascrb11_heading(
	heading,
	scribe_uid,
	display_order)
VALUES(
	"Scribe Heading",
	1,
	0
);

SET FOREIGN_KEY_CHECKS=1;