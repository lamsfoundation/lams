SET FOREIGN_KEY_CHECKS=0;

DELETE from lams_cr_node_version_property;
DELETE from lams_cr_node_version;
DELETE from lams_cr_node;

DELETE from lams_cr_workspace_credential  ;
DELETE from lams_cr_credential ;
DELETE from lams_cr_workspace  ;

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO lams_cr_credential (credential_id, name, password) VALUES (1, 'atool','atool');
INSERT INTO lams_cr_workspace  (workspace_id, name) VALUES (1, 'atoolWorkspace');
INSERT INTO lams_cr_workspace_credential  (wc_id, workspace_id, credential_id) VALUES  (1, 1,1);

INSERT INTO lams_cr_node (node_id, workspace_id, type, created_date_time) VALUES ('1', '1', 'ROOTNODE', 20050106103000);
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, parent_nv_id, created_date_time)
VALUES ('1', '1', '1', null, 20050106103000);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (1,1,"VERSIONDESC","Initial Version","STRING");

INSERT INTO lams_cr_node (node_id, workspace_id, type, created_date_time, path) VALUES ('2', '1', 'DATANODE', 20050106103100, "/test");
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, parent_nv_id, created_date_time)
VALUES ('2', '2', '1', '1', 20050106103100);
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, parent_nv_id, created_date_time)
VALUES ('3', '2', '2', '1', 20050106110000);

INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (2,2,"APPSPEC","abcd","STRING");
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (3,2,"VERSIONDESC","Initial Version.","STRING");
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (4,3,"APPSEC","The quick brown fox jumped over the lazy dog.","STRING");
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (5,3,"VERSIONDESC","The second version.","STRING");

-- Note : this file node is missing its file as can't setup the repository
INSERT INTO lams_cr_node (node_id, workspace_id, type, created_date_time, path) VALUES ('3', '1', 'FILENODE', 20050106103100, "");
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, parent_nv_id, created_date_time)
VALUES ('4', '3', '1', '1', 20050106103100);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (6,4,"VERSIONDESC","Initial Version.","STRING");
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (7,4,"FILENAME","nofile.txt","STRING");
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (8,4,"MIMETYPE","unknown/unknown","STRING");