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

