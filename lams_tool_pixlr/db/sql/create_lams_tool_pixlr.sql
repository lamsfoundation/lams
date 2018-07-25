SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lapixl10_pixlr (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  reflect_on_activity bit(1),
  allow_view_others_images bit(1),
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  image_file_name mediumtext,
  image_width bigint(20),
  image_height bigint(20),
  reflect_instructions mediumtext,
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lapixl10_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  pixlr_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FKE5C05E7FCB8ADA74 FOREIGN KEY (pixlr_uid)
  		REFERENCES tl_lapixl10_pixlr (uid) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tl_lapixl10_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  pixlr_session_uid bigint(20),
  entry_uid bigint(20),
  image_file_name mediumtext,
  imageHeight bigint(20),
  imageWidth bigint(20),
  imageHidden bit(1),
  PRIMARY KEY (uid),
  CONSTRAINT FK9A39C08236E23005 FOREIGN KEY (pixlr_session_uid)
  		REFERENCES tl_lapixl10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lapixl10_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key varchar(30),
  config_value mediumtext,
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

-- Inserting the required config item into the config table
-- Currently supported languages for pixlr
INSERT INTO tl_lapixl10_configuration (
	config_key, 
	config_value
)
VALUES(
	"LanguageCSV",
	"en,es,fr,de,it,ru,ro,zh-cn,pt-br,sv,pl,th"
);


INSERT INTO tl_lapixl10_pixlr (
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity,
	allow_view_others_images,
	image_width, 
	image_height,
	image_file_name
) 
VALUES(
	"Pixlr",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	0,
	1,
	400,
	450,
	"blank.jpg"
);