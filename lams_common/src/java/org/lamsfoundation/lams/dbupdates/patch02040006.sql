SET AUTOCOMMIT = 0;

-- SIF-10 Addding a release date to the lesson table so we can 
-- track release events
alter table lams_lesson add column release_date DATETIME;
alter table lams_lesson add index idx_release_date (release_date);

-- 	LDEV-2564 Option in "Edit configuration settings" for not displaying the "All My Lesson" option in My Profile tab
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ShowAllMyLessonLink','true', 'config.show.all.my.lesson.link', 'config.header.features', 'BOOLEAN', 1);

COMMIT;
SET AUTOCOMMIT = 1;