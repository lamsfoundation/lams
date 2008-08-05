<?xml version="1.0"?>
<queryset>

    <rdbms><type>postgresql</type><version>7.2</version></rdbms>


    <fullquery name="forums::move_message::update_moved_msg">
        <querytext>
            update forums_messages
	    set forum_id = :forum_id, 
	    tree_sortkey = tree_increment_key(:max_tree_sortkey) 
	    where message_id = $message(message_id)
        </querytext>
    </fullquery>
    

    <fullquery name="forums::move_message::get_all_child">
        <querytext>
	    select message_id, substring(tree_sortkey, 9, length(tree_sortkey)) as child_tree_sortkey 
	    from forums_messages 
	    where forum_id = $message(forum_id) and tree_sortkey between tree_left('$message(tree_sortkey)') and tree_right ('$message(tree_sortkey)') order by tree_sortkey desc
        </querytext>
    </fullquery>


    <fullquery name="forums::move_message::update_forums_final">
        <querytext>
	    update forums_forums
	    set thread_count = :thread_count + 1, approved_thread_count = :approved_thread_count + 1, max_child_sortkey = tree_increment_key(max_child_sortkey), last_post =  (select max(last_child_post)
	        from forums_messages
	        where forum_id = :forum_id)
	    where forum_id = :forum_id
        </querytext>
    </fullquery>
    
    <fullquery name="forums::move_message::update_msg">
        <querytext>
            update forums_messages
            set forum_id = :forum_id, tree_sortkey = '00000000'
            where message_id = $message(message_id)
        </querytext>
    </fullquery>
            
</queryset>
