-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-
CREATE TABLE lams_policy_state (
       policy_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (policy_state_id)
);

CREATE TABLE lams_policy_type (
       policy_type_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (policy_type_id)
);

CREATE TABLE lams_policy (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , policy_id BIGINT(20)
     , created_by BIGINT(20) NOT NULL
     , policy_name VARCHAR(255) NOT NULL
     , version LONG
     , summary TEXT
     , full_policy TEXT
     , last_modified DATETIME NOT NULL
     , policy_state_id INT(3) NOT NULL
     , policy_type_id INT(3) NOT NULL
     , PRIMARY KEY (uid)
     , KEY (created_by)
     , CONSTRAINT FK_lams_lesson_1 FOREIGN KEY (created_by)
                  REFERENCES lams_user (user_id)
     , KEY (policy_state_id)
     , CONSTRAINT FK_lams_policy_2 FOREIGN KEY (policy_state_id)
                  REFERENCES lams_policy_state (policy_state_id)
     , KEY (policy_type_id)
     , CONSTRAINT FK_lams_policy_3 FOREIGN KEY (policy_type_id)
                  REFERENCES lams_policy_type (policy_type_id)
);

CREATE TABLE lams_policy_consent (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , date_agreed_on DATETIME NOT NULL
     , policy_uid BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , KEY (policy_uid)
     , CONSTRAINT FK_lams_consent_1_1 FOREIGN KEY (policy_uid)
                  REFERENCES lams_policy (uid)
     , KEY (user_id)
     , CONSTRAINT FK_lams_consent_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
);

INSERT INTO lams_policy_state VALUES (1, 'ACTIVE');
INSERT INTO lams_policy_state VALUES (2, 'INACTIVE');
INSERT INTO lams_policy_state VALUES (3, 'DRAFT');

INSERT INTO lams_policy_type VALUES (1, 'TYPE_SITE_POLICY');
INSERT INTO lams_policy_type VALUES (2, 'TYPE_PRIVACY_POLICY');
INSERT INTO lams_policy_type VALUES (3, 'TYPE_THIRD_PARTIES_POLICY');
INSERT INTO lams_policy_type VALUES (4, 'TYPE_OTHER');

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;