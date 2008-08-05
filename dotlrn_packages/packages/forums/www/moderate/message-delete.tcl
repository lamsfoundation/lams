ad_page_contract {

    Delete a Message

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-24
    @cvs-id $Id$

} {
    message_id:integer,notnull
    {return_url "../message-view"}
    {confirm_p 0}
}

# Check that the user can moderate the forum
forum::security::require_moderate_message -message_id $message_id

# Select the stuff
forum::message::get -message_id $message_id -array message

set dynamic_script "
  <!--
  collapse_symbol = '<img src=\"/resources/forums/Collapse16.gif\" width=\"16\" height=\"16\" ALT=\"collapse message\" style=\"border:0\" title=\"collapse message\">';
  expand_symbol = '<img src=\"/resources/forums/Expand16.gif\" width=\"16\" height=\"16\" ALT=\"expand message\" style=\"border:0\" title=\"expand message\">';
  loading_symbol = '<img src=\"/resources/forums/dyn_wait.gif\" width=\"12\" height=\"16\" ALT=\"x\" style=\"border:0\">';
  loading_message = 'Loading...';
  rootdir = 'messages-get';
  sid = '$message(root_message_id)';
  //-->
"
# stylesheets
set lang [ad_conn language]
template::head::add_css -href /resources/forums/forums.css -media all -lang $lang

# js scripts
template::head::add_script -type "text/javascript" -src "/resources/forums/forums.js"
template::head::add_script -type "text/javascript" -script $dynamic_script

ad_return_template
