# packages/lams2conf/www/tooladapter_forum.tcl
ad_page_contract {
    
    Tool Adapter for Forum
    
    @author Ernie Ghiglione (ErnieG@mm.st)
    @creation-date 2008-08-05
    @arch-tag: 2432DA0D-5EBF-4C94-BE8D-D339FD6F5487
    @cvs-id $Id$
} {
    method
    extToolContentID:optional
    ts
    un
    hs
    cs
    upload_file:trim,optional
    upload_file.tmpfile:optional,tmpfile    
} -properties {
} -validate {
} -errors {
}

# Prepare all variables to compare hashs for authentication

set timestamp $ts
set username $un
set hash $hs
set course_id $cs

set datetime $timestamp

set lams_server_url [parameter::get -parameter lams_server_url -package_id [ad_conn package_id]]
set server_key [parameter::get -parameter server_key -package_id [ad_conn package_id]]
set server_id [parameter::get -parameter server_id -package_id [ad_conn package_id]]

set plaintext [string tolower [concat $timestamp$username$server_id$server_key]]
set hashValue [string tolower [ns_sha1 $plaintext]]


# Compare authentication hash
if {![string equal [string tolower $hash] $hashValue]} {

    # if authentication fails...
    ns_log Warning "LAMSint: userinfo request hash authentication failed. localhash: $hashValue requesthash: $hash"
    ns_write "HTTP/1.1 401 Usernameauthenticated"
    ad_script_abort
}

# Hash comparison OK, so continue...

switch $method {

    clone {
	
	# clones the instance in the course_id and the username
	set new_extToolContentID [forum::lams::clone_instance -forum_id $extToolContentID -course_id $course_id -user_id $username]

	# send the new extToolContentID to LAMS

	ReturnHeaders "text/plain"
	ns_write $new_extToolContentID
	ad_script_abort

    }

    import {
	# gets the file from LAMS with the content of the forum

	if { ![empty_string_p $upload_file] && [file isfile  ${upload_file.tmpfile}]} {

	    # if the file exists and it's indeed a file,
	    # then import the instance using the content of the file,
	    # the course_id and user_id

	    set new_extToolContentID [forum::lams::import_instance -file_path ${upload_file.tmpfile} -user_id $username -course_id $course_id]

	    ns_log Debug "Import: newextToolContentID $new_extToolContentID "
	    ReturnHeaders "text/plain"
	    ns_write $new_extToolContentID
	    ad_script_abort
	    
	} else {

	    # error with the input, return a -1 to denote error.
	    ReturnHeaders "text/plain"
	    ns_write "-1"
	    ad_script_abort
	}

    }

    export {
	# exports the content of the forum to a file

	forum::lams::export_instance -forum_id  $extToolContentID 

	ad_script_abort

    }

    export_portfolio {

	# ...

    }


    export_portfolio_class {

	# ...
    }

    get_outputs {

	# ...

    }

}


