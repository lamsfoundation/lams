<master>
<property name="title">@title@</property>
<property name="context">@context@</property>

<h1>@display_title@</h1>
<P>
<blockquote>
<b>Learner's Introduction</b>:
<P>
<div style="background-color: #D6E3E6; padding-left: 12px; padding-right: 12px; padding-top: 8px; padding-bottom: 8px; font-size: 100%; border: 1px s\olid black">
@introduction;noquote@
</div>
</p>
</blockquote>  
</p>

<!-- open LAMS Learner Link --> 
<div class="body" align="center">
<a class="button" href="javascript:;" onClick="window.open('@lams_server_url@/LoginRequest?uid=@username@&method=learner&ts=@datetime@&sid=@server_id@&hash=@hashValue@&courseid=@course_id@&lsid=@learning_session_id@&country=AU&lang=EN','LAMS_Learner','height=600,width=800,resizable')">Open Lesson</a>  
</div>
<!-- end LAMS Learner -->

<if @admin_p@ eq 1>
 <P>
 <!-- open LAMS Monitor Link --> 
<div class="body" align="center">
  <a class="button" href="javascript:;" onClick="window.open('@lams_server_url@/LoginRequest?uid=@username@&method=monitor&ts=@datetime@&sid=@server_id@&hash=@hashmonitor@&courseid=@course_id@&lsid=@learning_session_id@&country=AU&lang=EN','LAMS_Monitor','height=600,width=800,resizable')">Monitor this Lesson</a>  
  </div>
 <!-- end LAMS Learner -->
 </p>
</if>

<p>&nbsp</p>
<hr size="1">
<table width="100%">
    <tr class="list-header">
        <th class="list" valign="top" style="background-color: #e0e0e0; font-size: 90%" colspan="2">

          Tell us what you think about  @display_title@ 

          <div style="float: right;">
            @general_comments_link;noquote@
          </div>

        </th>
    </tr>
      <tr class="list-odd">
        <td  valign="top" style="background-color: #f0f0f0; font-size: 80%" colspan="2">
          <if @the_comments@ not eq "">

            <ul>
              @the_comments;noquote@
            </ul>
          </if>

        </td>
      </tr>                                                                                                                                       
</table>
