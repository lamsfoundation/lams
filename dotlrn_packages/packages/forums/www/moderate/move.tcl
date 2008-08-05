ad_page_contract {

    Move a thread to other forum.

    @author Natalia Pérez (nperper@it.uc3m.es)
    @creation-date 2005-03-14   

} {
    message_id:integer,notnull    
    selected_forum:integer,notnull
    {confirm_p 0}
}

set table_border_color [parameter::get -parameter table_border_color]

# Check that the user can moderate the forum
forum::security::require_moderate_message -message_id $message_id

# Select the stuff
forum::message::get -message_id $message_id -array message

#form to confirm if a user want to move the thread
ad_form -name confirmed_move -mode {display} -actions [list [list [_ forums.Yes] yes] [list No no] ] -export { message_id return_url selected_forum} -html {enctype multipart/form-data} -form {
 {data:text(hidden)                     {value 0}}
} 
#get the clicked button
set action [template::form::get_action confirmed_move]

if {$action == "yes"} {
    set confirm_p 1    
}
if {$action == "no"} {
    set confirm_p 2    
}

#get the name of forum where the thread will be moved
db_0or1row get_forum_name "select name from forums_forums where forum_id=:selected_forum"

# Confirmed
if {$confirm_p == 1} {    
    
    set forum_id $selected_forum
    
    #update the initial father message: update forum_id and tree_sortkey. If in final forum there is no any thread then tree_sortkey is 0,
    #else tree_sortkey=tree_sortkey+1  
    db_0or1row forums::move_message::select_num_msg {}
    if {$num_post == 0 } {
        db_dml forums::move_message::update_msg {}
    } else {
        db_foreach forums::move_message::select_tree_sortkey {} {
	    set max_tree_sortkey $tree_sortkey
	}
        db_dml forums::move_message::update_moved_msg {}
    }    
    
    #get all descendents
    db_0or1row forums::move_message::select_tree_sortkey_new {}
    db_foreach forums::move_message::get_all_child {} {          
	set join_tree_sortkey $message_tree_sortkey
	append join_tree_sortkey $child_tree_sortkey	
	#update children messages: forum_id and tree_sortkey
        db_dml forums::move_message::update_children {}        
    }
        
    #update final forum: increase thread_count, approved_thread_count and max_child_sortkey, update last_post
    db_0or1row forums::move_message::select_new_data_forums_forums {}    
    db_dml forums::move_message::update_forums_final {}
    
    #update initial forum: decrease thread_count, approved_thread_count and max_child_sortkey, update last_post 
    db_0or1row forums::move_message::select_data_forum_initial {}    
    db_dml forums::move_message::update_forum_initial {}        
        
    # Redirect to the forum
    ad_returnredirect "../forum-view?forum_id=$forum_id"
    ad_script_abort
}

set message_id $message(message_id)
set return_url "../message-view"

if {$confirm_p == 2} {
   #if confirm_p is no then return to the message view
   ad_returnredirect "../message-view?message_id=$message(message_id)"
}
set url_vars [export_url_vars message_id return_url selected_forum]

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
