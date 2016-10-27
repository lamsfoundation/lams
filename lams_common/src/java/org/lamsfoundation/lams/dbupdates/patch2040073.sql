-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3962 Bootstrap index page
DROP TABLE lams_user_organisation_collapsed;

CREATE TABLE lams_favorite_organisation (
       favorite_organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (favorite_organisation_id)
     , INDEX (organisation_id)
     , CONSTRAINT FK_lams_favorite_organisation_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (user_id)
     , CONSTRAINT FK_lams_favorite_organisation_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

ALTER TABLE lams_user ADD COLUMN last_visited_organisation_id BIGINT(20), 
	ADD CONSTRAINT FK_lams_user_7 FOREIGN KEY (last_visited_organisation_id) REFERENCES lams_organisation(organisation_id);

COMMIT;
SET AUTOCOMMIT = 1;
