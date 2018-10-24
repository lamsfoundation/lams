-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-4454 Add tables for Kumalive polls

CREATE TABLE lams_kumalive_poll (
	   poll_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , kumalive_id BIGINT(20) NOT NULL
     , name VARCHAR(250)
     , votes_released TINYINT(1) DEFAULT 0
     , voters_released TINYINT(1) DEFAULT 0
     , start_date DATETIME NOT NULL
     , finish_date DATETIME
     , PRIMARY KEY (poll_id)
     , CONSTRAINT FK_lams_kumalive_poll_1 FOREIGN KEY (kumalive_id)
                  REFERENCES lams_kumalive (kumalive_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_poll_answer (
	   answer_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , poll_id BIGINT(20) NOT NULL
     , order_id TINYINT NOT NULL
     , name VARCHAR(250)
     , PRIMARY KEY (answer_id)
     , CONSTRAINT FK_lams_kumalive_poll_answer_1 FOREIGN KEY (poll_id)
                  REFERENCES lams_kumalive_poll (poll_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_poll_vote (
	   answer_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , vote_date DATETIME
     , PRIMARY KEY (answer_id, user_id)
     , CONSTRAINT FK_lams_kumalive_poll_vote_1 FOREIGN KEY (answer_id)
                  REFERENCES lams_kumalive_poll_answer (answer_id) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_lams_kumalive_poll_vote_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
