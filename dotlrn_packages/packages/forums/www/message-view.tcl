ad_page_contract {
    
    view a message (and its children)

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-25
    @cvs-id $Id$

} {
    message_id:integer,notnull
    {display_mode ""}
}

#######################
#
# First check all reasons why we might abort
#
#######################

# Load up the message information
forum::message::get -message_id $message_id -array message
if {![array exists message]} {
    ns_returnnotfound
    ad_script_abort
}

# Load up the forum information
forum::get -forum_id $message(forum_id) -array forum

# If disabled!
if {$forum(enabled_p) != "t"} {
    ad_returnredirect "./"
    ad_script_abort
}

forum::security::require_read_message -message_id $message_id
forum::security::permissions -forum_id $message(forum_id) permissions

# Check if the user has admin on the message
set permissions(moderate_p) [forum::security::can_moderate_message_p -message_id $message_id]
if {!${permissions(moderate_p)}} {
    set permissions(post_p) [forum::security::can_post_forum_p -forum_id $message(forum_id)]
} else {
    set permissions(post_p) 1
}

# Check if the message is approved
if {!${permissions(moderate_p)} && ![string equal $message(state) approved]} {
    ad_returnredirect "forum-view?forum_id=$message(forum_id)"
    ad_script_abort
}

############################################
#
# Ok we're not aborting so lets do some work
#
############################################

# Show search box?
set searchbox_p [parameter::get -parameter ForumsSearchBoxP -default 1]

# If this is a top-level thread, we allow subscriptions here
if { [empty_string_p $message(parent_id)] } {
    set notification_chunk [notification::display::request_widget \
        -type forums_message_notif \
        -object_id $message(message_id) \
        -pretty_name $message(subject) \
        -url [ad_conn url]?message_id=$message(message_id) \
    ]
} else {
    set notification_chunk ""
}

if { [forum::use_ReadingInfo_p] && [string equal $message(state) approved] } {
    set msg_id $message(root_message_id)
    set user_id [ad_verify_and_get_user_id]
    set db_antwort [db_exec_plsql forums_reading_info__user_add_msg {}]
}

set context [list [list "./forum-view?forum_id=$message(forum_id)" "$message(forum_name)"]]
if {![empty_string_p $message(parent_id)]} {
    lappend context [list "./message-view?message_id=$message(root_message_id)" "$message(root_subject)"]
    lappend context [_ forums.One_Message]
} else {
    lappend context "$message(subject)"
}

if { $permissions(post_p) || [ad_conn user_id] == 0 } {
    set reply_url [export_vars -base message-post { { parent_id $message(message_id) } }]
}

set thread_url [export_vars -base forum-view { { forum_id $message(forum_id) } }]

set dynamic_script "
  <!--
  collapse_symbol = '<img src=\"/resources/forums/Collapse16.gif\" width=\"16\" height=\"16\" ALT=\"collapse message\" border=\"0\" title=\"collapse message\">';
  expand_symbol = '<img src=\"/resources/forums/Expand16.gif\" width=\"16\" height=\"16\" ALT=\"expand message\" border=\"0\" title=\"expand message\">';
  loading_symbol = '<img src=\"/resources/forums/dyn_wait.gif\" width=\"12\" height=\"16\" ALT=\"x\" border=\"0\">';
  loading_message = 'Loading...';
  rootdir = 'messages-get';
  sid = '$message(root_message_id)';
  //-->
"

if {$forum(presentation_type) eq "flat"} {
    set display_mode flat
}

# stylesheets
set lang [ad_conn language]
template::head::add_css -href /resources/forums/forums.css -media all -lang $lang
#template::head::add_css -alternate -href /resources/forums/collapse.css -media all -lang $lang -title "collapse"
#template::head::add_css -alternate -href /resources/forums/expand.css -media all -lang $lang -title "expand"
#template::head::add_css -alternate -href /resources/forums/print.css -media print -lang $lang
 
# js scripts
template::head::add_script -type "text/javascript" -src "/resources/forums/forums.js"
template::head::add_script -type "text/javascript" -src "/resources/forums/dynamic-comments.js"
template::head::add_script -type "text/javascript" -script $dynamic_script

# cookie handling for the expand/collpase
set onunload_script {
    saveMsgsState();
}

set onload_script {
    loadMsgsState();
}

template::add_body_handler -event onunload -script $onunload_script  
template::add_body_handler -event onload -script $onload_script  

set page_title "#forums.Thread_title#"
