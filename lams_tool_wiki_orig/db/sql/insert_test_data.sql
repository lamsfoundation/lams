-- $Id$

INSERT INTO tl_lawiki10_session ( uid,
								wiki_session_id,
								wiki_content_uid,
								session_start_date,
								session_end_date,
								session_status)
VALUES (null,
		'455',
		'1',
		now(),
		now(),
		'INCOMPLETE');
		
INSERT INTO tl_lawiki10_user ( 	uid,
								user_id,
								wiki_session_uid,
								username,
								fullname,
								user_status)
VALUES (null,
		'555',
		LAST_INSERT_ID(),
		'test',
		'test',
		'NOT_ATTEMPTED');
