-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

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
									      `qb_question_id` INT NOT NULL,
									   	  CONSTRAINT FK_lams_qb_collection_question_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid)
											ON DELETE CASCADE ON UPDATE CASCADE,
									   	  CONSTRAINT FK_lams_qb_collection_question_2 FOREIGN KEY (qb_question_id) REFERENCES lams_qb_question (question_id)
											ON DELETE CASCADE ON UPDATE CASCADE
									  );
							   				    
							    
CREATE TABLE lams_qb_collection_organisation (`collection_uid`  BIGINT NOT NULL,
									  		  `organisation_id` BIGINT NOT NULL,
									  		  CONSTRAINT FK_lams_qb_collection_share_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid)
												ON DELETE CASCADE ON UPDATE CASCADE,
									   		  CONSTRAINT FK_lams_qb_collection_share_2 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id)
												ON DELETE CASCADE ON UPDATE CASCADE
									  		  );
									  		  
-- add questions to public collection
INSERT INTO lams_qb_collection VALUES (1, 'Public questions', NULL, false);

INSERT INTO lams_qb_collection_question
	SELECT 1, question_id FROM lams_qb_question
	WHERE owner_id IS NULL;
	
-- add questions to private collections
INSERT INTO lams_qb_collection
	SELECT NULL, 'My questions', owner_id, true
	FROM (SELECT DISTINCT owner_id FROM lams_qb_question WHERE owner_id IS NOT NULL) AS qb;

INSERT INTO lams_qb_collection_question
	SELECT c.uid, qb.question_id 
		FROM lams_qb_question AS qb
		JOIN lams_qb_collection AS c ON qb.owner_id = c.user_id
	WHERE qb.owner_id IS NOT NULL;
	
ALTER TABLE lams_qb_question DROP COLUMN owner_id;


INSERT INTO lams_configuration VALUES
('QbCollectionsTransferEnable', 'true', 'config.qb.collections.transfer.enable', 'config.header.qb', 'BOOLEAN', 1);


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
