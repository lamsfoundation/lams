SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
-- LDEV-4508 Add Kumalive table to track learners raising/lowering hands
-- and possibly other activity in the future

CREATE TABLE lams_kumalive_log (
	   log_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , kumalive_id BIGINT(20) NOT NULL
     , user_id BIGINT(20)
     , log_date DATETIME NOT NULL
     , log_type TINYINT
     , PRIMARY KEY (log_id)
     , CONSTRAINT FK_lams_kumalive_log_1 FOREIGN KEY (kumalive_id)
                  REFERENCES lams_kumalive (kumalive_id) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_lams_kumalive_log_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
