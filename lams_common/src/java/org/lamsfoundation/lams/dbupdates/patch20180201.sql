SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4507 Make sure that duplicate groups are not created

-- Update staff group order ID so it is not considered a duplicate of learners' group
UPDATE lams_grouping AS gi JOIN lams_group AS g ON gi.staff_group_id = g.group_id SET g.order_id = 2;

-- delete duplicate groups
CREATE TABLE lams_group_delete 
SELECT g3.group_id FROM 
(SELECT g2.group_id, COUNT(g2.grouping_id) AS c2 FROM 
  (SELECT g1.group_id, g1.order_id, g1.grouping_id, COUNT(ug1.user_id) AS c1 
   FROM lams_group AS g1 
   LEFT JOIN lams_user_group AS ug1 USING (group_id) 
   GROUP BY g1.group_id 
   ORDER BY c1 ASC) AS g2 
GROUP BY g2.grouping_id, g2.order_id HAVING c2 > 1 
) AS g3; 

DELETE s FROM lams_tool_session AS s JOIN lams_group_delete AS g USING (group_id); 
DELETE ug FROM lams_user_group AS ug JOIN lams_group_delete AS g USING (group_id); 
DELETE e FROM lams_branch_activity_entry AS e JOIN lams_group_delete AS g USING (group_id); 
DELETE g2 FROM lams_group AS g2 JOIN lams_group_delete AS g USING (group_id); 

DROP TABLE lams_group_delete;

-- run delete again so groups with other order IDs are removed too
CREATE TABLE lams_group_delete 
SELECT g3.group_id FROM 
(SELECT g2.group_id, COUNT(g2.grouping_id) AS c2 FROM 
  (SELECT g1.group_id, g1.order_id, g1.grouping_id, COUNT(ug1.user_id) AS c1 
   FROM lams_group AS g1 
   LEFT JOIN lams_user_group AS ug1 USING (group_id) 
   GROUP BY g1.group_id 
   ORDER BY c1 ASC) AS g2 
GROUP BY g2.grouping_id, g2.order_id HAVING c2 > 1 
) AS g3; 

DELETE s FROM lams_tool_session AS s JOIN lams_group_delete AS g USING (group_id); 
DELETE ug FROM lams_user_group AS ug JOIN lams_group_delete AS g USING (group_id); 
DELETE e FROM lams_branch_activity_entry AS e JOIN lams_group_delete AS g USING (group_id); 
DELETE g2 FROM lams_group AS g2 JOIN lams_group_delete AS g USING (group_id); 

DROP TABLE lams_group_delete;

-- finally add an index
ALTER TABLE lams_group ADD UNIQUE KEY UQ_lams_group_1 (grouping_id, order_id);
 
COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
