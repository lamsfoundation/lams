SET AUTOCOMMIT = 0;

-- LDEV-2476
CREATE TABLE lams_planner_node_role (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , node_uid BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , role_id INT(6) NOT NULL
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

ALTER TABLE lams_planner_node_role ADD CONSTRAINT FK_planner_node_role_user FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE lams_planner_node_role ADD CONSTRAINT FK_planner_node_role_node FOREIGN KEY (node_uid) REFERENCES lams_planner_nodes (uid) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE lams_planner_node_role ADD CONSTRAINT FK_planner_node_role_role FOREIGN KEY (role_id) REFERENCES lams_role (role_id) ON DELETE CASCADE ON UPDATE NO ACTION;

COMMIT;
SET AUTOCOMMIT = 1;
