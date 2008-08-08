ad_page_contract {
    
    Edit a Forum

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-25
    @cvs-id $Id$

} {
    toolContentID:integer,notnull
    forum_id:integer,notnull
    {lamsUpdateURL ""}
}

set returnlams_url $lamsUpdateURL
set dispatch "updateContent"
set extToolContentID $forum_id

# Select the info
set package_id [ad_conn package_id]
forum::get -forum_id $forum_id -array forum

# Proper scoping?
if {$package_id != $forum(package_id)} {
    ns_log Error "Forum Administration: Bad Scoping of Forum #$forum_id in Forum Editing"
    ad_returnredirect [export_vars -base $returnlams_url {dispatch extToolContentID toolContentID}]
    ad_script_abort
}

set context [list [_ forums.Edit_forum]]
