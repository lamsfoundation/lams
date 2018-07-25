-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3292: Add missing indexes

CREATE INDEX tl_laqa11_content_qa_content_id ON tl_laqa11_content(qa_content_id);
CREATE INDEX tl_laqa11_session_qa_session_id ON tl_laqa11_session(qa_session_id);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;