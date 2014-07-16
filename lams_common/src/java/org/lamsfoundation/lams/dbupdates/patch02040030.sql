SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-3269 Introduce tool groups
CREATE TABLE lams_learning_library_group (
	  group_id INT NOT NULL AUTO_INCREMENT
	, name VARCHAR(64) NOT NULL
	, PRIMARY KEY (group_id)
)ENGINE=InnoDB;

CREATE TABLE lams_learning_library_to_group (
	  group_id INT NOT NULL
	, learning_library_id BIGINT(20) NOT NULL
	, PRIMARY KEY (group_id, learning_library_id)
)ENGINE=InnoDB;

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;