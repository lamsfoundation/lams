DROP INDEX PRIMARY ON lams_role;
DROP INDEX gname ON lams_role;
DROP INDEX PRIMARY ON lams_user;
DROP INDEX login ON lams_user;
DROP INDEX PRIMARY ON lams_organisation;
DROP INDEX idx_user_org_org_id ON lams_user_organisation;
DROP INDEX user_id ON lams_user_organisation;

DROP TABLE lams_user_organisation_role;
DROP TABLE lams_authentication_method_parameter;
DROP TABLE lams_user_organisation;
DROP TABLE lams_organisation;
DROP TABLE lams_user;
DROP TABLE lams_workspace;
DROP TABLE lams_workspace_folder;
DROP TABLE lams_authentication_method;
DROP TABLE lams_role;
DROP TABLE lams_authentication_method_type;
DROP TABLE lams_organisation_type;












