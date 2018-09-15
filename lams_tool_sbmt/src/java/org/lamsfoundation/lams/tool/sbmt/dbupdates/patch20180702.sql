-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4611 Release marks is now a state
ALTER TABLE tl_lasbmt11_session ADD COLUMN marks_released TINYINT(1) DEFAULT 0;

UPDATE tl_lasbmt11_session as sessions, tl_lasbmt11_report as report, tl_lasbmt11_submission_details as details
		SET sessions.marks_released = 1 
		WHERE report.report_id = details.submission_id AND details.session_id=sessions.session_id AND report.date_marks_released IS NOT NULL;

ALTER TABLE tl_lasbmt11_report DROP COLUMN date_marks_released;


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;