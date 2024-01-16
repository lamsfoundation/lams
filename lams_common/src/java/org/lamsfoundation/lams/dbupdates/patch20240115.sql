-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5428 Remove lesson presence and chat
ALTER TABLE lams_ext_server_org_map DROP COLUMN learner_im_avail,
						 	   		DROP COLUMN learner_presence_avail;
ALTER TABLE lams_lesson DROP COLUMN learner_im_avail,
						DROP COLUMN learner_presence_avail;

DROP TABLE lams_presence_user;
DROP TABLE lams_presence_chat_msgs;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
