
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lagmap10_attachment;
drop table if exists tl_lagmap10_gmap;
drop table if exists tl_lagmap10_marker;
drop table if exists tl_lagmap10_session;
drop table if exists tl_lagmap10_user;
drop table if exists tl_lagmap10_configuration;
create table tl_lagmap10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, gmap_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lagmap10_gmap (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, allow_edit_markers bit, show_all_markers bit, limit_markers bit, max_markers integer, allow_zoom bit, allow_terrain bit, allow_satellite bit, allow_hybrid bit, map_center_latitude double precision, map_center_longitude double precision, map_zoom integer, map_type varchar(20), reflect_on_activity bit, reflect_instructions text, default_geocoder_address varchar(255), primary key (uid))ENGINE=InnoDB;
create table tl_lagmap10_marker (uid bigint not null auto_increment, longitude double precision, latitude double precision, info_window_message text, create_date datetime, update_date datetime, is_authored bit, gmap_uid bigint, title varchar(55), created_by bigint, updated_by bigint, gmap_session_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lagmap10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), gmap_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lagmap10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, gmap_session_uid bigint, primary key (uid))ENGINE=InnoDB;
create table tl_lagmap10_configuration (uid bigint not null auto_increment, config_key varchar(30) unique, config_value varchar(255), primary key (uid))ENGINE=InnoDB;
alter table tl_lagmap10_attachment add index FKBA2ECCB274028C80 (gmap_uid), add constraint FKBA2ECCB274028C80 foreign key (gmap_uid) references tl_lagmap10_gmap (uid);
alter table tl_lagmap10_marker add index FK10F2274974028C80 (gmap_uid), add constraint FK10F2274974028C80 foreign key (gmap_uid) references tl_lagmap10_gmap (uid);
alter table tl_lagmap10_marker add index FK10F22749C5F056D9 (gmap_session_uid), add constraint FK10F22749C5F056D9 foreign key (gmap_session_uid) references tl_lagmap10_session (uid);
alter table tl_lagmap10_marker add index FK10F22749EF5F6920 (updated_by), add constraint FK10F22749EF5F6920 foreign key (updated_by) references tl_lagmap10_user (uid);
alter table tl_lagmap10_marker add index FK10F22749529F7FD3 (created_by), add constraint FK10F22749529F7FD3 foreign key (created_by) references tl_lagmap10_user (uid);
alter table tl_lagmap10_session add index FK519D516774028C80 (gmap_uid), add constraint FK519D516774028C80 foreign key (gmap_uid) references tl_lagmap10_gmap (uid);
alter table tl_lagmap10_user add index FK7CB3F69AC5F056D9 (gmap_session_uid), add constraint FK7CB3F69AC5F056D9 foreign key (gmap_session_uid) references tl_lagmap10_session (uid);

-- Inserting the required config item into the config table
-- Gmap API key is added here for shaun so the Gmap works there automatically for each build
INSERT INTO tl_lagmap10_configuration (
	config_key, 
	config_value
)
VALUES(
	"GmapKey",
	"ABQIAAAAvPAE96y1iioFQOnrP1RCBxQ3ZLSPwrKlL-Fn7FdXNTuNedFYMRT30phEMjEwUhQPccHtJ9JNU1mknQ"
);


INSERT INTO tl_lagmap10_gmap (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_edit_markers, 
	show_all_markers,
	limit_markers, 
	max_markers, 
	allow_zoom, 
	allow_terrain, 
	allow_satellite, 
	allow_hybrid, 
	map_center_latitude,
	map_center_longitude, 
	map_zoom,
	map_type,
	reflect_on_activity, 
	reflect_instructions, 
	default_geocoder_address
) 
VALUES(
	"Gmap",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	1,
	1,
	0,
	1,
	1,
	0,
	1,
	1,
	-33.774322,
	151.111988,
	13,
	"G_NORMAL_MAP",
	0,
	"",
	"Macquarie University, Sydney NSW"
);

SET FOREIGN_KEY_CHECKS=1;
