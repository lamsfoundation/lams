<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

    <fullquery name="select_forums">
        <querytext>
	    select forums_forums.package_id,
            acs_object__name(apm_package__parent_id(forums_forums.package_id)) as parent_name,
            (select site_node__url(site_nodes.node_id)
            from site_nodes
            where site_nodes.object_id = forums_forums.package_id) as url,
            forums_forums.forum_id,
            forums_forums.name,
						$unread_or_new_query
            from forums_forums_enabled forums_forums,
            acs_objects
            where acs_objects.object_id = forums_forums.forum_id and 
            forums_forums.package_id in ([join $list_of_package_ids ,])
            $private_data_restriction
             order by parent_name,
             forums_forums.name
        </querytext>
    </fullquery>

</queryset>
