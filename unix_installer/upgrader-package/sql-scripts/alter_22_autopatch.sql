
-- Need to create the patches table before we start deploying new tools
CREATE TABLE patches (
       system_name VARCHAR(30) NOT NULL
     , patch_level INTEGER(11) NOT NULL
     , patch_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , patch_in_progress CHAR(1) NOT NULL DEFAULT 'F'
     , PRIMARY KEY (system_name)
)TYPE=InnoDB;

-- 2.2 SPECIFIC UPDATE - make sure the spreadsheet is disabled 
update lams_learning_library set valid_flag=0 where title="SpreadSheet";