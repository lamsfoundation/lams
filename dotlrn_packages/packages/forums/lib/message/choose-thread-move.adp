<property name="title">@title;noquote@</property>
<property name="context">@context;noquote@</property>
  
<p> #forums.Move_thread_to_thread#</p>
  
<form name="input" action="move-thread-thread" method="get">
<div><input type="hidden" name="msg_id" value="@msg_id@"></div>
<div><input type="hidden" name="confirm_p" value="@confirm_p@"></div>
<if @messages:rowcount@ eq 0>
    <p>#forums.Sorry_you_can_not_move_this_thread_There_are_no_more_threads#</p>
</if>
<else>
<listtemplate name="available_messages"></listtemplate>
<p></p>
<div><input type="submit" value="#forums.Move_thread#"></div>
</else>
</form>
