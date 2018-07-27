SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lagmap10_gmap (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  allow_edit_markers bit(1),
  show_all_markers bit(1),
  limit_markers bit(1),
  max_markers int(11),
  allow_zoom bit(1),
  allow_terrain bit(1),
  allow_satellite bit(1),
  allow_hybrid bit(1),
  map_center_latitude double,
  map_center_longitude double,
  map_zoom int(11),
  map_type varchar(20),
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  default_geocoder_address varchar(255),
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lagmap10_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  gmap_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FK519D516774028C80 FOREIGN KEY (gmap_uid)
  		REFERENCES tl_lagmap10_gmap (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

 CREATE TABLE tl_lagmap10_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  gmap_session_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK7CB3F69AC5F056D9 FOREIGN KEY (gmap_session_uid)
  		REFERENCES tl_lagmap10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lagmap10_marker (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  longitude double,
  latitude double,
  info_window_message mediumtext,
  create_date datetime,
  update_date datetime,
  is_authored bit(1),
  gmap_uid bigint(20),
  title varchar(55),
  created_by bigint(20),
  updated_by bigint(20),
  gmap_session_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK10F22749529F7FD3 FOREIGN KEY (created_by)
  		REFERENCES tl_lagmap10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT FK10F2274974028C80 FOREIGN KEY (gmap_uid)
  		REFERENCES tl_lagmap10_gmap (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK10F22749C5F056D9 FOREIGN KEY (gmap_session_uid)
  		REFERENCES tl_lagmap10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK10F22749EF5F6920 FOREIGN KEY (updated_by)
  		REFERENCES tl_lagmap10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE tl_lagmap10_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key varchar(30),
  config_value varchar(255),
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

-- Inserting the required config item into the config table
-- Gmap API key is added here for shaun so the Gmap works there automatically for each build
INSERT INTO tl_lagmap10_configuration (
	config_key, 
	config_value
)
VALUES(
	"GmapKey",
	"ABQIAAAAvPAE96y1iioFQOnrP1RCBxQ3ZLSPwrKlL-Fn7FdXNTuNedFYMRT30phEMjEwUhQPccHtJ9JNU1mknQ"
);


INSERT INTO tl_lagmap10_gmap (
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_edit_markers, 
	show_all_markers,
	limit_markers, 
	max_markers, 
	allow_zoom, 
	allow_terrain, 
	allow_satellite, 
	allow_hybrid, 
	map_center_latitude,
	map_center_longitude, 
	map_zoom,
	map_type,
	reflect_on_activity, 
	reflect_instructions, 
	default_geocoder_address
) 
VALUES(
	"Gmap",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	1,
	1,
	0,
	1,
	1,
	0,
	1,
	1,
	-33.774322,
	151.111988,
	13,
	"G_NORMAL_MAP",
	0,
	"",
	"Macquarie University, Sydney NSW"
);

SET FOREIGN_KEY_CHECKS=1;