-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-4454 Add tables for Kumalive polls

CREATE TABLE lams_kumalive_poll (
	   poll_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , kumalive_id BIGINT(20) NOT NULL
     , name VARCHAR(250)
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

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
