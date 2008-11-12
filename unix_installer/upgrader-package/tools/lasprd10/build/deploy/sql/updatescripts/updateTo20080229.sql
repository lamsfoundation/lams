-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lasprd10_attachment ENGINE=InnoDB;
ALTER TABLE tl_lasprd10_item_instruction ENGINE=InnoDB;
ALTER TABLE tl_lasprd10_item_log ENGINE=InnoDB;
ALTER TABLE tl_lasprd10_spreadsheet ENGINE=InnoDB;
ALTER TABLE tl_lasprd10_spreadsheet_item ENGINE=InnoDB;
ALTER TABLE tl_lasprd10_session ENGINE=InnoDB;
ALTER TABLE tl_lasprd10_user ENGINE=InnoDB;

alter table tl_lasprd10_attachment add constraint FK_NEW_2065267438_1E7009430E79035 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_item_instruction add constraint FK_NEW_2065267438_A5665013980570ED foreign key (item_uid) references tl_lasprd10_spreadsheet_item (uid);
alter table tl_lasprd10_spreadsheet add constraint FK_NEW_2065267438_89093BF758092FB foreign key (create_by) references tl_lasprd10_user (uid);
alter table tl_lasprd10_spreadsheet_item add constraint FK_NEW_2065267438_F52D1F93758092FB foreign key (create_by) references tl_lasprd10_user (uid);
alter table tl_lasprd10_spreadsheet_item add constraint FK_NEW_2065267438_F52D1F9330E79035 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_spreadsheet_item add constraint FK_NEW_2065267438_F52D1F93EC0D3147 foreign key (session_uid) references tl_lasprd10_session (uid);
alter table tl_lasprd10_item_log add constraint FK_NEW_2065267438_693580A438BF8DFE foreign key (spreadsheet_item_uid) references tl_lasprd10_spreadsheet_item (uid);
alter table tl_lasprd10_item_log add constraint FK_NEW_2065267438_693580A441F9365D foreign key (user_uid) references tl_lasprd10_user (uid);
alter table tl_lasprd10_session add constraint FK_NEW_2065267438_24AA78C530E79035 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_user add constraint FK_NEW_2065267438_30113BFCEC0D3147 foreign key (session_uid) references tl_lasprd10_session (uid);
alter table tl_lasprd10_user add constraint FK_NEW_2065267438_30113BFC309ED320 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);

UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lasprd10";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lasprd10";
