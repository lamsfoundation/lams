# packages/lams2int/tcl/lams2int-ws-procs.tcl

ad_library {
    
    LAMS2.x Web Services Library
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id $Id$
}
namespace eval lams2int::ws {}

ad_proc -public lams2int::ws::generate_hash {
    {-datetime ""} 
    {-username ""}
    {-method ""}
    {-server_id ""}
    {-server_key ""}
} {
    Returns the validation hash. If method is not passed, it will not
    be included in the hash

    @param datetime
    @param username
    @param method (author, monitor, learner)
    @param server_id 
    @param server_key
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)

} {


    if {[empty_string_p $datetime]} {

        set datetime [lams2int::get_datetime]

    }

    if {[empty_string_p $username]} {
        set username [ad_conn user_id]
        
    }

    if {[empty_string_p $server_id]} {
        set server_id [string trim [lams2int::get_server_id]]
    }

    if {[empty_string_p $server_key]} {
        set server_key [string trim [lams2int::get_server_key]]
    }

    if {[empty_string_p $method]} {
        # we don't have a method, therefore we just create the
        # rawstring without it

        set rawstring [string tolower [concat $datetime$username$server_id$server_key]]
    } else {

        set rawstring [string tolower [concat $datetime$username$method$server_id$server_key]]
    }

    ns_log Notice "Hash string: [string tolower [ns_sha1 $rawstring]]  $datetime $server_key   --- $rawstring"

    return [string tolower [ns_sha1 $rawstring]]

}