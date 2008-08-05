<?xml version="1.0"?>
<queryset>

    <fullquery name="callback::MergePackageUser::impl::forums.upd_poster">
        <querytext>	
	update forums_messages
	set last_poster = :to_user_id
	where last_poster = :from_user_id
        </querytext>
    </fullquery>

    <fullquery name="callback::MergePackageUser::impl::forums.upd_user_id">
        <querytext>	
        update forums_messages
	set user_id = :to_user_id
	where user_id = :from_user_id
        </querytext>
    </fullquery>

    <fullquery name="callback::MergeShowUserInfo::impl::forums.sel_user_id">
        <querytext>	
        select message_id, subject
	from forums_messages
	where user_id = :user_id
        </querytext>
    </fullquery>

    <fullquery name="callback::MergeShowUserInfo::impl::forums.sel_poster">
        <querytext>	
        select message_id, subject
	from forums_messages
	where last_poster = :user_id
        </querytext>
    </fullquery>
 
  <fullquery name="callback::search::datasource::impl::forums_forum.datasource">
    <querytext>
      select
        forum_id as object_id,
        name as title,
        charter as content,
        'text/plain' as mime,
        'text' as storage_type,
        '' as keywords
      from forums_forums
      where forum_id = :forum_id
    </querytext>
  </fullquery>

</queryset>
