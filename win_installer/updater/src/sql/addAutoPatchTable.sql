-- As of 2.2 using the patches table with autopatch to do db updates
CREATE TABLE patches (
       system_name VARCHAR(30) NOT NULL
     , patch_level INTEGER(11) NOT NULL
     , patch_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , patch_in_progress CHAR(1) NOT NULL DEFAULT 'F'
     , PRIMARY KEY (system_name)
)TYPE=InnoDB;