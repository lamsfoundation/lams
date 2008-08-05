    <p>#forums.lt_Please_confirm_the_fo#</p>
    <div id="forum-thread">
      <include src="row" &message="message" preview="1" />
    </div>
    <form action="message-post" method="post">
      <input type="hidden" name="form:id" value="message">
        @exported_vars;noquote@
        <if @parent_id@ nil>
          #forums.lt_Would_you_like_to_sub# 
          <input type="radio" name="subscribe_p" value="0" checked>#forums.No#</input>
          <input type="radio" name="subscribe_p" value="1">#forums.Yes#</input>

          <if @forum_notification_p@>
            <br /><small>#forums.lt_Note_that_you_are_alr#</small>
          </if>
        </if>
        <input type="submit" name="formbutton:post" value="#forums.Post#" />
        <input type="submit" name="formbutton:edit" value="#forums.Edit_again#" />
    </form>
