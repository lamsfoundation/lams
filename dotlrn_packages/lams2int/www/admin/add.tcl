# packages/lams2int/www/admin/add.tcl

ad_page_contract {
    
    Adds a LAMS lesson
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id add.tcl,v 1.1 2007/09/12 06:37:02 ernieg Exp
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
set course_url "[ad_url][dotlrn_community::get_community_url $course_id]"

#set datetime 1

set hashValue [lams2int::ws::generate_hash -datetime $datetime -username $username -method "author"]


# get variables to pass to LAMS

set requestSrc [lams2int::get_request_source]
set notifyCloseURL "[ad_url][util_get_current_url]"


# get seq list

set hashSeq [lams2int::ws::generate_hash -datetime $datetime -username $username]

set get_sequences_url "$lams_server_url/services/xml/LearningDesignRepository?serverId=$server_id&datetime=[ad_urlencode $datetime]&hashValue=$hashSeq&username=$username&courseId=$course_id&mode=2&country=AU&lang=en"

ns_log Notice "URL requested $get_sequences_url"

if {[catch {set return_string [ad_httpget -url $get_sequences_url -timeout 30] } ] } {
    ad_return_complaint 0 "<b>It seems that the LAMS server you are trying to connect is down or there's a problem with the configuration. Please verify the connection setting or contact your system administrator.</b>"
    ad_script_abort
}

set xml [lindex $return_string 1]

set sequence_list [lams2int::process_sequence_xml -xml $xml]

