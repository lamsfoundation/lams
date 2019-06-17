-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4828 Add tables for QB collections
CREATE TABLE lams_qb_collection (`uid` BIGINT AUTO_INCREMENT, 
							     `name` VARCHAR(255),
							     `user_id` BIGINT,
							     `personal` TINYINT(1) NOT NULL DEFAULT 0,
							     PRIMARY KEY (uid),
							     INDEX (personal),
							     CONSTRAINT FK_lams_qb_collection_1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
							    );
							    
CREATE TABLE lams_qb_collection_question (`collection_uid`  BIGINT NOT NULL,
									      `qb_question_uid` BIGINT NOT NULL,
									   	  CONSTRAINT FK_lams_qb_collection_question_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid)
											ON DELETE CASCADE ON UPDATE CASCADE,
									   	  CONSTRAINT FK_lams_qb_collection_question_2 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid)
											ON DELETE CASCADE ON UPDATE CASCADE
									  );
							   				    
							    
CREATE TABLE lams_qb_collection_organisation (`collection_uid`  BIGINT NOT NULL,
									  		  `organisation_id` BIGINT NOT NULL,
									  		  CONSTRAINT FK_lams_qb_collection_share_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid)
												ON DELETE CASCADE ON UPDATE CASCADE,
									   		  CONSTRAINT FK_lams_qb_collection_share_2 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id)
												ON DELETE CASCADE ON UPDATE CASCADE
									  		  );

INSERT INTO lams_qb_collection VALUES (1, 'Public questions', NULL, false);

INSERT INTO lams_qb_collection_question
	SELECT 1, uid FROM lams_qb_question;
									  		  
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;