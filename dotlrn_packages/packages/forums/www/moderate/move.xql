<?xml version="1.0"?>
<queryset>

<fullquery name="forums::move_message::select_num_msg">
<querytext>
        select count(forum_id) as num_post
        from forums_messages
        where forum_id = :forum_id
</querytext>
</fullquery>


<fullquery name="forums::move_message::select_tree_sortkey">
<querytext>
	select tree_sortkey
	from forums_messages
	where forum_id = :forum_id and parent_id is null order by tree_sortkey asc
</querytext>
</fullquery>


<fullquery name="forums::move_message::select_tree_sortkey_new">
<querytext>
	select tree_sortkey as message_tree_sortkey
	from forums_messages
	where message_id = $message(message_id)
</querytext>
</fullquery>

<fullquery name="forums::move_message::update_children">
<querytext>
	update forums_messages
	set forum_id = :forum_id, tree_sortkey = :join_tree_sortkey
	where message_id = :message_id
</querytext>
</fullquery>

<fullquery name="forums::move_message::select_new_data_forums_forums">
<querytext>
	select ff.max_child_sortkey, ff.thread_count, ff.approved_thread_count
	from forums_forums ff
	where ff.forum_id = :forum_id
</querytext>
</fullquery>

<fullquery name="forums::move_message::select_data_forum_initial">
<querytext>
         select ff.max_child_sortkey, ff.thread_count, ff.approved_thread_count
         from forums_forums ff
         where ff.forum_id = $message(forum_id)	 
</querytext>
</fullquery>

<fullquery name="forums::move_message::update_forum_initial">
<querytext>
        update forums_forums
	set thread_count = :thread_count - 1, approved_thread_count = :approved_thread_count -1, last_post = (select max(fm.last_child_post)
          from forums_messages fm
          where fm.forum_id = $message(forum_id))
	where forum_id = $message(forum_id)
</querytext>
</fullquery>         

</queryset>
