-- SQL script for language-pack.sql
-- checks if the languages in the language pack exist
-- inserts rows into lams_supported_locale iff the languages dont exist
UPDATE lams_configuration set config_value='2007-06-01' WHERE config_key='DictionaryDateCreated';


