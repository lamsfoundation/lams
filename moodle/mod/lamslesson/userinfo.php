<?php

/**
 * This page return user info in CSV format to LAMS server.
 * The pass-in parameters are un, ts and hs.
 * un means username, ts means timestamp and hs means hash.
 * The plain text of the hash should be lower case string of
 * ts.trim()+un.trim()+serverId+serverKey. The hash algorithm
 * is sha1.
 * If the hash is not matched to the result calculated, then a
 * http error code should be returned.
 * Moodle's admin should be responsible for correctly setting
 * serverId and serverKey
 */
    include_once('../../config.php');
    include_once($CFG->libdir.'/datalib.php');
    global $DB;

    $hs = required_param('hs', PARAM_ALPHANUM);
    $ts = required_param('ts', PARAM_RAW_TRIMMED);
    $un = required_param('un', PARAM_RAW_TRIMMED);    
    $lsid = optional_param('lsid', '', PARAM_INT);

    if(!isset($CFG->lamslesson_serverid)||!isset($CFG->lamslesson_serverkey))
    {
        header('HTTP/1.1 401 Unauthenticated');
        exit(1);
    }
    $plaintext = trim($ts).trim($un).trim($CFG->lamslesson_serverid).trim($CFG->lamslesson_serverkey);
    $hash = sha1(strtolower($plaintext));
    if($hash != $hs){
        header('HTTP/1.1 401 Unauthenticated');
        exit(1);
    }

    //OK, the caller is authenticated. Now let's fulfill its request.
    //What it needs is user info in CSV format. It should be like this:
    //username,first name,last name,job title, department, organisation,
    //address,phone,fax,mobile,email
    $user = $DB->get_record('user', array('username'=>$un));

    //return false if none found
    if(!$user){
        header('HTTP/1.1 401 Unauthenticated');
        exit(1);
    }
    $array = array('',$user->firstname,$user->lastname,$user->address,$user->city,'','',$user->country,$user->phone1,'','',$user->email,$user->country,$USER->lang);
    $comma_separated = implode(",", $array);//need more sophiscated algorithm to generate CSV formatted string
    echo $comma_separated;

?>
