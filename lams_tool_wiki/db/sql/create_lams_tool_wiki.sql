SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lawiki10_wiki (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  allow_learner_create_pages bit(1),
  allow_learner_insert_links bit(1),
  allow_learner_attach_images bit(1),
  notify_updates bit(1),
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  minimum_edits int(11),
  maximum_edits int(11),
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  wiki_main_page_uid bigint(20),
  submission_deadline datetime,
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lawiki10_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  wiki_uid bigint(20),
  wiki_main_page_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY (session_id),
  CONSTRAINT FKF01D63C260B3B03B FOREIGN KEY (wiki_uid)
  		REFERENCES tl_lawiki10_wiki (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

 CREATE TABLE tl_lawiki10_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  wiki_session_uid bigint(20),
  entry_uid bigint(20),
  wiki_edits int(11),
  PRIMARY KEY (uid),
  CONSTRAINT FKED5D7A1FD8004954 FOREIGN KEY (wiki_session_uid)
  		REFERENCES tl_lawiki10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lawiki10_wiki_page (
	uid bigint not null auto_increment, 
	wiki_uid bigint, 
	title varchar(255), 
	editable bit,
	deleted bit DEFAULT 0,
	wiki_current_content bigint, 
	added_by bigint, 
	wiki_session_uid bigint, 
	primary key (uid), 
	unique key wiki_unique_key (wiki_uid, title, wiki_session_uid),
	CONSTRAINT wiki_page_fk_1 FOREIGN KEY (wiki_session_uid)
			REFERENCES tl_lawiki10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT wiki_page_fk_2 FOREIGN KEY (wiki_uid)
    		REFERENCES tl_lawiki10_wiki (uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT wiki_page_fk_3 FOREIGN KEY (added_by)
    		REFERENCES tl_lawiki10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE tl_lawiki10_wiki_page_content (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  wiki_page_uid bigint(20),
  body mediumtext,
  editor bigint(20),
  edit_date datetime,
  version bigint(20),
  PRIMARY KEY (uid),
  CONSTRAINT FK528051242D44CCF8 FOREIGN KEY (wiki_page_uid)
  		REFERENCES tl_lawiki10_wiki_page (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK528051243233D952 FOREIGN KEY (editor)
  		REFERENCES tl_lawiki10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);


ALTER TABLE tl_lawiki10_wiki ADD CONSTRAINT FKED5E3E04A3FF7EC0 FOREIGN KEY (wiki_main_page_uid)
  		REFERENCES tl_lawiki10_wiki_page (uid) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_lawiki10_session ADD CONSTRAINT FKF01D63C2A3FF7EC0 FOREIGN KEY (wiki_main_page_uid)
  		REFERENCES tl_lawiki10_wiki_page (uid) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_4 FOREIGN KEY (wiki_current_content)
    		REFERENCES tl_lawiki10_wiki_page_content (uid) ON DELETE SET NULL ON UPDATE CASCADE;
    		
  		
INSERT INTO tl_lawiki10_wiki 
(
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_learner_create_pages, 
	allow_learner_insert_links, 
	allow_learner_attach_images,
	notify_updates,
	reflect_on_activity, 
	reflect_instructions, 
	minimum_edits, 
	maximum_edits
) 
VALUES
(
	"Wiki",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	1,
	1,
	1,
	0,
	0,
	"",
	0,
	0
);

INSERT INTO tl_lawiki10_wiki_page 
(
	wiki_uid, 
	title, 
	editable
)
VALUES
(
	1,
	"Wiki Main Page",
	1
);

INSERT INTO tl_lawiki10_wiki_page_content 
(
	wiki_page_uid,
	body,
	version
)
VALUES
(
	1,
	"<div>Wiki Body</div>",
	0
);

UPDATE tl_lawiki10_wiki_page SET wiki_current_content=1 where uid=1;
UPDATE tl_lawiki10_wiki SET wiki_main_page_uid=1 where uid=1;


SET FOREIGN_KEY_CHECKS=1;