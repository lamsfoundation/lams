# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-04-07 15:02:07
# 


DROP TABLE IF EXISTS tool_imscp_content;
DROP TABLE IF EXISTS tool_imscp_userprogress;
DROP TABLE IF EXISTS tool_imscp_package;

--
-- Table structure for table 'tool_imscp_package'
-- Records the details of an IMS context package
-- creator_user_id is a user id from the LAMS user management service
CREATE TABLE tool_imscp_package (
  sid bigint(20) unsigned NOT NULL auto_increment,
  tool_content_id bigint(20) unsigned,
  define_later tinyint(1) unsigned default 0,
  title varchar(255),
  description text,
  ims_schema varchar(255),
  date_created datetime,
  creator_user_id bigint(20) unsigned,
  cr_uuid bigint(20) unsigned,
  cr_version_id bigint(20) unsigned,
  initial_item varchar(255),
  organizationXML text,
  PRIMARY KEY  (sid)
) TYPE=InnoDB COMMENT='Details of an IMS Content Package';

--
-- Table structure for table 'tool_imscp_userprogress'
-- Records the current state for a learner in a lesson.
-- Column learner_id is a user id from the LAMS user management service.
-- Column lesson_id is lesson id from the LAMS core.
-- Put another index on this table when we have worked out
-- how we are likely to access the values - the package_id index
-- exists to keep the foriegn key happy.
CREATE TABLE tool_imscp_userprogress (
  sid bigint(20) unsigned NOT NULL auto_increment,
  learner_id bigint(20) unsigned NOT NULL,
  lesson_id bigint(20) unsigned NOT NULL,
  package_id bigint(20) unsigned NOT NULL,
  current_item varchar(255),
  PRIMARY KEY  (sid),
  INDEX (package_id),
  CONSTRAINT FK_tool_imscp_userprogress_package
  		FOREIGN KEY (package_id) 
  		REFERENCES tool_imscp_package(sid) 
  		ON DELETE NO ACTION ON UPDATE NO ACTION
) TYPE=InnoDB COMMENT='Current state for a learner in a session';

