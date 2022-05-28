-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5316 Change content folder ID to default one for questions which have images referring the default content folder
-- and which do not have images which refer a new content folder
								   
UPDATE lams_qb_question AS q LEFT JOIN lams_qb_option AS o ON q.uid = o.qb_question_uid
	SET q.content_folder_id = '12345678-9012-abcd-0123-4567890abcde'
	WHERE (q.description LIKE '%secure/12/34/56/78/90/12/%' OR o.name LIKE '%secure/12/34/56/78/90/12/%')
	AND (q.content_folder_id IS NULL OR (q.description NOT LIKE CONCAT('%secure/', SUBSTRING(q.content_folder_id, 0, 2)) 
		AND (o.uid IS NULL OR o.name NOT LIKE CONCAT('%secure/', SUBSTRING(q.content_folder_id, 0, 2)))));

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
