-- Update the Scribe tables to 20071113
-- This is for the LAMS 2.1 release.

ALTER TABLE tl_lascrb11_scribe
ADD COLUMN aggregated_reports bit default 0;