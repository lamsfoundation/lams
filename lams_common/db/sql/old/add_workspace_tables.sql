# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-09 16:13:07
# 
CREATE TABLE lams_workspace_folder (
       workspace_folder_id INT(11) NOT NULL
     , parent_folder_id INT(11)
     , name VARCHAR(64) NOT NULL
     , workspace_id INT(11) NOT NULL
     , PRIMARY KEY (workspace_folder_id)
     , INDEX (parent_folder_id)
     , CONSTRAINT FK_lams_workspace_folder_2 FOREIGN KEY (parent_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
)TYPE=InnoDB;

CREATE TABLE lams_workspace (
       workspace_id INT(11) NOT NULL
     , root_folder_id INT(11) NOT NULL
     , PRIMARY KEY (workspace_id)
     , INDEX (root_folder_id)
     , CONSTRAINT FK_lams_workspace_1 FOREIGN KEY (root_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
)TYPE=InnoDB;

ALTER TABLE lams_user ADD workspace_id INT(11);
ALTER TABLE lams_user ADD INDEX idx_lams_user_workspace_id  (workspace_id);
ALTER TABLE lams_user ADD FOREIGN KEY (workspace_id) REFERENCES lams_workspace (workspace_id);

ALTER TABLE lams_organisation ADD workspace_id INT(11);
ALTER TABLE lams_organisation ADD INDEX idx_lams_organisation_workspace_id  (workspace_id);
ALTER TABLE lams_organisation ADD FOREIGN KEY (workspace_id) REFERENCES lams_workspace (workspace_id);