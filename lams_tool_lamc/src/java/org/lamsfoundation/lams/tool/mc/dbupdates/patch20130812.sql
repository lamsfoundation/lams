-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

--LDEV-3085 Autosave feature for MCQ
DELETE FROM tl_lamc11_usr_attempt WHERE uid NOT IN ( --remove all attempts except the last one 
  SELECT uid
  FROM (
    SELECT uid
    FROM tl_lamc11_usr_attempt
    ORDER BY attemptOrder DESC
    LIMIT 1
  ) foo
);
ALTER TABLE tl_lamc11_usr_attempt DROP COLUMN attemptOrder;

ALTER TABLE tl_lamc11_que_usr DROP COLUMN last_attempt_order;
ALTER TABLE tl_lamc11_que_usr ADD COLUMN number_attempts INTEGER DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
