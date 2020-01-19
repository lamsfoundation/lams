-- This patch file CANNOT be run as is or the Stored Procedure based thread id updates will fail.
-- The updates have been broken into a serious of batches - Batch 1 through Batch 7. Please
-- run each batch separately. 

-- They were designed to be run via auto update but the code should  also work pasted into a 
-- SQL management tool. The Stored Procedures are in Batches 3, 4 and 5. If you are using MySQL
-- Workbench you will need to create them using the Stored Procedure window (go to the Stored
-- Procedures list under the table list and do Create Stored Procedure). 

-- Batch 2 alters the table to add the new thread_message_uid. Batches 3, 4 and 5 set up the 
-- stored procs that will  update the existing data to use the new thread_message_uid. 
-- Batch 6 runs the stored procs to update the data and then cleans up the temporary data and 
-- stored procedures. Batch 2 (adding the column) should only be done once. 
-- Batches 3 thru 6 can be redone but all fourneed to be redone in order, as Batch 6 drops 
-- the stored procedures created in 3, 4 and 5. 

-- Batch 1 --------------------------------------------------------------------
-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here-------------------------

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lafrum11_forum DROP COLUMN online_instructions;
ALTER TABLE tl_lafrum11_forum DROP COLUMN offline_instructions;
ALTER TABLE tl_lafrum11_forum DROP COLUMN run_offline;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lafrum11';

--  patch20141216.sql
-- LDEV-3395 Implement a minimum number of characters for forum postings
ALTER TABLE tl_lafrum11_forum ADD COLUMN min_characters integer DEFAULT 0; 
ALTER TABLE tl_lafrum11_forum ADD COLUMN limited_min_characters tinyint DEFAULT 0;

UPDATE lams_tool SET tool_version='20141216' WHERE tool_signature='lafrum11';

--  patch20150217.sql
-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_lafrum11_forum MODIFY COLUMN reflect_instructions text;

-- Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;

-- End of Batch 1 --------------------------------------------------------------

-- Batch 2 --------------------------------------------------------------------
-- This is the start of the tricky updates. They were originally 5 patch files - 
-- patch20150224 thru patch20150228, all for LDEV-3443.

-- patch20150224.sql

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- Put all sql statements below here-------------------------

ALTER TABLE `tl_lafrum11_message_seq` 
ADD COLUMN `thread_message_uid` BIGINT(20) DEFAULT NULL,
ADD INDEX `fkfrum11mseqthread` (`thread_message_uid` ASC);
ALTER TABLE `tl_lafrum11_message_seq` 
ADD CONSTRAINT `fkfrum11mseqthread`
  FOREIGN KEY (`thread_message_uid`)
  REFERENCES `tl_lafrum11_message` (`uid`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

-- Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;

-- End of Batch 2 --------------------------------------------------------------

-- Batch 3 --------------------------------------------------------------------

-- patch20150225.sql
-- This stored proc is a generic stored proc that can be used to emulate
-- the WITH RECURSIVE feature not found in MySQL.
-- From http://guilhembichot.blogspot.co.uk/2013/11/with-recursive-and-mysql.html

CREATE PROCEDURE `with_emulator`(
recursive_table varchar(100),
initial_SELECT text, 
recursive_SELECT text,
final_SELECT text,
max_recursion int unsigned, 
create_table_options text 
)
BEGIN
  declare new_rows int unsigned;
  declare show_progress int default 0; 
  declare recursive_table_next varchar(120);
  declare recursive_table_union varchar(120);
  declare recursive_table_tmp varchar(120);
  set recursive_table_next  = concat(recursive_table, "_next");
  set recursive_table_union = concat(recursive_table, "_union");
  set recursive_table_tmp   = concat(recursive_table, "_tmp");
  SET @str = 
    CONCAT("CREATE TEMPORARY TABLE ", recursive_table, " ",
    create_table_options, " AS ", initial_SELECT);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  SET @str = 
    CONCAT("CREATE TEMPORARY TABLE ", recursive_table_union, " LIKE ", recursive_table);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  SET @str =
    CONCAT("CREATE TEMPORARY TABLE ", recursive_table_next, " LIKE ", recursive_table);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  if max_recursion = 0 then
    set max_recursion = 100; 
  end if;
  recursion: repeat
    SET @str =
      CONCAT("INSERT INTO ", recursive_table_union, " SELECT * FROM ", recursive_table);
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    set max_recursion = max_recursion - 1;
    if not max_recursion then
      if show_progress then
        select concat("max recursion exceeded");
      end if;
      leave recursion;
    end if;
    SET @str =
      CONCAT("INSERT INTO ", recursive_table_next, " ", recursive_SELECT);
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    select row_count() into new_rows;
    if show_progress then
      select concat(new_rows, " new rows found");
    end if;
    if not new_rows then
      leave recursion;
    end if;
    SET @str =
      CONCAT("ALTER TABLE ", recursive_table, " RENAME ", recursive_table_tmp);
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    SET @str =
      CONCAT("ALTER TABLE ", recursive_table_next, " RENAME ", recursive_table);
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    SET @str =
      CONCAT("ALTER TABLE ", recursive_table_tmp, " RENAME ", recursive_table_next);
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    SET @str =
      CONCAT("TRUNCATE TABLE ", recursive_table_next);
    PREPARE stmt FROM @str;
    EXECUTE stmt;
  until 0 end repeat;
  SET @str =
    CONCAT("DROP TEMPORARY TABLE ", recursive_table_next, ", ", recursive_table);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  SET @str =
    CONCAT("ALTER TABLE ", recursive_table_union, " RENAME ", recursive_table);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  SET @str = final_SELECT;
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  SET @str =
    CONCAT("DROP TEMPORARY TABLE ", recursive_table);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
END;

-- End of Batch 3 --------------------------------------------------------------

-- Batch 4 --------------------------------------------------------------------

-- patch20150226.sql
CREATE PROCEDURE `tl_lafrum11_get_all_thread_message_uids_tmp`()
BEGIN

PREPARE stmt FROM "drop temporary table if exists tl_lafrum11_thread_message_uid_tmp";
EXECUTE stmt;

CALL with_emulator(

"tl_lafrum11_recursive_tmp",
        
"select seq.uid as seq_uid, seq.root_message_uid as topic_uid, 
seq.message_uid as msg_uid, seq.message_level as message_level, m.body as body, 
m.parent_uid as parent_uid, m.uid as thread_uid
from tl_lafrum11_message_seq seq join tl_lafrum11_message m
where seq.message_level = 1 and seq.message_uid = m.uid; ",

"select chdseq.uid as seq_uid, chdseq.root_message_uid as topic_uid, 
chdseq.message_uid as msg_uid, chdseq.message_level as message_level, chdm.body as body, 
chdm.parent_uid as parent_uid, tm.thread_uid as thread_uid
from tl_lafrum11_recursive_tmp tm
join tl_lafrum11_message_seq chdseq 
join tl_lafrum11_message chdm
where tm.msg_uid = chdm.parent_uid and chdseq.message_uid = chdm.uid;",

"create temporary table tl_lafrum11_thread_message_uid_tmp as 
(select thread_uid, seq_uid from tl_lafrum11_recursive_tmp order by thread_uid)",

1000,

""
);

END;

-- End of Batch 4 --------------------------------------------------------------

-- Batch 5 --------------------------------------------------------------------

-- patch20150227.sql

CREATE PROCEDURE `tl_lafrum11_set_all_thread_message_uids_tmp`()
BEGIN

declare v_finished int default 0; 
declare v_thread_uid bigint(20);
declare v_seq_uid bigint(20);
        
DECLARE thread_cursor CURSOR FOR
	SELECT thread_uid, seq_uid FROM tl_lafrum11_thread_message_uid_tmp;

DECLARE CONTINUE HANDLER
    FOR NOT FOUND SET v_finished = 1;

OPEN thread_cursor;
 
get_thread: LOOP
 
	FETCH thread_cursor INTO v_thread_uid, v_seq_uid ;
	 
	IF v_finished = 1 THEN
		LEAVE get_thread;
	END IF;
	 
	update tl_lafrum11_message_seq set thread_message_uid = v_thread_uid where uid = v_seq_uid;
	 
END LOOP get_thread;
 
CLOSE thread_cursor;  

END;

-- End of Batch 5 --------------------------------------------------------------

-- Batch 6 --------------------------------------------------------------------

-- patch20150228.sql
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- Put all sql statements below here-------------------------

call tl_lafrum11_get_all_thread_message_uids_tmp();
call tl_lafrum11_set_all_thread_message_uids_tmp();

drop procedure if exists with_emulator;
drop procedure if exists tl_lafrum11_set_all_thread_message_uids_tmp;
drop procedure if exists tl_lafrum11_get_all_thread_message_uids_tmp;

drop temporary table if exists tl_lafrum11_thread_message_uid_tmp;
drop temporary table if exists tl_lafrum11_recursive_tmp;


-- Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;

-- End of Batch 6 --------------------------------------------------------------
-- End of thread id addition patches. All the rest of the updates should be able
-- to be run in one go.

-- Batch 7 --------------------------------------------------------------------
-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here-------------------------

-- patch20150930.sql
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lafrum11_tool_session ADD UNIQUE (session_id);

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lafrum11_attachment DROP FOREIGN KEY FK389AD9A2131CE31E;
ALTER TABLE tl_lafrum11_attachment ADD CONSTRAINT FK389AD9A2131CE31E FOREIGN KEY (`forum_uid`)
REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_attachment DROP FOREIGN KEY FK389AD9A2FE939F2A;
ALTER TABLE tl_lafrum11_attachment ADD CONSTRAINT FK389AD9A2FE939F2A FOREIGN KEY (`message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
 

ALTER TABLE tl_lafrum11_forum DROP FOREIGN KEY FK87917942E42F4351;
ALTER TABLE tl_lafrum11_forum ADD CONSTRAINT FK87917942E42F4351 FOREIGN KEY (`create_by`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

 
ALTER TABLE tl_lafrum11_message_rating DROP FOREIGN KEY FK_tl_lafrum11_message_rating_1;
ALTER TABLE tl_lafrum11_message_rating ADD CONSTRAINT FK_tl_lafrum11_message_rating_1 FOREIGN KEY (`user_id`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message_rating DROP FOREIGN KEY FK_tl_lafrum11_message_rating_2;
ALTER TABLE tl_lafrum11_message_rating ADD CONSTRAINT FK_tl_lafrum11_message_rating_2 FOREIGN KEY (`message_id`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_message_seq DROP FOREIGN KEY FKD2C71F8845213B4D;
ALTER TABLE tl_lafrum11_message_seq ADD CONSTRAINT FKD2C71F8845213B4D FOREIGN KEY (`root_message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message_seq DROP FOREIGN KEY FKD2C71F88FE939F2A;
ALTER TABLE tl_lafrum11_message_seq ADD CONSTRAINT FKD2C71F88FE939F2A FOREIGN KEY (`message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message_seq DROP FOREIGN KEY fkfrum11mseqthread;
ALTER TABLE tl_lafrum11_message_seq ADD CONSTRAINT fkfrum11mseqthread FOREIGN KEY (`thread_message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E8647A7264;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E8647A7264 FOREIGN KEY (`modified_by`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;
							 	
ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E8E42F4351;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E8E42F4351 FOREIGN KEY (`create_by`)
REFERENCES `tl_lafrum11_forum_user` (`uid`)	ON DELETE SET NULL ON UPDATE CASCADE;
							 	
ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E824089E4D;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E824089E4D FOREIGN KEY (`parent_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E8131CE31E;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E8131CE31E FOREIGN KEY (`forum_uid`)
REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
			
ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E89357B45B;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E89357B45B FOREIGN KEY (`forum_session_uid`)
REFERENCES `tl_lafrum11_tool_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
			

ALTER TABLE tl_lafrum11_tool_session DROP FOREIGN KEY FK5A04D7AE131CE31E;
ALTER TABLE tl_lafrum11_tool_session ADD CONSTRAINT FK5A04D7AE131CE31E FOREIGN KEY (`forum_uid`)
REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_timestamp DROP FOREIGN KEY ForumUserFK;
ALTER TABLE tl_lafrum11_timestamp ADD CONSTRAINT ForumUserFK FOREIGN KEY (`forum_user_uid`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_timestamp DROP FOREIGN KEY MessageFK;
ALTER TABLE tl_lafrum11_timestamp ADD CONSTRAINT MessageFK FOREIGN KEY (`message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_forum_user DROP FOREIGN KEY FK7B83A4A85F0116B6;
ALTER TABLE tl_lafrum11_forum_user ADD CONSTRAINT FK7B83A4A85F0116B6 FOREIGN KEY (`session_id`)
REFERENCES `tl_lafrum11_tool_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20160213.sql
--     LDEV-4218  Display messages as created in monitoring using a colour
ALTER TABLE tl_lafrum11_message  ADD COLUMN is_monitor SMALLINT(6) DEFAULT 0; 

-- patch20170101.sql
-- LDEV-4180
ALTER TABLE tl_lafrum11_forum MODIFY COLUMN allow_anonym TINYINT(1),
							  MODIFY COLUMN lock_on_finished TINYINT(1),
							  MODIFY COLUMN define_later TINYINT(1),
							  MODIFY COLUMN content_in_use TINYINT(1),
							  MODIFY COLUMN allow_edit TINYINT(1),
							  MODIFY COLUMN allow_rich_editor TINYINT(1),
							  MODIFY COLUMN allow_new_topic TINYINT(1),
							  MODIFY COLUMN allow_upload TINYINT(1),
							  MODIFY COLUMN allow_rate_messages TINYINT(1),
							  MODIFY COLUMN limited_input_flag TINYINT(1),
							  MODIFY COLUMN reflect_on_activity TINYINT(1),
							  MODIFY COLUMN notify_learners_on_forum_posting tinyint(1) DEFAULT 0,
   							  MODIFY COLUMN notify_teachers_on_forum_posting tinyint(1) DEFAULT 0,
   							  MODIFY COLUMN mark_release_notify tinyint(1) DEFAULT 0,
							  MODIFY COLUMN limited_min_characters tinyint(1) DEFAULT 0;
							  
ALTER TABLE tl_lafrum11_forum_user MODIFY COLUMN session_finished TINYINT(1);

ALTER TABLE tl_lafrum11_message MODIFY COLUMN is_authored TINYINT(1),
							    MODIFY COLUMN is_anonymous TINYINT(1),
							    MODIFY COLUMN hide_flag TINYINT(1),
							    MODIFY COLUMN is_monitor TINYINT(1) DEFAULT 0;
							    
ALTER TABLE tl_lafrum11_tool_session MODIFY COLUMN mark_released TINYINT(1);							    
							    
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lafrum11';

-- Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;

-- End of Batch 7 --------------------------------------------------------------
