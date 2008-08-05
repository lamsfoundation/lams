--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with significant concepts and code
-- lifted from Gilbert's UBB forums. Thanks Orchard Labs.
--

declare
    v_foo integer;
    v_impl_id integer;
begin
    -- forums_forum
    v_impl_id := acs_sc_impl.get_id (
          'NotificationType',             -- impl_contract_name
          'forums_forum_notif_type'       -- impl_name
    );

    select type_id into v_foo from notification_types
          where sc_impl_id = v_impl_id and short_name = 'forums_forum_notif';

    delete from notification_types_intervals
          where type_id = v_foo 
          and interval_id in ( 
          select interval_id
          from notification_intervals 
          where name in ('instant','hourly','daily')
    );

    delete from notification_types_del_methods
          where type_id = v_foo
          and delivery_method_id in (
                  select delivery_method_id
                  from notification_delivery_methods 
                  where short_name in ('email')
    );

    for row in (select type_id
          from notification_types
          where short_name in ('forums_forum_notif'))
          loop
                  notification_type.del(row.type_id);
          end loop;

    acs_sc_binding.del(
      contract_name => 'NotificationType',
      impl_name => 'forums_forum_notif_type'
    );

    v_foo := acs_sc_impl.delete_alias(
      'NotificationType',
      'forums_forum_notif_type',
      'GetURL'
    );

    v_foo := acs_sc_impl.delete_alias(
      'NotificationType',
      'forums_forum_notif_type',
      'ProcessReply'
    );

    acs_sc_impl.del(
      'NotificationType',
      'forums_forum_notif_type'
    );

    -- forums_message
    v_impl_id := acs_sc_impl.get_id (
          'NotificationType',             -- impl_contract_name
          'forums_message_notif_type'     -- impl_name
    );

    select type_id into v_foo from notification_types
          where sc_impl_id = v_impl_id and short_name = 'forums_message_notif';

    delete from notification_types_intervals
          where type_id = v_foo 
          and interval_id in ( 
          select interval_id
          from notification_intervals 
          where name in ('instant','hourly','daily')
    );

    delete from notification_types_del_methods
          where type_id = v_foo
          and delivery_method_id in (
                  select delivery_method_id
                  from notification_delivery_methods 
                  where short_name in ('email')
    );

    for row in (select type_id
          from notification_types
          where short_name in ('forums_message_notif'))
          loop
                  notification_type.del(row.type_id);
          end loop;

    acs_sc_binding.del(
      contract_name => 'NotificationType',
      impl_name => 'forums_message_notif_type'
    );

    v_foo := acs_sc_impl.delete_alias(
      'NotificationType',
      'forums_message_notif_type',
      'GetURL'
    );

    v_foo := acs_sc_impl.delete_alias(
      'NotificationType',
      'forums_message_notif_type',
      'ProcessReply'
    );

    acs_sc_impl.del(
      'NotificationType',
      'forums_message_notif_type'
    );
end;
/
show errors




