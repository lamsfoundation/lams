<?xml version="1.0"?>
<queryset>

    <fullquery name="forum::edit.update_forum">
        <querytext>
            update forums_forums
            set name= :name,
                charter= :charter,
                presentation_type= :presentation_type,
                posting_policy= :posting_policy
            where forum_id = :forum_id
        </querytext>
    </fullquery>

    <fullquery name="forum::edit.update_forum_object">
        <querytext>
            update acs_objects 
            set title= :name
            where object_id = :forum_id
        </querytext>
    </fullquery>

    <fullquery name="forum::list_forums.select_forums">
        <querytext>
            select forum_id,
                   name,
                   posting_policy,
                   presentation_type
            from forums_forums
            where package_id = :package_id
            order by name
        </querytext>
    </fullquery>

    <fullquery name="forum::get.select_forum">      
        <querytext>
            select forums_forums.*
            from forums_forums
            where forums_forums.forum_id = :forum_id
        </querytext>
    </fullquery>

    <fullquery name="forum::enable.update_forum_enabled_p">
        <querytext>
            update forums_forums
            set enabled_p = 't'
            where forum_id = :forum_id
        </querytext>
    </fullquery> 

    <fullquery name="forum::disable.update_forum_disabled_p">
        <querytext>
            update forums_forums
            set enabled_p = 'f'
            where forum_id = :forum_id
        </querytext>
    </fullquery>

</queryset>
