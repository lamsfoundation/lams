-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3450 Implement peer review feature
CREATE TABLE lams_rating_criteria_type (
       rating_criteria_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (rating_criteria_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_rating_criteria (
       rating_criteria_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , title VARCHAR(255)
     , rating_criteria_type_id INT(11) NOT NULL DEFAULT 0
     , comments_enabled TINYINT(1) NOT NULL DEFAULT 0
     , comments_min_words_limit INT(11) DEFAULT 0
     , order_id INT(11) NOT NULL
     , tool_content_id BIGINT(20)
     , item_id BIGINT(20)
     , lesson_id BIGINT(20)
     , PRIMARY KEY (rating_criteria_id)
     , INDEX (rating_criteria_type_id)
     , CONSTRAINT FK_lams_rating_criteria_1 FOREIGN KEY (rating_criteria_type_id)
                  REFERENCES lams_rating_criteria_type (rating_criteria_type_id)
     , INDEX (tool_content_id)
     , CONSTRAINT FK_lams_rating_criteria_2 FOREIGN KEY (tool_content_id)
                  REFERENCES lams_tool_content (tool_content_id) ON DELETE NO ACTION ON UPDATE NO ACTION                  
     , INDEX (lesson_id)
     , CONSTRAINT FK_lams_rating_criteria_3 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE lams_rating (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating_criteria_id BIGINT(20) NOT NULL
     , item_id BIGINT(20)
     , user_id BIGINT(20) NOT NULL
     , rating FLOAT
     , PRIMARY KEY (uid)
     , INDEX (rating_criteria_id)
     , CONSTRAINT FK_lams_rating_1 FOREIGN KEY (rating_criteria_id)
                  REFERENCES lams_rating_criteria (rating_criteria_id) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (user_id)
     , CONSTRAINT FK_lams_rating_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_rating_comment (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating_criteria_id BIGINT(20) NOT NULL
     , item_id BIGINT(20)
     , user_id BIGINT(20) NOT NULL
     , comment text
     , PRIMARY KEY (uid)
     , INDEX (rating_criteria_id)
     , CONSTRAINT FK_lams_rating_comment_1 FOREIGN KEY (rating_criteria_id)
                  REFERENCES lams_rating_criteria (rating_criteria_id) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (user_id)
     , CONSTRAINT FK_lams_rating_comment_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

INSERT INTO lams_rating_criteria_type VALUES (1, 'TOOL_ACTIVITY');
INSERT INTO lams_rating_criteria_type VALUES (2, 'AUTHORED_ITEM');
INSERT INTO lams_rating_criteria_type VALUES (3, 'LEARNER_ITEM');
INSERT INTO lams_rating_criteria_type VALUES (4, 'LESSON');


-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
