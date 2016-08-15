-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades
 
ALTER TABLE tl_lawiki10_session DROP FOREIGN KEY FKF01D63C260B3B03B;
ALTER TABLE tl_lawiki10_session ADD CONSTRAINT FKF01D63C260B3B03B FOREIGN KEY (`wiki_uid`)
REFERENCES `tl_lawiki10_wiki` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_session DROP FOREIGN KEY FKF01D63C2A3FF7EC0;
ALTER TABLE tl_lawiki10_session ADD CONSTRAINT FKF01D63C2A3FF7EC0 FOREIGN KEY (`wiki_main_page_uid`)
REFERENCES `tl_lawiki10_wiki_page` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lawiki10_user DROP FOREIGN KEY FKED5D7A1FD8004954;
ALTER TABLE tl_lawiki10_user ADD CONSTRAINT FKED5D7A1FD8004954 FOREIGN KEY (`wiki_session_uid`)
REFERENCES `tl_lawiki10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lawiki10_wiki DROP FOREIGN KEY FKED5E3E04A3FF7EC0;
ALTER TABLE tl_lawiki10_wiki ADD CONSTRAINT FKED5E3E04A3FF7EC0 FOREIGN KEY (`wiki_main_page_uid`)
REFERENCES `tl_lawiki10_wiki_page` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_1;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_1 FOREIGN KEY (`wiki_session_uid`)
REFERENCES `tl_lawiki10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_2;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_2 FOREIGN KEY (`wiki_uid`)
REFERENCES `tl_lawiki10_wiki` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_3;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_3 FOREIGN KEY (`added_by`)
REFERENCES `tl_lawiki10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_4;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_4 FOREIGN KEY (`wiki_current_content`)
REFERENCES `tl_lawiki10_wiki_page_content` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lawiki10_wiki_page_content DROP FOREIGN KEY FK528051242D44CCF8;
ALTER TABLE tl_lawiki10_wiki_page_content ADD CONSTRAINT FK528051242D44CCF8 FOREIGN KEY (`wiki_page_uid`)
REFERENCES `tl_lawiki10_wiki_page` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page_content DROP FOREIGN KEY FK528051243233D952;
ALTER TABLE tl_lawiki10_wiki_page_content ADD CONSTRAINT FK528051243233D952 FOREIGN KEY (`editor`)
REFERENCES `tl_lawiki10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;