-- SQL statements to update from LAMS 2.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

UPDATE lams_tool SET pedagogical_planner_url='tool/lafrum11/authoring/initPedagogicalPlannerForm.do' WHERE tool_signature='lafrum11';

-- timestamp table
create table tl_lafrum11_timestamp (
  uid BIGINT(20) not null auto_increment,
  message_uid BIGINT(20) not null,
  timestamp_date DATETIME not null,
  forum_user_uid BIGINT(20) not null,
  primary key (`uid`),
  unique key `uid` (`uid`),
  key `message_uid` (`message_uid`),
  key `forum_user_uid` (`forum_user_uid`)
)ENGINE=InnoDB;

alter table tl_lafrum11_timestamp add index ForumUserFK (forum_user_uid), add constraint ForumUserFK foreign key (forum_user_uid) references tl_lafrum11_forum_user (uid);
alter table tl_lafrum11_timestamp add index MessageFK (message_uid), add constraint MessageFK foreign key (message_uid) references tl_lafrum11_message (uid);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;