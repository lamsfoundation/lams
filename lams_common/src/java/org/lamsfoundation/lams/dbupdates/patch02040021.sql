-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3119 Course level groups
CREATE TABLE lams_organisation_grouping (
	grouping_id BIGINT(20) NOT NULL AUTO_INCREMENT,
	organisation_id BIGINT(20) NOT NULL,
	name     VARCHAR(255),
	PRIMARY KEY (grouping_id),
	CONSTRAINT FK_lams_organisation_grouping_1 FOREIGN KEY (organisation_id)
    	REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE lams_organisation_group (
	group_id BIGINT(20) NOT NULL AUTO_INCREMENT,
	grouping_id BIGINT(20) NOT NULL,
	name     VARCHAR(255),
	PRIMARY KEY (group_id),
	CONSTRAINT FK_lams_organisation_group_1 FOREIGN KEY (grouping_id)
    	REFERENCES lams_organisation_grouping (grouping_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE lams_user_organisation_group (
	group_id BIGINT(20) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	PRIMARY KEY (group_id, user_id),
	CONSTRAINT FK_lams_user_organisation_group_1 FOREIGN KEY (group_id)
    	REFERENCES lams_organisation_group (group_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_lams_user_organisation_group_2 FOREIGN KEY (user_id)
    	REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
