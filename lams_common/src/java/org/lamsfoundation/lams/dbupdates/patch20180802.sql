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
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (scale_id),
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
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (outcome_id),
	CONSTRAINT FK_lams_outcome_1 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE,
	-- Do not remove outcomes when a scale gets removed. Programmer needs to do it manually to make sure it is the right step.
	CONSTRAINT FK_lams_outcome_2 FOREIGN KEY (scale_id) REFERENCES lams_outcome_scale (scale_id) ON UPDATE CASCADE
	);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;