SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_laimag10_attachment;
drop table if exists tl_laimag10_imagegallery;
drop table if exists tl_laimag10_imagegallery_item;
drop table if exists tl_laimag10_image_comment;
drop table if exists tl_laimag10_image_rating;
drop table if exists tl_laimag10_image_vote;
drop table if exists tl_laimag10_item_log;
drop table if exists tl_laimag10_session;
drop table if exists tl_laimag10_user;
drop table if exists tl_laimag10_configuration;

create table tl_laimag10_attachment (
   attachment_uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   imageGallery_uid bigint,
   primary key (attachment_uid)
)ENGINE=InnoDB;
create table tl_laimag10_imagegallery (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   lock_on_finished tinyint,
   instructions text,
   next_image_title bigint,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   allow_comment_images tinyint,
   allow_share_images tinyint,
   allow_vote tinyint,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint,
   allow_rank tinyint,
   image_submit_notify tinyint DEFAULT 0,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_imagegallery_item (
   uid bigint not null auto_increment,
   description text,
   title varchar(255),
   create_by bigint,
   create_date datetime,
   create_by_author tinyint,
   sequence_id integer,
   is_hide tinyint,
   imageGallery_uid bigint,
   original_file_uuid bigint,
   original_image_width integer,
   original_image_height integer,
   medium_file_uuid bigint,
   medium_image_width integer,
   medium_image_height integer,   
   thumbnail_file_uuid bigint,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_image_comment (
   uid bigint not null auto_increment,
   comment text,
   imageGallery_item_uid bigint,
   create_by bigint,
   create_date datetime,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_image_rating (
   uid bigint not null auto_increment,
   rating integer,
   imageGallery_item_uid bigint,
   create_by bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_image_vote (
   uid bigint not null auto_increment,
   is_voted tinyint,
   imageGallery_item_uid bigint,
   create_by bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   imageGallery_item_uid bigint,
   user_uid bigint,
   complete tinyint,
   session_id bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   imageGallery_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   imageGallery_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laimag10_configuration (
   uid bigint not null auto_increment,
   config_key varchar(30) unique,
   config_value varchar(255),
   primary key (uid)
)ENGINE=InnoDB;

alter table tl_laimag10_attachment add index FK_NEW_1821149711_1E7009430E79035 (imageGallery_uid), add constraint FK_NEW_1821149711_1E7009430E79035 foreign key (imageGallery_uid) references tl_laimag10_imagegallery (uid);
alter table tl_laimag10_imagegallery add index FK_NEW_1821149711_89093BF758092FB (create_by), add constraint FK_NEW_1821149711_89093BF758092FB foreign key (create_by) references tl_laimag10_user (uid);
alter table tl_laimag10_imagegallery_item add index FK_NEW_1821149711_F52D1F93758092FB (create_by), add constraint FK_NEW_1821149711_F52D1F93758092FB foreign key (create_by) references tl_laimag10_user (uid);
alter table tl_laimag10_imagegallery_item add index FK_NEW_1821149711_F52D1F9330E79035 (imageGallery_uid), add constraint FK_NEW_1821149711_F52D1F9330E79035 foreign key (imageGallery_uid) references tl_laimag10_imagegallery (uid);
alter table tl_laimag10_image_comment add index FK_tl_laimag10_image_comment_3 (imageGallery_item_uid), add constraint FK_tl_laimag10_image_comment_3 foreign key (imageGallery_item_uid) references tl_laimag10_imagegallery_item (uid);
alter table tl_laimag10_image_comment add index FK_tl_laimag10_image_comment_2 (create_by), add constraint FK_tl_laimag10_image_comment_2 foreign key (create_by) references tl_laimag10_user (uid);
alter table tl_laimag10_image_rating add index FK_tl_laimag10_image_rating_3 (imageGallery_item_uid), add constraint FK_tl_laimag10_image_rating_3 foreign key (imageGallery_item_uid) references tl_laimag10_imagegallery_item (uid);
alter table tl_laimag10_image_rating add index FK_tl_laimag10_image_rating_2 (create_by), add constraint FK_tl_laimag10_image_rating_2 foreign key (create_by) references tl_laimag10_user (uid);
alter table tl_laimag10_image_vote add index FK_tl_laimag10_image_vote_3 (imageGallery_item_uid), add constraint FK_tl_laimag10_image_vote_3 foreign key (imageGallery_item_uid) references tl_laimag10_imagegallery_item (uid);
alter table tl_laimag10_image_vote add index FK_tl_laimag10_image_vote_2 (create_by), add constraint FK_tl_laimag10_image_vote_2 foreign key (create_by) references tl_laimag10_user (uid);
alter table tl_laimag10_item_log add index FK_NEW_1821149711_693580A438BF8DFE (imageGallery_item_uid), add constraint FK_NEW_1821149711_693580A438BF8DFE foreign key (imageGallery_item_uid) references tl_laimag10_imagegallery_item (uid);
alter table tl_laimag10_item_log add index FK_NEW_1821149711_693580A441F9365D (user_uid), add constraint FK_NEW_1821149711_693580A441F9365D foreign key (user_uid) references tl_laimag10_user (uid);
alter table tl_laimag10_session add index FK_NEW_1821149711_24AA78C530E79035 (imageGallery_uid), add constraint FK_NEW_1821149711_24AA78C530E79035 foreign key (imageGallery_uid) references tl_laimag10_imagegallery (uid);
alter table tl_laimag10_user add index FK_NEW_1821149711_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1821149711_30113BFCEC0D3147 foreign key (session_uid) references tl_laimag10_session (uid);
alter table tl_laimag10_user add index FK_NEW_1821149711_30113BFC309ED320 (imageGallery_uid), add constraint FK_NEW_1821149711_30113BFC309ED320 foreign key (imageGallery_uid) references tl_laimag10_imagegallery (uid);

INSERT INTO `tl_laimag10_imagegallery` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, `lock_on_finished`,
 `instructions`, `next_image_title`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`, `allow_comment_images`, 
 `allow_share_images`, `allow_vote`, `allow_rank`, `reflect_on_activity`, `image_submit_notify`) VALUES
  (1,NULL,NULL,NULL,'Image Gallery','0','0','Instructions ',1,null,null,0,0,${default_content_id},0,0,0,0,0,0);

INSERT INTO `tl_laimag10_configuration` (`config_key`, `config_value`) VALUES
  ('mediumImageDimensions',	'640');
INSERT INTO `tl_laimag10_configuration` (`config_key`, `config_value`) VALUES
  ('thumbnailImageDimensions', '100');

  
SET FOREIGN_KEY_CHECKS=1;
