-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2806 Fill Activities with missing transition data
UPDATE lams_learning_activity AS act
 LEFT JOIN  lams_learning_transition AS trans_to
  ON trans_to.to_activity_id = act.activity_id
  AND trans_to.transition_type = 0
 LEFT JOIN lams_learning_transition AS trans_from
  ON trans_from.from_activity_id = act.activity_id
  AND trans_from.transition_type = 0
 SET act.transition_from_id = trans_from.transition_id,
     act.transition_to_id = trans_to.transition_id;

     
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;