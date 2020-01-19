  -- SQL statements to update from LAMS 2.3.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV-2385 
alter table tl_lasbmt11_report add column mark_file_uuid bigint default null;
alter table tl_lasbmt11_report add column mark_file_version_id bigint default null;
alter table tl_lasbmt11_report add column mark_file_name varchar(255) default null;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;