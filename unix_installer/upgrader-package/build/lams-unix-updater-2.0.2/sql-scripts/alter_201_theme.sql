-- Script to be run for LAMS 2.0.1 release, on LAMS 2.0 tables.
-- Add a column to record where the images are for a custom theme.

ALTER TABLE lams_css_theme_ve
ADD COLUMN image_directory VARCHAR(100);

UPDATE lams_css_theme_ve 
SET image_directory = "css"
WHERE name = "defaultHTML";

-- Needed for one of the LI servers
-- UPDATE lams_css_theme_ve 
-- SET image_directory = "npsthemecss"
-- WHERE NAME = "npsthemeHTML";

COMMIT;
