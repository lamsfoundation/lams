-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- LDEV-2949
UPDATE lams_presence_chat_msgs SET room_name=SUBSTRING_INDEX(room_name, '-', 1);
UPDATE lams_presence_chat_msgs SET room_name=SUBSTRING_INDEX(room_name, '_', 1);

ALTER TABLE lams_presence_chat_msgs CHANGE COLUMN room_name lesson_id BIGINT(20) NOT NULL,
									ADD CONSTRAINT FK_lams_presence_chat_msgs_lesson FOREIGN KEY (lesson_id)
                  						REFERENCES lams_lesson (lesson_id)
                  						ON UPDATE CASCADE ON DELETE CASCADE,
                  					ADD INDEX idx_lams_presence_chat_msgs_from (from_user),
                  					ADD INDEX idx_lams_presence_chat_msgs_to   (to_user);
				 
CREATE TABLE lams_presence_user (
	nickname VARCHAR(255) NOT NULL,
	lesson_id BIGINT(20) NOT NULL,
	last_presence DATETIME,
	PRIMARY KEY (nickname, lesson_id),
    CONSTRAINT FK_lams_presence_user_lesson FOREIGN KEY (lesson_id)
        REFERENCES lams_lesson (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB;

DELETE FROM lams_configuration WHERE header_name='config.header.chat';

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
