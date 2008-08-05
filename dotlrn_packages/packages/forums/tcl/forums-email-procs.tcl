ad_library {

    Forums Library

    @creation-date 2002-05-17
    @author Ben Adida <ben@openforce.biz>
    @cvs-id $Id$

}

namespace eval forum { namespace eval email {} }

ad_proc -public forum::email::create_forward_email {
    {-pre_body:required}
    message_passed
} {
    create email content to forward a message
} {
    # Get the message data array
    upvar $message_passed message

    # Variables for I18N message lookup:
    set posting_date $message(posting_date_ansi)
    set useScreenNameP [parameter::get -parameter "UseScreenNameP" -default 0]
    if {$useScreenNameP != 0} {
	set user_name $message(screen_name)
    } else {
	set user_name $message(user_name)
    }

    # Set up the message body
    set new_body "[ad_html_to_text -- $pre_body]"
    append new_body "\n\n===================================\n\n"
    append new_body "[_ forums.email_alert_body_header]
[_ forums.Forum_1] $message(forum_name)
Thread: $message(root_subject)\n\n"
    append new_body [ad_html_text_convert -from $message(format) -to text/plain -- $message(content)]

    append new_body "\n\n-- \n[ad_url][ad_conn package_url]message-view?[export_vars -anchor $message(message_id) [list [list message_id $message(root_message_id)]]]\n"

    return $new_body
}
