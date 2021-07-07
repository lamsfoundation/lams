-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5155 Add new rating style, rubrics

ALTER TABLE lams_rating_criteria ADD COLUMN rating_criteria_group_id MEDIUMINT UNSIGNED AFTER rating_criteria_type_id;

CREATE TABLE lams_rating_rubrics_columns (
	uid INT UNSIGNED AUTO_INCREMENT,
	rating_criteria_group_id MEDIUMINT UNSIGNED,
	rating_criteria_id BIGINT,
	order_id TINYINT UNSIGNED NOT NULL,
	name VARCHAR(2000),
	PRIMARY KEY (uid),
	UNIQUE KEY UQ_lams_rating_rubrics_columns_1 (rating_criteria_group_id, rating_criteria_id, order_id),
	CONSTRAINT FK_lams_rating_rubrics_columns_1 FOREIGN KEY (rating_criteria_id) REFERENCES lams_rating_criteria (rating_criteria_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
