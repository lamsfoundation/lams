ad_page_contract {
    
    Disable a Forum

    @author Ben Adida (ben@openforce.net)
    @creation-date 2002-05-28
    @cvs-id $Id$

} {
    forum_id:integer,notnull
}

forum::enable -forum_id $forum_id

ad_returnredirect "."



