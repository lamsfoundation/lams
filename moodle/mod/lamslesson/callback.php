<?php

/**
 * This page is called from LAMS so it can trigger a call back
 * to get a specific user's gradebook mark
 * The pass-in parameters are un, lsId  ts and hs.
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
include_once('lib.php');
global $DB;

$hs = required_param('hs', PARAM_ALPHANUM);
$ts = required_param('ts', PARAM_RAW);
$un = required_param('un', PARAM_ALPHANUM);
$lsid = required_param('lsId', PARAM_INT);

if(!isset($CFG->lamslesson_serverid)||!isset($CFG->lamslesson_serverkey)) {
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
// and make Moodle get the latest marks for this user in this lesson

$user = $DB->get_record('user', array('username'=>$un));
if(!$user){
  header('HTTP/1.1 401 Unauthenticated');
  exit(1);
}

$lamslesson  = $DB->get_record('lamslesson', array('lesson_id' => $lsid), '*', MUST_EXIST);

$gradebookmark = lamslesson_get_lams_outputs($user->username,$lamslesson,$user->username);

//allow lessonComplete.jsp on LAMS side to make an Ajax call to this PHP script
$parsed_url = parse_url("$CFG->lamslesson_serverurl");
$lams_server_url = isset($parsed_url['scheme']) ? $parsed_url['scheme'].'://' : '';
$lams_server_url .= $parsed_url['host'];
$lams_server_url .= isset($parsed_url['port']) ? ':'.$parsed_url['port'] : '';
header("Access-Control-Allow-Origin: ".$lams_server_url);

// let LAMS know that mark has been stored successfully
echo 'OK';

?>
