DROP TABLE IF EXISTS lams_presence_chat_msgs;
CREATE TABLE lams_presence_chat_msgs (
	uid bigint NOT NULL auto_increment,
	room_name VARCHAR(255),
	from_user VARCHAR(255),
	to_user VARCHAR(255),
	date_sent DATETIME,
	message VARCHAR(1023),
	PRIMARY KEY (uid)
)TYPE=InnoDB;

