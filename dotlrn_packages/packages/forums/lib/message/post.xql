<?xml version="1.0"?>

<queryset>

    <fullquery name="select_name">
        <querytext>
          select first_names || ' ' || last_name
          from persons
          where person_id = :user_id
        </querytext>
    </fullquery>

    <fullquery name="select_forum_id">
        <querytext>
            select forum_id
            from forums_messages
            where message_id = :parent_id
        </querytext>
    </fullquery>

</queryset>
