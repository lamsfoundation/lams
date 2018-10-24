-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

--LDEV-3079 Use CKEditors as MCQ answers tags
ALTER TABLE tl_lamc11_options_content MODIFY COLUMN mc_que_option_text TEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
