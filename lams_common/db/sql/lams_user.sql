CREATE TABLE lams_user (
       user_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , login VARCHAR(20) NOT NULL
     , password VARCHAR(50) NOT NULL
     , title VARCHAR(32)
     , first_name VARCHAR(64)
     , last_name VARCHAR(128)
     , address_line_1 VARCHAR(64)
     , address_line_2 VARCHAR(64)
     , address_line_3 VARCHAR(64)
     , city VARCHAR(64)
     , state VARCHAR(64)
     , country VARCHAR(64)
     , day_phone VARCHAR(64)
     , evening_phone VARCHAR(64)
     , mobile_phone VARCHAR(64)
     , fax VARCHAR(64)
     , email VARCHAR(128)
     , disabled_flag TINYINT(1) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL
     , authentication_method_id BIGINT(20) NOT NULL DEFAULT 0
     , workspace_id BIGINT(20)
     , flash_theme_id BIGINT(20)
     , html_theme_id BIGINT(20)
     , chat_id VARCHAR(255)
     , locale_id TINYINT(4)
     , PRIMARY KEY (user_id)
     , INDEX (authentication_method_id)
     , CONSTRAINT FK_lams_user_1 FOREIGN KEY (authentication_method_id)
                  REFERENCES lams_authentication_method (authentication_method_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (flash_theme_id)
     , CONSTRAINT FK_lams_user_4 FOREIGN KEY (flash_theme_id)
                  REFERENCES lams_css_theme_ve (theme_ve_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (html_theme_id)
     , CONSTRAINT FK_lams_user_5 FOREIGN KEY (html_theme_id)
                  REFERENCES lams_css_theme_ve (theme_ve_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (locale_id)
     , CONSTRAINT FK_lams_user_6 FOREIGN KEY (locale_id)
                  REFERENCES lams_supported_locale (locale_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;
ALTER TABLE lams_user MODIFY COLUMN chat_id VARCHAR(255)
      COMMENT 'ID used for Jabber';
CREATE UNIQUE INDEX UQ_lams_user_login ON lams_user (login ASC);
CREATE INDEX login ON lams_user (login ASC);

