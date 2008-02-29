-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lantbk11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_notebook ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_session ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_user ENGINE=InnoDB;
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lantbk11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lantbk11";
