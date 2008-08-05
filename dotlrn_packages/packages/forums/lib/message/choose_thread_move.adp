<property name="title">@title;noquote@</property>
<property name="context">@context;noquote@</property>
  
<p> #forums.Move_thread_to_thread#</p>
  
<form name="input" action="move-thread-thread" method="get">
<input type="hidden" name="msg_id" value="@msg_id@">
<input type="hidden" name="confirm_p" value="@confirm_p@">
<if @messages:rowcount@ eq 0>
    #forums.Sorry_you_can_not_move_this_thread_There_are_no_more_threads#
</if>
<else>
<listtemplate name="available_messages"></listtemplate>
<p></p>
<input type="submit" value="#forums.Move_thread#">
</else>
</form>