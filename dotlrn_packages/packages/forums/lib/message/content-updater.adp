@message.content;noquote@
    <if @message.n_attachments@ not nil and @message.n_attachments@ gt 0>
      <div class="attachments">
        #forums.Attachments#
        <include src="attachment-list" &message="message">
      </div>
    </if>
  
