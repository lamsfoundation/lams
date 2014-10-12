-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

--LDEV-3138 Prevent erroneously creating 2 identical user results for 1 question in DB
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid
FROM tl_lamc11_usr_attempt GROUP BY que_usr_id, mc_que_content_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lamc11_usr_attempt WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lamc11_usr_attempt ADD UNIQUE INDEX attempt_unique_index (que_usr_id, mc_que_content_id);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
