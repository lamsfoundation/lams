-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3115 Flashless Authoring: annotation labels and regions, access dates for recently used LDs

CREATE TABLE IF NOT EXISTS lams_learning_design_annotation (
	  uid BIGINT(20) NOT NULL auto_increment
	, learning_design_id BIGINT(20) NOT NULL
	, ui_id INT(11)
	, title VARCHAR(1024)
	, xcoord INT(11)
	, ycoord INT(11)
	, end_xcoord INT(11)
	, end_ycoord INT(11)
	, color CHAR(7)
	, CONSTRAINT FK_lams_learning_design_annotation_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
                  ON UPDATE CASCADE ON DELETE CASCADE
	, PRIMARY KEY (uid)
)ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS lams_learning_design_access (
	  learning_design_id BIGINT(20) NOT NULL
	, user_id BIGINT(20) NOT NULL
	, access_date DATETIME
	, CONSTRAINT FK_lams_learning_design_access_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
                  ON UPDATE CASCADE ON DELETE CASCADE
    , CONSTRAINT FK_lams_learning_design_access_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
                  ON UPDATE CASCADE ON DELETE CASCADE
	, PRIMARY KEY (learning_design_id, user_id)
)ENGINE=InnoDB;


-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
