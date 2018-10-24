-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--  LDEV-3627 Improve Assessment tool's monitor paging
ALTER TABLE tl_laasse10_assessment_result ADD COLUMN latest TINYINT(1) DEFAULT 0;

UPDATE tl_laasse10_assessment_result result
  JOIN
    ( SELECT user_uid, MAX(start_date) AS max_date
      FROM tl_laasse10_assessment_result
      GROUP BY user_uid
    ) AS c
    ON result.user_uid = c.user_uid
    AND result.start_date = c.max_date
SET 
    result.latest = 1;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;