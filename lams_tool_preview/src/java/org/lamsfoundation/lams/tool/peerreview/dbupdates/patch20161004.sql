-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--     LDEV-3767 Peer review: two new review methods. Allow for self review

ALTER TABLE tl_laprev11_peerreview ADD COLUMN self_review TINYINT(4) DEFAULT '0';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;