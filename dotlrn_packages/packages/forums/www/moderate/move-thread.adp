<master>
<property name="title">@title;noquote@</property>
<property name="context">{@context;noquote@}</property>
  <h1>@title;noquote@</h1>

<table width="95%">
 
  #forums.Are_you_sure_you_want_to_move_this_message_and_obviously_all_of_its_descendents_lt#
  <p>
  
  <table style="color: black; background-color: @table_border_color@;" width="100%">
    <include src="/packages/forums/lib/message/row" &message="message" preview="1">
  </table>
  
  <p></p>
    
  <formtemplate id="confirmed_move"></formtemplate>

</table>
