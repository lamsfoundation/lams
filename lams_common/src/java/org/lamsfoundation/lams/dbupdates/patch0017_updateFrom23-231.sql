-- SQL statements to update from LAMS 2.3

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

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
) TYPE=InnoDB;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;