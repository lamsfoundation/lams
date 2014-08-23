-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--  LDEV-3295 Allow questions having user responses with ratings be deleted
ALTER TABLE tl_laqa11_response_rating DROP FOREIGN KEY FK_tl_laqa11_response_rating_2 ;
ALTER TABLE tl_laqa11_response_rating ADD CONSTRAINT FK_tl_laqa11_response_rating_2 FOREIGN KEY (response_id ) REFERENCES tl_laqa11_usr_resp (response_id) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;