SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
-- LDEV-3640 Add obvious cascades. Without them Learning Design delete fails.

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_6;
ALTER TABLE lams_learning_activity ADD CONSTRAINT FK_lams_learning_activity_6 FOREIGN KEY (learning_design_id)
REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_15;
ALTER TABLE lams_learning_activity ADD CONSTRAINT FK_lams_learning_activity_15 FOREIGN KEY (transition_to_id)
REFERENCES lams_learning_transition (transition_id) ON DELETE SET NULL ON UPDATE SET NULL;

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_16;
ALTER TABLE lams_learning_activity ADD CONSTRAINT FK_lams_learning_activity_16 FOREIGN KEY (transition_from_id)
REFERENCES lams_learning_transition (transition_id) ON DELETE SET NULL ON UPDATE SET NULL;

ALTER TABLE lams_learning_transition DROP FOREIGN KEY lddefn_transition_ibfk_1;
ALTER TABLE lams_learning_transition ADD CONSTRAINT lddefn_transition_ibfk_1 FOREIGN KEY (learning_design_id)
REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE;


COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
