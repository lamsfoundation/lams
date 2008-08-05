<ul>
<if @pending_threads:rowcount@ eq 0>
<i>#forums.None#</i>
</if>
<else>
<multiple name="pending_threads">
<li><b><a href="../message-view?message_id=@pending_threads.message_id@">@pending_threads.subject@</a></b>
</multiple>
</else>
</ul>
