<?xml version="1.0"?>
<queryset>

    <fullquery name="forum::message::set_format.update_message_format">
        <querytext>
            update forums_messages
            set format = :format
            where message_id = :message_id
        </querytext>
    </fullquery>

    <fullquery name="forum::message::edit.update_message_title">
        <querytext>
            update acs_objects 
            set title = :subject
            where object_id = :message_id and object_type = 'forums_message'
        </querytext>
    </fullquery>

    <fullquery name="forum::message::edit.update_message">
        <querytext>
            update forums_messages
            set subject = :subject,
                content = :content,
                format = :format
            where message_id = :message_id
        </querytext>
    </fullquery>

    <fullquery name="forum::message::new.message_exists_p">
        <querytext>
	  select count(message_id)
	  from forums_messages 
	  where message_id = :message_id
        </querytext>
    </fullquery>

</queryset>
