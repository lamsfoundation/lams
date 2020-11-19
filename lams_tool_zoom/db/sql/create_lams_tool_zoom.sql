SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lazoom10_zoom (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  tool_content_id bigint(20),
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  start_in_monitor bit(1) DEFAULT 1,
  duration smallint,
  content_in_use bit(1),
  define_later bit(1),
  api_id bigint(20),
  meeting_id varchar(64),
  meeting_start_url varchar(1000),
  meeting_password CHAR(6),
  PRIMARY KEY (uid),
  CONSTRAINT fk_lazoom10_zoom_to_api FOREIGN KEY (api_id)
  	REFERENCES tl_lazoom10_api (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE tl_lazoom10_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_id bigint(20),
  session_name varchar(250),
  session_start_date datetime,
  session_end_date datetime,
  status int(11),
  zoom_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT fk_lazoom10_zoom_session_to_zoom FOREIGN KEY (zoom_uid)
  	REFERENCES tl_lazoom10_zoom (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lazoom10_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  email varchar(255),
  first_name varchar(255),
  last_name varchar(255),
  finishedActivity bit(1),
  zoom_session_uid bigint(20),
  notebook_entry_uid bigint(20),
  meeting_join_url varchar(255),
  PRIMARY KEY (uid),
  CONSTRAINT fk_lazoom10_zoom_user_to_zoom_session
  	FOREIGN KEY (zoom_session_uid) REFERENCES tl_lazoom10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lazoom10_api (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(64),
  api_key varchar(64),
  api_secret varchar(64),
  PRIMARY KEY (uid)
);

INSERT INTO tl_lazoom10_zoom (
	title,
	instructions,
	tool_content_id,
	start_in_monitor,
	duration,
	content_in_use,
	define_later,
	reflect_on_activity
	)
VALUES(
	"Zoom",
	"Instructions",
	${default_content_id},
	1,
	120,
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;