ad_page_contract {
    
    Create a Forum
    By default redirects to the level above as that is prolly where the index page is

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-25
    @cvs-id $Id$

}

if { ![info exists name] } {
    set name {}
}

set package_id [ad_conn package_id]

form create forum

element create forum forum_id \
    -label [_ forums.Forum_ID] \
    -datatype integer \
    -widget hidden

forums::form::forum forum

if {[form is_valid forum]} {
    template::form get_values forum \
        forum_id name charter presentation_type posting_policy new_threads_p

    set forum_id [forum::new -forum_id $forum_id \
        -name $name \
        -charter [template::util::richtext::get_property contents $charter] \
        -presentation_type $presentation_type \
        -posting_policy $posting_policy \
        -package_id $package_id \
    ]

    forum::posting_policy_set -posting_policy $posting_policy \
        -forum_id $forum_id
    
    # Users can create new threads?
    if {$new_threads_p} {
        forum::new_questions_allow -forum_id $forum_id
    } else {
        forum::new_questions_deny -forum_id $forum_id
    }

    ad_returnredirect $return_url
    ad_script_abort
}

if { [form is_request forum] } {
    # Pre-fetch the forum_id
    set forum_id [db_nextval acs_object_id_seq]
    element set_properties forum forum_id -value $forum_id
    element set_value forum new_threads_p 1
    element set_value forum name $name
}

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
