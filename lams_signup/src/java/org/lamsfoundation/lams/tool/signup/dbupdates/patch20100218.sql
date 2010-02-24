CREATE TABLE lams_signup_organisation (
       signup_organisation_id bigint(20) not null auto_increment,
       organisation_id bigint(20) not null,
       add_to_lessons tinyint(1) default 1,
       add_as_staff tinyint(1) default 0,
       course_key varchar(255),
       blurb text,
       create_date datetime,
       disabled tinyint(1) default 0,
       context varchar(255) unique,
       primary key (signup_organisation_id),
       index (organisation_id)
)TYPE=InnoDB;

CREATE TABLE lams_signup_user (
  signup_user_id bigint(20) not null auto_increment,
  user_id bigint(20) not null,
  organisation_id bigint(20) not null,
  signup_date datetime not null,
  primary key (signup_user_id)
)TYPE=InnoDB;