SET FOREIGN_KEY_CHECKS=0;

create table tl_laprev11_peerreview (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished TINYINT(1),
   show_ratings_left_for_user TINYINT(1) DEFAULT 1,
   instructions MEDIUMTEXT,
   content_in_use TINYINT(1),
   define_later TINYINT(1),
   content_id bigint,
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity TINYINT(1),
   minimum_rates integer DEFAULT 0,
   maximum_rates integer DEFAULT 0,
   maximum_rates_per_user integer DEFAULT 0,
   self_review tinyint(1) DEFAULT 0,
   show_ratings_left_by_user tinyint(1) DEFAULT 0,
   tolerance TINYINT UNSIGNED NOT NULL DEFAULT 0,
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_laprev11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   peerreview_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK_NEW_1661738138_24AA78C530E79035 FOREIGN KEY (peerreview_uid)
   		REFERENCES tl_laprev11_peerreview (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laprev11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished TINYINT(1),
   session_uid bigint,
   peerreview_uid bigint,
   hidden TINYINT(1) DEFAULT '0',
   primary key (uid),
   UNIQUE KEY prev11uniqusersession (user_id,session_uid),
   CONSTRAINT FK_NEW_1661738138_30113BFC309ED320 FOREIGN KEY (peerreview_uid)
   		REFERENCES tl_laprev11_peerreview (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1661738138_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_laprev11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE tl_laprev11_peerreview ADD CONSTRAINT FK_NEW_1661738138_89093BF758092FB FOREIGN KEY (create_by)
   		REFERENCES tl_laprev11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
   		

INSERT INTO tl_laprev11_peerreview (uid, title, lock_on_finished,
 instructions, content_in_use, define_later, content_id, reflect_on_activity, show_ratings_left_for_user) VALUES
  (1,'Peer review','0','Instructions ',0,0,${default_content_id},0, 1);
  
SET FOREIGN_KEY_CHECKS=1;