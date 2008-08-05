<if @rownum@ odd><div id="@message.message_id@" class="odd level@message.tree_level@"></if>
<else><div id="@message.message_id@" class="even level@message.tree_level@"></else>       
            
 <if @preview@ nil>
    <div class="action-list" style="float: right; text-decoration: none; background-color: #f0f0f0; ">
    <div id= "actions@message.message_id@">
    <ul>
       <li><a href="message-post?parent_id=@message.message_id@" title="#forums.reply#" class="button">#forums.reply#</a></li>
      <li><a href="message-email?message_id=@message.message_id@" title="#forums.forward#" class="button">#forums.forward#</a></li>
      <if @moderate_p@>
        <li><a href="moderate/message-edit?message_id=@message.message_id@" class="button">#forums.Edit#</a></li>
        <li><a href="moderate/message-delete?message_id=@message.message_id@" class="button">#forums.delete#</a></li>	
	<if @message.parent_id@ nil>
	  <li><a href="moderate/thread-move?message_id=@message.message_id@" class="button">#forums.Move_thread_to_other_forum#</a></li>
	  <li><a href="moderate/thread-move-thread?message_id=@message.message_id@" class="button">#forums.Move_thread_to_other_thread#</a></li>
	</if>
	<else>
	  <li><a href="moderate/message-move?message_id=@message.message_id@" class="button">#forums.Move_to_other_thread#</a></li>
	</else>
        <if @forum_moderated_p@>
          <if @message.state@ ne approved>
            <li><a href="moderate/message-approve?message_id=@message.message_id@" class="button">#forums.approve#</a></li>
          </if>
          <if @message.state@ ne rejected and @message.max_child_sortkey@ nil>
            <li><a href="moderate/message-reject?message_id=@message.message_id@" class="button">#forums.reject#</a></li>
          </if>
        </if>
      </if>
    </ul>
    </div>
    </div>
 </if>



 <div class="details">
 <div style="float: left; background-color:#f0f0f0;">  
	<a  id="toggle@message.message_id@" href="#"  onclick="dynamicExpand('@message.message_id@');return false;"><img src="/resources/forums/Collapse16.gif" width="16" height="16" alt="+"/></a>       
        <if @total_number_messages@ le @max_number_messages@>
          <a href="#" title="#forums.Expand_only_direct_reply_of_message#" onclick="expandChilds('@message.message_id@','@children_direct_list@'); return false;"><img src="/resources/forums/expand.png"  width="20" height="20" alt="#forums.Expand_only_direct_reply_of_message#" border="none"/></a>          	
          <a href="#" title="Expand all messages" onclick="expandChilds('@message.message_id@','@children_string@'); return false;"><img  src="/resources/forums/ExpandAll16.gif" width="15" height="15" alt="#forums.Expand_all_messages#"  border="none"/></a>   
    	  <a href="#" title="Collapse all messages" onclick="collapseChilds('@message.message_id@','@children_string@'); return false;"><img  src="/resources/forums/CollapseAll16.gif" width="15" height="15" alt="#forums.Collapse_all_messages#" border="none"/></a> 
   	</if>
 </div>   
  

<div id= subject@message.message_id@ style="display:inline">
  <if @display_subject_p@ true>    
    <if @preview@ nil>   
        <div class="subject" >  
        &nbsp;
        <a href="@message.direct_url@" title="Direct link to this post" class="reference">@message.number@</a>:
        <a href="message-view?message_id=@message.message_id@" title="Link to this post on a separate page" class="alone">@message.subject@</a>
    </if>
    <else>
      <div class="subject">@message.subject@
    </else>
  </if>
  <else>
    <div class="subject"><a href="@message.direct_url@" title="Direct link to this post" class="reference">@message.number@</a>
  </else>
  <if @message.parent_number@ not nil>
    <span class="response">(response to <a href="@message.parent_direct_url@" class="reference">@message.parent_number@</a>)</span>
  </if>
  </div>
  <div class="attribution" id="attribution@message.message_id@">#forums.Posted_by# <a href="user-history?user_id=@message.user_id@">@message.user_name@</a> on <span class="post-date">@message.posting_date_pretty@</span></div>
  </div>
</div>


<div class="join">
<div id ="join@message.message_id@" style="display:none;">
    <if @display_subject_p@ true>
       <if @preview@ nil>
           &nbsp;
           <a href="@message.direct_url@" title="Direct link to this post" class="reference">@message.number@</a>:
           <a href="message-view?message_id=@message.message_id@" title="Link to this post on a separate page" class="alone">@message.subject@</a>
       </if>
       <else>
    	   @message.subject@
       </else>
    </if>
    <else>
    	   <a href="@message.direct_url@" title="Direct link to this post" class="reference">@message.number@</a>
    </else>
    <if @message.parent_number@ not nil>
        (response to <a href="@message.parent_direct_url@" class="reference">@message.parent_number@</a>)
    </if>
   	#forums.Posted_by# <a href="user-history?user_id=@message.user_id@">@message.user_name@</a> on <span class="post-date">@message.posting_date_pretty@</span>
</div>
</div>



 <div class="content">
 <div id="content@message.message_id@">
   <if @is_direct_child@ eq 1>
      @message.content;noquote@
      <if @message.n_attachments@ not nil and @message.n_attachments@ gt 0>
         <div class="attachments">
          #forums.Attachments#
          <include src="attachment-list" &message="message">
         </div>
      </if>  
   </if>
 </div>
 </div>
</div>

<!-------------- For IE  --------------->

 <if @rownum@ eq 0>
     <SCRIPT FOR=window EVENT=onload LANGUAGE="JScript">
          showExpandedOnLoad('@message.message_id@','@children_direct_list@','@children_string@');
     </SCRIPT>
 </if>
 

 <!--------------For other browsers --------------->
      <body onload="showExpandedOnLoad('@message.message_id@','@children_direct_list@','@children_string@')">
     
