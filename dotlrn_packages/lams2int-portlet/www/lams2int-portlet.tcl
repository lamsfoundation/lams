# packages/lams2int-portlet/www/lams2int-portlet.tcl

ad_page_contract {
    
    The logic for the lorsm portlet.
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-17
    @cvs-id $Id$
} {
    
} -properties {
} -validate {
} -errors {
}

array set config $cf

set shaded_p $config(shaded_p)
set list_of_package_ids [lsort $config(package_id)]
set one_instance_p [ad_decode [llength $list_of_package_ids] 1 1 0]

ad_return_template