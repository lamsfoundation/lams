-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades
 
ALTER TABLE tl_lagmap10_marker DROP FOREIGN KEY FK10F22749529F7FD3;
ALTER TABLE tl_lagmap10_marker ADD CONSTRAINT FK10F22749529F7FD3 FOREIGN KEY (`created_by`)
REFERENCES `tl_lagmap10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lagmap10_marker DROP FOREIGN KEY FK10F2274974028C80;
ALTER TABLE tl_lagmap10_marker ADD CONSTRAINT FK10F2274974028C80 FOREIGN KEY (`gmap_uid`)
REFERENCES `tl_lagmap10_gmap` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lagmap10_marker DROP FOREIGN KEY FK10F22749C5F056D9;
ALTER TABLE tl_lagmap10_marker ADD CONSTRAINT FK10F22749C5F056D9 FOREIGN KEY (`gmap_session_uid`)
REFERENCES `tl_lagmap10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lagmap10_marker DROP FOREIGN KEY FK10F22749EF5F6920;
ALTER TABLE tl_lagmap10_marker ADD CONSTRAINT FK10F22749EF5F6920 FOREIGN KEY (`updated_by`)
REFERENCES `tl_lagmap10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lagmap10_session DROP FOREIGN KEY FK519D516774028C80;
ALTER TABLE tl_lagmap10_session ADD CONSTRAINT FK519D516774028C80 FOREIGN KEY (`gmap_uid`)
REFERENCES `tl_lagmap10_gmap` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lagmap10_user DROP FOREIGN KEY FK7CB3F69AC5F056D9;
ALTER TABLE tl_lagmap10_user ADD CONSTRAINT FK7CB3F69AC5F056D9 FOREIGN KEY (`gmap_session_uid`)
REFERENCES `tl_lagmap10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;