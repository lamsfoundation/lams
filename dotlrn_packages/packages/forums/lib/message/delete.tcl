ad_page_contract {

    Delete a Message

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-24
    @cvs-id $Id$

} 

set table_border_color [parameter::get -parameter table_border_color]

# Confirmed?
if {$confirm_p} {
    # Delete the message and all children
    forum::message::delete -message_id $message(message_id)

    # Redirect to the forum
    ad_returnredirect "../forum-view?forum_id=$message(forum_id)"
    ad_script_abort
}

set message_id $message(message_id)

set message(tree_level) 0

set confirm_url [export_vars -base "message-delete" {message_id return_url {confirm_p 1}}]

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
