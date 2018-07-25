-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4459 Add email notification archive
CREATE TABLE lams_email_notification_archive (
	uid BIGINT(20) NOT NULL AUTO_INCREMENT,
	organisation_id BIGINT(20),
	lesson_id BIGINT(20),
	search_type TINYINT NOT NULL,
	sent_on DATETIME NOT NULL,
	body TEXT,
	PRIMARY KEY (uid),
	CONSTRAINT FK_lams_email_notification_archive_1 FOREIGN KEY (organisation_id)
    	REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_lams_email_notification_archive_2 FOREIGN KEY (lesson_id)
    	REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_email_notification_recipient_archive (
	email_notification_uid BIGINT(20) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	CONSTRAINT FK_lams_email_notification_recipient_archive_1 FOREIGN KEY (email_notification_uid)
    	REFERENCES lams_email_notification_archive (uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_lams_email_notification_recipient_archive_2 FOREIGN KEY (user_id)
    	REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
						
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;