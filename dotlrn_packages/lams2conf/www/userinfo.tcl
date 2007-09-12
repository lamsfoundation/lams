# packages/lams2conf/www/userinfo.tcl

ad_page_contract {
    
    This page return user info in CSV format to LAMS server. 
    
    @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
    @creation-date 2007-04-16
    @cvs-id $Id$
} {
    ts
    un
    hs
} -properties {
} -validate {
} -errors {
}

set timestamp $ts
set username $un
set hash $hs

set datetime $timestamp

ns_log Notice "LAMS2int: LAMS requestimestamp userinfo for user_id $username (datetime: $timestamp hash: $hash)"

set lams_server_url [parameter::get -parameter lams_server_url -package_id [ad_conn package_id]]
set server_key [parameter::get -parameter server_key -package_id [ad_conn package_id]]
set server_id [parameter::get -parameter server_id -package_id [ad_conn package_id]]



set plaintext [string tolower [concat $timestamp$username$server_id$server_key]]
set hashValue [string tolower [ns_sha1 $plaintext]]

if {![string equal [string tolower $hash] $hashValue]} {

    ns_log Warning "LAMSint: userinfo request hash authentication failed. localhash: $hashValue requesthash: $hash"
    ns_write "HTTP/1.1 401 Usernameauthenticated"

}

# get the user info we need to pass to LAMS
set first_name [acs_user::get_element -user_id $username -element first_names]
set last_name [acs_user::get_element -user_id $username -element last_name]
set email [acs_user::get_element -user_id $username -element email]   

ns_log Notice "LAMSint: userinfo transfered to LAMS (user_id $username)"

# send it to LAMS
ReturnHeaders "text/plain"
ns_write "'',$first_name,$last_name,'','','','','','','','',$email,'',''"



