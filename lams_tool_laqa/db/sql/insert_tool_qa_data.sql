-- $Id$ 


-- test data for content table
-- test data for content table
INSERT INTO tl_laqa11_content (qa_content_id, 
								creation_date
								) 
VALUES (10,
		NOW());



-- test data for content questions table
INSERT INTO tl_laqa11_que_content (qa_que_content_id, 
									question, 
									display_order, 
									qa_content_id) VALUES (20,'What is the name of this activity?',1,10);




