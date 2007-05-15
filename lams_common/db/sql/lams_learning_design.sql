CREATE TABLE lams_learning_design (
       edit_override_user_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_design_ui_id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , first_activity_id BIGINT(20)
     , max_id INT(11)
     , valid_design_flag TINYINT(4) NOT NULL
     , read_only_flag TINYINT(4) NOT NULL
     , date_read_only DATETIME
     , user_id BIGINT(20) NOT NULL
     , help_text TEXT
     , online_instructions TEXT
     , offline_instructions TEXT
     , copy_type_id TINYINT(4) NOT NULL
     , create_date_time DATETIME NOT NULL
     , version VARCHAR(56)
     , original_learning_design_id BIGINT(20)
     , workspace_folder_id BIGINT(20)
     , duration BIGINT(38)
     , license_id BIGINT(20)
     , license_text TEXT
     , last_modified_date_time DATETIME
     , content_folder_id VARCHAR(32)
     , edit_override_lock TINYINT DEFAULT 0
     , edit_override_user_id BIGINT(20)
     , PRIMARY KEY (edit_override_user_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_learning_design_3 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (workspace_folder_id)
     , CONSTRAINT FK_lams_learning_design_4 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
     , INDEX (license_id)
     , CONSTRAINT FK_lams_learning_design_5 FOREIGN KEY (license_id)
                  REFERENCES lams_license (license_id)
     , INDEX (copy_type_id)
     , CONSTRAINT FK_lams_learning_design_6 FOREIGN KEY (copy_type_id)
                  REFERENCES lams_copy_type (copy_type_id)
     , INDEX (edit_override_user_id)
     , CONSTRAINT FK_lams_learning_design_7 FOREIGN KEY (edit_override_user_id)
                  REFERENCES lams_user (user_id)
)TYPE=InnoDB;
ALTER TABLE lams_learning_design MODIFY COLUMN edit_override_lock TINYINT DEFAULT 0
      COMMENT '1 when edit on fly in progress. edit_override_user_id should be set.';
CREATE INDEX idx_design_parent_id ON lams_learning_design (original_learning_design_id ASC);
CREATE INDEX idx_design_first_act ON lams_learning_design (first_activity_id ASC);

