-- SQL statements to update from LAMS 2.3.1

SET AUTOCOMMIT = 0;

-- LDEV-2420 -------
SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE lams_user DROP FOREIGN KEY FK_lams_user_4;
ALTER TABLE lams_user DROP FOREIGN KEY FK_lams_user_5;
DROP TABLE lams_css_property;
DROP TABLE lams_css_style;
DROP TABLE lams_css_theme_ve;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE lams_theme (
       theme_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , name VARCHAR(100) NOT NULL
     , description VARCHAR(100)
     , image_directory VARCHAR(100)
     , theme_type TINYINT(11)
     , PRIMARY KEY (theme_id)
     , UNIQUE UQ_name (name)
)TYPE=InnoDB;

INSERT INTO lams_theme (theme_id, name, description, image_directory, theme_type) VALUES (1, "default", "Default Flash style", null, 2);
INSERT INTO lams_theme (theme_id, name, description, image_directory, theme_type) VALUES (2, "defaultHTML", "Default HTML style", "css", 1);
INSERT INTO lams_theme (theme_id, name, description, image_directory, theme_type) VALUES (3, "highContrast", "High Contrast HTML style", "css", 1);

ALTER TABLE lams_user ADD CONSTRAINT FK_lams_user_4 FOREIGN KEY (flash_theme_id) REFERENCES lams_theme (theme_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE lams_user ADD CONSTRAINT FK_lams_user_5 FOREIGN KEY (html_theme_id) REFERENCES lams_theme (theme_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

COMMIT;
SET AUTOCOMMIT = 1;