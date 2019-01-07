SET FOREIGN_KEY_CHECKS=0;

create table tl_laimag10_imagegallery (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished tinyint(1),
   instructions MEDIUMTEXT,
   next_image_title bigint,
   content_in_use tinyint(1),
   define_later tinyint(1),
   content_id bigint,
   allow_share_images tinyint(1),
   allow_vote tinyint(1),
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity tinyint(1),
   allow_rank tinyint(1),
   image_submit_notify tinyint(1) DEFAULT 0,
   minimum_rates int(11) DEFAULT 0,
   maximum_rates int(11) DEFAULT 0,
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_laimag10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   imageGallery_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK_NEW_1821149711_24AA78C530E79035 FOREIGN KEY (imageGallery_uid)
   		REFERENCES tl_laimag10_imagegallery (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimag10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   imageGallery_uid bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_1821149711_30113BFC309ED320 FOREIGN KEY (imageGallery_uid)
   		REFERENCES tl_laimag10_imagegallery (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1821149711_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_laimag10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimag10_imagegallery_item (
   uid bigint not null auto_increment,
   description MEDIUMTEXT,
   title varchar(255),
   create_by bigint,
   create_date datetime,
   create_by_author tinyint(1),
   sequence_id integer,
   is_hide tinyint(1),
   imageGallery_uid bigint,
   original_file_uuid bigint,
   original_image_width integer,
   original_image_height integer,
   medium_file_uuid bigint,
   medium_image_width integer,
   medium_image_height integer,   
   thumbnail_file_uuid bigint,
   file_name varchar(255),
   primary key (uid),
   CONSTRAINT FK_NEW_1821149711_F52D1F9330E79035 FOREIGN KEY (imageGallery_uid)
   		REFERENCES tl_laimag10_imagegallery (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1821149711_F52D1F93758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_laimag10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

create table tl_laimag10_image_vote (
   uid bigint not null auto_increment,
   is_voted tinyint(1),
   imageGallery_item_uid bigint,
   create_by bigint,
   primary key (uid),
   CONSTRAINT FK_tl_laimag10_image_vote_2 FOREIGN KEY (create_by)
   		REFERENCES tl_laimag10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT FK_tl_laimag10_image_vote_3 FOREIGN KEY (imageGallery_item_uid)
   		REFERENCES tl_laimag10_imagegallery_item (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimag10_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   imageGallery_item_uid bigint,
   user_uid bigint,
   complete tinyint(1),
   session_id bigint,
   primary key (uid),
   CONSTRAINT FK_NEW_1821149711_693580A438BF8DFE FOREIGN KEY (imageGallery_item_uid)
   		REFERENCES tl_laimag10_imagegallery_item (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1821149711_693580A441F9365D FOREIGN KEY (user_uid)
   		REFERENCES tl_laimag10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laimag10_configuration (
   uid bigint not null auto_increment,
   config_key varchar(30),
   config_value varchar(255),
   primary key (uid),
   UNIQUE KEY config_key (config_key)
);


ALTER TABLE tl_laimag10_imagegallery ADD CONSTRAINT FK_NEW_1821149711_89093BF758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_laimag10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
   		

INSERT INTO tl_laimag10_imagegallery (uid, title, lock_on_finished,
 instructions, next_image_title, content_in_use, define_later, content_id, 
 allow_share_images, allow_vote, allow_rank, reflect_on_activity, image_submit_notify) VALUES
  (1,'Image Gallery','0','Instructions ',1,0,0,${default_content_id},0,0,0,0,0);

INSERT INTO tl_laimag10_configuration (config_key, config_value) VALUES
  ('mediumImageDimensions',	'640');
INSERT INTO tl_laimag10_configuration (config_key, config_value) VALUES
  ('thumbnailImageDimensions', '100');

  
SET FOREIGN_KEY_CHECKS=1;