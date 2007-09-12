# packages/lams2int/www/admin/add-2.tcl

ad_page_contract {
    
    Creates a new instance of LAMS sequence and adds a new lesson to a course
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id $Id$
} {
    name
    lesson_id:integer
    sequence_id:integer
    introduction:optional    
} -properties {
} -validate {
} -errors {
}


# get parameters data
set datetime [lams2int::get_datetime]
set lams_server_url [lams2int::get_lams_server_url]
set server_id [lams2int::get_server_id]
set username [ad_verify_and_get_user_id] 

# get course data
set course_id [dotlrn_community::get_community_id]


# methods

# start, schedule, delete

set method "start"

set hashSeq [lams2int::ws::generate_hash -datetime $datetime -username $username]

set lesson_url "$lams_server_url/services/xml/LessonManager?method=$method&serverId=$server_id&ldId=$sequence_id&datetime=[ad_urlencode $datetime]&hashValue=$hashSeq&username=$username&courseId=$course_id&title=$name&desc=$introduction&country=AU&lang=en"


set xml [lindex [ad_httpget -url $lesson_url -timeout 30] 1]

set xml [encoding convertfrom utf-8 $xml]
set doc [dom parse  $xml]
set content [$doc documentElement]


set lesson_id [$content getAttribute lessonId]

set seq_id [lams2int::add -learning_session_id $lesson_id -display_title $name -introduction $introduction \
     -hide "f" -creation_user $username -package_id [ad_conn package_id] -community_id $course_id]

ad_returnredirect -message "Your lesson <b>$name</b> is now available" -html [dotlrn_community::get_community_url $course_id]

