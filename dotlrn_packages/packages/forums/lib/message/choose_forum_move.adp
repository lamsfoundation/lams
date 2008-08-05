<property name="title">@title;noquote@</property>
<property name="context">@context;noquote@</property>
  
<p> #forums.Move_thread_to_forum#</p>
  
<form name="input" action="move"  method="get">
<input type="hidden" name="message_id" value="@message_id@">
<input type="hidden" name="confirm_p" value="@confirm_p@">
<if @forums:rowcount@ eq 0>
  <p>#forums.Sorry_you_can_not_move_this_thread_There_are_no_other_forums#
</if>
<else>
<listtemplate name="available_forums"></listtemplate>
<p></p>
<input type="submit" value="#forums.Move_thread#">
</else>
</form>