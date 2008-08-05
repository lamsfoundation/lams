<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

    <fullquery name="forum::message::do_notifications.select_forums_package_url">
        <querytext>
            select site_node__url(node_id)
            from site_nodes
            where object_id = (select package_id
                               from forums_forums
                               where forums_forums.forum_id = :forum_id)
        </querytext>
    </fullquery>

    <fullquery name="forum::message::url.select_forums_package_url">
        <querytext>
            select site_node__url(min(node_id))
            from site_nodes
            where object_id = (select package_id
                               from forums_forums
                               where forums_forums.forum_id = :forum_id)
        </querytext>
    </fullquery>

    <fullquery name="forum::forum::url.select_forums_package_url">
        <querytext>
            select site_node__url(min(node_id))
            from site_nodes
            where object_id = (select package_id
                               from forums_forums
                               where forums_forums.forum_id = :forum_id)
        </querytext>
    </fullquery>

    <fullquery name="forum::message::datasource.messages">
      <querytext>
        select subject, content, format
        from forums_messages
        where message_id=:message_id or (tree_sortkey between tree_left(:tree_sortkey) and tree_right(:tree_sortkey))
        and forum_id=:forum_id
        order by tree_sortkey
      </querytext>
    </fullquery>

</queryset>
