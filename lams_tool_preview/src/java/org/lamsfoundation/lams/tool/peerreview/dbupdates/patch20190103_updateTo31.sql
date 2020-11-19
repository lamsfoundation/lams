SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170215.sql to patch20200607.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4233: Ability to remove users from Peer Review tool
ALTER TABLE tl_laprev11_user ADD COLUMN hidden TINYINT(1) DEFAULT '0';




--     LDEV-2999 Include a rating option for viewed resources. The one in 
-- the core needs to run first but we cannot force that.
-- Update existing Peer Review
DROP TABLE IF EXISTS tmp_peerreview_rating_sessionid;
CREATE TEMPORARY TABLE tmp_peerreview_rating_sessionid AS
SELECT r.uid rating_uid, prs.session_id session_id
FROM lams_rating r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laprev11_peerreview pr ON rc.tool_content_id = pr.content_id
JOIN tl_laprev11_session prs ON pr.uid = prs.peerreview_uid
JOIN tl_laprev11_user pru ON r.user_id = pru.user_id AND prs.uid = pru.session_uid;

UPDATE lams_rating r
JOIN tmp_peerreview_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_peerreview_rating_sessionid;
CREATE TEMPORARY TABLE tmp_peerreview_rating_sessionid AS
SELECT r.uid rating_uid, prs.session_id session_id
FROM lams_rating_comment r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laprev11_peerreview pr ON rc.tool_content_id = pr.content_id
JOIN tl_laprev11_session prs ON pr.uid = prs.peerreview_uid
JOIN tl_laprev11_user pru ON r.user_id = pru.user_id AND prs.uid = pru.session_uid;

UPDATE lams_rating_comment r
JOIN tmp_peerreview_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_peerreview_rating_sessionid;




-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_laprev11_peerreview MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_laprev11_peerreview MODIFY reflect_instructions MEDIUMTEXT;



--LDEV-5035 Add tolerance to SAP and SAPA factors in Peer Review
ALTER TABLE tl_laprev11_peerreview ADD COLUMN tolerance TINYINT UNSIGNED NOT NULL DEFAULT 0;



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='laprev11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;