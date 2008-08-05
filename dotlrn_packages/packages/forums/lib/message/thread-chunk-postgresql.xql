<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

    <fullquery name="select_message_ordering">
        <querytext>
	SELECT fma.message_id
        FROM   forums_messages_approved fma
        WHERE  fma.forum_id = :forum_id
          and    fma.tree_sortkey between (select fm.tree_sortkey from forums_messages fm where fm.message_id = :root_message_id)
          and    (select tree_right(fm.tree_sortkey) from forums_messages fm where fm.message_id = :root_message_id)
        ORDER  BY fma.message_id
        </querytext>
    </fullquery>

    <fullquery name="select_message_responses">
        <querytext>
            select message_id,
                   0 as n_attachments,
                   t.subject,
                   t.content,
                   t.format,
                   person__name(t.user_id) as user_name,
                   to_char(t.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   tree_level(t.tree_sortkey) as tree_level,
                   t.state,
                   t.user_id,
                   t.parent_id,
                   t.open_p,
                   t.max_child_sortkey,
                   u.screen_name
            from   $table_name t, users u
            where  t.forum_id = :forum_id
	    and	   u.user_id = t.user_id
            and    t.tree_sortkey between tree_left(:tree_sortkey) and tree_right(:tree_sortkey)
            order  by $order_by
        </querytext>
    </fullquery>

    <fullquery name="select_message_responses_attachments">
        <querytext>
            select message_id,
                   (select count(*) from attachments where object_id = t.message_id) as n_attachments,
                   t.subject,
                   t.content,
                   t.format,
                   person__name(t.user_id) as user_name,
                   to_char(t.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   tree_level(t.tree_sortkey) as tree_level,
                   t.state,
                   t.user_id,
                   t.parent_id,
                   t.open_p,
                   t.max_child_sortkey,
		   u.screen_name
            from   $table_name t, users u
            where  t.forum_id = :forum_id
	    and	   u.user_id = t.user_id
            and    t.tree_sortkey between tree_left(:tree_sortkey) and tree_right(:tree_sortkey)
            order  by $order_by
        </querytext>
    </fullquery>


</queryset>
