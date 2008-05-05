-- Script to be run for LAMS 2.0.4 release, on LAMS 2.0.2 tables.
-- Removes existing duplicate rows if they exist
-- Adds a new unique index to the transition table as duplicates cause an error in authoring (LDEV-1229)

-- Removing duplicate rows
DELETE bad_rows.* 
FROM lams_learning_transition as bad_rows
   INNER JOIN (
      SELECT MIN(transition_id) AS min_id, to_activity_id, from_activity_id
      FROM lams_learning_transition
      GROUP BY  to_activity_id
      HAVING COUNT(*) > 1
   ) AS good_rows 
   	 ON good_rows.to_activity_id = bad_rows.to_activity_id
     AND good_rows.from_activity_id = bad_rows.from_activity_id
     AND good_rows.min_id <> bad_rows.transition_id;


-- Altering table
ALTER TABLE lams_learning_transition
ADD UNIQUE UQ_transition_activities (from_activity_id, to_activity_id);

COMMIT;
