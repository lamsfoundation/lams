

CREATE TABLE tl_lanb11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_content_id BIGINT(20) UNIQUE NOT NULL
     , title TEXT
     , content TEXT
     , online_instructions TEXT
     , offline_instructions TEXT
     , define_later TINYINT(1)
     , force_offline TINYINT(1)
     , reflect_on_activity TINYINT(1)
     , reflect_instructions TEXT
     , content_in_use TINYINT(1)
     , creator_user_id BIGINT(20)
     , date_created DATETIME
     , date_updated DATETIME
     , PRIMARY KEY (uid)
)ENGINE=InnoDB;

CREATE TABLE tl_lanb11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_session_id BIGINT(20) UNIQUE NOT NULL
     , nb_session_name VARCHAR(255) NOT NULL
     , nb_content_uid BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_status VARCHAR(100)
     , PRIMARY KEY (uid)
     , INDEX (nb_content_uid)
     , CONSTRAINT FK_tl_lanb11_session_1 FOREIGN KEY (nb_content_uid)
                  REFERENCES tl_lanb11_content (uid)
)ENGINE=InnoDB;

CREATE TABLE tl_lanb11_user (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL
     , nb_session_uid BIGINT(20) NOT NULL
     , username VARCHAR(255)
     , fullname VARCHAR(255)
     , user_status VARCHAR(50)
     , PRIMARY KEY (uid)
     , INDEX (nb_session_uid)
     , CONSTRAINT FK_tl_lanb11_user_1 FOREIGN KEY (nb_session_uid)
                  REFERENCES tl_lanb11_session (uid)
)ENGINE=InnoDB;


CREATE TABLE tl_lanb11_attachment (
       attachment_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_content_uid BIGINT(20) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , uuid BIGINT(20) NOT NULL
     , version_id BIGINT(20)
     , online_file TINYINT(1) NOT NULL
     , PRIMARY KEY (attachment_id)
     , INDEX (nb_content_uid)
     , CONSTRAINT FK_tl_lanb11_attachment_1 FOREIGN KEY (nb_content_uid)
                  REFERENCES tl_lanb11_content (uid)
)ENGINE=InnoDB;

-- Default Content For Noticeboard Tool

INSERT INTO tl_lanb11_content ( nb_content_id,
								title,
								content,
								online_instructions,
								offline_instructions,
								define_later,
								force_offline,
								reflect_on_activity,
								reflect_instructions,
								content_in_use,
								date_created) 
VALUES (${default_content_id},
		'Noticeboard',
		'Content',
		'',
		'',
		0,
		0,
		0,
		'Reflect on noticeboard',
		0,
		now());
