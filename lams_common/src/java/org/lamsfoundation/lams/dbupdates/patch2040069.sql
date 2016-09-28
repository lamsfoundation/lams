-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3997 LAMS to become LTI compliant tool provider (version 1.1)
CREATE TABLE lams_ext_server_type (
       server_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (server_type_id)
)ENGINE=InnoDB;

INSERT INTO lams_ext_server_type VALUES (1, 'INTEGRATED SERVER');
INSERT INTO lams_ext_server_type VALUES (2, 'LTI TOOL CONSUMER');

ALTER TABLE lams_ext_server_org_map ADD COLUMN server_type_id INT(11) NOT NULL DEFAULT 1, ADD CONSTRAINT FK_lams_ext_server_type FOREIGN KEY (server_type_id) REFERENCES lams_ext_server_type(server_type_id);
UPDATE lams_ext_server_org_map SET server_type_id=1;

ALTER TABLE lams_ext_server_lesson_map ADD COLUMN resource_link_id VARCHAR(255);
ALTER TABLE lams_ext_user_userid_map ADD COLUMN tc_gradebook_id VARCHAR(250);

-- LDEV-3998 Remove reference to Organisation from ExtServerOrgMap class
ALTER TABLE lams_ext_server_org_map DROP FOREIGN KEY lams_ext_server_org_map_fk;
ALTER TABLE lams_ext_server_org_map DROP COLUMN orgid, DROP INDEX orgid;

COMMIT;
SET AUTOCOMMIT = 1;
