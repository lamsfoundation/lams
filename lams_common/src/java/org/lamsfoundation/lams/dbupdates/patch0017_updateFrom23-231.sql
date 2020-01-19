-- SQL statements to update from LAMS 2.3

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- Adding new column to external organisation table for tool adapters
ALTER TABLE lams_ext_server_org_map ADD COLUMN server_url varchar(255) default null;

--
-- Table structure for table `lams_ext_server_tool_map`
--
DROP TABLE IF EXISTS lams_ext_server_tool_map;
CREATE TABLE lams_ext_server_tool_map (
  uid BIGINT(20) NOT NULL auto_increment,
  tool_id BIGINT(20) NOT NULL,
  ext_server_org_map_id int(11) NOT NULL,
  PRIMARY KEY  (uid),
  UNIQUE KEY unique_adapter_map (ext_server_org_map_id, tool_id),
  CONSTRAINT lams_ext_server_tool_map_fk1 FOREIGN KEY (ext_server_org_map_id) REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT lams_ext_server_tool_map_fk2 FOREIGN KEY (tool_id) REFERENCES lams_tool (tool_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- LDEV-2369 Add tutorial video support
ALTER TABLE lams_user ADD COLUMN tutorials_disabled TINYINT(1) DEFAULT 0,
	                  ADD COLUMN first_login TINYINT(1) DEFAULT 1;
 	
CREATE TABLE lams_user_disabled_tutorials (
  user_id BIGINT(20) NOT NULL,
  page_str CHAR(5) NOT NULL,
  CONSTRAINT FK_lams_user_disabled_tutorials_1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (user_id,page_str)
)ENGINE=InnoDB;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;