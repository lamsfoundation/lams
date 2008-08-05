ad_page_contract {
    one forum view

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-24
    @cvs-id $Id$
}

if {![info exists flush_p]} {set flush_p 0}

set user_id [ad_verify_and_get_user_id]
# Get forum data

forum::get -forum_id $forum_id -array forum

set useReadingInfo [forum::use_ReadingInfo_p]
if { $useReadingInfo } {
    set unread_or_new_query_clause [db_map unread_query]
    set unread_join [db_map unread_join]
} else {
    set unread_or_new_query_clause [db_map new_query]
    set unread_join ""
}
if {![info exists base_url]} {
    set base_url ""
}

if {$moderate_p} {
    set replies reply_count
} else {
    set replies approved_reply_count
}

set actions [list]

if {![exists_and_not_null page_size]} {
    set page_size 30
}

if {![exists_and_not_null base_url]} {
    set base_url ""
}

# new postings are allowed if

# 1. Users can create new threads AND the posting policy is open or
# moderated 2. User is a moderator or adminsitrator

if {([forum::new_questions_allowed_p -forum_id $forum_id] && ($forum(posting_policy) == "open" || $forum(posting_policy) == "moderated")) ||  [template::util::is_true $permissions(admin_p)] ||  [template::util::is_true $permissions(moderate_p)]  } {
    lappend actions [_ forums.Post_a_New_Message] [export_vars -base "${base_url}message-post" { forum_id }] [_ forums.Post_a_New_Message]
}

if { [template::util::is_true $permissions(admin_p)] } {
    lappend actions [_ forums.Administer] [export_vars \
					       -base "${base_url}admin/forum-edit" {forum_id {return_url [ad_return_url]}}] [_ forums.Administer]
}

if { [template::util::is_true $permissions(moderate_p)] } {
    lappend actions [_ forums.ManageModerate] [export_vars -base "${base_url}moderate/forum" { forum_id }] [_ forums.ManageModerate]
}

if { $useReadingInfo } {
    lappend actions [_ forums.mark_all_as_read] [export_vars -base "${base_url}mark-all-read" { forum_id }] {}
}

template::list::create \
    -name messages \
    -multirow messages \
    -page_size $page_size \
    -page_flush_p $flush_p \
    -page_query_name messages_select_paginate \
    -pass_properties {moderate_p useReadingInfo} \
    -actions $actions \
    -elements {
        subject {
            label "#forums.Subject#"
            display_template {
                <h2 class="forum-heading">
                 <a href="@messages.message_url@" title="#forums.goto_thread_subject#">
                  <if @useReadingInfo@>
                   <if @messages.unread_p@>
                    <b>@messages.subject@</b>
                   </if>
                   <else>@messages.subject@</else>
                  </if>
                  <else>
                   <if @messages.new_p@>
                    <b>@messages.subject@</b>
                   </if>
                   <else>@messages.subject@</else>
                  </else>
                 </a>
                </h2>
            }
        }
        state_pretty {
            label "\#forums.Moderate\#"
            hide_p {[ad_decode $moderate_p 1 0 1]}
        }
        user_name {
            label "#forums.Author#"
            link_url_col user_url
	    link_html {title "\#forums.show_history_user_name\#"}
        }
        n_messages {
            label "#forums.Replies#"
            display_col n_messages_pretty
            html {align right}
        }
        posting_date {
            label "#forums.First_Post#"
            display_col posting_date_pretty
        }
        last_child_post {
            label "#forums.Last_Post#"
            display_col last_child_post_pretty
        }
    } -orderby {
        posting_date {
            label "#forums.First_Post#"
            orderby posting_date
            default_direction desc
        }
        last_child_post {
            label "#forums.Last_Post#"
            orderby last_child_post
            default_direction desc
        }
        subject {
            label "#forums.Subject#"
            orderby upper(subject)
        }
        user_name {
            label "#forums.Author#"
            orderby_asc_name "orderby_user_name_asc"
            orderby_desc_name "orderby_user_name_desc"
        }
        n_messages {
            label "#forums.Replies#"
            orderby_asc {n_messages asc, posting_date desc}
            orderby_desc {n_messages desc, posting_date desc}
        }
    } -filters {
        forum_id {}
    }

set useScreenNameP [parameter::get -parameter "UseScreenNameP" -default 0]

db_multirow -extend { 
    last_child_post_pretty
    posting_date_pretty
    message_url
    user_url
    n_messages_pretty
    state_pretty
} messages messages_select {} {
    set last_child_post_ansi [lc_time_system_to_conn $last_child_post_ansi]
    set last_child_post_pretty [lc_time_fmt $last_child_post_ansi "%x %X"]

    set posting_date_ansi [lc_time_system_to_conn $posting_date_ansi]
    set posting_date_pretty [lc_time_fmt $posting_date_ansi "%x %X"]

    set message_url [export_vars -base "${base_url}message-view" { message_id }]
    if { $useScreenNameP } {
	set user_name $screen_name
	set user_url ""
    } else {
	set user_url [export_vars -base "${base_url}user-history" { user_id }]
    }
    set n_messages_pretty [lc_numeric $n_messages]

    switch $state {
        pending {
            set state_pretty [_ forums.Pending]
        }
        rejected {
            set state_pretty [_ forums.Rejected]
        }
        default {
            set state_pretty {}
        }
    }
}

if {[exists_and_not_null alt_template]} {
    ad_return_template $alt_template
}
