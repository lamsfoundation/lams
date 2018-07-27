-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2905 Integrate lams_signup to LAMS core
CREATE TABLE IF NOT EXISTS lams_signup_organisation (
	signup_organisation_id BIGINT(20) NOT NULL auto_increment,
	organisation_id BIGINT(20) NOT NULL,
	add_to_lessons TINYINT(1) DEFAULT 1,
	add_as_staff TINYINT(1) DEFAULT 0,
	add_with_author TINYINT(1) DEFAULT 0,
	add_with_monitor TINYINT(1) DEFAULT 0,
	course_key VARCHAR(255),
	blurb TEXT,
	create_date DATETIME,
	disabled TINYINT(1) DEFAULT 0,
	context VARCHAR(255) UNIQUE,
	PRIMARY KEY (signup_organisation_id),
	INDEX (organisation_id)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS lams_signup_user;

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;