<?xml version="1.0"?>
<queryset>

<fullquery name="forums::move_thread_thread::get_forum_name">
<querytext>
        select ff.name, fm.subject, fm.forum_id 
        from forums_forums ff, forums_messages fm 
        where fm.message_id =:message_id and ff.forum_id=fm.forum_id
</querytext>
</fullquery>


<fullquery name="forums::move_thread_thread::get_father_tree_sortkey">
<querytext>
        select tree_sortkey as father_tree_sortkey, max_child_sortkey as father_max_child_sortkey 
        from forums_messages 
        where message_id= :message_id
</querytext>
</fullquery>


<fullquery name="forums::move_thread_thread::update_child_thread_father">
<querytext>
	update forums_messages
	set forum_id = :forum_id, tree_sortkey = :join_tree_sortkey 
	where message_id = :msge_id
</querytext>
</fullquery>


<fullquery name="forums::move_thread_thread::update_final_reply_count">
<querytext>
	update forums_messages
	set reply_count = reply_count + 1, approved_reply_count = approved_reply_count + 1
	where message_id = :message_id
</querytext>
</fullquery>


<fullquery name="forums::move_thread_thread::update_thread_father">
<querytext>
	update forums_messages
	set parent_id = :message_id, tree_sortkey = :join_tree_sortkey, forum_id = :forum_id 
	where message_id = $message(message_id)
</querytext>
</fullquery>


<fullquery name="forums::move_thread_thread::select_data_forum_initial">
        <querytext>
		  select ff.max_child_sortkey, ff.thread_count, ff.approved_thread_count
		  from forums_forums ff
		  where ff.forum_id = $message(forum_id)		  
        </querytext>
    </fullquery>

    
<fullquery name="forums::move_thread_thread::update_forum_initial">
<querytext>
	update forums_forums
	set thread_count = :thread_count - 1, approved_thread_count = :approved_thread_count - 1, last_post = (select max(fm.last_child_post)
	    from forums_messages fm
            where fm.forum_id = $message(forum_id))
	where forum_id = $message(forum_id)
</querytext>
</fullquery>


<fullquery name="forums::move_thread_thread::update_forums_final">
<querytext>
        update forums_forums
        set last_post = (select max(last_child_post) 
	    from forums_messages
	    where forum_id = :forum_id)
        where forum_id = :forum_id
</querytext>
</fullquery>
    
</queryset>
