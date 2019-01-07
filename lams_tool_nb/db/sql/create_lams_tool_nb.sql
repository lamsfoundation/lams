CREATE TABLE tl_lanb11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_content_id BIGINT(20) NOT NULL
     , title TEXT
     , content MEDIUMTEXT
     , define_later TINYINT(1)
     , reflect_on_activity TINYINT(1)
     , reflect_instructions MEDIUMTEXT
     , content_in_use TINYINT(1)
     , creator_user_id BIGINT(20)
     , date_created DATETIME
     , date_updated DATETIME
     , allow_comments tinyint(1) DEFAULT 0
     , comments_like_dislike tinyint(1) DEFAULT 0
     , allow_anonymous TINYINT(1) DEFAULT 0
     , PRIMARY KEY (uid)
     , UNIQUE KEY nb_content_id (nb_content_id)
);

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
);

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
);

-- Default Content For Noticeboard Tool

INSERT INTO tl_lanb11_content ( nb_content_id,
								title,
								content,
								define_later,
								reflect_on_activity,
								reflect_instructions,
								content_in_use,
								date_created) 
VALUES (${default_content_id},
		'Noticeboard',
		'Content',
		0,
		0,
		'Reflect on noticeboard',
		0,
		now());