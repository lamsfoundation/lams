# packages/lams2int/tcl/lams2int-procs.tcl

ad_library {
    
    LAMS2.x Integration Procedures
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id lams2int-procs.tcl,v 1.1 2007/09/12 06:37:02 ernieg Exp
}

#
#  Copyright (C) 2005 LAMS Foundation
#
#  This package is free software; you can redistribute it and/or modify it under the
#  terms of the GNU General Public License as published by the Free Software
#  Foundation; version 2 of the License
#
#  It is distributed in the hope that it will be useful, but WITHOUT ANY
#  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
#  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
#  details.
#

namespace eval lams2int {}

ad_proc -public lams2int::add {
    {-learning_session_id:required} 
    {-display_title:required}
    {-introduction ""}
    {-hide "f"}
    {-start_date ""}
    {-creation_user}
    {-package_id}
    {-community_id}
} {
    Adds a new LAMS lesson to .LRN

    @param learning_session_id LAMS learning session id
    @param display_title title for the lesson to be displayed in .LRN
    @param introduction Introduction to be displayed in .LRN
    @param hide boolean, whether the lesson should be hiden to students or not
    @param start_date start date to show the lesson to students
    @param creation_user creation user
    @param package_id package id
    @param community_id the community where this lesson will live.
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)

} {

    # this should kick in *after* we get a learning_session_id from
    # LAMS

    set creation_ip [ad_conn peeraddr]

    db_transaction {
        set lesson_id [db_exec_plsql new_seq {
            select lams_lesson__new (
                                       :learning_session_id,
                                       :display_title,
                                       :introduction,
                                       :hide,
                                       current_timestamp,
                                       :creation_user,
                                       :creation_ip,
                                       :package_id,
                                       :community_id
				       );
            
        }
		    ]
        
    }

    return $lesson_id
    
}

ad_proc -public lams2int::get_datetime {

} {
    Generates the datetime for the hash

} {

    set user_now_time [lc_time_system_to_conn [dt_systime]]
    return  [lc_time_fmt $user_now_time "%B %d,%Y %r"] 

}

ad_proc -public lams2int::get_lams_server_url {

} {
    Gets the lams_server_url from the LAMS Configuration package

} {

    set lams2conf_package_id [db_string pack_id_lams2_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter lams_server_url -package_id $lams2conf_package_id]


}


ad_proc -public lams2int::get_server_id {

} {
    Gets the lams_server_id from the LAMS2.x Configuration package (parameter)

} {

    set lams2conf_package_id [db_string pack_id_lams_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter server_id -package_id $lams2conf_package_id]

}

ad_proc lams2int::get_server_key {

} {
    Gets the lams_server_key from the LAMS Configuration package (parameter)

} {

    set lams2conf_package_id [db_string pack_id_lams_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter server_key -package_id $lams2conf_package_id]

}

ad_proc lams2int::get_request_source {

} {
    Gets the request_source from the LAMS Configuration package (parameter)

} {

    set lams2conf_package_id [db_string pack_id_lams_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter request_source -package_id $lams2conf_package_id]

}

ad_proc -public lams2int::process_sequence_xml {
    -xml:required
} {
    Gets the XML with the sequences and folder and process it. 

} {

    encoding convertfrom utf-8 $xml
    set doc [dom parse  $xml]
    set content [$doc documentElement]

    set sequence_list [concat [lams2int::process_node -node $content] " \] "]

}

ad_proc -private lams2int::process_node {
    -node:required 

} {
    Process the XML nodes 
    
} { 

    if {[string equal [$node nodeName] "Folder"]} {

	regsub -all {'} [$node getAttribute name] {\\'} folder_name
	
	append output "\[ '$folder_name', null , "
	
	if {[$node hasChildNodes]} {

	    foreach child [$node childNodes] {

		append output [lams2int::process_node -node $child]  

		if  {[string equal [$child nodeName] "Folder"]} {

		    if {![empty_string_p [$child nextSibling]]} {
			append output " \], " 
		    } else {
			append output " \]"
		    }
		}

	    }

	} else {

	    append output "\['', null\]"

	}
    } else {
	# the node is a LearningDesign

	regsub -all {'} [$node getAttribute name] {\\'} design_name

	append output "\[ '$design_name', 'javascript:selectSequence([$node getAttribute resourceId])' \] "

	if {![empty_string_p [$node nextSibling]]} {
	    append output ", " 
	} else {
	    append output ""
	}

    }

    return $output
}

