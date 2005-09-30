CREATE TABLE lams.tl_lamc11_usr_attempt (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , attempt_id BIGINT(20) NOT NULL
     , que_usr_id BIGINT(20) NOT NULL
     , qa_que_content_id BIGINT(20) NOT NULL
     , qa_que_option_id CHAR(10) NOT NULL
     , attempt_time DATETIME
     , time_zone VARCHAR(255)
     , PRIMARY KEY (uid)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_1 FOREIGN KEY (que_usr_id)
                  REFERENCES lams.tl_lamc11_que_usr (uid)
     , INDEX (qa_que_content_id)
     , CONSTRAINT FK_tl_laqa11_usr_attempt_2 FOREIGN KEY (qa_que_content_id)
                  REFERENCES lams.tl_lamc11_que_content (uid)
     , INDEX (qa_que_option_id)
     , CONSTRAINT FK_tl_laqa11_usr_attempt_3 FOREIGN KEY (qa_que_option_id)
                  REFERENCES lams.tl_lamc11_options_content (uid)
)TYPE=InnoDB;

