-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

ALTER TABLE tl_lachat11_user DROP COLUMN jabber_id,
							 CHANGE COLUMN jabber_nickname nickname varchar(255),
							 ADD COLUMN last_presence datetime;
						
ALTER TABLE tl_lachat11_session DROP COLUMN jabber_room,
								DROP COLUMN room_created;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;