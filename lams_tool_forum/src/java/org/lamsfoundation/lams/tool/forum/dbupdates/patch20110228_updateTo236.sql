-- SQL statements to update to LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;
-- Put all sql statements below here

ALTER TABLE tl_lafrum11_forum ADD COLUMN allow_rate_messages smallint not null default 0;

-- LDEV-2651 Forum option to rate other student's postings
CREATE TABLE tl_lafrum11_message_rating (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating float
     , user_id BIGINT(20) NOT NULL
     , message_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (user_id)
     , CONSTRAINT FK_tl_lafrum11_message_rating_1 FOREIGN KEY (user_id)
                  REFERENCES tl_lafrum11_forum_user (uid)
     , INDEX (message_id)
     , CONSTRAINT FK_tl_lafrum11_message_rating_2 FOREIGN KEY (message_id)
                  REFERENCES tl_lafrum11_message (uid)
)ENGINE=InnoDB;


UPDATE lams_tool SET tool_version = "20110228" WHERE tool_signature = "lafrum11";

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
 SET FOREIGN_KEY_CHECKS = 1;
