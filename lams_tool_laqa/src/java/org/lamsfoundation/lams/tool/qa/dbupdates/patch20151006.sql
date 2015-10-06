-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--  LDEV-3568 Move old rating data into the new DB model
INSERT INTO lams_rating_criteria (title, rating_criteria_type_id, comments_enabled, order_id, tool_content_id)
SELECT "", 3, false, 1, qa_content_id
FROM tl_laqa11_content WHERE allow_rate_answers=TRUE;

INSERT INTO lams_rating (rating_criteria_id, item_id, user_id, rating)
SELECT criteria.rating_criteria_id, rating.response_id, rating.user_id, rating.rating
FROM tl_laqa11_response_rating rating, tl_laqa11_usr_resp response, tl_laqa11_que_content question, tl_laqa11_content qa, lams_rating_criteria criteria 
	WHERE response.response_id=rating.response_id AND response.qa_que_content_id=question.uid AND qa.uid=question.qa_content_id AND qa.qa_content_id=criteria.tool_content_id AND criteria.order_id=1;

DROP TABLE tl_laqa11_response_rating;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;