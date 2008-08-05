ad_library {

    Forums Library - for Messages

    @creation-date 2002-05-20
    @author Ben Adida <ben@openforce.biz>
    @cvs-id $Id$

}

namespace eval forum::message {}

ad_proc -public forum::message::new {
    {-forum_id:required}
    {-message_id ""}
    {-parent_id ""}
    {-subject:required}
    {-content:required}
    {-format "text/plain"}
    {-user_id ""}
    -no_callback:boolean
} {
    create a new message
} {
    # If no user_id is provided, we set it
    # to the currently logged-in user
    if {[empty_string_p $user_id]} {
        set user_id [ad_conn user_id]
    }

    set original_message_id $message_id

    db_transaction {

        set var_list [list \
            [list forum_id $forum_id] \
            [list message_id $message_id] \
            [list parent_id $parent_id] \
            [list subject $subject] \
            [list content $content] \
            [list format $format] \
            [list user_id $user_id]]


        set message_id [package_instantiate_object -var_list $var_list forums_message]

        get -message_id $message_id -array message
        if {[info exists message(state)] && [string equal $message(state) approved]} {
            do_notifications -message_id $message_id -user_id $user_id
        }

    }  on_error {

        db_abort_transaction
        
        # Check to see if the message with a message_id matching the
        # message_id arguement was in the database before calling
        # this procedure.  If so, the error is due to a double click 
        # and we should continue without returning an error.
        
        if {![empty_string_p $original_message_id]} {
    	# The was a non-null message_id arguement
            if {[db_string message_exists_p  { *SQL* }]} {
    	    return $message_id
            } else {
                # OK - it wasn't a simple double-click, so bomb
                ad_return_error \
                    "OACS Internal Error" \
                    "Error in forums::message::new - $errmsg"
            }
        }
    }

    return $message_id
}
    
ad_proc -public forum::message::do_notifications {
    {-message_id:required}
    {-user_id ""}
} {
    # Select all the important information
    forum::message::get -message_id $message_id -array message

    set forum_id $message(forum_id)
    set url "[ad_url][db_string select_forums_package_url {}]"

    set useScreenNameP [parameter::get -parameter "UseScreenNameP" -default 0]
    if {($useScreenNameP == 0) && ($user_id != 0)} {
	if {[empty_string_p $user_id]} {
	    set user_id [ad_conn user_id]
	}
    } else {
        set user_id [party::get_by_email -email [ad_parameter -package_id [ad_acs_kernel_id] HostAdministrator]]
    }
    set notif_user $user_id

    set attachments [attachments::get_attachments -object_id $message(message_id)]

    set message_text [ad_html_text_convert -from $message(format) -to text/plain -- $message(content)]
    set message_html [ad_html_text_convert -from $message(format) -to text/html -- $message(content)]

    set html_version ""
    append html_version "Forum:  <a href=\"${url}forum-view?forum_id=$message(forum_id)\">$message(forum_name)</a><br>\n"
    append html_version "Thread: <a href=\"${url}message-view?message_id=$message(root_message_id)\">$message(root_subject)</a><br>\n"
    if {$useScreenNameP == 0} {
        append html_version "Author: <a href=\"mailto:$message(user_email)\">$message(user_name)</a><br>\n"
    } else {
        append html_version "Author: $message(screen_name)<br>\n"
    }
    append html_version "Posted: $message(posting_date)<br>"
    append html_version "\n<br>\n"
    append html_version $message_html
    append html_version "<p>   "

    if {[llength $attachments] > 0} {
    append html_version "Attachments:
                            <ul> "

    foreach attachment $attachments {
       append html_version "<li><a href=\"[lindex $attachment 2]\">[lindex $attachment 1]</a></li>"
            }
    append html_version "</ul>"

    }

    set html_version $html_version

    set text_version ""
    append text_version "
Forum: $message(forum_name)
Thread: $message(root_message_id)\n"
    if {$useScreenNameP == 0} {
	append text_version "Author: $message(user_name)"
    } else {
	append text_version "Author: $message(screen_name)"
    }
    append text_version "
Posted: $message(posting_date)
----------------------------------
$message_text
---------------------------------
To post a reply to this email or view this message go to: 
${url}message-view?message_id=$message(root_message_id)

To view Forum $message(forum_name) go to:
${url}forum-view?forum_id=$message(forum_id)
"
    # Do the notification for the forum
    notification::new \
        -type_id [notification::type::get_type_id \
        -short_name forums_forum_notif] \
        -object_id $message(forum_id) \
        -response_id $message(message_id) \
        -notif_subject "\[$message(forum_name)\] $message(subject)" \
        -notif_text $text_version \
        -notif_html $html_version

    
    # Eventually we need notification for the root message too
    notification::new \
        -type_id [notification::type::get_type_id \
        -short_name forums_message_notif] \
        -object_id $message(root_message_id) \
        -response_id $message(message_id) \
        -notif_subject "\[$message(forum_name)\] $message(subject)" \
        -notif_text $text_version \
        -notif_html $html_version
}
    
ad_proc -public forum::message::edit {
    {-message_id:required}
    {-subject:required}
    {-content:required}
    {-format:required}
    -no_callback:boolean
} {
    Editing a message. There is no versioning here!
    This means this function is for admins only!
} {
    # do the update
    db_dml update_message {}
    db_dml update_message_title {}

    if {!$no_callback_p} {
	callback forum::message_edit -package_id [ad_conn package_id] -message_id $message_id
    }
}

ad_proc -public forum::message::set_format {
    {-message_id:required}
    {-format:required}
} {
    set whether a message is HTML or not
} {
    # Straight update to the DB
    db_dml update_message_format
}

ad_proc -public forum::message::get {
    {-message_id:required}
    {-array:required}
} {
    get the fields for a message
} {
    # Select the info into the upvar'ed Tcl Array
    upvar $array row

    set query select_message

    if {[ad_conn isconnected] && [forum::attachments_enabled_p]} {
        set query select_message_with_attachment
    }

    if {![db_0or1row $query {} -column_array row]} {
        if {[array exists row]} {
            array unset row
        }
    } else {
        # Convert to user's date/time format
        set row(posting_date_ansi) [lc_time_system_to_conn $row(posting_date_ansi)]
        set row(posting_date_pretty) [lc_time_fmt $row(posting_date_ansi) "%x %X"]
    }
}

ad_proc -private forum::message::set_state {
    {-message_id:required}
    {-state:required}
} {
    Set the new state for a message
    Usually used for approval
} {
    set var_list [list \
        [list message_id $message_id] \
        [list state $state]]
    package_exec_plsql -var_list $var_list forums_message set_state
}

ad_proc -public forum::message::reject {
    {-message_id:required}
} {
    Reject a message
} {
    set_state -message_id $message_id -state rejected
}

ad_proc -public forum::message::approve {
    {-message_id:required}
} {
    approve a message
} {
    db_transaction {
        set_state -message_id $message_id -state approved
        do_notifications -message_id $message_id
    }
}

ad_proc -public forum::message::delete {
    {-message_id:required}
    -no_callback:boolean
} {
    delete a message and obviously all of its descendents
} {
    db_transaction {
	if {!$no_callback_p} {
	    callback forum::message_delete -package_id [ad_conn package_id] -message_id $message_id
	}

			if { [forum::use_ReadingInfo_p] && [expr { [db_string is_root "select parent_id from forums_messages where message_id = :message_id"] == "" } ] } {
				set db_antwort [db_string forums_reading_info__remove_msg {
        select forums_reading_info__remove_msg (
            :message_id
        );
    }]
			}


        # Remove the notifications
        notification::request::delete_all -object_id $message_id

        # Remove the message
        set var_list [list [list message_id $message_id]]
        package_exec_plsql -var_list $var_list forums_message delete_thread
    }
}

ad_proc -public forum::message::close {
    {-message_id:required}
} {
    close a thread
    This is not exactly a cheap operation if the thread is long
} {
    db_exec_plsql thread_close {}
}

ad_proc -public forum::message::open {
    {-message_id:required}
} {
    reopen a thread
    This is not exactly a cheap operation if the thread is long
} {
    db_exec_plsql thread_open {}
}
    
ad_proc -public forum::message::get_attachments {
    {-message_id:required}
} {
    get the attachments for a message
} {
    # If attachments aren't enabled, then we stop
    if {![forum::attachments_enabled_p]} {
        return [list]
    }

    return [attachments::get_attachments -object_id $message_id]
}

ad_proc -public forum::message::subject_sort_filter {
    -forum_id:required
    -order_by:required
} {
    Return a piece of HTML for toggling the sort order of threads (subjects)
    in a forum. The user can either sort by the first postings in subjects
    (the creation date of the subjects) or the last one.

    @author Peter Marklund
} {
    set subject_label "[_ forums.lt_First_post_in_subject]"
    set child_label "[_ forums.Last_post_in_subject]"
    set new_order_by [ad_decode $order_by posting_date last_child_post posting_date]

    set export_vars [ad_export_vars -override [list [list order_by $new_order_by]] {order_by forum_id}]
    set toggle_url "[ad_conn url]?${export_vars}"
    if { [string equal $order_by posting_date] } {
        # subject selected
        set subject_link "<b>$subject_label</b>"
        set child_link "<a href=\"$toggle_url\">$child_label</a>"
    } else {
        # child selected
        set subject_link "<a href=\"$toggle_url\">$subject_label</a>"
        set child_link "<b>$child_label</b>"
    }
    set sort_filter "$subject_link | $child_link"

    return $sort_filter
}

ad_proc -public forum::message::initial_message {
    {-forum_id {}}
    {-parent {}}
    {-message:required}
} {
    Create an array with values initialised for a new message.
} {
    upvar $message init_msg

    if { [empty_string_p $forum_id] && [empty_string_p $parent] } {
        return -code error [_ forums.lt_You_either_have_to]
    } 

    if { ![empty_string_p $parent] } {
        upvar $parent parent_msg

        set init_msg(parent_id) $parent_msg(message_id)
        set init_msg(forum_id) $parent_msg(forum_id)
        set init_msg(subject) \
            [forum::format::reply_subject $parent_msg(subject)]
    } else {
        set init_msg(forum_id) $forum_id
        set init_msg(parent_id) ""
    }
}
