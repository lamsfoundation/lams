ad_page_contract {
    a message attachment chunk to be included to display attachments

    @author ben (ben@openforce.net)
    @creation-date 2002-07-02
    @cvs-id $Id$
}

if {![array exists message]} {
    ad_return_complaint 1 "[_ forums.lt_need_to_provide_a_mes]"
}

if {![exists_and_not_null bgcolor]} {
    set bgcolor "#ffffff"
}

# get the attachments
set attachments [attachments::get_attachments -object_id $message(message_id)]

set attachment_graphic [attachments::graphic_url]

if {[exists_and_not_null alt_template]} {
  ad_return_template $alt_template
}
