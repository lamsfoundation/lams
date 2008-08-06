ad_page_contract {
    
    Create a Forum

    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2008-07-23
    @cvs-id $Id$

} -query {
    {name ""}
    {toolContentID 0}
    {lamsUpdateURL ""}
}

ns_log Notice "lamsUpdateURL $lamsUpdateURL"
set context [list [_ forums.Create_New_Forum]]

ad_return_template
