CREATE TABLE lams.tl_lamc11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , mc_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_status VARCHAR(100)
     , mc_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (mc_content_id)
                  REFERENCES lams.tl_lamc11_content (uid)
)TYPE=InnoDB;

