-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS tl_lalead11_leaderselection;
DROP TABLE IF EXISTS tl_lalead11_session;
DROP TABLE IF EXISTS tl_lalead11_user;
CREATE TABLE tl_lalead11_leaderselection (uid BIGINT NOT NULL AUTO_INCREMENT, create_date DATETIME, update_date DATETIME, create_by BIGINT, title VARCHAR(255), instructions TEXT, run_offline BIT, content_in_use BIT, define_later BIT, tool_content_id BIGINT, PRIMARY KEY (uid))ENGINE=InnoDB;
CREATE TABLE tl_lalead11_session (uid BIGINT NOT NULL AUTO_INCREMENT, session_end_date DATETIME, session_start_date DATETIME, status integer, session_id BIGINT, session_name VARCHAR(250), leaderselection_uid BIGINT, group_leader_uid BIGINT, PRIMARY KEY (uid))ENGINE=InnoDB;
CREATE TABLE tl_lalead11_user (uid BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT, last_name VARCHAR(255), login_name VARCHAR(255), first_name VARCHAR(255), finishedActivity BIT, leaderselection_session_uid BIGINT, PRIMARY KEY (uid))ENGINE=InnoDB;
ALTER TABLE tl_lalead11_session ADD INDEX FK_NEW_1316293302_B7C198E2FC940906 (leaderselection_uid), ADD CONSTRAINT FK_NEW_1316293302_B7C198E2FC940906 FOREIGN KEY (leaderselection_uid) REFERENCES tl_lalead11_leaderselection (uid);
ALTER TABLE tl_lalead11_session ADD INDEX FK_lalead11_1 (group_leader_uid), ADD CONSTRAINT FK_lalead11_1 FOREIGN KEY (group_leader_uid) REFERENCES tl_lalead11_user (uid);
ALTER TABLE tl_lalead11_user ADD INDEX FK_NEW_1316293302_CB8A58FFA3B0FADF (leaderselection_session_uid), ADD CONSTRAINT FK_NEW_1316293302_CB8A58FFA3B0FADF FOREIGN KEY (leaderselection_session_uid) REFERENCES tl_lalead11_session (uid);

INSERT INTO tl_lalead11_leaderselection (
	title,
	instructions,
	tool_content_id,
	run_offline,
	content_in_use,
	define_later) 
VALUES(
	"Leader Selection",
	"Instructions",
	${default_content_id},
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;
