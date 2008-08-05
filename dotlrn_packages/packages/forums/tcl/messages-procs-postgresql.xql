<?xml version="1.0"?>

<queryset>
    <rdbms><type>postgresql</type><version>7.1</version></rdbms>

    <fullquery name="forum::message::get.select_message">
        <querytext>
            select forums_messages.*,
                   tree_level(forums_messages.tree_sortkey) as tree_level,
                   person__name(forums_messages.user_id) as user_name, 
                   party__email(forums_messages.user_id) as user_email,
                   forums_forum__name(forums_messages.forum_id) as forum_name, 
                   forums_message__root_message_id(forums_messages.message_id) as root_message_id,
		   users.screen_name,
                   (select fm2.subject
                    from forums_messages fm2 
                    where fm2.message_id = forums_message__root_message_id(forums_messages.message_id)) as root_subject, 
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi
            from forums_messages, users
            where forums_messages.message_id= :message_id
	    and forums_messages.user_id = users.user_id
        </querytext>
    </fullquery>

    <fullquery name="forum::message::get.select_message_with_attachment">
        <querytext>
            select forums_messages.*,
                   (select count(*) from attachments where object_id= message_id) as n_attachments,
                   person__name(forums_messages.user_id) as user_name, 
                   party__email(forums_messages.user_id) as user_email,
                   forums_forum__name(forums_messages.forum_id) as forum_name, 
                   forums_message__root_message_id(forums_messages.message_id) as root_message_id,
		   users.screen_name, 
                   (select fm2.subject
                    from forums_messages fm2 
                    where fm2.message_id = forums_message__root_message_id(forums_messages.message_id)) as root_subject, 
                   to_char(forums_messages.posting_date, 'YYYY-MM-DD HH24:MI:SS') as posting_date_ansi
            from forums_messages, users
            where forums_messages.message_id= :message_id
	    and forums_messages.user_id = users.user_id
        </querytext>
    </fullquery>

    <fullquery name="forum::message::do_notifications.select_forums_package_url">
        <querytext>
            select site_node__url(node_id)
            from site_nodes
            where object_id = (select package_id
                               from forums_forums
                               where forums_forums.forum_id = :forum_id)
        </querytext>
    </fullquery>

    <fullquery name="forum::message::close.thread_close">
        <querytext>
            select forums_message__thread_close(:message_id);
        </querytext>
    </fullquery>

    <fullquery name="forum::message::open.thread_open">
        <querytext>
            select forums_message__thread_open(:message_id);
        </querytext>
    </fullquery>

</queryset>
