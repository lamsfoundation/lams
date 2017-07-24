-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-4366 Add tables for Kumalive

CREATE TABLE lams_kumalive (
	   kumalive_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , created_by BIGINT(20)
     , finished TINYINT(1) NOT NULL DEFAULT 0
     , name VARCHAR(250)
     , PRIMARY KEY (kumalive_id)
     , CONSTRAINT FK_lams_kumalive_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
  	 , CONSTRAINT FK_lams_kumalive_2 FOREIGN KEY (created_by)
                  REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_rubric (
	   rubric_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , kumalive_id BIGINT(20)
     , order_id TINYINT NOT NULL
     , name VARCHAR(250)
     , PRIMARY KEY (rubric_id)
     , CONSTRAINT FK_lams_kumalive_rubric_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
  	 , CONSTRAINT FK_lams_kumalive_rubric_2 FOREIGN KEY (kumalive_id)
                  REFERENCES lams_kumalive (kumalive_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_score (
	   score_id BIGINT(20) NOT NULL AUTO_INCREMENT
	 , rubric_id BIGINT(20) NOT NULL
     , user_id BIGINT(20)
     , score TINYINT
     , PRIMARY KEY (score_id)
     , CONSTRAINT FK_lams_kumalive_score_1 FOREIGN KEY (rubric_id)
                  REFERENCES lams_kumalive_rubric (rubric_id) ON DELETE CASCADE ON UPDATE CASCADE
  	 , CONSTRAINT FK_lams_kumalive_score_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
