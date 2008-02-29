-- LDEV1511 make tables InnoDB
ALTER TABLE tl_larsrc11_attachment ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_item_instruction ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_item_log ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_resource ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_resource_item ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_session ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_user ENGINE=InnoDB;
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "larsrc11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "larsrc11";
