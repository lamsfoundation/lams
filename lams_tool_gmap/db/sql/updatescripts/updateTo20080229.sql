-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lagmap10_attachment ENGINE=InnoDB;
ALTER TABLE tl_lagmap10_gmap ENGINE=InnoDB;
ALTER TABLE tl_lagmap10_session ENGINE=InnoDB;
ALTER TABLE tl_lagmap10_user ENGINE=InnoDB;
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lagmap10";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lagmap10";
