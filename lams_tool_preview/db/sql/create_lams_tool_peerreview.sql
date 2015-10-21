SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_laprev11_peerreview;
drop table if exists tl_laprev11_session;
drop table if exists tl_laprev11_user;
create table tl_laprev11_peerreview (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished tinyint,
   show_ratings_left_for_user tinyint DEFAULT 1,
   instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   reflect_instructions text, 
   reflect_on_activity smallint,
   minimum_rates integer DEFAULT 0,
   maximum_rates integer DEFAULT 0,
   maximum_rates_per_user integer DEFAULT 0,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laprev11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   peerreview_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)ENGINE=InnoDB;
create table tl_laprev11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   peerreview_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
alter table tl_laprev11_peerreview add index FK_NEW_1661738138_89093BF758092FB (create_by), add constraint FK_NEW_1661738138_89093BF758092FB foreign key (create_by) references tl_laprev11_user (uid);
alter table tl_laprev11_session add index FK_NEW_1661738138_24AA78C530E79035 (peerreview_uid), add constraint FK_NEW_1661738138_24AA78C530E79035 foreign key (peerreview_uid) references tl_laprev11_peerreview (uid);
alter table tl_laprev11_user add index FK_NEW_1661738138_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1661738138_30113BFCEC0D3147 foreign key (session_uid) references tl_laprev11_session (uid);
alter table tl_laprev11_user add index FK_NEW_1661738138_30113BFC309ED320 (peerreview_uid), add constraint FK_NEW_1661738138_30113BFC309ED320 foreign key (peerreview_uid) references tl_laprev11_peerreview (uid);

INSERT INTO `tl_laprev11_peerreview` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `lock_on_finished`,
 `instructions`, `content_in_use`, `define_later`, `content_id`, `reflect_on_activity`, `show_ratings_left_for_user`) VALUES
  (1,NULL,NULL,NULL,'Peer review','0','Instructions ',0,0,${default_content_id},0, 1);
  
SET FOREIGN_KEY_CHECKS=1;
