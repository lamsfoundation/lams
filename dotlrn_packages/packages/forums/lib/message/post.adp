<if @parent_id@ ne "">
  <div id="forum-thread">
    <include src="row" &message="parent_message" preview="1">
  </div>
</if>
<if @useScreenNameP@>#forums.in_this_forum_your_screename_is_used# "<a href="@pvt_home@">@screen_name@</a>"</if><br>
<if @anonymous_allowed_p@>#forums.anonymious_posts_are_allowed#</if>
<formtemplate id="message"></formtemplate>
