# packages/lams2int/lib/user-lams2int.tcl
#
# @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
# @creation-date 2007-04-17
# @cvs-id $Id$

template::list::create \
    -name d_lesson \
    -multirow d_lesson \
    -html {width 100%} \
    -key lesson_id \
    -no_data "No lessons available" \
    -elements {
        lesson_name {
            label "Title"
            display_eval {[string_truncate -len 25 -ellipsis "..." $display_title]}
            link_url_eval {[export_vars -base $comm_url/lams2int/ lesson_id]} 
            html { width 50% }
            link_html {title "View lesson"}
        }
        subject {
            label "Subject"
            display_eval {[dotlrn_community::get_community_name $community_id]}
            html { align center width 50% }
            link_url_eval {[dotlrn_community::get_community_url $community_id]}
            link_html {title "Access Course"}       
        }
    }

set user_id [ad_conn user_id]

foreach package $package_id {

    db_multirow -extend {comm_url Community} -append d_lesson select_d_lesson {
        select 
           lesson_id, display_title, introduction, hide, start_time, community_id
        from
           lams_lessons
        where 
           package_id = :package
        and 
            hide = 'f'
        order by start_time desc
    } {
        set comm_url [dotlrn_community::get_community_url $community_id]
    }

}