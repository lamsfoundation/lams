-- LDEV-1511
SET foreign_key_checks=0;

-- LAMS Core Tables --
ALTER TABLE lams_configuration ENGINE=InnoDB;
ALTER TABLE lams_qtz_BLOB_TRIGGERS ENGINE=InnoDB;
ALTER TABLE lams_qtz_CALENDARS ENGINE=InnoDB;
ALTER TABLE lams_qtz_CRON_TRIGGERS ENGINE=InnoDB;
ALTER TABLE lams_qtz_FIRED_TRIGGERS ENGINE=InnoDB;
ALTER TABLE lams_qtz_JOB_DETAILS ENGINE=InnoDB;
ALTER TABLE lams_qtz_JOB_LISTENERS ENGINE=InnoDB;
ALTER TABLE lams_qtz_LOCKS ENGINE=InnoDB;
ALTER TABLE lams_qtz_PAUSED_TRIGGER_GRPS ENGINE=InnoDB;
ALTER TABLE lams_qtz_SCHEDULER_STATE ENGINE=InnoDB;
ALTER TABLE lams_qtz_SIMPLE_TRIGGERS ENGINE=InnoDB;
ALTER TABLE lams_qtz_TRIGGERS ENGINE=InnoDB;
ALTER TABLE lams_qtz_TRIGGER_LISTENERS ENGINE=InnoDB;

ALTER TABLE lams_qtz_JOB_LISTENERS ADD FOREIGN KEY (JOB_NAME,JOB_GROUP) REFERENCES lams_qtz_JOB_DETAILS (JOB_NAME,JOB_GROUP);       
ALTER TABLE lams_qtz_TRIGGERS ADD FOREIGN KEY (JOB_NAME,JOB_GROUP) REFERENCES lams_qtz_JOB_DETAILS (JOB_NAME,JOB_GROUP);
ALTER TABLE lams_qtz_SIMPLE_TRIGGERS ADD FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) REFERENCES lams_qtz_TRIGGERS (TRIGGER_NAME,TRIGGER_GROUP);
ALTER TABLE lams_qtz_CRON_TRIGGERS ADD FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) REFERENCES lams_qtz_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP);
ALTER TABLE lams_qtz_BLOB_TRIGGERS ADD FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) REFERENCES lams_qtz_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP);       
ALTER TABLE lams_qtz_TRIGGER_LISTENERS ADD FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) REFERENCES lams_qtz_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP);

-- Notebook --
ALTER TABLE lams_notebook_entry ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_notebook ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_session ENGINE=InnoDB;
ALTER TABLE tl_lantbk11_user ENGINE=InnoDB;

ALTER TABLE tl_lantbk11_attachment ADD CONSTRAINT FK12090F57FC940906 FOREIGN KEY (notebook_uid) REFERENCES tl_lantbk11_notebook (uid);
ALTER TABLE tl_lantbk11_session ADD CONSTRAINT FKB7C198E2FC940906 FOREIGN KEY (notebook_uid) REFERENCES tl_lantbk11_notebook (uid);
ALTER TABLE tl_lantbk11_user ADD CONSTRAINT FKCB8A58FFA3B0FADF FOREIGN KEY (notebook_session_uid) REFERENCES tl_lantbk11_session (uid);

-- Chat --
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

-- Resources --
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

-- Scribe --
ALTER TABLE tl_lascrb11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_heading ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_report_entry ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_scribe ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_session ENGINE=InnoDB;
ALTER TABLE tl_lascrb11_user ENGINE=InnoDB;

alter table tl_lascrb11_attachment add constraint FK57953706B3FA1495 foreign key (scribe_uid) references tl_lascrb11_scribe (uid);
alter table tl_lascrb11_heading add constraint FK428A22FFB3FA1495 foreign key (scribe_uid) references tl_lascrb11_scribe (uid);
alter table tl_lascrb11_report_entry add constraint FK5439FACAEA50D086 foreign key (scribe_heading_uid) references tl_lascrb11_heading (uid);
alter table tl_lascrb11_report_entry add constraint FK5439FACA1C266FAE foreign key (scribe_session_uid) references tl_lascrb11_session (uid);
alter table tl_lascrb11_session add constraint FK89732793B3FA1495 foreign key (scribe_uid) references tl_lascrb11_scribe (uid);
alter table tl_lascrb11_session add constraint FK89732793E46919FF foreign key (appointed_scribe_uid) references tl_lascrb11_user (uid);
alter table tl_lascrb11_user add constraint FK187DAFEE1C266FAE foreign key (scribe_session_uid) references tl_lascrb11_session (uid);

-- Survey --
ALTER TABLE tl_lasurv11_answer ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_attachment ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_option ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_question ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_session ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_survey ENGINE=InnoDB;
ALTER TABLE tl_lasurv11_user ENGINE=InnoDB;

alter table tl_lasurv11_answer add constraint FK6DAAFE3BB1423DC1 foreign key (user_uid) references tl_lasurv11_user (uid);
alter table tl_lasurv11_answer add constraint FK6DAAFE3B25F3BB77 foreign key (question_uid) references tl_lasurv11_question (uid);
alter table tl_lasurv11_attachment add constraint FKD92A9120D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_option add constraint FK85AB46F26966134F foreign key (question_uid) references tl_lasurv11_question (uid);
alter table tl_lasurv11_question add constraint FK872D4F23D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_question add constraint FK872D4F23E4C99A5F foreign key (create_by) references tl_lasurv11_user (uid);
alter table tl_lasurv11_session add constraint FKF08793B9D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);
alter table tl_lasurv11_survey add constraint FK8CC465D7E4C99A5F foreign key (create_by) references tl_lasurv11_user (uid);
alter table tl_lasurv11_user add constraint FK633F25884F803F63 foreign key (session_uid) references tl_lasurv11_session (uid);
alter table tl_lasurv11_user add constraint FK633F2588D14146E5 foreign key (survey_uid) references tl_lasurv11_survey (uid);

SET foreign_key_checks=1;