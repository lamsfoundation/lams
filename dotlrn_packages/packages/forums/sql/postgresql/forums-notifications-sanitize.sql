--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with significant concepts and code
-- lifted from Gilbert's UBB forums. Thanks Orchard Labs.
--

create function inline_0 ()
returns integer as '
declare
    row                             record;
begin
    for row in select nt.type_id
               from notification_types nt
               where nt.short_name in (''forums_forum_notif'', ''forums_message_notif'')
    loop
        perform notification_type__delete(row.type_id);
    end loop;

    return null;
end;' language 'plpgsql';

select inline_0();
drop function inline_0 ();

--
-- Service contract drop stuff was missing - Roberto Mello 
--

create function inline_0() returns integer as '
declare
        impl_id integer;
        v_foo   integer;
begin

        -- the notification type impl
        impl_id := acs_sc_impl__get_id (
                      ''NotificationType'',		-- impl_contract_name
                      ''forums_forum_notif_type''	-- impl_name
        );

        PERFORM acs_sc_binding__delete (
                    ''NotificationType'',
                    ''forums_forum_notif_type''
        );

        v_foo := acs_sc_impl_alias__delete (
                    ''NotificationType'',		-- impl_contract_name	
                    ''forums_forum_notif_type'',	-- impl_name
                    ''GetURL''				-- impl_operation_name
        );

        v_foo := acs_sc_impl_alias__delete (
                    ''NotificationType'',		-- impl_contract_name	
                    ''forums_forum_notif_type'',	-- impl_name
                    ''ProcessReply''			-- impl_operation_name
        );

	select into v_foo type_id 
	  from notification_types
	 where sc_impl_id = impl_id
	  and short_name = ''forums_forum_notif'';

	perform notification_type__delete (v_foo);

	delete from notification_types_intervals
	 where type_id = v_foo 
	   and interval_id in ( 
		select interval_id
		  from notification_intervals 
		 where name in (''instant'',''hourly'',''daily'')
	);

	delete from notification_types_del_methods
	 where type_id = v_foo
	   and delivery_method_id in (
		select delivery_method_id
		  from notification_delivery_methods 
		 where short_name in (''email'')
	);

        -- the notification type impl
        impl_id := acs_sc_impl__get_id (
                      ''NotificationType'',
                      ''forums_message_notif_type''
                   );

        PERFORM acs_sc_binding__delete (
                    ''NotificationType'',
                    ''forums_message_notif_type''
        );

        v_foo := acs_sc_impl_alias__delete (
                    ''NotificationType'',
                    ''forums_message_notif_type'',
                    ''GetURL''
        );

        v_foo := acs_sc_impl_alias__delete (
                    ''NotificationType'',
                    ''forums_message_notif_type'',
                    ''ProcessReply''
        );

	select into v_foo type_id 
	  from notification_types
	 where sc_impl_id = impl_id
	   and short_name = ''forums_message_notif'';

	perform notification_type__delete (v_foo);

	delete from notification_types_intervals
	 where type_id = v_foo 
	   and interval_id in ( 
		select interval_id
		  from notification_intervals 
		 where name in (''instant'',''hourly'',''daily'')
	);

	delete from notification_types_del_methods
	 where type_id = v_foo
	   and delivery_method_id in (
		select delivery_method_id
		  from notification_delivery_methods 
		 where short_name in (''email'')
	);

	return (0);
end;
' language 'plpgsql';

select inline_0();
drop function inline_0();
