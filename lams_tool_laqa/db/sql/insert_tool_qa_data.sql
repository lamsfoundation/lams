SET FOREIGN_KEY_CHECKS=0;
-- test data for content table
-- test data for content table
INSERT INTO tl_laqa11_content (qa_content_id, 
								creation_date,
								questions_sequenced
								) 
VALUES (10,
		NOW(),1);



-- test data for content questions table
INSERT INTO tl_laqa11_que_content (uid, 
									question, 
									display_order, 
									qa_content_id) VALUES (20,'What is the name of this activity?',1,1);


SET FOREIGN_KEY_CHECKS=1;

