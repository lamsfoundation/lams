<?xml version="1.0"?>
<queryset>

<fullquery name="forums::move_thread::get_subject_message">
<querytext>
        select subject, forum_id 
        from forums_messages 
        where message_id=:selected_message
</querytext>
</fullquery>


<fullquery name="forums::move_thread::get_father_tree_sortkey">
<querytext>
        select tree_sortkey as father_tree_sortkey, max_child_sortkey as father_max_child_sortkey
        from forums_messages 
        where message_id= :message_id
</querytext>
</fullquery>

<fullquery name="forums::move_thread::get_father_message_id">
<querytext>
        select message_id as father_message_id
        from forums_messages 
        where forum_id = $message(forum_id) and tree_sortkey = '$initial_tree_sortkey'
</querytext>
</fullquery>


<fullquery name="forums::move_thread::update_father_reply_count">
<querytext>
	update forums_messages
	set reply_count = reply_count - 1, approved_reply_count = approved_reply_count - 1 
	where message_id = $father_message_id
</querytext>
</fullquery>


<fullquery name="forums::move_thread::update_child_thread_father">
<querytext>
	update forums_messages
	set tree_sortkey = :join_tree_sortkey 
	where message_id = :msge_id
</querytext>
</fullquery>

<fullquery name="forums::move_thread::update_final_reply_count">
<querytext>
	update forums_messages
	set reply_count = reply_count + 1, approved_reply_count = approved_reply_count + 1
	where message_id = :message_id
</querytext>
</fullquery>


<fullquery name="forums::move_thread::update_thread_father">
<querytext>
	update forums_messages
	set parent_id = :message_id, tree_sortkey = :join_tree_sortkey 
	where message_id = $message(message_id)
</querytext>
</fullquery>


</queryset>