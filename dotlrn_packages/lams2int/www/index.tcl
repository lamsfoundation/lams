# packages/lams2int/www/index.tcl

ad_page_contract {
    
    Jumps directly to a LAMS sequence
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-17
    @cvs-id $Id$
} {
    lesson_id
} -properties {
} -validate {
} -errors {
}


set lams_server_url [lams2int::get_lams_server_url]
set username [ad_verify_and_get_user_id] 
set datetime [lams2int::get_datetime]
set server_id [lams2int::get_server_id]
set hashValue [lams2int::ws::generate_hash -datetime $datetime -username $username -method "learner"]
set course_id [dotlrn_community::get_community_id]


# LAMS Sequence info

db_1row seq_info {select display_title, introduction, learning_session_id, user_id from lams_lessons where lesson_id = :lesson_id}

set introduction [ad_convert_to_html $introduction] 

set title $display_title
set context [list "View Lesson: $display_title"]


# if the user that started the sequence is the same as the user
# trying to access the sequence, we give him admin access
if {[string equal $user_id $username]} {
    set admin_p 1

    set hashmonitor [lams2int::ws::generate_hash -datetime $datetime -username $username -method "monitor"]

} else {
    set admin_p 0
}


# General comments
set the_comments [general_comments_get_comments -print_content_p 1 $lesson_id]
set general_comments_link [general_comments_create_link -object_name "$display_title" -link_text "Add comments to $display_title" -link_attributes {style="font:85% arial; border: solid 1px black; background-color: \\#e1e1e1; text-align: center; padding: 1px; padding-left: 8px; padding-right: 8px; color: black; text-decoration: none; white-space: nowrap;"} $lesson_id [ns_conn url]?[ns_conn query]]  
