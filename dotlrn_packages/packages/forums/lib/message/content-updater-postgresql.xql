<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

       <fullquery name="select_message_responses">
        <querytext>
            select message_id,
                   0 as n_attachments,
                   subject,
                   content,
                   format,
                   person__name(user_id) as user_name,
                   to_char(posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   tree_level(tree_sortkey) as tree_level,
                   state,
                   user_id,
                   parent_id,
                   open_p,
                   max_child_sortkey
            from   $table_name
            where  forum_id = :forum_id
            and    tree_sortkey between tree_left(:tree_sortkey) and tree_right(:tree_sortkey)
            order  by $order_by
        </querytext>
    </fullquery>

    <fullquery name="select_message_responses_attachments">
        <querytext>
            select message_id,
                   (select count(*) from attachments where object_id = message_id) as n_attachments,
                   subject,
                   content,
                   format,
                   person__name(user_id) as user_name,
                   to_char(posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   tree_level(tree_sortkey) as tree_level,
                   state,
                   user_id,
                   parent_id,
                   open_p,
                   max_child_sortkey
            from   $table_name
            where  forum_id = :forum_id
            and    tree_sortkey between tree_left(:tree_sortkey) and tree_right(:tree_sortkey)
            order  by $order_by
        </querytext>
    </fullquery>


</queryset>
