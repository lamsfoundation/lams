-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- SP-4 Prevent duplicates in ext user mapping table
ALTER TABLE lams_ext_user_userid_map ADD KEY UQ_lams_ext_user_userid_map_2 (external_username);
DELETE a FROM lams_ext_user_userid_map AS a JOIN (SELECT sid, ext_server_org_map_id, external_username FROM lams_ext_user_userid_map GROUP BY ext_server_org_map_id, external_username) AS b USING (ext_server_org_map_id, external_username) WHERE a.sid <> b.sid;
ALTER TABLE lams_ext_user_userid_map ADD UNIQUE KEY UQ_lams_ext_user_userid_map_1 (ext_server_org_map_id, external_username);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
