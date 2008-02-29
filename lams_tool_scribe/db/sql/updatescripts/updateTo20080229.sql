-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lascrb11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_heading ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_report_entry ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_scribe ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_session ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_user ENGINE=InnoDB;
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lascrb11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lascrb11";
