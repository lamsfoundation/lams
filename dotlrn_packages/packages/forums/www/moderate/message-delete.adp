<master>
  <property name="title">#forums.Confirm_Delete# @message.subject;noquote@</property>
  <property name="context">#forums.delete#</property>

  <include src="/packages/forums/lib/message/delete" &message="message" confirm_p="@confirm_p@" return_url="@return_url@" />
