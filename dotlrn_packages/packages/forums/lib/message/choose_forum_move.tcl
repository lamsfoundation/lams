# packages/forums/admin/moderate/choose_forum_move.tcl

ad_page_contract {

	Show a list of available forums so that user can choose one.

	@author Natalia Perez (nperper@it.uc3m.es)
	@creation-date 2005-03-15	
} {
    {return_url "../message-view"}
} -properties {
	context:onevalue
} -validate {
} -errors {
}


 template::list::create \
    -name available_forums \
    -multirow forums \
    -key forum_id \
    -elements {
        selected {
	    label #forums.Selected#
	    display_template {
	        <input name="selected_forum" value="@forums.forum_id@" type="radio">
	    }
	}
	forum_id {
	    hide_p 1
	}
	forum_name {
	    label #forums.Forum_Name#
	    display_col name
	}
	}

db_0or1row get_package_id "select distinct(package_id) from forums_forums where forum_id = $message(forum_id)"
db_multirow forums get_forums "
    select forum_id, name from forums_forums where forum_id <> $message(forum_id) and package_id = :package_id and enabled_p = 't'
"

set message_id $message(message_id)

# set context & title
set context [list "[_ forums.Available_Forums]"]
set title "[_ forums.Available_Forums]"

		
