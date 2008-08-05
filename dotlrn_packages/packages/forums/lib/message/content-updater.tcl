ad_page_contract {
    
   Gets the content of an specified message

    @author Veronica De La Cruz (veronica@viaro.net)
    @creation-date 2006-04-21
    

}

 
   

# Set some  sql variables

set forum_id $message(forum_id)
set tree_sortkey $message(tree_sortkey)

if {[forum::attachments_enabled_p]} {
    set query select_message_responses_attachments
} else {
    set query select_message_responses
}

# Find the ordering of messages so it can be consistent
# with the code that is in thread-chunk

if { [string equal $forum(presentation_type) flat] } {
    set order_by "fma.posting_date, fma.tree_sortkey"
} else {
    set order_by "fma.tree_sortkey"
}


set message(content) [ad_html_text_convert -from $message(format) -to text/html -- $message(content)]

# convert emoticons to images if the parameter is set
if { [string is true [parameter::get -parameter DisplayEmoticonsAsImagesP -default 0]] } {
    set message(content) [forum::format::emoticons -content $message(content)]}
