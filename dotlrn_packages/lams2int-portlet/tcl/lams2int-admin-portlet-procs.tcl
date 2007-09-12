# packages/lams2int-portlet/tcl/lams2int-admin-portlet-procs.tcl

ad_library {
    
    Procedures to support the LAMS Integration portlet.
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-17
    @cvs-id $Id$
}

namespace eval lams2int_admin_portlet {

    ad_proc -private get_my_name {
    } {
        return "lams2int_admin_portlet"
    }

    ad_proc -public get_pretty_name {
    } {
        return "LAMS Integration Admin"
    }

    ad_proc -private my_package_key {
    } {
        return "lams2int-portlet"
    }

    ad_proc -public link {
    } {
        return ""
    }

    ad_proc -public add_self_to_page {
        {-portal_id:required}
        {-package_id:required}
    } {
        Adds a LAMS Integration admin PE to the given admin
									 # portal. There should only
        ever be one of these portals on an admin page with only one
										     # lams2int_package_id

        @param portal_id The page to add self to
        @param package_id the id of the lams2int package

        @return element_id The new element's id
    } {
        return [portal::add_element_parameters \
            -portal_id $portal_id \
		    -portlet_name [get_my_name] \
            -key package_id \
            -value $package_id
		]
    }


    ad_proc -public remove_self_from_page {
        portal_id
    } {
        Removes a LAMS Integration admin PE from the given portal
    } {
        portal::remove_element -portal_id $portal_id -portlet_name [get_my_name]
    }

    ad_proc -public show {
        cf
    } {
    } {
        portal::show_proc_helper \
            -package_key [my_package_key] \
            -config_list $cf \
            -template_src "lams2int-admin-portlet"
    }

}
