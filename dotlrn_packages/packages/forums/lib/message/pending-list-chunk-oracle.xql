<?xml version="1.0"?>

<queryset>
    <rdbms><type>oracle</type><version>8.1.6</version></rdbms>

    <fullquery name="select_pending_threads">
        <querytext>
            select message_id,
                   subject
            from forums_messages
            where forum_id = :forum_id
              and tree_sortkey in (select substr(tree_sortkey, 1, 6)
                                    from forums_messages_pending
                                    where forum_id = :forum_id)
            order by tree_sortkey

        </querytext>
    </fullquery>

</queryset>
