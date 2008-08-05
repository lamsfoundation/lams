<?xml version="1.0"?>
<queryset>
    <rdbms><type>oracle</type><version>8.1.6</version></rdbms>

    <fullquery name="forum::notification::get_url.select_forums_package_url">
        <querytext>
            select site_node.url(node_id)
            from site_nodes
            where object_id = (select package_id
                               from forums_forums
                               where forums_forums.forum_id = :forum_id)
        </querytext>
    </fullquery>

 
</queryset>










