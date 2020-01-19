-- SQL statements to update from LAMS 2.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

UPDATE lams_tool SET pedagogical_planner_url='tool/lawiki10/pedagogicalPlanner.do' WHERE tool_signature='lawiki10';

SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lawiki10_wiki_page_temp;
create table tl_lawiki10_wiki_page_temp (
	uid bigint not null auto_increment, 
	wiki_uid bigint, 
	title varchar(255), 
	editable bit, 
	wiki_current_content bigint, 
	added_by bigint, 
	wiki_session_uid bigint, 
	primary key (uid), 
	unique key wiki_unique_key (wiki_uid, title, wiki_session_uid)
)ENGINE=InnoDB;

INSERT INTO tl_lawiki10_wiki_page_temp SELECT * from tl_lawiki10_wiki_page;
drop table if exists tl_lawiki10_wiki_page;
COMMIT;
rename table tl_lawiki10_wiki_page_temp to tl_lawiki10_wiki_page;
alter table tl_lawiki10_wiki_page add index wiki_page_index_1 (wiki_session_uid), add constraint wiki_page_fk_1 foreign key (wiki_session_uid) references tl_lawiki10_session (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_2 (wiki_uid), add constraint wiki_page_fk_2 foreign key (wiki_uid) references tl_lawiki10_wiki (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_3 (added_by), add constraint wiki_page_fk_3 foreign key (added_by) references tl_lawiki10_user (uid);
alter table tl_lawiki10_wiki_page add index wiki_page_index_4 (wiki_current_content), add constraint wiki_page_fk_4 foreign key (wiki_current_content) references tl_lawiki10_wiki_page_content (uid);

SET FOREIGN_KEY_CHECKS=1;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;