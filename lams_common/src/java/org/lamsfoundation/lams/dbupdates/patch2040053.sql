-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3631 	Simple Commenting Widget
CREATE TABLE IF NOT EXISTS lams_comment_session (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `external_id` bigint(20) DEFAULT NULL,
  `external_id_type` int(1) DEFAULT NULL,
  `external_signature` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `comment_ext_sig_user` (`external_id`,`external_id_type`,`external_signature`)
);

CREATE TABLE IF NOT EXISTS lams_comment (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) NOT NULL,
  `body` text DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `last_reply_date` datetime DEFAULT NULL,
  `reply_number` int(11) DEFAULT NULL,
  `hide_flag` smallint(6) DEFAULT NULL,
  `parent_uid` bigint(20) DEFAULT NULL,
  `root_comment_uid` bigint(20) DEFAULT NULL,
  `comment_level` smallint(6) DEFAULT NULL,
  `thread_comment_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_comment_session` (`session_id`),
  KEY `FK_comment_create` (`create_by`),
  KEY `FK_comment_modify` (`update_by`),
  KEY `FK_comment_parent` (`parent_uid`),
  KEY `FK_comment_root` (`root_comment_uid`),
  KEY `FK_comment_thread` (`thread_comment_uid`),
  CONSTRAINT `FK_comment_session` FOREIGN KEY (`session_id`) REFERENCES `lams_comment_session` (`uid`),
  CONSTRAINT `FK_comment_create` FOREIGN KEY (`create_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_comment_modify` FOREIGN KEY (`update_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_comment_parent` FOREIGN KEY (`parent_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_comment_root` FOREIGN KEY (`root_comment_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_comment_thread` FOREIGN KEY (`thread_comment_uid`) REFERENCES `lams_comment` (`uid`)
);

CREATE TABLE IF NOT EXISTS lams_comment_likes (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `vote` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `comment_like_unique` (`comment_uid`, `user_id`),
  KEY `FK_commentlike_comment` (`comment_uid`),
  KEY `FK_commentlike_user` (`user_id`),
  CONSTRAINT `FK_commentlike_comment` FOREIGN KEY (`comment_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_commentlike_user` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
);

COMMIT;
SET AUTOCOMMIT = 1;



