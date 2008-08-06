ad_page_contract {
    
    Create a Forum

    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2008-07-23
    @cvs-id $Id$

} -query {
    {name ""}
    {toolContentID 0}
}

set url ""
ns_log Notice "URL: $url"

# if the session exists...
if {[nsv_exists A_$forum_id [ad_conn session_id]]} {

    # and the list element 1 (forum_id) is the same as the one passed,
    # then we know that we need to add the Next Activity button. 
    if {[string equal $forum_id [lindex [nsv_get A_$forum_id [ad_conn session_id]] 1]]} {

	set url [lindex [nsv_get A_$forum_id [ad_conn session_id]] 0]

    }
}

