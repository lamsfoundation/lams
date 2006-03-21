-- $Id$

INSERT INTO tl_lanb11_session ( uid,
								nb_session_id,
								nb_content_uid,
								session_start_date,
								session_end_date,
								session_status)
VALUES (null,
		'455',
		'1',
		now(),
		now(),
		'INCOMPLETE');
		
INSERT INTO tl_lanb11_user ( 	uid,
								user_id,
								nb_session_uid,
								username,
								fullname,
								user_status)
VALUES (null,
		'555',
		LAST_INSERT_ID(),
		'test',
		'test',
		'NOT_ATTEMPTED');