-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3228 Ability to change, add, remove MCQ questions even after student have reached it
ALTER TABLE tl_lamc11_content DROP COLUMN content_in_use;

ALTER TABLE tl_lamc11_options_content DROP FOREIGN KEY FK_tl_lamc11_options_content_1;
ALTER TABLE tl_lamc11_options_content MODIFY COLUMN mc_que_content_id BIGINT(20), 
  ADD CONSTRAINT FK_tl_lamc11_options_content_1 FOREIGN KEY (mc_que_content_id) REFERENCES tl_lamc11_que_content(uid);

UPDATE lams_tool SET tool_version='20140512' WHERE tool_signature='lamc11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;