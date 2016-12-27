-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--    LDEV-4161: enhancements. Differentiate between showing results left for user and results left by user

ALTER TABLE tl_laprev11_peerreview ADD COLUMN show_ratings_left_by_user TINYINT(4) DEFAULT '0';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;