<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

    <fullquery name="select_pending_threads">
        <querytext>
            select message_id,
                   subject
            from forums_messages
            where forum_id = :forum_id
              and tree_sortkey in (select tree_ancestor_key(tree_sortkey, 1)
                                    from forums_messages_pending
                                    where forum_id = :forum_id)
            order by tree_sortkey

        </querytext>
    </fullquery>

</queryset>
