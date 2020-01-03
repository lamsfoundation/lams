SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4918 Collapsible subcourses

CREATE TABLE `lams_user_organisation_collapsed` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `collapsed` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`uid`),
  KEY `organisation_id` (`organisation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Add "Enable collapsing subcourses" to system settings and organisation
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('EnableCollapsingSubcourses','false', 'config.enable.collapsing.subcourses', 'config.header.features', 'BOOLEAN', 0);




COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;

