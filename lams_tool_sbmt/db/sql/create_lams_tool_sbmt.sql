CREATE TABLE tl_lasbmt11_content (
       content_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , title VARCHAR(64) NOT NULL
     , instructions VARCHAR(64) NOT NULL
     , PRIMARY KEY (content_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_submission_details (
       submission_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , filePath VARCHAR(250) NOT NULL
     , fileDescription VARCHAR(250) NOT NULL
     , date_of_submission DATETIME NOT NULL
     , uuid BIGINT(20) NOT NULL
     , version_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL   
     , PRIMARY KEY (submission_id)
     , INDEX (content_id)
     , CONSTRAINT FK_tl_lasbmt11_submission_details_1 FOREIGN KEY (content_id)
                  REFERENCES tl_lasbmt11_content (content_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_report (
       report_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , submission_id BIGINT(20) NOT NULL
     , comments VARCHAR(250)
     , marks BIGINT(20)
     , date_marks_released DATETIME
	 , UNIQUE unique_submission_report (submission_id)
     , PRIMARY KEY (report_id)
     , INDEX (submission_id)
     , CONSTRAINT FK_tl_lasbmt11_report_1 FOREIGN KEY (submission_id)
                  REFERENCES tl_lasbmt11_submission_details (submission_id)
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


