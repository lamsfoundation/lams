ad_page_contract {

    Reject a Message

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-24
    @cvs-id $Id$

} {
    message_id:integer,notnull
    {return_url "../message-view"}
}

# Check that the user can moderate the forum
forum::security::require_moderate_message -message_id $message_id

# Reject the message
forum::message::reject -message_id $message_id

ad_returnredirect "$return_url?message_id=$message_id"



