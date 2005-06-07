CREATE TABLE lams.tl_lanb11_content (
       nb_content_id BIGINT(20) NOT NULL
     , title VARCHAR(255)
     , content TEXT
     , online_instructions TEXT
     , offline_instructions TEXT
     , define_later TINYINT(1)
     , force_offline TINYINT(1)
     , creator_user_id BIGINT(20)
     , date_created DATETIME
     , date_updated DATETIME
     , PRIMARY KEY (nb_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lanb11_session (
       nb_session_id BIGINT(20) NOT NULL
     , nb_content_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_status VARCHAR(100)
     , PRIMARY KEY (nb_session_id)
     , INDEX (nb_content_id)
     , CONSTRAINT FK_tl_lanb11_session_1 FOREIGN KEY (nb_content_id)
                  REFERENCES lams.tl_lanb11_content (nb_content_id)
)TYPE=InnoDB;

