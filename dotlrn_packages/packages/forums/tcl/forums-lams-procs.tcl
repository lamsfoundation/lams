# packages/forums/tcl/forums-lams-procs.tcl

ad_library {
    
    LAMS Integration for Forum
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2008-07-23
    @arch-tag: F3DCDE7B-7FDF-4CD0-8CE4-AC182F82661E
    @cvs-id $Id$
}

namespace eval forum::lams {}

ad_proc -public forum::lams::is_lams {
    {-forum_id ""}
} {
    set a forum to be a LAMS forum instance only
} {

    db_dml is_lams {}

}


ad_proc -public forum::lams::clone_instance {
    -forum_id:required
    -course_id:required
    {-user_id ""}
} {
    Clones an instance of a forum
} {

    # since we pass the Community_id to LAMS, then we use this as
    # course_id
    set community_id $course_id 
    set forum_package_id [forum::lams::get_package_instance -community_id $community_id]

    if {![string equal 0 $forum_id]} {
	# The forum actually exists, therefore we clone it

	forum::get -forum_id $forum_id -array forum

	# we get the parameters out of the forum array
	set forum_name $forum(name)
	set forum_charter $forum(charter)
	set presentation_type $forum(presentation_type)
	set posting_policy  $forum(posting_policy)
    
    } else {

	# Forum instance doesn't exist, therefore we create a new
	# one. We set the parameters based on default forum content
	set forum_name "Forum"
	set forum_charter ""
	set presentation_type "flat"
	set posting_policy  "open"

    }

    set new_forum_id [forum::new -name $forum_name \
                          -charter $forum_charter \
                          -presentation_type $presentation_type \
                          -posting_policy $posting_policy \
                          -package_id $forum_package_id \
			 ]

    forum::lams::is_lams -forum_id $new_forum_id

    return $new_forum_id

}

ad_proc -private forum::lams::get_package_instance  {
    -community_id:required
} {
    gets the package instance in the community for Forum
} {
    set pkg_id [db_string get_package_id { } -default 0]
    if { [string equal $pkg_id "0"] } {
        ad_return_complaint 1 "<b>You need to have forums-portlet in your class</b>"
        ad_script_abort
    } else {
        return $pkg_id
    }

}


