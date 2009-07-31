-- LDEV-2366

DROP TABLE tl_mdscrm10_configuration;
ALTER TABLE tl_mdscrm10_mdlscorm ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090624 WHERE tool_signature='mdscrm10';

DROP TABLE tl_mdwiki10_configuration;
ALTER TABLE tl_mdwiki10_mdlwiki ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090605 WHERE tool_signature='mdwiki10';

DROP TABLE tl_mdquiz10_configuration;
ALTER TABLE tl_mdquiz10_mdlquiz ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090605 WHERE tool_signature='mdquiz10';

DROP TABLE tl_mdlesn10_configuration;
ALTER TABLE tl_mdlesn10_mdllesson ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090605 WHERE tool_signature='mdlesn10';

DROP TABLE tl_mdglos10_configuration;
ALTER TABLE tl_mdglos10_mdlglossary ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090605 WHERE tool_signature='mdglos10';

DROP TABLE tl_mdchat10_configuration;
ALTER TABLE tl_mdchat10_mdlchat ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090604 WHERE tool_signature='mdchat10';

DROP TABLE tl_mdasgm10_configuration;
ALTER TABLE tl_mdasgm10_mdlassignment ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090604 WHERE tool_signature='mdasgm10';

DROP TABLE tl_mdchce10_configuration;
ALTER TABLE tl_mdchce10_mdlchoice ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090604 WHERE tool_signature='mdchce10';

DROP TABLE tl_mdfrum10_configuration;
ALTER TABLE tl_mdfrum10_mdlforum ADD COLUMN ext_lms_id VARCHAR(255);
UPDATE lams_tool SET tool_version=20090604 WHERE tool_signature='mdfrum10';