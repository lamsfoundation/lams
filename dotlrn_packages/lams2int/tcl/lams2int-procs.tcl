# packages/lams2int/tcl/lams2int-procs.tcl

ad_library {
    
    LAMS2.x Integration Procedures
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id $Id$
}

#
#  Copyright (C) 2005 LAMS Foundation
#
#  This package is free software; you can redistribute it and/or modify it under the
#  terms of the GNU General Public License as published by the Free Software
#  Foundation; version 2 of the License
#
#  It is distributed in the hope that it will be useful, but WITHOUT ANY
#  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
#  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
#  details.
#

namespace eval lams2int {}

ad_proc -public lams2int::add {
    {-learning_session_id:required} 
    {-display_title:required}
    {-introduction ""}
    {-hide "f"}
    {-start_date ""}
    {-creation_user}
    {-package_id}
    {-community_id}
} {
    Adds a new LAMS lesson to .LRN

    @param learning_session_id LAMS learning session id
    @param display_title title for the lesson to be displayed in .LRN
    @param introduction Introduction to be displayed in .LRN
    @param hide boolean, whether the lesson should be hiden to students or not
    @param start_date start date to show the lesson to students
    @param creation_user creation user
    @param package_id package id
    @param community_id the community where this lesson will live.
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)

} {

    # this should kick in *after* we get a learning_session_id from
    # LAMS

    set creation_ip [ad_conn peeraddr]

    db_transaction {
        set lesson_id [db_exec_plsql new_seq {
            select lams_lesson__new (
                                       :learning_session_id,
                                       :display_title,
                                       :introduction,
                                       :hide,
                                       current_timestamp,
                                       :creation_user,
                                       :creation_ip,
                                       :package_id,
                                       :community_id
				       );
            
        }
		    ]
        
    }

    return $lesson_id
    
}

ad_proc -public lams2int::get_datetime {

} {
    Generates the datetime for the hash

} {

    set user_now_time [lc_time_system_to_conn [dt_systime]]
    return  [lc_time_fmt $user_now_time "%B %d,%Y %r"] 

}

ad_proc -public lams2int::get_lams_server_url {

} {
    Gets the lams_server_url from the LAMS Configuration package

} {

    set lams2conf_package_id [db_string pack_id_lams2_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter lams_server_url -package_id $lams2conf_package_id]


}


ad_proc -public lams2int::get_server_id {

} {
    Gets the lams_server_id from the LAMS2.x Configuration package (parameter)

} {

    set lams2conf_package_id [db_string pack_id_lams_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter server_id -package_id $lams2conf_package_id]

}

ad_proc lams2int::get_server_key {

} {
    Gets the lams_server_key from the LAMS Configuration package (parameter)

} {

    set lams2conf_package_id [db_string pack_id_lams_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter server_key -package_id $lams2conf_package_id]

}

ad_proc lams2int::get_request_source {

} {
    Gets the request_source from the LAMS Configuration package (parameter)

} {

    set lams2conf_package_id [db_string pack_id_lams_conf {select package_id from apm_packages where package_key = 'lams2conf'}]
    return [parameter::get -parameter request_source -package_id $lams2conf_package_id]

}