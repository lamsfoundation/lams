-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lascrb11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_heading ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_report_entry ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_scribe ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_session ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_user ENGINE=InnoDB;
alter table tl_lascrb11_attachment add foreign key (scribe_uid) references tl_lascrb11_scribe (uid);
alter table tl_lascrb11_heading add foreign key (scribe_uid) references tl_lascrb11_scribe (uid);
alter table tl_lascrb11_report_entry add foreign key (scribe_heading_uid) references tl_lascrb11_heading (uid);
alter table tl_lascrb11_report_entry add foreign key (scribe_session_uid) references tl_lascrb11_session (uid);
alter table tl_lascrb11_session add foreign key (scribe_uid) references tl_lascrb11_scribe (uid);
alter table tl_lascrb11_session add foreign key (appointed_scribe_uid) references tl_lascrb11_user (uid);
alter table tl_lascrb11_user add foreign key (scribe_session_uid) references tl_lascrb11_session (uid);
UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lascrb11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lascrb11";
