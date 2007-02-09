-- Update the Survey tables from version 20061102 to 20070208
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_lasurv11_answer 
CHANGE COLUMN answer_text answer_text text;
