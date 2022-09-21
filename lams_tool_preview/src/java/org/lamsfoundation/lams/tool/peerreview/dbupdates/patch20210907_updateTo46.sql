SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20210907.sql
-- It should upgrade this tool to version 4.6

-- LDEV-5232 Remove notify students option from authoring
ALTER TABLE tl_laprev11_peerreview DROP COLUMN notify_users_of_results;
				  
SET FOREIGN_KEY_CHECKS=1;