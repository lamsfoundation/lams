ad_page_contract {
    
   Update the content of an specified message, for preloading purpuoses

    @author Veronica De La Cruz (veronica@viaro.net)
    @creation-date 2006-04-21
    

} {
    message_id:integer,notnull
    {table_name "forums_messages" }
}


# Get the message information
forum::message::get -message_id $message_id -array message
if {![array exists message]} {
    ns_returnnotfound
    ad_script_abort
}

# Load up the forum information
forum::get -forum_id $message(forum_id) -array forum
