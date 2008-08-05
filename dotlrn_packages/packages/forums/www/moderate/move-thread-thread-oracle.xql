<?xml version="1.0"?>
<queryset>

    <rdbms><type>oracle</type><version>8.1.6</version></rdbms>

    <fullquery name="forums::move_thread_thread::update_final_message">
        <querytext>
		  update forums_messages
		  set reply_count = reply_count + 1, approved_reply_count = approved_reply_count + 1, max_child_sortkey = tree.increment_key(max_child_sortkey) 
		  where message_id = :message_id
        </querytext>
    </fullquery>
    

    <fullquery name="forums::move_thread_thread::get_all_child">
        <querytext>		  
		  select message_id as msge_id, substr(tree_sortkey, 7, length(tree_sortkey)) as child_tree_sortkey
		  from forums_messages 
                  where forum_id = $message(forum_id) and tree_sortkey between tree.left('$message(tree_sortkey)') and tree.right('$message(tree_sortkey)')
		  order by tree_sortkey desc
        </querytext>
    </fullquery>
    
    <fullquery name="forums::move_thread_thread::update_last_child_post">
        <querytext>
	          update forums_messages
		  set last_child_post = (select max(last_child_post)
		      from forums_messages 
                      where forum_id = :forum_id and tree_sortkey between tree.left((select tree_sortkey from forums_messages where message_id = :message_id)) and tree.right((select tree_sortkey from forums_messages where message_id = :message_id)))
		  where message_id = :message_id     
        </querytext>
    </fullquery>
    
        
</queryset>
