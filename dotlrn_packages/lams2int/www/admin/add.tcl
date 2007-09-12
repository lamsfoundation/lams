# packages/lams2int/www/admin/add.tcl

ad_page_contract {
    
    Adds a LAMS lesson
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id $Id$
} {
    
} -properties {
} -validate {
} -errors {
}


set title "Add LAMS Lesson"
set context [list "Add LAMS Lesson"]

# get parameters data
set datetime [lams2int::get_datetime]
set lams_server_url [lams2int::get_lams_server_url]
set server_id [lams2int::get_server_id]
set username [ad_verify_and_get_user_id] 

# get course data
set course_id [dotlrn_community::get_community_id]

#set datetime 1

set hashValue [lams2int::ws::generate_hash -datetime $datetime -username $username -method "author"]


# get variables to pass to LAMS

set requestSrc [lams2int::get_request_source]
set notifyCloseURL "[ad_url][util_get_current_url]"


# get seq list

set hashSeq [lams2int::ws::generate_hash -datetime $datetime -username $username]

set get_sequences_url "$lams_server_url/services/xml/LearningDesignRepository?serverId=$server_id&datetime=[ad_urlencode $datetime]&hashValue=$hashSeq&username=$username&courseId=$course_id&mode=2&country=AU&lang=en"

ns_log Notice "URL requested $get_sequences_url"


set xml [lindex [ad_httpget -url $get_sequences_url -timeout 30] 1]

set xml [encoding convertfrom utf-8 $xml]
set doc [dom parse  $xml]
set content [$doc documentElement]

proc process_node { node } {

    if {[string equal [$node nodeName] "Folder"]} {

	append output "\[ '[$node getAttribute name]', null , "

	if {[$node hasChildNodes]} {

	    foreach child [$node childNodes] {

		append output [process_node $child]  

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

	append output "\[ '[$node getAttribute name]', 'javascript:selectSequence([$node getAttribute resourceId])' \] "

	if {![empty_string_p [$node nextSibling]]} {
	    append output ", " 
	} else {
	    append output ""
	}

    }

    return $output
}



set sequence_list [concat [process_node $content] " \] "]

