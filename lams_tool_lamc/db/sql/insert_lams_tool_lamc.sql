


INSERT INTO tl_lamc11_content (mc_content_id, 
                               creation_date
) 
VALUES (10,
	NOW());




INSERT INTO tl_lamc11_session (mc_session_id, 
								mc_content_id) VALUES (20,LAST_INSERT_ID());





INSERT INTO tl_lamc11_que_content (mc_que_content_id, 
									question, 
									display_order, 
									mc_content_id) VALUES (20,'What is the name of this activity?',1,1);



INSERT INTO tl_lamc11_que_usr (que_usr_id, 
									username, 
									fullname, 
									mc_session_id) VALUES (40,'ozgurd','Ozgur Demirtas',1);



INSERT INTO tl_lamc11_options_content (mc_que_option_id, 
									correct_option, 
									mc_que_content_id, 
									mc_que_option_text) VALUES (100,0,1,'survey');


INSERT INTO tl_lamc11_options_content (mc_que_option_id, 
									correct_option, 
									mc_que_content_id, 
									mc_que_option_text) VALUES (101,0,1,'q/a');

INSERT INTO tl_lamc11_options_content (mc_que_option_id, 
									correct_option, 
									mc_que_content_id, 
									mc_que_option_text) VALUES (102,1,1,'mc');
									
									
									
INSERT INTO tl_lamc11_usr_attempt (attempt_id, 
									que_usr_id, 
									mc_que_content_id, 
									mc_que_option_id)
									 VALUES (200, 1, 1, 1);
									

INSERT INTO tl_lamc11_usr_attempt (attempt_id, 
									que_usr_id, 
									mc_que_content_id, 
									mc_que_option_id)
									 VALUES (201, 1, 1, 2);


INSERT INTO tl_lamc11_usr_attempt (attempt_id, 
									que_usr_id, 
									mc_que_content_id, 
									mc_que_option_id)
									 VALUES (202, 1, 1, 3);





