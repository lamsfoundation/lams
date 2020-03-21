SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV- Introduce Favorite Lesson

CREATE TABLE `lams_favorite_lesson` (
  `favorite_lesson_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`favorite_lesson_id`),
  KEY `lesson_id` (`lesson_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_favorite_lesson_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_favorite_lesson_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;