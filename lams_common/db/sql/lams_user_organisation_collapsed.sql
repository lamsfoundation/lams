CREATE TABLE lams_user_organisation_collapsed (
       user_organisation_collapsed_id BIGINT(20) NOT NULL
     , user_organisation_id BIGINT(20) NOT NULL
     , collapsed TINYINT(1) NOT NULL DEFAULT 1
     , PRIMARY KEY (user_organisation_collapsed_id)
     , INDEX (user_organisation_id)
     , CONSTRAINT FK_lams_user_organisation_collapsed_1 FOREIGN KEY (user_organisation_id)
                  REFERENCES lams_user_organisation (user_organisation_id)
)TYPE=InnoDB;

