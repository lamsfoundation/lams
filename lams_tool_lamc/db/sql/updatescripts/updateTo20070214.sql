-- Update the Multiple Choice tables from version 20061113 to 20070214
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_lamc11_que_usr 
CHANGE COLUMN username username VARCHAR(255),
CHANGE COLUMN fullname fullname VARCHAR(255);
