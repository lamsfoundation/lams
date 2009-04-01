drop table if exists lams_presence_chat_msgs;
create table lams_presence_chat_msgs (uid bigint not null auto_increment, room_name varchar(255), from_user varchar(255), to_user varchar(255), date_sent datetime, message varchar(1023), primary key (uid))type=innodb;
