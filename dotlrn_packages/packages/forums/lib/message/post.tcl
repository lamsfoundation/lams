ad_page_contract {
    
    Form to create message and insert it

    @author Ben Adida (ben@openforce.net)
    @creation-date 2003-12-09
    @cvs-id $Id$

}

set user_id [ad_conn user_id]
set screen_name [db_string select_screen_name { select screen_name from users where user_id = :user_id}]
set useScreenNameP [parameter::get -parameter "UseScreenNameP" -default 0]
set pvt_home [ad_pvt_home]

if {[array exists parent_message]} {
  set parent_id $parent_message(message_id)
} else {
  set parent_id ""
}

set table_border_color [parameter::get -parameter table_border_color]
set table_bgcolor [parameter::get -parameter table_bgcolor]


##############################
# Form definition
#

set edit_buttons [list [list [_ forums.Post] post] \
                        [list [_ forums.Preview] preview]]

set form_elements {
    {message_id:key}
    {subject:text(text)
        {html {size 60}}
        {label "[_ forums.Subject]"}
    }
    {message_body:richtext(richtext) 
        {html {rows 20 cols 60}}
        {label "[_ forums.Body]"}
    }
    {forum_id:integer(hidden)
    }
    {parent_id:integer(hidden),optional
    }
    {subscribe_p:text(hidden),optional
    }
    {confirm_p:text(hidden),optional
    }
}

# Deal with anonymous postings
if {[expr {$user_id != 0 && $anonymous_allowed_p}]} {
    append form_elements {
        {anonymous_p:integer(checkbox),optional
            {options {{"[_ forums.post_anonymously]" 1}}}
            {label "[_ forums.Anonymous]"}
        }
    }
} else {
    append form_elements {
        {anonymous_p:integer(hidden),optional
        }
    }
}

# Attachments
if {$user_id != 0} {
    append form_elements {
        {attach_p:integer(radio),optional
            {options {{[_ acs-kernel.common_No] 0} {[_ acs-kernel.common_Yes] 1}}}
            {label "[_ forums.Attach]"}
        }
    }
} else {
    append form_elements {
        {attach_p:integer(hidden),optional
        }
    }
}

ad_form -html {enctype multipart/form-data} \
    -name message \
    -edit_buttons $edit_buttons \
    -form $form_elements \
    -new_request {
        ##############################
        # Form initialisation
        #
        set message_id [db_nextval acs_object_id_seq]
        if {[empty_string_p $parent_id]} {
            set parent_id ""
        } else {
            set forum_id $parent_message(forum_id)
            set subject [forum::format::reply_subject $parent_message(subject)]
        }
        
        set confirm_p 0
        set subscribe_p 0
        set anonymous_p 0
        set attach_p 0
    } -on_submit {

        ##############################
        # Form processing
        #
        
        if { [empty_string_p $anonymous_p] } { set anonymous_p 0 }
        

        set action [template::form::get_button message]

        # Make post the default action
        if {$action eq ""} {
            set action preview
        }

        set displayed_user_id [ad_decode \
                                   [expr {$anonymous_allowed_p && $anonymous_p}] \
                                   0 $user_id \
                                   0]

        if { [string equal $action "preview"] } {
            
            set confirm_p 1
            set subject.spellcheck ":nospell:"
            set content.spellcheck ":nospell:"
            set content [template::util::richtext::get_property content $message_body]
            set format [template::util::richtext::get_property format $message_body]
            
            set exported_vars [export_vars -form {message_id forum_id parent_id subject {message_body $content} {message_body.format $format} confirm_p subject.spellcheck content.spellcheck anonymous_p attach_p}]
            
            set message(format) $format
            set message(subject) $subject
            set message(content) $content
            set message(user_id) $displayed_user_id
            set message(user_name) [db_string select_name {}]
            set message(screen_name) $screen_name
            set message(posting_date_ansi) [db_string select_date {}]
            set message(posting_date_pretty) [lc_time_fmt $message(posting_date_ansi) "%x %X"]
            
            # Let's check if this person is subscribed to the forum
            # in case we might want to subscribe them to the thread
            if {[empty_string_p $parent_id]} {
                if {![empty_string_p [notification::request::get_request_id \
                                          -type_id [notification::type::get_type_id -short_name forums_forum_notif] \
                                          -object_id $forum_id \
                                          -user_id [ad_conn user_id]]]} {
                    set forum_notification_p 1
                } else {
                    set forum_notification_p 0
                }
            }
            
            ad_return_template "/packages/forums/lib/message/post-confirm"
        }

        # DRB: Malte: this redirect_url var isn't used as it is reset in the code below.  Please
        # review and either discard this line or fix the stuff below ...
#        set redirect_url "[ad_conn package_url]message-view?message_id=[set redirect_message_id]&\#$message_id"

        if { [string equal $action "post"] } {
            set content [template::util::richtext::get_property content $message_body]
            set format [template::util::richtext::get_property format $message_body]
            
            forum::message::new \
                -forum_id $forum_id \
                -message_id $message_id \
                -parent_id $parent_id \
                -subject $subject \
                -content $content \
                -format $format \
                -user_id $displayed_user_id
            
            # DRB: Black magic cache flush call which will disappear when list builder is
            # rewritten to paginate internally rather than use the template paginator.
            cache flush "messages,forum_id=$forum_id*"

if { [forum::use_ReadingInfo_p] } {
        # remove reading info for this thread for all users (mark it unread)
    set db_antwort [db_exec_plsql forums_reading_info__remove_msg {}]
}
            # VGUERRA Redirecting to the first message ALWAYS
            forum::message::get -message_id $message_id -array msg
            set redirect_url "[ad_conn package_url]message-view?message_id=$msg(root_message_id)" 
            
            # Wrap the notifications URL
            if {![empty_string_p $subscribe_p] && $subscribe_p && [empty_string_p $parent_id]} {
                set notification_url [notification::display::subscribe_url \
                                          -type forums_message_notif \
                                          -object_id $message_id \
                                          -url $redirect_url \
                                          -user_id $user_id]
                
                # redirect to notification stuff
                set redirect_url $notification_url
            }
            
            # Wrap the attachments URL
            if {$attachments_enabled_p} {
                if { ![empty_string_p $attach_p] && $attach_p} {
                    set redirect_url [attachments::add_attachment_url -object_id $message_id -return_url $redirect_url -pretty_name "[_ forums.Forum_Posting] \"$subject\""]
                }
            }
            
            # Do the redirection
            ad_returnredirect $redirect_url
            ad_script_abort
        }
    }


if {[exists_and_not_null alt_template]} {
    ad_return_template $alt_template
}
