
-- LDEV-2478 Adding Option to allow the student to have a rich text editor
alter table tl_laasse10_assessment_question add column allow_rich_editor tinyint DEFAULT 0;