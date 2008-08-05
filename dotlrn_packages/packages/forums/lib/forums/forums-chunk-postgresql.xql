<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

    <fullquery name="select_forums">
        <querytext>
            select forums_forums_enabled.*,
                   approved_thread_count as n_threads,
                   to_char(last_post, 'YYYY-MM-DD HH24:MI:SS') as last_post_ansi,
                   $unread_or_new_query_clause
            from forums_forums_enabled
            where forums_forums_enabled.package_id = :package_id
            and (
                    forums_forums_enabled.posting_policy = 'open'
                 or forums_forums_enabled.posting_policy = 'moderated'
                 or 't' = acs_permission__permission_p(forums_forums_enabled.forum_id, :user_id,'forum_read')
                )
            order by forums_forums_enabled.name
        </querytext>
    </fullquery>

    <partialquery name="unread_or_new_query">
        <querytext>
	approved_thread_count-coalesce((SELECT forums_reading_info_user.threads_read
					FROM forums_reading_info_user
					WHERE forums_reading_info_user.forum_id=forums_forums_enabled.forum_id 
					AND forums_reading_info_user.user_id=:user_id), 0)
	as count_unread
        </querytext>
    </partialquery>


</queryset>
