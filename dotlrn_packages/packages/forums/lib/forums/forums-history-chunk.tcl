ad_page_contract {
    
    Forums History 

    @author Natalia Pérez (nperper@it.uc3m.es)
    @creation-date 2005-03-17    

}

set package_id [ad_conn package_id]
set user_id [ad_conn user_id]

# get the colors from the params
set table_border_color [parameter::get -parameter table_border_color]
set table_bgcolor [parameter::get -parameter table_bgcolor]
set table_other_bgcolor [parameter::get -parameter table_other_bgcolor]

# provide screen_name functionality
set screen_name [db_string select_screen_name { select screen_name from users where user_id = :user_id}]
set useScreenNameP [parameter::get -parameter "UseScreenNameP" -default 0]

template::list::create \
    -html {width 50%} \
    -name persons \
    -multirow persons \
    -key message_id \
    -pass_properties {useScreenNameP screen_name} \
    -elements {
	name {
	    label "\#forums.User\#"
	    html {align left}
            display_template {<if @useScreenNameP@>@screen_name@</if><else><a href="user-history?user_id=@persons.user_id@">@persons.first_names@ @persons.last_name@</a></else>}
	}
	num_msg {
	    label "\#forums.Number_of_Posts\#"
	    html {align left}
	}
	last_post {
	    label "\#forums.Posted\#"
	    html {align right}
	}
    }

db_multirow persons select_users_wrote_post {} 

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
