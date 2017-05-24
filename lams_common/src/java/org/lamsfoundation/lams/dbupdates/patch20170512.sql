-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

--     LDEV-2999 Include a rating option for viewed resources
ALTER TABLE lams_rating ADD COLUMN tool_session_id BIGINT(20) DEFAULT NULL,
	ADD CONSTRAINT FK_lams_rating_3  FOREIGN KEY (tool_session_id)
  	REFERENCES lams_tool_session (tool_session_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
  
ALTER TABLE lams_rating_comment ADD COLUMN tool_session_id BIGINT(20) DEFAULT NULL,
	ADD CONSTRAINT FK_lams_rating_comment_3  FOREIGN KEY (tool_session_id)
  	REFERENCES lams_tool_session (tool_session_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Update existing Image Gallery Ratings
DROP TABLE IF EXISTS tmp_imagegallery_rating_sessionid;
CREATE TEMPORARY TABLE tmp_imagegallery_rating_sessionid AS
SELECT r.uid rating_uid, igs.session_id session_id
FROM lams_rating r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laimag10_imagegallery ig ON rc.tool_content_id = ig.content_id
JOIN tl_laimag10_session igs ON ig.uid = igs.imageGallery_uid 
JOIN tl_laimag10_user igu ON r.user_id = igu.user_id AND igs.uid = igu.session_uid;

UPDATE lams_rating r
JOIN tmp_imagegallery_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_imagegallery_rating_sessionid;
CREATE TEMPORARY TABLE tmp_imagegallery_rating_sessionid AS
SELECT r.uid rating_uid, igs.session_id session_id
FROM lams_rating_comment r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laimag10_imagegallery ig ON rc.tool_content_id = ig.content_id
JOIN tl_laimag10_session igs ON ig.uid = igs.imageGallery_uid 
JOIN tl_laimag10_user igu ON r.user_id = igu.user_id AND igs.uid = igu.session_uid;

UPDATE lams_rating_comment r
JOIN tmp_imagegallery_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

-- Update existing Peer Review done by the Peer Review tool

-- Update existing QA
DROP TABLE IF EXISTS tmp_qa_rating_sessionid;
CREATE TEMPORARY TABLE tmp_qa_rating_sessionid AS
SELECT r.uid rating_uid, qas.qa_session_id session_id
FROM lams_rating r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laqa11_content qa ON qa.qa_content_id = rc.tool_content_id
JOIN tl_laqa11_session qas ON qas.qa_content_id = qa.uid
JOIN tl_laqa11_que_usr qau ON r.user_id = qau.que_usr_id AND qas.uid = qau.qa_session_id;

UPDATE lams_rating r
JOIN tmp_qa_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_qa_rating_sessionid;
CREATE TEMPORARY TABLE tmp_qa_rating_sessionid AS
SELECT r.uid rating_uid, qas.qa_session_id session_id
FROM lams_rating_comment r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laqa11_content qa ON qa.qa_content_id = rc.tool_content_id
JOIN tl_laqa11_session qas ON qas.qa_content_id = qa.uid
JOIN tl_laqa11_que_usr qau ON r.user_id = qau.que_usr_id AND qas.uid = qau.qa_session_id;

UPDATE lams_rating_comment r
JOIN tmp_qa_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_qa_rating_sessionid;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
