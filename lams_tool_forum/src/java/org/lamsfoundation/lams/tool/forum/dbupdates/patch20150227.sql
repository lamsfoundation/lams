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

