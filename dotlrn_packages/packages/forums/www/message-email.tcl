ad_page_contract {
    
    Forward a message to a friend

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-28
    @cvs-id $Id$

} {
    message_id:integer,notnull
}

forum::security::require_read_message -message_id $message_id

# Get the message information
forum::message::get -message_id $message_id -array message
set message(tree_level) 0

form create message

element create message message_id \
    -label [_ forums.Message_ID] \
    -datatype integer \
    -widget hidden

forums::form::forward_message message

if {[form is_valid message]} {
    template::form get_values message message_id to_email subject pre_body

    # Create the email body
    set email_body [forum::email::create_forward_email -pre_body $pre_body message]

    # Send the email
    acs_mail_lite::send -to_addr $to_email \
        -from_addr [cc_email_from_party [ad_conn user_id]] \
        -subject $subject \
        -body $email_body

    ad_returnredirect "message-view?message_id=$message_id"
    ad_script_abort
}

if {[template::form is_request message]} {
  element set_properties message message_id -value $message_id
  element set_properties message subject -value $message(subject)
}

set context [list [list "./forum-view?forum_id=$message(forum_id)" "$message(forum_name)"]]
if {![empty_string_p $message(parent_id)]} {
    lappend context [list "./message-view?message_id=$message(root_message_id)" "[_ forums.Entire_Thread]"]
}
lappend context [list "./message-view?message_id=$message(message_id)" "$message(subject)"]
lappend context [_ forums.Email_to_a_friend]

set lang [ad_conn language]
template::head::add_css -href /resources/forums/forums.css -media all -lang $lang
template::head::add_css -alternate -href /resources/forums/flat.css -media all -lang $lang -title "flat"
template::head::add_css -alternate -href /resources/forums/flat-collapse.css -media all -lang $lang -title "flat-collapse"
template::head::add_css -alternate -href /resources/forums/collapse.css -media all -lang $lang -title "collapse"
template::head::add_css -alternate -href /resources/forums/expand.css -media all -lang $lang -title "expand"

ad_return_template
