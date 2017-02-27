SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_labbb10_bbb (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  PRIMARY KEY (uid)
);

CREATE TABLE tl_labbb10_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  bbb_uid bigint(20),
  meeting_created bit(1),
  meeting_key varchar(255),
  attendee_password varchar(20),
  moderator_password varchar(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT fk_labbb10_bbb_session_to_bbb FOREIGN KEY (bbb_uid)
  	REFERENCES tl_labbb10_bbb (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_labbb10_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  bbb_session_uid bigint(20),
  notebook_entry_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT fk_labbb10_bbb_user_to_bbb_session
  	FOREIGN KEY (bbb_session_uid) REFERENCES tl_labbb10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_labbb10_config (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key varchar(255),
  config_value varchar(255),
  PRIMARY KEY (uid)
);

INSERT INTO tl_labbb10_bbb (
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity
	)
VALUES(
	"Web Conference",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	0
);

-- default configuration values
INSERT INTO tl_labbb10_config (config_key, config_value) VALUES ("server_url", "http://172.20.100.253/");
INSERT INTO tl_labbb10_config (config_key, config_value) VALUES ("security_salt", "12309usadoiaksdjblquge12312");


SET FOREIGN_KEY_CHECKS=1;