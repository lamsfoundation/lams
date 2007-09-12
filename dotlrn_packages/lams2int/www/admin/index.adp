<master>
<property name="title">@title@</property>
<property name="context">@context@</property>

<p>
<a title="Add a LAMS lesson to this course" href="add" class="button"> Add a LAMS Lesson </a>
&nbsp;&nbsp;

<a title="Open LAMS Authoring to create or edit lessons" href="javascript:;" onClick="window.open('@lams_server_url@/LoginRequest?uid=@username@&method=author&ts=@datetime@&sid=@server_id@&hash=@hashauthor@&courseid=@course_id@&country=AU&lang=EN&requestSrc=@requestSrc@&notifyCloseURL=@notifyCloseURL@','LAMS_Author','height=600,width=800,resizable');" class="button"> LAMS Author </a>
&nbsp;&nbsp;

</p>

<listtemplate name="d_lesson"></listtemplate>
