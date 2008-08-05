
--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with significant concepts and code
-- lifted from Gilbert's UBB forums. Thanks Orchard Labs.
--

-- the integration with Notifications

create function inline_0() returns integer as '
declare
        impl_id integer;
        v_foo   integer;
begin
        -- the notification type impl
        impl_id := acs_sc_impl__new (
                      ''NotificationType'',
                      ''forums_forum_notif_type'',
                      ''forums''
        );

        v_foo := acs_sc_impl_alias__new (
                    ''NotificationType'',
                    ''forums_forum_notif_type'',
                    ''GetURL'',
                    ''forum::notification::get_url'',
                    ''TCL''
        );

        v_foo := acs_sc_impl_alias__new (
                    ''NotificationType'',
                    ''forums_forum_notif_type'',
                    ''ProcessReply'',
                    ''forum::notification::process_reply'',
                    ''TCL''
        );

        PERFORM acs_sc_binding__new (
                    ''NotificationType'',
                    ''forums_forum_notif_type''
        );

        v_foo:= notification_type__new (
	        NULL,
                impl_id,
                ''forums_forum_notif'',
                ''Forum Notification'',
                ''Notifications for Entire Forums'',
		now(),
                NULL,
                NULL,
		NULL
        );

        -- enable the various intervals and delivery methods
        insert into notification_types_intervals
        (type_id, interval_id)
        select v_foo, interval_id
        from notification_intervals where name in (''instant'',''hourly'',''daily'');

        insert into notification_types_del_methods
        (type_id, delivery_method_id)
        select v_foo, delivery_method_id
        from notification_delivery_methods where short_name in (''email'');

        -- the notification type impl
        impl_id := acs_sc_impl__new (
                      ''NotificationType'',
                      ''forums_message_notif_type'',
                      ''forums''
                   );

        v_foo := acs_sc_impl_alias__new (
                    ''NotificationType'',
                    ''forums_message_notif_type'',
                    ''GetURL'',
                    ''forum::notification::get_url'',
                    ''TCL''
        );

        v_foo := acs_sc_impl_alias__new (
                    ''NotificationType'',
                    ''forums_message_notif_type'',
                    ''ProcessReply'',
                    ''forum::notification::process_reply'',
                    ''TCL''
        );

        PERFORM acs_sc_binding__new (
                    ''NotificationType'',
                    ''forums_message_notif_type''
        );

        v_foo:= notification_type__new (
		NULL,
                impl_id,
                ''forums_message_notif'',
                ''Message Notification'',
                ''Notifications for Message Thread'',
		now(),
                NULL,
                NULL,
		NULL
        );

        -- enable the various intervals and delivery methods
        insert into notification_types_intervals
        (type_id, interval_id)
        select v_foo, interval_id
        from notification_intervals where name in (''instant'',''hourly'',''daily'');

        insert into notification_types_del_methods
        (type_id, delivery_method_id)
        select v_foo, delivery_method_id
        from notification_delivery_methods where short_name in (''email'');

	return (0);
end;
' language 'plpgsql';

select inline_0();
drop function inline_0();