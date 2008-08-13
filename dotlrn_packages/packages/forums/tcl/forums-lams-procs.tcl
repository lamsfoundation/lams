# packages/forums/tcl/forums-lams-procs.tcl

ad_library {
    
    LAMS Integration for Forum
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2008-07-23
    @arch-tag: F3DCDE7B-7FDF-4CD0-8CE4-AC182F82661E
    @cvs-id $Id$
}

namespace eval forum::lams {}

ad_proc -private forum::lams::is_lams {
    -forum_id:required
} {
    set a forum to be a LAMS forum instance only
} {

    db_dml is_lams {}

}

ad_proc -private forum::lams::update_user_id {
    -forum_id:required
    -user_id:required
} {
    set a forum to be a LAMS forum instance only
} {

    db_dml update_user_id {}

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
    forum::lams::update_user_id -forum_id $new_forum_id -user_id $user_id

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


ad_proc -public forum::lams::export_instance {
    -forum_id:required
} {
    Exports an instance of forum 
} {

    # gets the content of the forum
    forum::get -forum_id $forum_id -array forum
    
    # exports the array to text
    set export_forum_as_string [array get forum]

    set tmp_folder [ns_tmpnam]
    file mkdir $tmp_folder
    set path_to_file [file join $tmp_folder forum_$forum_id]

    set filer [open $path_to_file w+]

    puts -nonewline $filer $export_forum_as_string 

    close $filer

    # send the file back to the call

    ns_set put [ad_conn outputheaders] Content-Disposition "attachment;filename=\"forum_$forum_id.txt\""
    ns_set put [ad_conn outputheaders] Content-Type "text/txt"
    ns_set put [ad_conn outputheaders] Content-Size "[file size $path_to_file]"
    ns_returnfile 200  text/txt $path_to_file

    # clean up
    file delete -force $path_to_file
    ad_script_abort
    
}

ad_proc -public forum::lams::import_instance {
    -file_path:required
    -user_id:required
    -course_id:required
} {
    Import an instance of forum
} {

    # open file

    set filer [open $file_path r] 
    
    set content [read $filer]

    ns_log Debug "Importing tool content: $content"

    array set forum $content

    close $filer

    set forum_name $forum(name)
    set forum_charter $forum(charter)
    set presentation_type $forum(presentation_type)
    set posting_policy  $forum(posting_policy)
    set forum_package_id [forum::lams::get_package_instance -community_id $course_id]
    
    set new_forum_id [forum::new -name $forum_name \
                          -charter $forum_charter \
                          -presentation_type $presentation_type \
                          -posting_policy $posting_policy \
                          -package_id $forum_package_id \
			  ]

    forum::lams::is_lams -forum_id $new_forum_id
    forum::lams::update_user_id -forum_id $new_forum_id -user_id $user_id

    return $new_forum_id
    
}


ad_proc -public forum::lams::export_portfolio {
    -forum_id:required
    {-user_id ""}
} {
    Export the portfolio and returns an HTML file

} {

    # Since .LRN Forum doesn't provide a page with all the threads in
    # one single HTML, we simply return a page stating that this
    # feature is not implemented

    set return_string "<html><title>Export Portfolio for .LRN Forum</title><body><h1>Sorry, this feature is not implemented</h1></body></html>"

    set tmp_folder [ns_tmpnam]
    file mkdir $tmp_folder
    set path_to_file [file join $tmp_folder export_portfolio_$forum_id]

    set filer [open $path_to_file w+]

    puts -nonewline $filer $return_string

    close $filer

    # send the file back to the call

    ns_set put [ad_conn outputheaders] Content-Disposition "attachment;filename=\"export_portfolio_$forum_id\""
    ns_set put [ad_conn outputheaders] Content-Type "text/txt"
    ns_set put [ad_conn outputheaders] Content-Size "[file size $path_to_file]"
    ns_returnfile 200  text/html $path_to_file
    

}
