# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-22 15:34:31
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-22 15:21:41
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-22 14:57:54
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-22 13:18:58
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-22 11:59:03
# 
-- Tables are not sorted because: 
-- Circular dependency between table DEFAULT_SCHEMA.lams_learning_activity and table DEFAULT_SCHEMA.lams_learning_design: both have foreign keys that reference the other table.
-- Try generating Foreign Keys with ALTER TABLE statements.
CREATE TABLE lams_learning_activity (
       activity_id BIGINT DEFAULT '0' NOT NULL
     , id INT
     , description TEXT
     , title VARCHAR(255)
     , xcoord INT
     , ycoord INT
     , parent_activity_id BIGINT
     , learning_activity_type_id INT NOT NULL
     , learning_grouping_id BIGINT
     , order_id INT
     , define_later_flag TINYINT DEFAULT '0' NOT NULL
     , learning_design_id BIGINT DEFAULT '0'
     , learning_library_id BIGINT DEFAULT '0'
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (activity_id)
)TYPE=InnoDB;
CREATE INDEX idx_activity_library_id ON lams_learning_activity (learning_library_id);
CREATE INDEX idx_activity_design_id ON lams_learning_activity (learning_design_id);
CREATE INDEX idx_activity_grouping_id ON lams_learning_activity (learning_grouping_id);
CREATE INDEX idx_activity_activity_type_id ON lams_learning_activity (learning_activity_type_id);
CREATE INDEX idx_activity_parent_id ON lams_learning_activity (parent_activity_id);


CREATE TABLE lams_learning_design (
       learning_design_id BIGINT DEFAULT '0' NOT NULL
     , id INT
     , description TEXT
     , title VARCHAR(255)
     , first_activity_id BIGINT
     , max_id INT
     , object_type VARCHAR(255)
     , valid_design_flag TINYINT
     , read_only_flag TINYINT
     , date_read_only DATETIME
     , read_access BIGINT
     , write_access BIGINT
     , owning_user_id BIGINT
     , help_text TEXT
     , lesson_copy_flag TINYINT DEFAULT '0' NOT NULL
     , create_date_time DATETIME NOT NULL
     , version VARCHAR(56) NOT NULL
     , parent_learning_design_id BIGINT
     , open_date_time DATETIME
     , close_date_time DATETIME
     , PRIMARY KEY (learning_design_id)
)TYPE=InnoDB;
CREATE INDEX idx_design_parent_id ON lams_learning_design (parent_learning_design_id);
CREATE INDEX idx_design_user_id ON lams_learning_design (owning_user_id);
CREATE INDEX idx_design_first_act ON lams_learning_design (first_activity_id);

CREATE TABLE lams_learning_library (
       learning_library_id BIGINT DEFAULT '0' NOT NULL
     , description TEXT
     , title VARCHAR(255)
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (learning_library_id)
)TYPE=InnoDB;

CREATE TABLE lams_learning_transition (
       transition_id BIGINT DEFAULT '0' NOT NULL
     , id INT
     , description TEXT
     , title VARCHAR(255)
     , to_activity_id BIGINT NOT NULL
     , from_activity_id BIGINT NOT NULL
     , learning_design_id BIGINT NOT NULL
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (transition_id)
)TYPE=InnoDB;
CREATE INDEX ldId ON lams_learning_transition (learning_design_id);
CREATE INDEX idx_transition_to_activity ON lams_learning_transition (to_activity_id);
CREATE INDEX idx_transition_from_activity ON lams_learning_transition (from_activity_id);


CREATE TABLE lams_learning_grouping (
       learning_grouping_id BIGINT NOT NULL
     , grouping_type_id INT NOT NULL
     , number_of_groups INT
     , learners_per_group INT
     , PRIMARY KEY (learning_grouping_id)
)TYPE=InnoDB;
CREATE INDEX idx_grouping_grouping_type ON lams_learning_grouping (grouping_type_id);

CREATE TABLE lams_learning_group (
       learning_group_id BIGINT NOT NULL
     , learning_grouping_id BIGINT NOT NULL
     , PRIMARY KEY (learning_group_id)
)TYPE=InnoDB;
CREATE INDEX idx_group_grouping ON lams_learning_group (learning_grouping_id);

CREATE TABLE lams_learning_activity_type (
       learning_activity_type_id INT NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (learning_activity_type_id)
)TYPE=InnoDB;



CREATE TABLE lams_learning_tool_activity (
       activity_id BIGINT DEFAULT '0' NOT NULL
     , tool_id BIGINT NOT NULL
     , tool_content_id BIGINT
     , PRIMARY KEY (activity_id)
)TYPE=InnoDB;
CREATE INDEX idx_toole_act_activity ON lams_learning_tool_activity (activity_id);
CREATE INDEX idx_toole_act_tool ON lams_learning_tool_activity (tool_id);
CREATE INDEX idx_toole_act_tool_content ON lams_learning_tool_activity (tool_content_id);

CREATE TABLE lams_learning_gate_activity (
       activity_id BIGINT DEFAULT '0' NOT NULL
     , start_date_time DATETIME
     , end_date_time DATETIME
     , gate_activity_level_id INT NOT NULL
     , PRIMARY KEY (activity_id)
)TYPE=InnoDB;
CREATE INDEX idx_gate_act_activity ON lams_learning_gate_activity (activity_id);
CREATE INDEX idx_gate_act_level ON lams_learning_gate_activity (gate_activity_level_id);

CREATE TABLE lams_grouping_type (
       grouping_type_id INT NOT NULL
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (grouping_type_id)
)TYPE=InnoDB;

CREATE TABLE lams_gate_activity_level (
       gate_activity_level_id INT NOT NULL
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (gate_activity_level_id)
)TYPE=InnoDB;

CREATE TABLE lams_learning_options_activity (
       activity_id BIGINT DEFAULT '0' NOT NULL
     , min_number_of_options INT
     , max_number_of_options INT
     , PRIMARY KEY (activity_id)
)TYPE=InnoDB;
CREATE INDEX idx_options_act_activity ON lams_learning_options_activity (activity_id);

CREATE TABLE lams_learning_grouping_activity (
       activity_id BIGINT DEFAULT '0' NOT NULL
     , learning_grouping_id BIGINT NOT NULL
     , PRIMARY KEY (activity_id)
)TYPE=InnoDB;
CREATE INDEX idx_grouping_act_activity ON lams_learning_grouping_activity (activity_id);
CREATE INDEX idx_grouping_act_grouping ON lams_learning_grouping_activity (learning_grouping_id);

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

