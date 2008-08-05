<property name="title">@title;noquote@</property>
<property name="context">@context;noquote@</property>
  
<p> #forums.Move_message_to_message#</p>
  
<form name="input" action="move-thread" method="get">
<input type="hidden" name="msg_id" value="@msg_id@">
<input type="hidden" name="confirm_p" value="@confirm_p@">
<if @messages:rowcount@ eq 0 >
    <p>#forums.Sorry_you_can_not_move_this_message_There_are_no_other_threads#</p>    
</if>
<else>
<listtemplate name="available_messages"></listtemplate>
<p></p>
<input type="submit" value="#forums.Move_message#">
</else>
</form>