CREATE TABLE lams_organisation_type (
       organisation_type_id INT(3) NOT NULL
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (organisation_type_id)
)TYPE=InnoDB;

CREATE TABLE lams_authentication_method_type (
       authentication_method_type_id INT(3) NOT NULL
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (authentication_method_type_id)
)TYPE=InnoDB;

CREATE TABLE lams_role (
       role_id INT(6) NOT NULL DEFAULT 0
     , name VARCHAR(64) NOT NULL
     , description TEXT
     , create_date BIGINT(20)
     , PRIMARY KEY (role_id)
)TYPE=InnoDB;
CREATE INDEX gname ON lams_role (name ASC);

CREATE TABLE lams_authentication_method (
       authentication_method_id INT(11) NOT NULL
     , authentication_method_type_id INT(3) NOT NULL
     , PRIMARY KEY (authentication_method_id)
     , INDEX (authentication_method_type_id)
     , CONSTRAINT FK_lams_authorization_method_1 FOREIGN KEY (authentication_method_type_id)
                  REFERENCES lams_authentication_method_type (authentication_method_type_id)
)TYPE=InnoDB;

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

CREATE TABLE lams_user (
       user_id INT(11) NOT NULL DEFAULT 0
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
     , disabled_flag BOOL NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL
     , authentication_method_id INT(11) NOT NULL
     , workspace_id INT(11)
     , PRIMARY KEY (user_id)
     , INDEX (authentication_method_id)
     , CONSTRAINT FK_lams_user_1 FOREIGN KEY (authentication_method_id)
                  REFERENCES lams_authentication_method (authentication_method_id)
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id)
)TYPE=InnoDB;
CREATE INDEX login ON lams_user (login ASC);

CREATE TABLE lams_organisation (
       organisation_id INT(11) NOT NULL DEFAULT 0
     , name VARCHAR(250)
     , description VARCHAR(250)
     , parent_organisation_id INT(11)
     , organisation_type_id INT(3) NOT NULL
     , create_date DATETIME NOT NULL
     , workspace_id INT(11)
     , PRIMARY KEY (organisation_id)
     , INDEX (organisation_type_id)
     , CONSTRAINT FK_lams_organisation_1 FOREIGN KEY (organisation_type_id)
                  REFERENCES lams_organisation_type (organisation_type_id)
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_organisation_2 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id)
     , INDEX (parent_organisation_id)
     , CONSTRAINT FK_lams_organisation_3 FOREIGN KEY (parent_organisation_id)
                  REFERENCES lams_organisation (organisation_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_organisation (
       user_organisation_id INT(11) NOT NULL
     , organisation_id INT(11) NOT NULL
     , user_id INT(11) NOT NULL
     , PRIMARY KEY (user_organisation_id)
     , INDEX (user_id)
     , CONSTRAINT u_user_organisation_ibfk_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (organisation_id)
     , CONSTRAINT u_user_organisation_ibfk_2 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

CREATE TABLE lams_authentication_method_parameter (
       authentication_parameter_id INT(11) NOT NULL
     , authentication_method_id INT(11) NOT NULL
     , name VARCHAR(128) NOT NULL
     , value VARCHAR(255)
     , PRIMARY KEY (authentication_parameter_id)
     , INDEX (authentication_method_id)
     , CONSTRAINT FK_lams_authorization_method_parameter_1 FOREIGN KEY (authentication_method_id)
                  REFERENCES lams_authentication_method (authentication_method_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_organisation_role (
       user_organisation_role_id INT(11) NOT NULL
     , user_organisation_id INT(11) NOT NULL
     , role_id INT(6) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_organisation_role_id)
     , INDEX (role_id)
     , CONSTRAINT FK_lams_user_organisation_role_2 FOREIGN KEY (role_id)
                  REFERENCES lams_role (role_id)
     , INDEX (user_organisation_id)
     , CONSTRAINT FK_lams_user_organisation_role_3 FOREIGN KEY (user_organisation_id)
                  REFERENCES lams_user_organisation (user_organisation_id)
)TYPE=InnoDB;

