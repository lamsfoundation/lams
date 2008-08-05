# packages/forums/admin/moderate/choose_message_move.tcl

ad_page_contract {

	Show a list of available messages so that user can choose one.

	@author Natalia Perez (nperper@it.uc3m.es)
	@creation-date 2005-03-17	
} {
    {return_url "../message-view"}
} -properties {
	context:onevalue
} -validate {
} -errors {
}

 template::list::create \
    -name available_messages \
    -multirow messages \
    -key message_id \
    -elements {
        selected {
	    label #forums.Selected#
	    display_template {
	        <input name="selected_message" value="@messages.message_id@" type="radio">
	    }
	}
	message_id {
	    hide_p 1
	}
	message_subject {
	    label #forums.Message_Subject#
	    display_col subject
	}
    }

db_0or1row get_tree_sortkey { }
        
db_foreach get_parent_id { } {    
    db_multirow messages get_messages "
        select message_id, subject from forums_messages where forum_id = $message(forum_id) and message_id <> $message(message_id) and parent_id is null and message_id <> :parent_id
    "
}

set msg_id $message(message_id)

# set context & title
set context [list "[_ forums.Available_Messages]"]
set title "[_ forums.Available_Messages]"

		
