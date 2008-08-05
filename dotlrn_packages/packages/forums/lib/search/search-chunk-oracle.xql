<?xml version="1.0"?>

<queryset>
    <rdbms><type>oracle</type><version>8.1.6</version></rdbms>

    <fullquery name="search_all_forums">
        <querytext>
            select forums_messages.*,
                   person.name(forums_messages.user_id) as user_name,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   100 as the_score
            from   forums_messages,
                   forums_forums
            where  forums_messages.forum_id = forums_forums.forum_id
            and    forums_forums.package_id = :package_id
            and    forums_messages.state = 'approved'
            and    upper(forums_messages.subject || ' ' || dbms_lob.substr(forums_messages.content,2500) || ' ' || person.name(forums_messages.user_id))
                       like '%' || upper(:search_text) || '%'
            order  by forums_messages.posting_date desc
        </querytext>
    </fullquery>

    <fullquery name="search_one_forum">
        <querytext>
            select forums_messages.*,
                   person.name(forums_messages.user_id) as user_name,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   100 as the_score
            from   forums_messages,
                   forums_forums
            where  forums_forums.forum_id = :forum_id
            and    forums_forums.package_id = :package_id
            and    forums_messages.forum_id = forums_forums.forum_id
            and    forums_messages.state = 'approved'
            and    upper(forums_messages.subject || ' ' || dbms_lob.substr(forums_messages.content,3500) || ' ' || person.name(forums_messages.user_id))
                       like '%' || upper(:search_text) || '%'
            order  by forums_messages.posting_date desc
        </querytext>
    </fullquery>

    <fullquery name="search_all_forums_intermedia">
        <querytext>
            select forums_messages.*,
                   person.name(forums_messages.user_id) as user_name,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   score(1) as the_score
            from forums_messages,
                 forums_forums
            where forums_messages.forum_id = forums_forums.forum_id
            and forums_forums.package_id = :package_id
            and forums_messages.state = 'approved'
            and contains(forums_messages.content, '%' || :search_text || '%', 1) > 0
            order by the_score desc,
                     forums_messages.posting_date desc
        </querytext>
    </fullquery>

    <fullquery name="search_one_forum_intermedia">
        <querytext>
            select forums_messages.*,
                   person.name(forums_messages.user_id) as user_name,
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi,
                   score(1) as the_score
            from forums_messages,
                 forums_forums
            where forums_forums.forum_id = :forum_id
            and forums_forums.package_id = :package_id
            and forums_messages.forum_id = forums_forums.forum_id
            and forums_messages.state = 'approved'
            and contains(forums_messages.content, '%' || :search_text || '%', 1) > 0
            order by the_score desc,
                     forums_messages.posting_date desc
        </querytext>
    </fullquery>

</queryset>
