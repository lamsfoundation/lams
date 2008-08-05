<if @rownum@ odd><div id="msg_@message.message_id@" class="odd level@message.tree_level@"></if>
<else><div id="msg_@message.message_id@" class="even level@message.tree_level@"></else>
    <div class="details">
  <div style="float: left; padding-right: 4px;">
    <a id="toggle@message.message_id@" class="dynexpanded" href="javascript:void(forums_toggle(@message.message_id@))" title="#forums.collapse_message#"><img src="/resources/forums/Collapse16.gif" width="16" height="16" alt="#forums.collapse#"></a>
  </div>
  <if @display_subject_p@ true>
    <if @preview@ nil>
      <div class="subject">
        <a href="@message.direct_url@" title="#forums.Direct_link_to_this_post#" class="reference">@message.number@</a>:
        <a href="message-view?message_id=@message.message_id@" title="#forums.link_on_separate_page#" class="alone">@message.subject@</a>
    </if>
    <else>
      <div class="subject">@message.subject@
    </else>
  </if>
  <else>
    <div class="subject"><a href="@message.direct_url@" title="#forums.Direct_link_to_this_post#" class="reference">@message.number@</a>
  </else>
  <if @message.parent_number@ not nil>
    <span class="response">(response to <a href="@message.parent_direct_url@" class="reference" title="#forums.Direct_link_to_this_post#">@message.parent_number@</a>)</span>
  </if>
    </div>
    <div class="attribution">
	#forums.Posted_by# 
      <if @useScreenNameP@>@message.screen_name@</if>
      <else><a href="user-history?user_id=@message.user_id@"
      title="#forums.show_posting_history_message_username#">@message.user_name@</a></else> #forums.on# <span class="post-date">@message.posting_date_pretty@</span>
  </div>
  </div>

  <div class="content">
  <div id="content@message.message_id@" class="dynexpanded">@message.content;noquote@
    <if @message.n_attachments@ not nil and @message.n_attachments@ gt 0>
      <div class="attachments">
        #forums.Attachments#
        <include src="attachment-list" &message="message">
      </div>
    </if>
  </div>
  </div>

    <if @preview@ nil>
  <div class="action-list">
    <ul>
      <if @presentation_type@ ne "flat"><li><a href="message-post?parent_id=@message.message_id@" class="button" title="#forums.reply#">#forums.reply#</a></li></if>
      <li><a href="message-email?message_id=@message.message_id@" class="button" title="#forums.forward#">#forums.forward#</a></li>
      <if @moderate_p@>
        <li><a href="moderate/message-edit?message_id=@message.message_id@" class="button" title="#forums.edit#">#forums.edit#</a></li>
        <li><a href="moderate/message-delete?message_id=@message.message_id@" class="button" title="#forums.delete#">#forums.delete#</a></li>	
	<if @message.parent_id@ nil>
	  <li><a href="moderate/thread-move?message_id=@message.message_id@" class="button" title="#forums.Move_thread_to_other_forum#">#forums.Move_thread_to_other_forum#</a></li>
	  <li><a href="moderate/thread-move-thread?message_id=@message.message_id@" class="button" title="#forums.Move_thread_to_other_thread#">#forums.Move_thread_to_other_thread#</a></li>
	</if>
	<else>
	  <li><a href="moderate/message-move?message_id=@message.message_id@" class="button" title="#forums.Move_to_other_thread#">#forums.Move_to_other_thread#</a></li>
	</else>
        <if @forum_moderated_p@>
          <if @message.state@ ne approved>
            <li><a href="moderate/message-approve?message_id=@message.message_id@" class="button" title="#forums.approve#">#forums.approve#</a></li>
          </if>
          <if @message.state@ ne rejected and @message.max_child_sortkey@ nil>
            <li><a href="moderate/message-reject?message_id=@message.message_id@" class="button" title="#forums.reject#">#forums.reject#</a></li>
          </if>
        </if>
      </if>
    </ul>
  </div>
    </if>

</div>
