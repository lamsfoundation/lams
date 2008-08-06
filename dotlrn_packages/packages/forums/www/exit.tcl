# packages/forums/www/exit.tcl

ad_page_contract {
    
    Goes to the next LAMS Activity
    
    @author Ernie Ghiglione (ErnieG@mm.st)
    @creation-date 2008-08-01
    @arch-tag: 329163D6-BFAB-473F-8253-46873E51FBB4
    @cvs-id $Id$
} {
    returnUrl
} -properties {
} -validate {
} -errors {
}

if {[nsv_exists lams [ad_conn session_id]]} {
    nsv_unset lams [ad_conn session_id]
}

ad_returnredirect $returnUrl
