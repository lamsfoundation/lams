SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files from patch20230303.sql to patch20230507.sql
-- It should upgrade this tool to version 4.7

-- LDEV-5369 Add alternative rubrics learner view
ALTER TABLE tl_laprev11_peerreview ADD COLUMN rubrics_view TINYINT UNSIGNED NOT NULL DEFAULT 1;

-- LDEV-5370 Add half point rubrics columns
ALTER TABLE tl_laprev11_peerreview ADD COLUMN rubrics_in_between_columns TINYINT UNSIGNED NOT NULL DEFAULT 0;

-- LDEV-5370 Add "students must rate all peers in rubrics" option
ALTER TABLE tl_laprev11_peerreview ADD COLUMN rubrics_require_ratings TINYINT UNSIGNED NOT NULL DEFAULT 0;

-- LDEV-5394 Mark which group has been sent out emails to
ALTER TABLE tl_laprev11_session ADD COLUMN emails_sent TINYINT UNSIGNED NOT NULL DEFAULT 0;

SET FOREIGN_KEY_CHECKS=1;