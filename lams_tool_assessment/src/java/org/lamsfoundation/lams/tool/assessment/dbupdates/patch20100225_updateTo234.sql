-- LDEV-2516 Show wrong and right answers separately
alter table tl_laasse10_assessment add column allow_wrong_answers tinyint DEFAULT 0;
update tl_laasse10_assessment set allow_wrong_answers=1 where allow_right_wrong_answers=1;
alter table tl_laasse10_assessment change column allow_right_wrong_answers allow_right_answers tinyint;
