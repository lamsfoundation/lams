CREATE TABLE DEFAULT_SCHEMA.tl_lantbk11_user_1 (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20)
     , last_name VARCHAR(255)
     , login_name VARCHAR(255)
     , first_name VARCHAR(255)
     , finished_activity BIT
     , gmap_session_uid BIGINT(20)
     , PRIMARY KEY (uid)
);
CREATE INDEX FK4EB82169C8469FC ON DEFAULT_SCHEMA.tl_lantbk11_user_1 (gmap_session_uid ASC);

CREATE TABLE DEFAULT_SCHEMA.tl_lagmap_gmap (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , create_date DATETIME
     , update_date DATETIME
     , create_by BIGINT(20)
     , title VARCHAR(255)
     , instructions TEXT
     , run_offline BIT
     , lock_on_finished BIT
     , online_instructions TEXT
     , offline_instructions TEXT
     , content_in_use BIT
     , define_later BIT
     , tool_content_id BIGINT(20)
     , allow_edit_markers BIT
     , show_all_markers BIT
     , limit_markers BIT
     , max_markers SMALLINT
     , allow_zoom BIT
     , allow_terrain BIT
     , allow_satellite BIT
     , allow_hybrid BIT
     , map_center_latitude DOUBLE PRECISION
     , map_center_longitude DOUBLE PRECISION
     , `map_zoom ` SMALLINT
     , map_type VARCHAR(20)
     , PRIMARY KEY (uid)
     , INDEX (create_by)
     , CONSTRAINT FK_tl_lagmap_gmap_1 FOREIGN KEY (create_by)
                  REFERENCES DEFAULT_SCHEMA.tl_lantbk11_user_1 (uid)
);

CREATE TABLE DEFAULT_SCHEMA.tl_lagmap_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , session_end_date DATETIME
     , session_start_date DATETIME
     , status INT(11)
     , session_id BIGINT(20)
     , session_name VARCHAR(250)
     , gmap_uid BIGINT(20)
     , PRIMARY KEY (uid)
     , INDEX (gmap_uid)
     , CONSTRAINT FK_tl_lagmap_session_1 FOREIGN KEY (gmap_uid)
                  REFERENCES DEFAULT_SCHEMA.tl_lagmap_gmap (uid)
);

CREATE TABLE DEFAULT_SCHEMA.tl_lagmap10_attachment (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , file_version_id BIGINT(20)
     , file_type VARCHAR(255)
     , file_name VARCHAR(255)
     , file_uuid BIGINT(20)
     , create_date DATETIME
     , gmap_uid BIGINT(20)
     , PRIMARY KEY (uid)
     , INDEX (gmap_uid)
     , CONSTRAINT FK_tl_lagmap10_attachment_1 FOREIGN KEY (gmap_uid)
                  REFERENCES DEFAULT_SCHEMA.tl_lagmap_gmap (uid)
);

CREATE TABLE DEFAULT_SCHEMA.tl_lagmap10_marker (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , longitude DOUBLE PRECISION
     , latitude DOUBLE PRECISION
     , info_window_message TEXT
     , create_date DATETIME
     , UPDATE_DATE DATETIME
     , is_authored BIT
     , gmap_uid BIGINT(20)
     , title VARCHAR(55)
     , created_by BIGINT(20)
     , updated_by BIGINT(20)
     , gmap_session_uid BIGINT(20)
     , PRIMARY KEY (uid)
     , INDEX (created_by)
     , CONSTRAINT FK_tl_lagmap10_marker_1 FOREIGN KEY (created_by)
                  REFERENCES DEFAULT_SCHEMA.tl_lantbk11_user_1 (uid)
     , INDEX (updated_by)
     , CONSTRAINT FK_tl_lagmap10_marker_2 FOREIGN KEY (updated_by)
                  REFERENCES DEFAULT_SCHEMA.tl_lantbk11_user_1 (uid)
     , INDEX (gmap_session_uid)
     , CONSTRAINT FK_tl_lagmap10_marker_3 FOREIGN KEY (gmap_session_uid)
                  REFERENCES DEFAULT_SCHEMA.tl_lagmap_session (uid)
     , INDEX (gmap_uid)
     , CONSTRAINT FK_tl_lagmap10_marker_4 FOREIGN KEY (gmap_uid)
                  REFERENCES DEFAULT_SCHEMA.tl_lagmap_gmap (uid)
);

