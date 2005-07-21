SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lasbmt11_content (
       content_id BIGINT(20) NOT NULL
     , title VARCHAR(64) NOT NULL
     , instructions VARCHAR(64) NOT NULL
     , defineLater TINYINT(1)
	 , runOffline TINYINT(1)
     , PRIMARY KEY (content_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_report (
       report_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , comments VARCHAR(250)
     , marks BIGINT(20)
     , date_marks_released DATETIME
     , PRIMARY KEY (report_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_session (
       session_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , status INT(11) NOT NULL
     , PRIMARY KEY (session_id)
     , INDEX (content_id)
     , CONSTRAINT FK_tl_lasbmt11_session_1 FOREIGN KEY (content_id)
                  REFERENCES tl_lasbmt11_content (content_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_submission_details (
       submission_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , session_id BIGINT(20)
     , report_id BIGINT(20)
     , filePath VARCHAR(250) NOT NULL
     , fileDescription VARCHAR(250) NOT NULL
     , date_of_submission DATETIME NOT NULL
     , uuid BIGINT(20) NOT NULL
     , version_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL   
     , PRIMARY KEY (submission_id)
     , INDEX (session_id)
     , CONSTRAINT FK_tl_lasbmt11_submission_details_1 FOREIGN KEY (session_id)
                  REFERENCES tl_lasbmt11_session (session_id)
     , INDEX (report_id)
     , CONSTRAINT FK_tl_lasbmt11_submission_details_2 FOREIGN KEY (report_id)
                  REFERENCES tl_lasbmt11_report (report_id)
)TYPE=InnoDB;

INSERT INTO `tl_lasbmt11_content` (content_id,title,instructions,defineLater,runOffline) values(1,"Java Submission","Submit your java programs",0,0);
INSERT INTO `tl_lasbmt11_session` (session_id,content_id,status) values(1,1,1);
SET FOREIGN_KEY_CHECKS=1;

