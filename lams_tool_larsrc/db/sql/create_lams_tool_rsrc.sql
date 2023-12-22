SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_larsrc11_item_log (
                                      uid bigint NOT NULL AUTO_INCREMENT,
                                      access_date datetime DEFAULT NULL,
                                      complete_date datetime DEFAULT NULL,
                                      resource_item_uid bigint DEFAULT NULL,
                                      user_uid bigint DEFAULT NULL,
                                      complete tinyint(1) DEFAULT NULL,
                                      session_id bigint DEFAULT NULL,
                                      PRIMARY KEY (uid),
                                      KEY idx_item_log_session_id (session_id),
                                      KEY FK693580A438BF8DFE (resource_item_uid),
                                      KEY FK693580A441F9365D (user_uid),
                                      CONSTRAINT FK693580A438BF8DFE FOREIGN KEY (resource_item_uid) REFERENCES tl_larsrc11_resource_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT FK693580A441F9365D FOREIGN KEY (user_uid) REFERENCES tl_larsrc11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_larsrc11_resource (
                                      uid bigint NOT NULL AUTO_INCREMENT,
                                      create_date datetime DEFAULT NULL,
                                      update_date datetime DEFAULT NULL,
                                      create_by bigint DEFAULT NULL,
                                      title varchar(255) DEFAULT NULL,
                                      lock_on_finished tinyint(1) DEFAULT NULL,
                                      instructions mediumtext,
                                      content_in_use tinyint(1) DEFAULT NULL,
                                      define_later tinyint(1) DEFAULT NULL,
                                      content_id bigint DEFAULT NULL,
                                      allow_add_files tinyint(1) DEFAULT NULL,
                                      allow_add_urls tinyint(1) DEFAULT NULL,
                                      mini_view_resource_number int DEFAULT NULL,
                                      reflect_instructions mediumtext,
                                      reflect_on_activity tinyint(1) DEFAULT NULL,
                                      assigment_submit_notify tinyint(1) DEFAULT '0',
                                      file_upload_notify tinyint(1) DEFAULT '0',
                                      PRIMARY KEY (uid),
                                      UNIQUE KEY content_id (content_id),
                                      KEY FK89093BF758092FB (create_by),
                                      CONSTRAINT FK89093BF758092FB FOREIGN KEY (create_by) REFERENCES tl_larsrc11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE tl_larsrc11_resource_item (
                                           uid bigint NOT NULL AUTO_INCREMENT,
                                           file_uuid bigint DEFAULT NULL,
                                           file_version_id bigint DEFAULT NULL,
                                           instructions text,
                                           title varchar(255) DEFAULT NULL,
                                           `url` text,
                                           create_by bigint DEFAULT NULL,
                                           create_date datetime DEFAULT NULL,
                                           create_by_author tinyint(1) DEFAULT NULL,
                                           is_hide tinyint(1) DEFAULT NULL,
                                           item_type smallint DEFAULT NULL,
                                           init_item varchar(255) DEFAULT NULL,
                                           file_type varchar(255) DEFAULT NULL,
                                           file_name varchar(255) DEFAULT NULL,
                                           resource_uid bigint DEFAULT NULL,
                                           session_uid bigint DEFAULT NULL,
                                           order_id int DEFAULT NULL,
                                           is_allow_rating tinyint(1) DEFAULT '0',
                                           is_allow_comments tinyint(1) DEFAULT '0',
                                           PRIMARY KEY (uid),
                                           KEY FKF52D1F9330E79035 (resource_uid),
                                           KEY FKF52D1F93758092FB (create_by),
                                           KEY FKF52D1F93EC0D3147 (session_uid),
                                           CONSTRAINT FKF52D1F9330E79035 FOREIGN KEY (resource_uid) REFERENCES tl_larsrc11_resource (uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                           CONSTRAINT FKF52D1F93758092FB FOREIGN KEY (create_by) REFERENCES tl_larsrc11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                           CONSTRAINT FKF52D1F93EC0D3147 FOREIGN KEY (session_uid) REFERENCES tl_larsrc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_larsrc11_session (
                                     uid bigint NOT NULL AUTO_INCREMENT,
                                     session_end_date datetime DEFAULT NULL,
                                     session_start_date datetime DEFAULT NULL,
                                     `status` int DEFAULT NULL,
                                     resource_uid bigint DEFAULT NULL,
                                     session_id bigint DEFAULT NULL,
                                     session_name varchar(250) DEFAULT NULL,
                                     PRIMARY KEY (uid),
                                     UNIQUE KEY session_id (session_id),
                                     KEY FK24AA78C530E79035 (resource_uid),
                                     CONSTRAINT FK24AA78C530E79035 FOREIGN KEY (resource_uid) REFERENCES tl_larsrc11_resource (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_larsrc11_user (
                                  uid bigint NOT NULL AUTO_INCREMENT,
                                  user_id bigint DEFAULT NULL,
                                  last_name varchar(255) DEFAULT NULL,
                                  first_name varchar(255) DEFAULT NULL,
                                  login_name varchar(255) DEFAULT NULL,
                                  session_finished tinyint(1) DEFAULT NULL,
                                  session_uid bigint DEFAULT NULL,
                                  resource_uid bigint DEFAULT NULL,
                                  PRIMARY KEY (uid),
                                  KEY idx_user_user_id (user_id),
                                  KEY FK30113BFC309ED320 (resource_uid),
                                  KEY FK30113BFCEC0D3147 (session_uid),
                                  CONSTRAINT FK30113BFC309ED320 FOREIGN KEY (resource_uid) REFERENCES tl_larsrc11_resource (uid) ON DELETE SET NULL ON UPDATE CASCADE,
                                  CONSTRAINT FK30113BFCEC0D3147 FOREIGN KEY (session_uid) REFERENCES tl_larsrc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO tl_larsrc11_resource (uid, title, lock_on_finished,
 instructions, content_in_use, define_later, content_id, allow_add_files, 
 allow_add_urls, mini_view_resource_number, allow_auto_run,reflect_on_activity) VALUES
  (1,'Resources','0','Instructions ',0,0,${default_content_id},0,0,0,0,0);
  
INSERT INTO tl_larsrc11_resource_item (uid, title, url,create_date, create_by_author, is_hide, item_type, open_url_new_window, resource_uid,order_id) VALUES 
  (1,'Web Search','http://www.google.com ',NOW(),1,0,1,1,1,1);
INSERT INTO tl_larsrc11_item_instruction (uid, description, sequence_id, item_uid) VALUES 
  (1,'Use Google to search the web',0,1);
    
SET FOREIGN_KEY_CHECKS=1;