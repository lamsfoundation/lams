<master>
  <property name="title">#forums.Email_Message# @message.forum_name;noquote@ - @message.subject;noquote@</property>
  <property name="context">@context;noquote@</property>

  <p>#forums.lt_Email_a_copy_of_the_f#</p>

  <div id="forum-thread">
    <include src="/packages/forums/lib/message/row" &message="message" preview=1 />
  </div>

  <formtemplate id="message"></formtemplate>
