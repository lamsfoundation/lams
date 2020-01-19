-- SQL statements to update to LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

ALTER TABLE tl_laqa11_content ADD COLUMN allow_rate_answers TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-2649 Q&A option to rate other student's responses
CREATE TABLE tl_laqa11_response_rating (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating float
     , user_id BIGINT(20) NOT NULL
     , response_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (user_id)
     , CONSTRAINT FK_tl_laqa11_response_rating_1 FOREIGN KEY (user_id)
                  REFERENCES tl_laqa11_que_usr (uid)
     , INDEX (response_id)
     , CONSTRAINT FK_tl_laqa11_response_rating_2 FOREIGN KEY (response_id)
                  REFERENCES tl_laqa11_usr_resp (response_id)
)ENGINE=InnoDB;

-- 	LDEV-2653 autosave feature
ALTER TABLE tl_laqa11_usr_resp DROP COLUMN hidden;

-- LDEV-2657 Date and time restriction 
ALTER TABLE tl_laqa11_content ADD COLUMN submission_deadline DATETIME DEFAULT null;

UPDATE lams_tool SET tool_version = "20110217" WHERE tool_signature = "laqa11";

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;