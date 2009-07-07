
-- LDEV-2638 Adding completion time for each question
alter table tl_laasse10_question_result add column finish_date datetime default null;