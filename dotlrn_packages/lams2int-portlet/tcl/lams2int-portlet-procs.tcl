# packages/lams2int-portlet/tcl/lams2int-portlet-procs.tcl

ad_library {
    
    Procedures to support the .LRN LAMS2.x Integrations portlet.
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-17
    @cvs-id $Id$
}

namespace eval lams2int_portlet {

    ad_proc -private get_my_name {
    } {
        return "lams2int_portlet"
    }

    ad_proc -private my_package_key {
    } {
        return "lams2int-portlet"
    }

    ad_proc -public get_pretty_name {
    } {
        return "LAMS Lessons"
    }

    ad_proc -public link {
    } {
        return ""
    }

    ad_proc -public add_self_to_page {
        {-portal_id:required}
        {-package_id:required}
        {-param_action:required}
    } {
        Adds a LAMS Integration PE to the given portal or appends the given
        lams2int_package_id to the params of the lams2int pe already there

        @param portal_id The page to add self to
        @param package_id the id of the lams2int package for this community

        @return element_id The new element's id
    } {
        return [portal::add_element_parameters \
            -portal_id $portal_id \
		    -portlet_name [get_my_name] \
            -key package_id \
            -value $package_id \
		    -pretty_name [get_pretty_name] \
		    -force_region [parameter::get_from_package_key \
				       -package_key [my_package_key] \
				       -parameter "lams2int_portlet_force_region"] \
            -param_action $param_action
		]
    }

    ad_proc -public remove_self_from_page {
        {-portal_id:required}
        {-package_id:required}
    } {
        Removes a lams2int PE from the given page or just the passed
        in lams2int_package_id parameter from the portlet
        (that has other lams2int_package_ids)

        @param portal_id The page to remove self from
        @param package_id
    } {
        portal::remove_element_parameters \
            -portal_id $portal_id \
            -portlet_name [get_my_name] \
            -key package_id \
            -value $package_id
    }

    ad_proc -public show {
        cf
    } {
    } {
        portal::show_proc_helper \
            -package_key [my_package_key] \
            -config_list $cf \
            -template_src "lams2int-portlet"
    }

}