<master>
<property name="title">#forums.Post_to_Forum# @forum.name;noquote@</property>
<property name="context">@context;noquote@</property>
<property name="focus">message.subject</property>

<include src="/packages/forums/lib/message/post" forum_id="@forum_id@" 
                             &parent_message="parent_message"
                             anonymous_allowed_p="@anonymous_allowed_p@"
                             attachments_enabled_p="@attachments_enabled_p@">
