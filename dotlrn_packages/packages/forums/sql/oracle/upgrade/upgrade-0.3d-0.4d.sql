-- Need these two for RI checks 
create index forums_messages_user_id_idx ON forums_messages(user_id);
create index forums_messages_parent_id_idx ON forums_messages(parent_id);
