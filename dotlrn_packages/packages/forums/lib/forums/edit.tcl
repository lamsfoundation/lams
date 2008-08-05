ad_page_contract {
    
    Edit a Forum

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-25
    @cvs-id $Id$

} {
}

form create forum

element create forum return_url \
        -datatype text -widget hidden -optional

element create forum forum_id \
        -label [_ forums.Forum_ID] -datatype integer -widget hidden

forums::form::forum forum

if {[form is_valid forum]} {
    template::form get_values forum return_url forum_id name charter presentation_type posting_policy new_threads_p

    forum::edit -forum_id $forum_id \
            -name $name \
            -charter [template::util::richtext::get_property contents $charter] \
            -presentation_type $presentation_type \
            -posting_policy $posting_policy
    
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
    element set_properties forum return_url -value $return_url
    element set_properties forum forum_id -value $forum(forum_id)
    element set_properties forum name -value $forum(name)
    element set_properties forum charter -value [template::util::richtext create $forum(charter) "text/html"]
    element set_properties forum presentation_type -value $forum(presentation_type)
    element set_properties forum posting_policy -value $forum(posting_policy)
    element set_properties forum new_threads_p -value [forum::new_questions_allowed_p -forum_id $forum(forum_id)]
}

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
