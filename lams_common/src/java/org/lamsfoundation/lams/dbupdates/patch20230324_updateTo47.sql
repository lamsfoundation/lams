SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20220126.sql to patch20230324.sql
-- It should upgrade LAMS to version 4.7

--LDEV-5277 Add LTI Advantage model
ALTER TABLE lams_ext_server_lesson_map ADD COLUMN lti_adv_deployment_id VARCHAR(255),
									   ADD COLUMN lti_adv_grading_type  VARCHAR(10),
									   ADD COLUMN lti_adv_grading_url   VARCHAR(255);

ALTER TABLE lams_ext_server_org_map    ADD COLUMN use_course_prefix					TINYINT NOT NULL DEFAULT 1,
									   ADD COLUMN user_registration_enabled			TINYINT NOT NULL DEFAULT 1,
									   ADD COLUMN user_name_lower_case				TINYINT NOT NULL DEFAULT 0,

									   ADD COLUMN lti_adv_enforce_cookie			TINYINT NOT NULL DEFAULT 0,
									   ADD COLUMN lti_adv_reregistration_enabled	TINYINT NOT NULL DEFAULT 1,
									   ADD COLUMN lti_adv_issuer 					VARCHAR(64),
									   ADD COLUMN lti_adv_client_id 				VARCHAR(64),
									   ADD COLUMN lti_adv_platform_key_set_url 		VARCHAR(255),
									   ADD COLUMN lti_adv_oidc_auth_url				VARCHAR(255),
									   ADD COLUMN lti_adv_access_token_url			VARCHAR(255),
									   ADD COLUMN lti_adv_tool_name 				VARCHAR(64),
									   ADD COLUMN lti_adv_tool_description			VARCHAR(255),
									   ADD COLUMN lti_adv_tool_key_set_url			VARCHAR(255),
									   ADD COLUMN lti_adv_tool_key_id				VARCHAR(32),
									   ADD COLUMN lti_adv_public_key				TEXT,
									   ADD COLUMN lti_adv_private_key				TEXT;

--LDEV-5277 Add default country, locale and timezone to LTI consumer

ALTER TABLE lams_ext_server_org_map    ADD COLUMN default_country  	CHAR(2) 		 AFTER user_id_parameter_name,
									   ADD COLUMN default_locale_id TINYINT UNSIGNED AFTER default_country,
									   ADD COLUMN default_timezone 	VARCHAR(255) 	 AFTER default_locale_id;

--LDEV-5286

ALTER TABLE lams_ext_server_org_map ADD COLUMN add_staff_to_all_lessons TINYINT NOT NULL DEFAULT 0 AFTER time_to_live_login_request_enabled;

--LDEV-5307 Allow exact matching of VSAs

ALTER TABLE lams_qb_question ADD COLUMN exact_match TINYINT NOT NULL DEFAULT 0 AFTER case_sensitive;

--LDEV-5277 Add ID for communication with LTI Advantage platform, which is not necessarily the same as external user ID

ALTER TABLE lams_ext_user_userid_map ADD COLUMN lti_adv_username VARCHAR(250) AFTER external_username;

-- LDEV-5316 Change content folder ID to default one for questions which have images referring the default content folder
-- and which do not have images which refer a new content folder

UPDATE lams_qb_question AS q LEFT JOIN lams_qb_option AS o ON q.uid = o.qb_question_uid
    SET q.content_folder_id = '12345678-9012-abcd-0123-4567890abcde'
WHERE (q.description LIKE '%secure/12/34/56/78/90/12/%' OR o.name LIKE '%secure/12/34/56/78/90/12/%')
  AND (q.content_folder_id IS NULL OR (q.description NOT LIKE CONCAT('%secure/', SUBSTRING(q.content_folder_id, 0, 2))
  AND (o.uid IS NULL OR o.name NOT LIKE CONCAT('%secure/', SUBSTRING(q.content_folder_id, 0, 2)))));

-- LDEV-5328 Bump version
UPDATE lams_configuration SET config_value = '4.6' WHERE config_key = 'ServerVersionNumber';

-- LDEV-5329 Remove obsolete configuration items
DELETE FROM lams_configuration WHERE config_key IN ('AuthoringScreenSize', 'MonitorScreenSize', 'LearnerScreenSize', 'AdminScreenSize');

-- LDEV-5302 Rename SYSADMIN to APPADMIN, add new SYSADMIN role

UPDATE lams_role SET name = 'APPADMIN', description = 'LAMS Application Administrator' WHERE name = 'SYSADMIN';

INSERT INTO lams_role(role_id, name, description, create_date) VALUES (7, 'SYSADMIN', 'LAMS System Administrator', NOW());
INSERT INTO lams_user_organisation_role SELECT NULL, user_organisation_id, 7 FROM lams_user_organisation_role WHERE role_id = 1;

--  LDEV-5357 Use complex UUIDs for all files
ALTER TABLE lams_cr_node RENAME COLUMN portrait_uuid TO uuid,
    RENAME INDEX IDX_portrait_uuid TO IDX_uuid;

UPDATE lams_cr_node SET uuid = UUID_TO_BIN(LOWER(CONCAT(
        HEX(RANDOM_BYTES(4)), '-',
        HEX(RANDOM_BYTES(2)), '-4',
        SUBSTR(HEX(RANDOM_BYTES(2)), 2, 3), '-',
        CONCAT(HEX(FLOOR(ASCII(RANDOM_BYTES(1)) / 64)+8),SUBSTR(HEX(RANDOM_BYTES(2)), 2, 3)), '-',
        HEX(RANDOM_BYTES(6))
))) WHERE uuid IS NULL;

-- SP-4 Prevent duplicates in ext user mapping table
ALTER TABLE lams_ext_user_userid_map ADD KEY UQ_lams_ext_user_userid_map_2 (external_username);
DELETE a FROM lams_ext_user_userid_map AS a JOIN (SELECT sid, ext_server_org_map_id, external_username FROM lams_ext_user_userid_map GROUP BY ext_server_org_map_id, external_username) AS b USING (ext_server_org_map_id, external_username) WHERE a.sid <> b.sid;
ALTER TABLE lams_ext_user_userid_map ADD UNIQUE KEY UQ_lams_ext_user_userid_map_1 (ext_server_org_map_id, external_username);

-- LDEV-5277 Remove prefix uniqueness as sometimes duplicate feature becomes an useful feature

ALTER TABLE lams_ext_server_org_map DROP KEY prefix;

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;