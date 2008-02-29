-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lachat11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lachat11_chat ENGINE=InnoDB;
ALTER TABLE tl_lachat11_message ENGINE=InnoDB;
ALTER TABLE tl_lachat11_session ENGINE=InnoDB;
ALTER TABLE tl_lachat11_user ENGINE=InnoDB;
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lachat11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lachat11";