CREATE PROCEDURE `TL_LAFRUM11_GET_ALL_THREAD_MESSAGE_UIDS_TMP`()
BEGIN

PREPARE stmt FROM "drop temporary table if exists tl_lafrum11_thread_message_uid_temp";
EXECUTE stmt;

CALL WITH_EMULATOR(

"TL_LAFRUM11_ALL_THREAD_MEMBERS_TMP",
        
"select seq.uid as seq_uid, seq.root_message_uid as topic_uid, 
seq.message_uid as msg_uid, seq.message_level as message_level, m.body as body, 
m.parent_uid as parent_uid, m.uid as thread_uid
from tl_lafrum11_message_seq seq join tl_lafrum11_message m
where seq.message_level = 1 and seq.message_uid = m.uid; ",

"select chdseq.uid as seq_uid, chdseq.root_message_uid as topic_uid, 
chdseq.message_uid as msg_uid, chdseq.message_level as message_level, chdm.body as body, 
chdm.parent_uid as parent_uid, tm.thread_uid as thread_uid
from TL_LAFRUM11_ALL_THREAD_MEMBERS_TMP tm
join tl_lafrum11_message_seq chdseq 
join tl_lafrum11_message chdm
where tm.msg_uid = chdm.parent_uid and chdseq.message_uid = chdm.uid;",

"create temporary table tl_lafrum11_thread_message_uid_temp as 
(select thread_uid, seq_uid from TL_LAFRUM11_ALL_THREAD_MEMBERS_TMP order by thread_uid)",

1000,

""
);

END;

