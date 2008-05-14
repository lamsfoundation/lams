-- LDEV1511 make tables InnoDB
ALTER TABLE tl_larsrc11_attachment ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_item_instruction ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_item_log ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_resource ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_resource_item ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_session ENGINE=InnoDB;
ALTER TABLE tl_larsrc11_user ENGINE=InnoDB;

alter table tl_larsrc11_attachment add constraint FK1E7009430E79035 foreign key (resource_uid) references tl_larsrc11_resource (uid);
alter table tl_larsrc11_item_instruction add constraint FKA5665013980570ED foreign key (item_uid) references tl_larsrc11_resource_item (uid);
alter table tl_larsrc11_resource add constraint FK89093BF758092FB foreign key (create_by) references tl_larsrc11_user (uid);
alter table tl_larsrc11_resource_item add constraint FKF52D1F93758092FB foreign key (create_by) references tl_larsrc11_user (uid);
alter table tl_larsrc11_resource_item add constraint FKF52D1F9330E79035 foreign key (resource_uid) references tl_larsrc11_resource (uid);
alter table tl_larsrc11_resource_item add constraint FKF52D1F93EC0D3147 foreign key (session_uid) references tl_larsrc11_session (uid);
alter table tl_larsrc11_item_log add constraint FK693580A438BF8DFE foreign key (resource_item_uid) references tl_larsrc11_resource_item (uid);
alter table tl_larsrc11_item_log add constraint FK693580A441F9365D foreign key (user_uid) references tl_larsrc11_user (uid);
alter table tl_larsrc11_session add constraint FK24AA78C530E79035 foreign key (resource_uid) references tl_larsrc11_resource (uid);
alter table tl_larsrc11_user add constraint FK30113BFCEC0D3147 foreign key (session_uid) references tl_larsrc11_session (uid);
alter table tl_larsrc11_user add constraint FK30113BFC309ED320 foreign key (resource_uid) references tl_larsrc11_resource (uid);

UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "larsrc11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "larsrc11";
