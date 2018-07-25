-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

--LDEV-3085 Autosave feature for MCQ
CREATE TEMPORARY TABLE temp_select AS SELECT attempt.uid
	FROM tl_lamc11_usr_attempt attempt
	INNER JOIN(
	    SELECT que_usr_id, mc_que_content_id, max(attemptOrder) attemptOrder
	    FROM tl_lamc11_usr_attempt
	    GROUP BY que_usr_id, mc_que_content_id
	) ss ON attempt.que_usr_id = ss.que_usr_id AND attempt.mc_que_content_id = ss.mc_que_content_id AND attempt.attemptOrder = ss.attemptOrder;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lamc11_usr_attempt WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lamc11_usr_attempt DROP COLUMN attemptOrder;
ALTER TABLE tl_lamc11_que_usr DROP COLUMN last_attempt_order;
ALTER TABLE tl_lamc11_que_usr ADD COLUMN number_attempts INTEGER DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
