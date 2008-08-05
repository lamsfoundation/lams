-- forums/sql/oracle/upgrade-0.2d-0.3d.sql
--
-- Changes for scalability davis@xarg.net

create unique index forums_mess_forum_sk_un on forums_messages(forum_id, tree_sortkey);

