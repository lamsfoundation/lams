<h2> <a href="@url@">Recent messages</a></h2>
<if @messages:rowcount@ gt 0>
<ul><multiple name="messages">
  <li> <a href="@url@/@messages.link@">@messages.subject@</a> <if @messages.n@ gt 1>[@messages.n@]</if> @messages.posted@</li>
</multiple></ul>
</if>
<else>
<em>none</em>
</else>
