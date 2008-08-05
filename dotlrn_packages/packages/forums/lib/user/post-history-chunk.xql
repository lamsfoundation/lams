<?xml version="1.0"?>

<queryset>

    <fullquery name="select_messages">
        <querytext>
            select forums_messages.message_id,
	           forums_forums.forum_id,
                   forums_messages.subject,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   forums_forums.forum_id,
                   forums_forums.name as forum_name
            from forums_messages,
                 forums_forums
            where forums_messages.user_id = :user_id
            and forums_messages.forum_id = forums_forums.forum_id
            and forums_forums.package_id = :package_id
        </querytext>
    </fullquery>

    <fullquery name="select_messages_by_forum">
        <querytext>
            select forums_messages.message_id,
	           forums_forums.forum_id,
                   forums_messages.subject,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   forums_forums.forum_id,
                   forums_forums.name as forum_name
            from forums_messages,
                 forums_forums
            where forums_messages.user_id = :user_id
            and forums_messages.forum_id = forums_forums.forum_id
            and forums_forums.package_id = :package_id
	    order by forums_forums.name asc, forums_messages.posting_date desc
        </querytext>
    </fullquery>
    
    <fullquery name="select_num_post">
        <querytext>
            select count(user_id) as num_msg, to_char(max(last_child_post), 'YYYY-MM-DD HH24:MI:SS') as last_post, forums_forums.name, forums_forums.forum_id
            from forums_messages, forums_forums
            where forums_messages.user_id = :user_id and forums_forums.forum_id = forums_messages.forum_id and package_id = :package_id
            group by forums_forums.name, forums_forums.forum_id  
	    order by forums_forums.name asc
        </querytext>
    </fullquery>

<fullquery name="pagination">
      <querytext>
            select forums_messages.message_id,
                   forums_messages.subject,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   forums_forums.forum_id,
                   forums_forums.name as forum_name
            from forums_messages,
                 forums_forums
            where forums_messages.user_id = :user_id
	    [template::list::filter_where_clauses -and -name messages]
            forums_messages.forum_id = forums_forums.forum_id
            and forums_forums.package_id = :package_id
            order by forums_messages.posting_date desc
      </querytext>
</fullquery>


</queryset>
