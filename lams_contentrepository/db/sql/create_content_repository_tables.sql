USE lams;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS lams_cr_node_version_property;
DROP TABLE IF EXISTS lams_cr_node_version;
DROP TABLE IF EXISTS lams_cr_node;
DROP TABLE IF EXISTS lams_cr_workspace_credential;
DROP TABLE IF EXISTS lams_cr_credential;
DROP TABLE IF EXISTS lams_cr_workspace;


--
-- Table structure for table 'lams_cr_workspace'
--

CREATE TABLE lams_cr_workspace (
  workspace_id bigint(20) unsigned NOT NULL auto_increment,
  name varchar(255) NOT NULL default '0',
  root_node_id bigint(20) unsigned,
  PRIMARY KEY  (workspace_id),
  UNIQUE KEY workspace_id (workspace_id,name),
  KEY name (name)
) TYPE=InnoDB COMMENT='Content repository workspace';

--
-- Table structure for table 'lams_cr_credential'
--

CREATE TABLE lams_cr_credential (
  credential_id bigint(20) unsigned NOT NULL auto_increment,
  name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY  (credential_id),
  UNIQUE KEY name (name)
) TYPE=InnoDB COMMENT='Records the identification properties for a tool.';

--
-- Table structure for table 'lams_cr_workspace_credential'
--

CREATE TABLE lams_cr_workspace_credential (
  wc_id bigint(20) unsigned NOT NULL auto_increment,
  workspace_id bigint(20) unsigned NOT NULL default '0',
  credential_id bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (wc_id),
  INDEX (credential_id),
  INDEX (workspace_id),
  CONSTRAINT FK_lams_cr_workspace_credential_1 
  		FOREIGN KEY (credential_id) 
  		REFERENCES lams_cr_credential(credential_id) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_lams_cr_workspace_credential_2 
  		FOREIGN KEY (workspace_id) 
  		REFERENCES lams_cr_workspace (workspace_id) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION
) TYPE=InnoDB COMMENT='Maps which tools access which workspaces';

---
--- Table structure for table 'lams_cr_node'
---

CREATE TABLE lams_cr_node (
  node_id bigint(20) unsigned NOT NULL auto_increment,
  workspace_id bigint(20) unsigned NOT NULL,
  path varchar(255),
  type varchar(255) NOT NULL,
  created_date_time timestamp(14) NOT NULL,
  PRIMARY KEY  (node_id),
  UNIQUE KEY node_id (node_id),
  KEY workspace_id (workspace_id),
  CONSTRAINT FK_lams_cr_node_1 
  		FOREIGN KEY (workspace_id) 
  		REFERENCES lams_cr_workspace (workspace_id) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION
) TYPE=InnoDB COMMENT='The main table containing the node definition';

--
-- Table structure for table 'lams_cr_node_version'
-- Has a "dummy" id nv_id (node version id) which is unique for all node_id and 
-- version_id combinations. 
--
-- It is the node_id and version_id combination that is the "real" key for this table,
-- so that's what is used in foriegn keys. Middlegen doesn't like it but tough - it still
-- seems to work! If we start getting problems here, that may be the issue.

CREATE TABLE lams_cr_node_version (
  nv_id bigint(20) unsigned NOT NULL auto_increment,
  node_id bigint(20) unsigned NOT NULL default '0',
  version_id bigint(20) unsigned NOT NULL default '0',
  created_date_time timestamp(14) NOT NULL,
  parent_nv_id bigint(20) unsigned,
  PRIMARY KEY  (nv_id),
  INDEX (node_id, version_id),
  INDEX (parent_nv_id),
  CONSTRAINT FK_lams_cr_node_version_1 
  		FOREIGN KEY (node_id) 
  		REFERENCES lams_cr_node (node_id) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_lams_cr_node_version_2 
  		FOREIGN KEY (parent_nv_id) 
  		REFERENCES lams_cr_node_version (nv_id) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION
) TYPE=InnoDB COMMENT='Represents a version of a node';

--
-- Table structure for table 'lams_cr_node_property'
--

CREATE TABLE lams_cr_node_version_property (
  id bigint(20) unsigned NOT NULL auto_increment,
  nv_id bigint(20) unsigned NOT NULL default '0', 
  name varchar(255) NOT NULL,
  value varchar(255) NOT NULL,
  type tinyint(3) unsigned NOT NULL,
  PRIMARY KEY  (id),
  UNIQUE KEY id (id),
  INDEX (nv_id),
  CONSTRAINT FK_lams_version_property_1 
  		FOREIGN KEY (nv_id) 
  		REFERENCES lams_cr_node_version (nv_id) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION
) TYPE=InnoDB COMMENT='Records the property for a node';

SET FOREIGN_KEY_CHECKS=1;
