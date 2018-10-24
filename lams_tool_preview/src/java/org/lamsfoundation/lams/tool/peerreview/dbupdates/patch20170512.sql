-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

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

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
