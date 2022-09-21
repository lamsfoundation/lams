SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lamind10_mindmap (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  submission_deadline datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  multiuser_mode bit(1),
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  export_content mediumtext,
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  gallery_walk_enabled TINYINT NOT NULL DEFAULT 0,
  gallery_walk_read_only TINYINT NOT NULL DEFAULT 0,
  gallery_walk_started TINYINT NOT NULL DEFAULT 0,
  gallery_walk_finished TINYINT NOT NULL DEFAULT 0,
  gallery_walk_edit_enabled TINYINT NOT NULL DEFAULT 0,
  gallery_walk_instructions TEXT,
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lamind10_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  mindmap_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FK_NEW_972920762_B7C198E2FC940906 FOREIGN KEY (mindmap_uid)
  		REFERENCES tl_lamind10_mindmap (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lamind10_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  mindmap_session_uid bigint(20),
  entry_uid bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK_NEW_972920762_CB8A58FFA3B0FADF FOREIGN KEY (mindmap_session_uid)
  		REFERENCES tl_lamind10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lamind10_node (
  node_id bigint(20) NOT NULL AUTO_INCREMENT,
  unique_id bigint(20),
  parent_id bigint(20),
  node_text varchar(100),
  node_color varchar(7),
  session_id bigint(20),
  user_id bigint(20),
  mindmap_id bigint(20),
  PRIMARY KEY (node_id),
  CONSTRAINT MindmapNode_Mindmap_FK FOREIGN KEY (mindmap_id)
  		REFERENCES tl_lamind10_mindmap (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT MindmapNode_User_FK FOREIGN KEY (user_id)
  		REFERENCES tl_lamind10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE tl_lamind10_request (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  unique_id bigint(20),
  global_id bigint(20),
  request_type tinyint(4),
  node_id bigint(20),
  node_child_id bigint(20),
  user_id bigint(20),
  mindmap_id bigint(20),
  PRIMARY KEY (uid),
  KEY mindmap_id (mindmap_id),
  KEY user_id (user_id),
  CONSTRAINT tl_lamind10_request_fk FOREIGN KEY (mindmap_id)
  		REFERENCES tl_lamind10_mindmap (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT tl_lamind10_request_fk1 FOREIGN KEY (user_id)
  		REFERENCES tl_lamind10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_lamind10_node ADD CONSTRAINT MindmapNode_Parent_FK FOREIGN KEY (parent_id)
  		REFERENCES tl_lamind10_node (node_id) ON DELETE CASCADE ON UPDATE CASCADE;
  		

INSERT INTO tl_lamind10_mindmap (
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	multiuser_mode,
	reflect_on_activity) 
VALUES(
	"Mindmap",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;