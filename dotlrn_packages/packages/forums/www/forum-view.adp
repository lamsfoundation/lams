<master>
  <property name="title">@page_title;noquote@</property>
  <property name="context">@context;noquote@</property>
  <property name="displayed_object_id">@forum_id;noquote@</property>

<h1>@page_title;noquote@</h1>
<if @searchbox_p@ true>
  <include src="/packages/forums/lib/search/search-form" forum_id="@forum_id@">
</if>

<p>
@notification_chunk;noquote@
</p>

<include src="/packages/forums/lib/message/threads-chunk" forum_id="@forum_id@" flush_p="@flush_p@" &="permissions" moderate_p="@permissions.moderate_p@" orderby="@orderby@" &="page" admin_p="@permissions.admin_p@">
