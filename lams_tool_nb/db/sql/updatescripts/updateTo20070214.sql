-- Update the Noticeboard tables to 20070214
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_lanb11_user 
CHANGE COLUMN username username VARCHAR(255),
CHANGE COLUMN fullname fullname VARCHAR(255);
