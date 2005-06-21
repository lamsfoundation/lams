USE lams;

DROP TABLE IF EXISTS lams.tool_laqa11_usr_resp;
DROP TABLE IF EXISTS lams.tool_laqa11_que_usr;
DROP TABLE IF EXISTS lams.tool_laqa11_que_content;
DROP TABLE IF EXISTS lams.tool_laqa11_session;
DROP TABLE IF EXISTS lams.tool_laqa11_content;


CREATE TABLE lams.tl_laqa11_content (
       qa_content_id BIGINT(20) NOT NULL
     , title VARCHAR(100) DEFAULT 'Questions and Answers'
     , instructions VARCHAR(255) DEFAULT 'Please, take a minute to answer the following questions.'
     , creation_date DATE 
     , update_date DATE
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , report_title VARCHAR(100) DEFAULT 'Report'
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , synch_in_monitor TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions VARCHAR(255) DEFAULT 'Please, take a minute to fill in offline instructions.'
     , online_instructions VARCHAR(255) DEFAULT 'Please, take a minute to fill in online instructions.'
     , PRIMARY KEY (qa_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_session (
       qa_session_id BIGINT(20) NOT NULL
     , session_start_date DATE
     , session_end_date DATE
     , session_status VARCHAR(100)
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (qa_session_id)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tool_lasr11_qa_session_1 FOREIGN KEY (qa_content_id)
                  REFERENCES lams.tl_laqa11_content (qa_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_que_content (
       qa_que_content_id BIGINT(20) NOT NULL
     , question VARCHAR(255)
     , display_order INT(5)
     , qa_content_id BIGINT(20)
     , PRIMARY KEY (qa_que_content_id)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tool_lasr11_qa_que_content_1 FOREIGN KEY (qa_content_id)
                  REFERENCES lams.tl_laqa11_content (qa_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_que_usr (
       que_usr_id BIGINT(20) NOT NULL
     , username VARCHAR(100)
     , qa_session_id BIGINT(20) NOT NULL
     , qa_que_content_id BIGINT(20) NOT NULL
     , fullname VARCHAR(100)
     , PRIMARY KEY (que_usr_id)
     , INDEX (qa_session_id)
     , CONSTRAINT FK_tool_lasr11_qa_que_usr_1 FOREIGN KEY (qa_session_id)
                  REFERENCES lams.tl_laqa11_session (qa_session_id)
     , INDEX (qa_que_content_id)
     , CONSTRAINT FK_tool_lasr11_qa_que_usr_2 FOREIGN KEY (qa_que_content_id)
                  REFERENCES lams.tl_laqa11_que_content (qa_que_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_usr_resp (
       response_id BIGINT(20) NOT NULL
     , answer VARCHAR(255)
     , attempt_time DATE
     , que_usr_id BIGINT(20)
     , qa_que_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (response_id)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tool_lasr11_qa_usr_resp_1 FOREIGN KEY (que_usr_id)
                  REFERENCES lams.tl_laqa11_que_usr (que_usr_id)
     , INDEX (qa_que_content_id)
     , CONSTRAINT FK_tool_lasr11_qa_usr_resp_3 FOREIGN KEY (qa_que_content_id)
                  REFERENCES lams.tl_laqa11_que_content (qa_que_content_id)
)TYPE=InnoDB;



INSERT INTO tl_laqa11_content (qa_content_id, 
								define_later, 
								username_visible,
								synch_in_monitor) 
VALUES (10,
		1,
		1,
		1);



-- test data for content questions table
INSERT INTO tl_laqa11_que_content (qa_que_content_id, 
									question, 
									display_order, 
									qa_content_id) VALUES (20,'What is your name?',1,10);


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





--test data for user table
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






