-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4644 Add learning outcomes tables

CREATE TABLE lams_outcome_scale (
	scale_id MEDIUMINT AUTO_INCREMENT,
	organisation_id BIGINT,
	name VARCHAR(255),
	code VARCHAR(50),
	description TEXT,
	content_folder_id CHAR(36),
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (scale_id),
	INDEX (name),
	INDEX (code),
	UNIQUE INDEX (code, organisation_id),
	CONSTRAINT FK_lams_outcome_scale_1 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
	);
	
CREATE TABLE lams_outcome_scale_item (
	item_id INT AUTO_INCREMENT,
	scale_id MEDIUMINT,
	value TINYINT,
	name VARCHAR(255),
	PRIMARY KEY (item_id),
	CONSTRAINT FK_lams_outcome_scale_item_1 FOREIGN KEY (scale_id) REFERENCES lams_outcome_scale (scale_id) ON DELETE CASCADE ON UPDATE CASCADE
	);

CREATE TABLE lams_outcome (
	outcome_id MEDIUMINT AUTO_INCREMENT,
	organisation_id BIGINT,
	scale_id MEDIUMINT NOT NULL,
	name VARCHAR(255),
	code VARCHAR(50),
	description TEXT,
	content_folder_id CHAR(36),
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (outcome_id),
	INDEX (name),
	INDEX (code),
	UNIQUE INDEX (code, organisation_id),
	CONSTRAINT FK_lams_outcome_1 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE,
	-- Do not remove outcomes when a scale gets removed. Programmer needs to do it manually to make sure it is the right step.
	CONSTRAINT FK_lams_outcome_2 FOREIGN KEY (scale_id) REFERENCES lams_outcome_scale (scale_id) ON UPDATE CASCADE
	);
	
CREATE TABLE lams_outcome_mapping (
	mapping_id BIGINT AUTO_INCREMENT,
	outcome_id MEDIUMINT NOT NULL,
	lesson_id BIGINT,
	tool_content_id BIGINT,
	item_id BIGINT,
	PRIMARY KEY (mapping_id),
	CONSTRAINT FK_lams_outcome_mapping_1 FOREIGN KEY (outcome_id) REFERENCES lams_outcome (outcome_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_mapping_2 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_mapping_3 FOREIGN KEY (tool_content_id) REFERENCES lams_tool_content (tool_content_id) ON DELETE CASCADE ON UPDATE CASCADE
	);

CREATE TABLE lams_outcome_result (
	result_id BIGINT AUTO_INCREMENT,
	mapping_id BIGINT NOT NULL,
	user_id BIGINT,
	value TINYINT,
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (result_id),
	CONSTRAINT FK_lams_outcome_result_1 FOREIGN KEY (mapping_id) REFERENCES lams_outcome_mapping (mapping_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_result_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_result_3 FOREIGN KEY (create_by) REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
	);
	
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;