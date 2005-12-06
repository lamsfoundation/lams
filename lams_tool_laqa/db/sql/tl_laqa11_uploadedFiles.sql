CREATE TABLE lams.tl_laqa11_uploadedFiles (
       submissionId BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid VARCHAR(255) NOT NULL
     , isOnline_File TINYINT(5) NOT NULL
     , filename VARCHAR(20) NOT NULL
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (submissionId)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_que_content_1_1 FOREIGN KEY (qa_content_id)
                  REFERENCES lams.tl_laqa11_content (qa_content_id)
)TYPE=InnoDB;

