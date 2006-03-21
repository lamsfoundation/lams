CREATE TABLE lams.tl_lanb11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_content_id BIGINT(20) UNIQUE NOT NULL
     , title TEXT
     , content TEXT
     , online_instructions TEXT
     , offline_instructions TEXT
     , define_later TINYINT(1)
     , force_offline TINYINT(1)
     , content_in_use TINYINT(1)
     , creator_user_id BIGINT(20)
     , date_created DATETIME
     , date_updated DATETIME
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lanb11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_session_id BIGINT(20) UNIQUE NOT NULL
     , nb_content_uid BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_status VARCHAR(100)
     , PRIMARY KEY (uid)
     , INDEX (nb_content_uid)
     , CONSTRAINT FK_tl_lanb11_session_1 FOREIGN KEY (nb_content_uid)
                  REFERENCES lams.tl_lanb11_content (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lanb11_user (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) UNIQUE NOT NULL
     , nb_session_uid BIGINT(20) NOT NULL
     , username VARCHAR(50)
     , fullname VARCHAR(50)
     , user_status VARCHAR(50)
     , PRIMARY KEY (uid)
     , INDEX (nb_session_uid)
     , CONSTRAINT FK_tl_lanb11_user_1 FOREIGN KEY (nb_session_uid)
                  REFERENCES lams.tl_lanb11_session (uid)
)TYPE=InnoDB;


CREATE TABLE lams.tl_lanb11_attachment (
       attachment_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , nb_content_uid BIGINT(20) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , uuid BIGINT(20) NOT NULL
     , version_id BIGINT(20)
     , online_file TINYINT(1) NOT NULL
     , PRIMARY KEY (attachment_id)
     , INDEX (nb_content_uid)
     , CONSTRAINT FK_tl_lanb11_attachment_1 FOREIGN KEY (nb_content_uid)
                  REFERENCES lams.tl_lanb11_content (uid)
)TYPE=InnoDB;

