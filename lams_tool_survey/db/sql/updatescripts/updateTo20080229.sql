-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lasurv11_answer ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_option ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_question ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_session ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_survey ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_user ENGINE=InnoDB;
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lasurv11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lasurv11";
