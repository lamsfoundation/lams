
-- test data for noticeboard content table

INSERT INTO tl_lanb11_content (nb_content_id,
								title,
								content,
								online_instructions,
								offline_instructions,
								define_later,
								force_offline,
								creator_user_id,
								date_created,
								date_updated) 
VALUES ('2500',
		'Welcome',
		'Welcome to these activities',
		'Enter the online instructions here',
		'Enter the offline instructions here',
		0,
		0,
		'2300',
		now(),
		NULL);
		
INSERT INTO tl_lanb11_session (	nb_session_id,
								nb_content_id,
								session_start_date,
								session_end_date,
								session_status)
VALUES ('2400',
		'2500',
		now(),
		now(),
		'Inactive');


-- test data for noticeboard content table

INSERT INTO tl_lanb11_content (nb_content_id,
								title,
								content,
								online_instructions,
								offline_instructions,
								define_later,
								force_offline,
								creator_user_id,
								date_created,
								date_updated) 
VALUES ('5000',
		'Test Noticeboard',
		'Test Noticeboard: Welcome to these activities',
		'Please Enter the online instructions here',
		'Please Enter the offline instructions here',
		0,
		0,
		'2300',
		now(),
		NULL);
		
INSERT INTO tl_lanb11_session (	nb_session_id,
								nb_content_id,
								session_start_date,
								session_end_date,
								session_status)
VALUES ('6000',
		'5000',
		now(),
		now(),
		'Inactive');


