-- Tables are not sorted because: 
-- Circular dependency between table DEFAULT_SCHEMA.lams_learning_activity and table DEFAULT_SCHEMA.lams_learning_design: both have foreign keys that reference the other table.
-- Try generating Foreign Keys with ALTER TABLE statements.
DROP INDEX PRIMARY;
DROP INDEX ldId;
DROP INDEX optional_activity_id;
DROP INDEX PRIMARY;
DROP INDEX idx_learning_design_libary_id;
DROP INDEX PRIMARY;
DROP INDEX PRIMARY;
DROP INDEX ldId;
DROP INDEX activity_id;

DROP TABLE lams_learning_grouping_activity;
DROP TABLE lams_learning_options_activity;
DROP TABLE lams_gate_activity_level;
DROP TABLE lams_grouping_type;
DROP TABLE lams_learning_gate_activity;
DROP TABLE lams_learning_tool_activity;
DROP TABLE lams_learning_activity_type;
DROP TABLE lams_learning_group;
DROP TABLE lams_learning_grouping;
DROP TABLE lams_learning_transition;
DROP TABLE lams_learning_library;
DROP TABLE lams_learning_design;
DROP TABLE lams_learning_activity;














ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_2
      FOREIGN KEY (parent_activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_3
      FOREIGN KEY (learning_activity_type_id)
      REFERENCES lams_learning_activity_type (learning_activity_type_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_4
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_5
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_6
      FOREIGN KEY (learning_grouping_id)
      REFERENCES lams_learning_grouping (learning_grouping_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_6
      FOREIGN KEY (learning_design_id)
      REFERENCES lams_learning_design (learning_design_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_7
      FOREIGN KEY (learning_library_id)
      REFERENCES lams_learning_library (learning_library_id);

ALTER TABLE lams_learning_design
  ADD CONSTRAINT FK_lams_learning_design_1
      FOREIGN KEY (first_activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_design
  ADD CONSTRAINT FK_lams_learning_design_2
      FOREIGN KEY (parent_learning_design_id)
      REFERENCES lams_learning_design (learning_design_id);

ALTER TABLE lams_learning_transition
  ADD CONSTRAINT lddefn_transition_ibfk_1
      FOREIGN KEY (learning_design_id)
      REFERENCES lams_learning_design (learning_design_id);

ALTER TABLE lams_learning_transition
  ADD CONSTRAINT FK_learning_transition_2
      FOREIGN KEY (to_activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_transition
  ADD CONSTRAINT FK_learning_transition_3
      FOREIGN KEY (from_activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_grouping
  ADD CONSTRAINT FK_lams_learning_grouping_1
      FOREIGN KEY (grouping_type_id)
      REFERENCES lams_grouping_type (grouping_type_id);

ALTER TABLE lams_learning_group
  ADD CONSTRAINT FK_lams_learning_group_1
      FOREIGN KEY (learning_grouping_id)
      REFERENCES lams_learning_grouping (learning_grouping_id);

ALTER TABLE lams_learning_tool_activity
  ADD CONSTRAINT FK_lams_learning_tool_activity_1
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_gate_activity
  ADD CONSTRAINT FK_lams_learning_gate_activity_1
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_gate_activity
  ADD CONSTRAINT FK_lams_learning_gate_activity_2
      FOREIGN KEY (gate_activity_level_id)
      REFERENCES lams_gate_activity_level (gate_activity_level_id);

ALTER TABLE lams_learning_options_activity
  ADD CONSTRAINT FK_lams_learning_options_activity_1
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_grouping_activity
  ADD CONSTRAINT FK_lams_learning_grouping_activity_1
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_learning_grouping_activity
  ADD CONSTRAINT FK_lams_learning_grouping_activity_2
      FOREIGN KEY (learning_grouping_id)
      REFERENCES lams_learning_grouping (learning_grouping_id);

