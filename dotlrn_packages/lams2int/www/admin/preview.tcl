# packages/lams2int/www/admin/preview.tcl

ad_page_contract {
    
    Preview a LAMS lesson
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-10-22
    @cvs-id $Id$
} {
    sequence_id:integer
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

# start, schedule, delete, preview

set method "preview"
set name $method
set introduction [concat $method sequence_id $sequence_id]

set hashSeq [lams2int::ws::generate_hash -datetime $datetime -username $username]

set lesson_url "$lams_server_url/services/xml/LessonManager?method=$method&serverId=$server_id&ldId=$sequence_id&datetime=[ad_urlencode $datetime]&hashValue=$hashSeq&username=$username&courseId=$course_id&title=[ad_urlencode $name]&desc=[ad_urlencode $introduction]&country=AU&lang=en"


set xml [lindex [ad_httpget -url $lesson_url -timeout 30] 1]

set xml [encoding convertfrom utf-8 $xml]
set doc [dom parse  $xml]
set content [$doc documentElement]


set lesson_id [$content getAttribute lessonId]

#set seq_id [lams2int::add -learning_session_id $lesson_id -display_title $name -introduction $introduction \
     -hide "f" -creation_user $username -package_id [ad_conn package_id] -community_id $course_id]

#set preview_url
# "$lams_server_url/LoginRequest?uid=$username&method=$method&ts=$datetime&sid=$server_id&hash=$hashValue&course_id=$course_id&lsid=$"

set preview_url "$lams_server_url/lams/learning/main.jsp?lessonID=$lesson_id&mode=$method"

set hashValue [lams2int::ws::generate_hash -datetime $datetime -username $username -method "learner"]

set preview_url "$lams_server_url/LoginRequest?uid=$username&method=learner&ts=$datetime&sid=$server_id&hash=$hashValue&courseid=$course_id&lsid=$lesson_id&country=AU&lang=EN"

ad_returnredirect -html $preview_url

