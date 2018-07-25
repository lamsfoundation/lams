SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------Put all sql statements below here-------------------------

call tl_lafrum11_get_all_thread_message_uids_tmp();
call tl_lafrum11_set_all_thread_message_uids_tmp();

drop procedure if exists with_emulator;
drop procedure if exists tl_lafrum11_set_all_thread_message_uids_tmp;
drop procedure if exists tl_lafrum11_get_all_thread_message_uids_tmp;

drop temporary table if exists tl_lafrum11_thread_message_uid_tmp;
drop temporary table if exists tl_lafrum11_recursive_tmp;

-- End of thread id addition patches.

-- --------------------Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;

