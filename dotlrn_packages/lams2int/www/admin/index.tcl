# packages/lams2int/www/admin/index.tcl

ad_page_contract {
    
    LAMS Integration Admin
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-17
    @cvs-id $Id$
} {
    
} -properties {
} -validate {
} -errors {
}

# 
set title "LAMS Administration"
set context [list "LAMS Administration"]

# LAMS Variables & settings 
set username [ad_verify_and_get_user_id] 
set package_id [ad_conn package_id]
set lams_server_url [lams2int::get_lams_server_url]
set datetime [lams2int::get_datetime]
set server_id [lams2int::get_server_id]
set course_id [dotlrn_community::get_community_id]
set requestSrc [lams2int::get_request_source]
set notifyCloseURL "[ad_url][util_get_current_url]"

set hashauthor [lams2int::ws::generate_hash -datetime $datetime -username $username -method "author"]



template::list::create \
    -name d_lesson \
    -multirow d_lesson \
    -html {width 100%} \
    -key lesson_id \
    -no_data "No available lessons" \
    -elements {
        lesson_name {
            label "Title"
            display_eval {[string_truncate -len 40 -ellipsis "..." $display_title]}
            link_url_eval {[export_vars -base ../index lesson_id]} 
            link_html {title "View lesson"}
        }
        hide {
            label "Display?"
            display_template {@d_lesson.hide_p;noquote@}
            html {align "center"} 

        }
        creation_user {
            label "Started by"
            display_eval {[person::name -person_id $creation_user]}
            link_url_eval {[acs_community_member_url -user_id $creation_user]}
            html {align "center"}   
        }
        start_time {
            label "Started"
            display_eval {[lc_time_fmt $start_time "%x %X"]}
            html {align "center"}                            
        }

    }



db_multirow -extend {hide_p} d_lesson select_d_lesson {
    select 
    lesson_id, display_title, introduction, hide, start_time, user_id as creation_user
    from
    lams_lessons
    where 
    package_id = :package_id
    order by start_time desc
} {

    # show Display or Hide checkbox
    if {[string equal $hide "f"]} {
        set hide_p "<a href=\"[export_vars -base hide {lesson_id hide}]\" title=\"Click to hide\"><img src=\"/resources/checkboxchecked.gif\"></a>"

    } else {
        set hide_p "<a href=\"[export_vars -base hide {lesson_id hide}]\" title=\"Click to display\"><img src=\"/resources/checkbox.gif\"></a>"
    }

}