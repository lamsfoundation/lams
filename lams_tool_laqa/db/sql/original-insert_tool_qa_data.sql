
-- test data for content table
-- test data for content table
INSERT INTO tl_laqa11_content (qa_content_id, 
								define_later,
								questions_sequenced, 
								username_visible,
								synch_in_monitor,
								force_offline,
								creation_date
								) 
VALUES (10,
		0,
		0,
		0,
		0,
		0,
		NOW());



-- test data for content questions table
INSERT INTO tl_laqa11_que_content (qa_que_content_id, 
									question, 
									display_order, 
									qa_content_id) VALUES (20,'What is the capital of Uruguay?',1,10);


INSERT INTO tl_laqa11_que_content (qa_que_content_id, 
									question, 
									display_order, 
									qa_content_id)  VALUES (21,'What is your favorite color?',2,10);


INSERT INTO tl_laqa11_que_content (qa_que_content_id, 
									question, 
									display_order, 
									qa_content_id)  VALUES (22,'Where were you born?',3,10);




--test data for session table
INSERT INTO tl_laqa11_session (qa_session_id, 
								session_start_date, 
								session_end_date,
								qa_content_id) 
VALUES (101,
       '2004-11-12T12:34:10',
       '2005-12-12T12:14:10',
       10);


INSERT INTO tl_laqa11_que_usr (que_usr_id, 
								username, 
								qa_session_id,
								qa_que_content_id,
								fullname) 
VALUES (500,'ozgurd',101, 20,'Ozgur Demirtas');


INSERT INTO tl_laqa11_que_usr (que_usr_id, 
								username, 
								qa_session_id,
								qa_que_content_id,
								fullname) 
VALUES (600,'admin',101, 20,'Mr Admin');


--test date for user responses table
INSERT INTO tl_laqa11_usr_resp (response_id, 
								answer, 
								attempt_time,
								que_usr_id,
								qa_que_content_id) 
VALUES (5000,'my name is michael','2004-11-12T12:34:10',500,20);


INSERT INTO tl_laqa11_usr_resp (response_id, 
								answer, 
								attempt_time,
								que_usr_id,
								qa_que_content_id) 
VALUES (5001,'my favorite color is blue','2004-12-12T12:34:10',500,21);


