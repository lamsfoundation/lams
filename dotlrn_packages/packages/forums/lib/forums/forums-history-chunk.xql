<?xml version="1.0"?>

<queryset>
 <fullquery name="select_users_wrote_post">
        <querytext>
            select distinct forums_messages.user_id, persons.first_names, persons.last_name, count(user_id) as num_msg, to_char(max(last_child_post), 'YYYY-MM-DD HH24:MI:SS') as last_post
	    from persons, forums_messages
	    where forums_messages.user_id = persons.person_id and forums_messages.forum_id = :forum_id 
	    group by forums_messages.user_id, persons.first_names, persons.last_name
        </querytext>
    </fullquery>   
</queryset>
