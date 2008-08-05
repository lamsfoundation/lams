<?xml version="1.0"?>

<queryset>
    <rdbms><type>oracle</type><version>8.1.6</version></rdbms>
    
    <fullquery name="get_tree_sortkey">
        <querytext>
            select substr(tree_sortkey, 0, 6) as father_tree_sortkey 
	    from forums_messages 
	    where message_id=$message(message_id)

        </querytext>
    </fullquery>
    

    <fullquery name="get_parent_id">
        <querytext>
            select parent_id
	    from forums_messages
	    where forum_id = $message(forum_id) and tree_sortkey between tree.left('$father_tree_sortkey') and tree.right('$father_tree_sortkey') 
	    order by tree_sortkey desc

        </querytext>
    </fullquery>

</queryset>
