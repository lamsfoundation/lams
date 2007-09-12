# packages/dotlrn-lams2int/tcl/apm-callback-procs.tcl

ad_library {
    
    Procedures for registering implementations for the
    dotlrn lams2int package. 
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-17
    @cvs-id $Id$
}

namespace eval dotlrn_lams2int {}

ad_proc -public dotlrn_lams2int::install {} {
    dotLRN LAMS2INT package install proc
} {
    register_portal_datasource_impl
}

ad_proc -public dotlrn_lams2int::uninstall {} {
    dotLRN LAMS2INT package uninstall proc
} {
    unregister_portal_datasource_impl
}

ad_proc -public dotlrn_lams2int::register_portal_datasource_impl {} {
    Register the service contract implementation for the dotlrn_applet service contract
} {
    set spec {
        name "dotlrn_lams2int"
        contract_name "dotlrn_applet"
        owner "dotlrn_lams2int"
        aliases {
            GetPrettyName dotlrn_lams2int::get_pretty_name
            AddApplet dotlrn_lams2int::add_applet
            RemoveApplet dotlrn_lams2int::remove_applet
            AddAppletToCommunity dotlrn_lams2int::add_applet_to_community
            RemoveAppletFromCommunity dotlrn_lams2int::remove_applet_from_community
            AddUser dotlrn_lams2int::add_user
            RemoveUser dotlrn_lams2int::remove_user
            AddUserToCommunity dotlrn_lams2int::add_user_to_community
            RemoveUserFromCommunity dotlrn_lams2int::remove_user_from_community
            AddPortlet dotlrn_lams2int::add_portlet
            RemovePortlet dotlrn_lams2int::remove_portlet
            Clone dotlrn_lams2int::clone
            ChangeEventHandler dotlrn_lams2int::change_event_handler
        }
    }
    
    acs_sc::impl::new_from_spec -spec $spec
}

ad_proc -public dotlrn_lams2int::unregister_portal_datasource_impl {} {
    Unregister service contract implementations
} {
    acs_sc::impl::delete \
        -contract_name "dotlrn_applet" \
        -impl_name "dotlrn_lams2int"
}