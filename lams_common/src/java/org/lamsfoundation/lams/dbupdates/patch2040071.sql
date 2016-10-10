-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

--     LDEV-3767 Peer review: two new review methods
ALTER TABLE lams_rating_criteria ADD COLUMN rating_style BIGINT(20) NOT NULL DEFAULT 1;
ALTER TABLE lams_rating_criteria ADD COLUMN max_rating BIGINT(20) NOT NULL DEFAULT 5;
ALTER TABLE lams_rating_criteria ADD COLUMN minimum_rates INT(11) DEFAULT '0';
ALTER TABLE lams_rating_criteria ADD COLUMN maximum_rates INT(11) DEFAULT '0';
  
UPDATE lams_rating_criteria SET rating_style = 0 WHERE comments_enabled = 1;
UPDATE lams_rating_criteria SET title = "Comment" WHERE comments_enabled = 1 AND title is null;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
