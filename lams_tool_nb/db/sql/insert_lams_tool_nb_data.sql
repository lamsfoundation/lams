
-- test data for noticeboard content table

INSERT INTO tl_lanb11_content ( uid,
								nb_content_id,
								title,
								content,
								online_instructions,
								offline_instructions,
								define_later,
								force_offline,
								creator_user_id,
								date_created,
								date_updated) 
VALUES (null,
		'2500',
		'Welcome',
		'Welcome to these activities',
		'Enter the online instructions here',
		'Enter the offline instructions here',
		0,
		0,
		'2300',
		now(),
		NULL);


INSERT INTO tl_lanb11_session ( uid,
								nb_session_id,
								nb_content_uid,
								session_start_date,
								session_end_date,
								session_status)
VALUES (null,
		'2400',
		LAST_INSERT_ID(),
		now(),
		now(),
		'NOT_ATTEMPTED');