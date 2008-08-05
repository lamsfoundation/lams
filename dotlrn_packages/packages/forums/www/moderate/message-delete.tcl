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

# stylesheets
set lang [ad_conn language]
template::head::add_css -href /resources/forums/forums.css -media all -lang $lang

# js scripts
template::head::add_script -type "text/javascript" -src "/resources/forums/forums.js"

ad_return_template
