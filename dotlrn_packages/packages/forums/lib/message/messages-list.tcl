set package_id [site_node::get_element -url $url -element package_id]

db_multirow -extend {link} messages messages {
        select m.subject, count(*) as n, max(m.message_id) as message_id,to_date(max(m.posting_date), 'YYYY-MM-DD HH24:MI:SS') as posted
        from forums_forums f, forums_messages m 
        where f.package_id = :package_id 
        and m.forum_id = f.forum_id 
        and posting_date > now() - '5 days'::interval group by subject order by max(m.posting_date) desc
} { 
        set posted [lc_time_fmt $posted "%x %X"]
        set link "message-view?message_id=$message_id"
}
