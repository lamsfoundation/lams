ad_page_contract {

    Move a Message to other thread in the same forum.

    @author Natalia Pérez (nperper@it.uc3m.es)
    @creation-date 2005-03-14   

} {
    msg_id:integer,notnull    
    selected_message:integer,notnull
    {confirm_p 0}
}

set table_border_color [parameter::get -parameter table_border_color]

# Check that the user can moderate the forum
forum::security::require_moderate_message -message_id $msg_id

# Select the stuff
forum::message::get -message_id $msg_id -array message

#form to confirm 
ad_form -name confirmed_move -mode {display} -actions [list [list  [_ forums.Yes]  yes] [list No no] ] -export { msg_id return_url selected_message} -html {enctype multipart/form-data} -form {
    {data:text(hidden) {value 0}}
}
#get the clicked button 
set action [template::form::get_action confirmed_move]
if {$action == "yes"} {
    set confirm_p 1
}
if {$action == "no"} {
    set confirm_p 2
}

#information about final message
db_0or1row forums::move_thread::get_subject_message {}

# Confirmed
if {$confirm_p == 1} {    
    
    set message_id $selected_message
    
    #update the final message: increase reply_count, approved_reply_count and max_child_sortkey
    db_dml forums::move_thread::update_final_message {}
      
    #get the tree_sortkey of final message
    db_0or1row forums::move_thread::get_father_tree_sortkey {}
    
    #update the initial message
    db_0or1row forums::move_thread::get_final_tree_sortkey {}
        
    #update the initial father message: decreasing reply_count and approved_reply_count
    db_0or1row forums::move_thread::get_father_message_id {}    
    db_dml forums::move_thread::update_father_reply_count {}       
    
    #get all descendents
    db_foreach forums::move_thread::get_all_child {} {
       #update los ocho primeros dígitos de tree_sortkey
       set join_tree_sortkey $father_tree_sortkey
       append join_tree_sortkey $father_max_child_sortkey
       append join_tree_sortkey $child_tree_sortkey
       #update the children's initial message: update tree_sortkey       
       db_dml forums::move_thread::update_child_thread_father {} 
       #update the final message: increasing reply_count and approved_reply_count
       db_dml forums::move_thread::update_final_reply_count {}
       #update the initial father message: decreasing reply_count and approved_reply_count       
       db_dml forums::move_thread::update_father_reply_count {}
    }       
    
    #update the initial message    
    set join_tree_sortkey $father_tree_sortkey
    append join_tree_sortkey $father_max_child_sortkey
    db_dml forums::move_thread::update_thread_father {}    
    
    #update the last_child_post final thread and last_child_post initial father thread
    db_dml forums::move_thread::update_last_child_post_initial {}
    db_dml forums::move_thread::update_last_child_post_final {}
    
    # Redirect to the forum
    ad_returnredirect "../forum-view?forum_id=$message(forum_id)"
    ad_script_abort
}

set msg_id $message(message_id)
set return_url "../message-view"

#if confirm is no 
if {$confirm_p == 2} {
    ad_returnredirect "../message-view?message_id=$message(message_id)"
}

set url_vars [export_url_vars msg_id return_url selected_message]

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}

set title "#forums.Move_message_to_thread# \"$subject\""
set context $title