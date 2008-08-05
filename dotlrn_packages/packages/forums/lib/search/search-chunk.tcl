ad_page_contract {

    @author yon@openforce.net
    @creation-date 2002-07-01
    @cvs-id $Id$

}
set package_id [ad_conn package_id]
set useScreenNameP [parameter::get -parameter "UseScreenNameP" -default 0]

set searchbox_p [parameter::get -parameter ForumsSearchBoxP -package_id $package_id -default 1]
if {$searchbox_p} {
    form create search
    forums::form::search search

    if {[form is_valid search]} {
        form get_values search search_text forum_id

        # remove any special characters from the search text so we
        # don't crash interMedia
        regsub -all {[^[:alnum:]_[:blank:]]} $search_text {} search_text

        set query search_all_forums
        if {![empty_string_p $forum_id]} {
            set query search_one_forum
        }
        
        if { [parameter::get -parameter UseIntermediaForSearchP -default 0] } {
            append query "_intermedia"
        }
    
        db_multirow -extend { author posting_date_pretty} messages $query {} {
            set posting_date_pretty [lc_time_fmt $posting_date_ansi "%x %X"]
            if { $useScreenNameP } {
                set author [db_string select_screen_name {select screen_name from users where user_id = :user_id}]
            } else {
                set author $user_name
            }
        }
    } else {
        set messages:rowcount 0
    }

    if {[exists_and_not_null alt_template]} {
        ad_return_template $alt_template
    }
}
