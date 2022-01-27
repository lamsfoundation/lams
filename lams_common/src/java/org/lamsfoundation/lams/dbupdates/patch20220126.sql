-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5277 Add LTI Advantage model
ALTER TABLE lams_ext_server_lesson_map ADD COLUMN lti_adv_deployment_id VARCHAR(255),
									   ADD COLUMN lti_adv_grading_type  VARCHAR(10),
									   ADD COLUMN lti_adv_grading_url   VARCHAR(255);
									   
ALTER TABLE lams_ext_server_org_map    ADD COLUMN lti_adv_issuer 				VARCHAR(64),
									   ADD COLUMN lti_adv_client_id 			VARCHAR(64),
									   ADD COLUMN lti_adv_platform_key_set_url 	VARCHAR(255),
									   ADD COLUMN lti_adv_oidc_auth_url			VARCHAR(255),
									   ADD COLUMN lti_adv_access_token_url		VARCHAR(255),
									   ADD COLUMN lti_adv_tool_key_set_url		VARCHAR(255),
									   ADD COLUMN lti_adv_tool_key_id			VARCHAR(32),
									   ADD COLUMN lti_adv_public_key			TEXT,
									   ADD COLUMN lti_adv_private_key			TEXT;
									

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
