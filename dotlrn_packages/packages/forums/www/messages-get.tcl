ad_page_contract {
} {
    cid:integer
    {op noop}
    {sid nosid}
    {dynamicmode:integer}
}

if {$dynamicmode} {
    forum::message::get -message_id $cid -array message
    set message(content) [ad_html_text_convert -from $message(format) -to text/html -- $message(content)]
} else {
    set message(content) {}
}

