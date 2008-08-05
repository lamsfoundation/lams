<master>
<property name="title">Subscribe others to forum: @forum.name;noquote@</property>
<property name="context">@context;noquote@</property>

<form method="post" action="subscribe-others-2.tcl">

#forums.Enter_a_list_of_email#
<pre>
user@example.com, Jane, Austin
user2@example.com, Emily, Dickinson
user3@example.com
</pre>
<textarea rows="20" cols="60" name="emails"></textarea>
<br>
#forums.If_checked_create_users#
<input type="checkbox" value="t" name="create_new_users_p">

<p>
Additionally you can choose from any of the members below:
<p>@member_html;noquote@
<p>
#notifications.Frequency#: @interval_html;noquote@

<br>
#notifications.Delivery_Method#: @delivery_html;noquote@


@hidden_vars;noquote@

<br>
<input type="submit" value="Okay">

</form>
