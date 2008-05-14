-- LDEV1511 make tables InnoDB
ALTER TABLE tl_lachat11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lachat11_chat ENGINE=InnoDB;
ALTER TABLE tl_lachat11_message ENGINE=InnoDB;
ALTER TABLE tl_lachat11_session ENGINE=InnoDB;
ALTER TABLE tl_lachat11_user ENGINE=InnoDB;

alter table tl_lachat11_attachment add constraint FK9ED6CB2E1A3926E3 foreign key (chat_uid) references tl_lachat11_chat (uid);
alter table tl_lachat11_message add constraint FKCC08C1DC2AF61E05 foreign key (to_user_uid) references tl_lachat11_user (uid);
alter table tl_lachat11_message add constraint FKCC08C1DC9C8469FC foreign key (chat_session_uid) references tl_lachat11_session (uid);
alter table tl_lachat11_message add constraint FKCC08C1DCCF3BF9B6 foreign key (from_user_uid) references tl_lachat11_user (uid);
alter table tl_lachat11_session add constraint FK96E446B1A3926E3 foreign key (chat_uid) references tl_lachat11_chat (uid);
alter table tl_lachat11_user add constraint FK4EB82169C8469FC foreign key (chat_session_uid) references tl_lachat11_session (uid);

UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lachat11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lachat11";