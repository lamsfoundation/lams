SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20201122.sql to patch20210728.sql
-- It should upgrade this tool to version 4.6

--LDEV-5120 Add column for Doku Gallery Walk
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN gallery_walk_enabled TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_read_only TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_started TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_finished TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_instructions TEXT;


--LDEV-5141 Add column for time limit manual/automatic start
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN time_limit_manual_start TINYINT NOT NULL DEFAULT 0 AFTER time_limit;


--LDEV-5142 Rewrite time limits to be similar to Assessment's LDEV-5041
ALTER TABLE tl_ladoku11_user ADD COLUMN time_limit_launched_date DATETIME;

UPDATE tl_ladoku11_user AS u 
	LEFT JOIN tl_ladoku11_session AS s ON u.session_uid = s.uid
	JOIN tl_ladoku11_dokumaran AS d ON u.dokumaran_uid = d.uid OR s.dokumaran_uid = d.uid
SET u.time_limit_launched_date = d.time_limit_launched_date,
	u.dokumaran_uid = d.uid;

ALTER TABLE tl_ladoku11_dokumaran DROP COLUMN time_limit_manual_start,
								  DROP COLUMN time_limit_launched_date,
								  ADD COLUMN absolute_time_limit DATETIME AFTER time_limit;
								  
ALTER TABLE tl_ladoku11_dokumaran CHANGE COLUMN time_limit relative_time_limit smallint unsigned NOT NULL DEFAULT '0';
								  
CREATE TABLE tl_ladoku11_time_limit (
	dokumaran_uid BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	adjustment SMALLINT NOT NULL DEFAULT 0,
	CONSTRAINT FK_tl_ladoku11_time_limit_1 FOREIGN KEY (dokumaran_uid)
		REFERENCES tl_ladoku11_dokumaran (uid) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_tl_ladoku11_time_limit_2 FOREIGN KEY (user_id)
		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
	
--LDEV-5122 Use blank Etherpad by default
UPDATE tl_ladoku11_dokumaran SET instructions = NULL WHERE uid = 1;

--LDEV-5222 Allow reedit in doKu tool
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN gallery_walk_edit_enabled TINYINT NOT NULL DEFAULT 0 AFTER gallery_walk_finished;

SET FOREIGN_KEY_CHECKS=1;