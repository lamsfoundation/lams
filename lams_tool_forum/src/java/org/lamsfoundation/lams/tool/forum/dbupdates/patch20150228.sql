SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------Put all sql statements below here-------------------------

call TL_LAFRUM11_GET_ALL_THREAD_MESSAGE_UIDS_TMP();
call TL_LAFRUM11_SET_ALL_THREAD_MESSAGE_UIDS_TMP();

drop procedure if exists WITH_EMULATOR;
drop procedure if exists TL_LAFRUM11_SET_ALL_THREAD_MESSAGE_UIDS_TMP;
drop procedure if exists TL_LAFRUM11_GET_ALL_THREAD_MESSAGE_UIDS_TMP;

drop temporary table if exists tl_lafrum11_thread_message_uid_temp;
drop temporary table if exists TL_LAFRUM11_ALL_THREAD_MEMBERS_TMP;

-- End of thread id addition patches.

-- --------------------Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;

