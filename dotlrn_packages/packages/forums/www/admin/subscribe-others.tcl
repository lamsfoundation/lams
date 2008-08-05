ad_page_contract {
    
    Subscribe a list of email addresses to a forum

    @author Jade Rubick (jade@rubick.com)
    @creation-date 2005-04-08
    @cvs-id $Id$

} {
    forum_id:integer,notnull
    {return_url "."}
}

# Select the info
set package_id [ad_conn package_id]
set subsite_id [ad_conn subsite_id]
set group_id [application_group::group_id_from_package_id -package_id $subsite_id]

set member_ids [group::get_members -group_id $group_id]
set member_html ""
foreach member_id $member_ids {
    append member_html "<input type=\"checkbox\" name=\"subscriber_ids\" value=\"$member_id\" id=\"subscriber,$member_id\" title=\"\"> [party::name -party_id $member_id]<br>\n"
}

forum::get -forum_id $forum_id -array forum

# Proper scoping?
if {$package_id != $forum(package_id)} {
    ns_log Error "Forum Administration: Bad Scoping of Forum #$forum_id in Forum Editing"
    ad_returnredirect -message "Forum Administration: Bad Scoping of Forum #$forum_id in Forum Editing" "./"
    ad_script_abort
}

set context [list [_ forums.Subscribe_others]]

set type forums_forum_notif
set type_id [notification::type::get_type_id -short_name $type]

# Get the list for the members and the ones already subscribed
set subscribed_members_list [notification::request::subscribers -type_id $type_id -object_id $forum_id]
set member_ids [group::get_members -group_id $group_id]
set member_ids [lsort -integer -unique [concat $subscribed_members_list $member_ids]]

set member_html ""
foreach member_id $member_ids {
    if {[lsearch $subscribed_members_list $member_id] > -1} {
	set checked_html "checked"
    } else {
	set checked_html ""
    }

    append member_html "<input type=\"checkbox\" name=\"subscriber_ids\" value=\"$member_id\" id=\"subscriber,$member_id\" $checked_html> [party::name -party_id $member_id]<br>\n"
}


set hidden_vars [export_vars -form {forum_id type_id}]

set intervals [notification::get_intervals -type_id $type_id]
set interval_html "<select name=\"interval\">"
foreach interval $intervals {
    set iname [lindex $interval 0]
    set iid   [lindex $interval 1]
    append interval_html "<option value=\"$iid\">$iname</option>"
}
append interval_html "</select>"

set delivery_methods [notification::get_delivery_methods -type_id $type_id]

set delivery_html "<select name=\"delivery_method\">"
foreach dm $delivery_methods {
    set dname [lindex $dm 0]
    set did   [lindex $dm 1]
    append delivery_html "<option value=\"$did\">$dname</option>"
}
append delivery_html "</select>"


