SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lakalt11_kaltura (
	uid BIGINT NOT NULL AUTO_INCREMENT, 
	create_date DATETIME, 
	update_date DATETIME, 
	create_by BIGINT, 
	title VARCHAR(255), 
	instructions TEXT, 
	lock_on_finished BIT, 
	allow_contribute_videos BIT,
	allow_seeing_other_users_recordings BIT, 
	learner_contribution_limit integer DEFAULT -1,
	allow_comments BIT, 
	allow_ratings BIT, 
	content_in_use BIT, 
	define_later BIT, 
	tool_content_id BIGINT, 
	submission_deadline DATETIME DEFAULT NULL, 
	reflect_on_activity BIT,
	reflect_instructions TEXT,
	PRIMARY KEY (uid)
);

CREATE TABLE tl_lakalt11_session (
	uid BIGINT NOT NULL AUTO_INCREMENT, 
	session_end_date DATETIME, 
	session_start_date DATETIME, 
	status INTEGER, 
	session_id BIGINT, 
	session_name VARCHAR(250), 
	kaltura_uid BIGINT, 
	PRIMARY KEY (uid),
	UNIQUE KEY session_id (session_id),
    CONSTRAINT FK_NEW_1533935501_B7C198E2FC940906 FOREIGN KEY (kaltura_uid)
    		REFERENCES tl_lakalt11_kaltura (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lakalt11_user (
	uid BIGINT NOT NULL AUTO_INCREMENT, 
	user_id BIGINT, 
	last_name VARCHAR(255), 
	login_name VARCHAR(255), 
	first_name VARCHAR(255), 
	finishedActivity BIT, 
	kaltura_session_uid BIGINT, 
	kaltura_uid BIGINT, 
	PRIMARY KEY (uid),
    CONSTRAINT FK_tl_lakalt11_user_1 FOREIGN KEY (kaltura_session_uid)
    		REFERENCES tl_lakalt11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_tl_lakalt11_user_2 FOREIGN KEY (kaltura_uid)
    		REFERENCES tl_lakalt11_kaltura (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lakalt11_kaltura_item (
   uid bigint not null AUTO_INCREMENT,
   entry_id VARCHAR(255),
   title VARCHAR(255),
   duration INTEGER,
   create_by BIGINT,
   create_date DATETIME,
   create_by_author TINYINT(1),
   sequence_id INTEGER,
   is_hidden TINYINT(1),
   kaltura_uid BIGINT,
   mark BIGINT,
   PRIMARY KEY (uid),
   CONSTRAINT FK_tl_lakalt11_item_1 FOREIGN KEY (create_by)
   		REFERENCES tl_lakalt11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT FK_tl_lakalt11_item_2 FOREIGN KEY (kaltura_uid)
   		REFERENCES tl_lakalt11_kaltura (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lakalt11_comment (
   uid BIGINT not null AUTO_INCREMENT,
   comment TEXT,
   kaltura_item_uid BIGINT,
   create_by BIGINT,
   create_date DATETIME,
   is_hidden TINYINT(1),
   PRIMARY KEY (uid),
   CONSTRAINT FK_tl_lakalt11_comment_2 FOREIGN KEY (create_by)
   		REFERENCES tl_lakalt11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT FK_tl_lakalt11_comment_3 FOREIGN KEY (kaltura_item_uid)
   		REFERENCES tl_lakalt11_kaltura_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lakalt11_rating (
   uid BIGINT not null AUTO_INCREMENT,
   rating FLOAT,
   kaltura_item_uid BIGINT,
   create_by BIGINT,
   PRIMARY KEY (uid),
   CONSTRAINT FK_tl_lakalt11_rating_2 FOREIGN KEY (create_by)
   		REFERENCES tl_lakalt11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_lakalt11_rating_3 FOREIGN KEY (kaltura_item_uid)
   		REFERENCES tl_lakalt11_kaltura_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lakalt11_item_log (
   uid BIGINT not null AUTO_INCREMENT,
   access_date DATETIME,
   kaltura_item_uid BIGINT,
   user_uid BIGINT,
   complete TINYINT(1),
   session_id BIGINT,
   PRIMARY KEY (uid),
   CONSTRAINT FK_tl_lakalt11_item_log_1 FOREIGN KEY (kaltura_item_uid)
   		REFERENCES tl_lakalt11_kaltura_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_lakalt11_item_log_2 FOREIGN KEY (user_uid)
   		REFERENCES tl_lakalt11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_lakalt11_kaltura ADD CONSTRAINT FK_NEW_174079131_89093BF758092FB FOREIGN KEY (create_by)
			REFERENCES tl_lakalt11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;

			
INSERT INTO tl_lakalt11_kaltura (
	uid,
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	allow_contribute_videos,
	learner_contribution_limit,
	content_in_use,
	define_later,
	allow_seeing_other_users_recordings,
	allow_comments,
	allow_ratings,
	reflect_on_activity) 
VALUES(
	1,
	"Kaltura Video",
	"Instructions",
	${default_content_id},
	0,
	1,
	-1,
	0,
	0,
	1,
	1,
	1,
	0
);

SET FOREIGN_KEY_CHECKS=1;