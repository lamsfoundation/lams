SET AUTOCOMMIT = 0;

-- SIF-4 Adding an openid_url for each user
ALTER TABLE lams_user ADD COLUMN openid_url VARCHAR(255) UNIQUE;
ALTER TABLE lams_user ADD INDEX idx_openid_url (openid_url);

CREATE TABLE lams_openid_config (
     config_key VARCHAR(20) NOT NULL
   , config_value VARCHAR(255) NOT NULL
   , PRIMARY KEY (config_key)
)ENGINE=InnoDB;

INSERT INTO lams_openid_config(config_key, config_value) values ("enabled", "false");
INSERT INTO lams_openid_config(config_key, config_value) values ("portalURL", "");
INSERT INTO lams_openid_config(config_key, config_value) values ("trustedIDPs", "");

COMMIT;
SET AUTOCOMMIT = 1;