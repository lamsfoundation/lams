-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4438 Add unique constraint to prevent duplicate results with 'latest' flag being ON

--remove all duplicates that have identical pairs (assessment_uid, user_uid, latest) where latest=1 
DELETE a
FROM tl_laasse10_assessment_result as a, tl_laasse10_assessment_result as b
WHERE
          (a.assessment_uid   = b.assessment_uid OR a.assessment_uid IS NULL AND b.assessment_uid IS NULL)
      AND (a.user_uid = b.user_uid OR a.user_uid IS NULL AND b.user_uid IS NULL)
      AND (a.latest = 1 AND b.latest = 1)
      AND a.uid < b.uid;
      
--set up tl_laasse10_assessment_result table to properly handle constraint
UPDATE tl_laasse10_assessment_result SET latest=NULL WHERE latest='0';
ALTER TABLE tl_laasse10_assessment_result CHANGE COLUMN latest latest TINYINT(1) NULL DEFAULT NULL;

--add unique constraint
ALTER TABLE tl_laasse10_assessment_result ADD UNIQUE KEY UQ_tl_laasse10_assessment_result_5 (assessment_uid, user_uid, latest);

UPDATE lams_tool SET tool_version='20171011' WHERE tool_signature='laasse10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;