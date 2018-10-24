-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3773 Get rid of Workspaces. Only use Workspace Folders.
ALTER TABLE lams_user DROP FOREIGN KEY FK_lams_user_2,
					  CHANGE COLUMN workspace_id workspace_folder_id BIGINT(20);
UPDATE lams_user AS u JOIN lams_workspace AS w ON u.workspace_folder_id = w.workspace_id
	SET u.workspace_folder_id = w.default_fld_id;
ALTER TABLE lams_user ADD CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lams_workspace_folder ADD COLUMN organisation_id BIGINT(20) AFTER user_id;
UPDATE lams_workspace_folder AS f
	JOIN lams_workspace AS w ON f.workspace_folder_id = w.default_fld_id OR f.workspace_folder_id = w.def_run_seq_fld_id
	JOIN lams_organisation AS o USING (workspace_id)
	SET f.organisation_id = o.organisation_id;
ALTER TABLE lams_organisation DROP FOREIGN KEY FK_lams_organisation_2,
							  DROP COLUMN workspace_id;

DROP TABLE lams_wkspc_wkspc_folder,
		   lams_workspace;

-- additional clean up
ALTER TABLE lams_workspace_folder_content DROP FOREIGN KEY FK_lams_workspace_folder_content_2;
DROP TABLE lams_wkspc_fld_content_type;
       

COMMIT;
SET AUTOCOMMIT = 1;