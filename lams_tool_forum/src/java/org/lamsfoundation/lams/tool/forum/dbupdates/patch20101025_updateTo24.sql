-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV-2545 Deliver email messages to students/teachers on forum postings
alter table tl_lafrum11_forum add column notify_learners_on_forum_posting tinyint DEFAULT 0;
alter table tl_lafrum11_forum add column notify_teachers_on_forum_posting tinyint DEFAULT 0;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;