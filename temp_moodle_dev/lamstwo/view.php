
<?php  // $Id$

/// This page prints a particular instance of lamstwo
/// (Replace lamstwo with the name of your module)

require_once("../../config.php");
require_once("lib.php");
require_once("constants.php");

$id = optional_param('id', 0, PARAM_INT);    // Course Module ID, or

if (! $cm = get_coursemodule_from_id('lamstwo', $id)) {
    error("Course Module ID was incorrect");
}

if (! $course = get_record("course", "id", $cm->course)) {
    error("Course is misconfigured");
}

if (! $lamstwo = get_record("lamstwo", "id", $cm->instance)) {
    error("Course module is incorrect");
}

require_login($course->id);

add_to_log($course->id, "lamstwo", "view", "view.php?id=$cm->id", "$lamstwo->id");

/// Print the page header

//if ($course->category) {
//    $navigation = "<A HREF=\"../../course/view.php?id=$course->id\">$course->shortname</A> ->";
//}
print_header_simple(format_string($lamstwo->name), "",
        "<a href=\"index.php?id=$course->id\">$strchoices</a> -> ".format_string($lamstwo->name), "", "", true,
        update_module_button($cm->id, $course->id, get_string("lesson","lamstwo")), navmenu($course, $cm));

echo '<table id="layout-table"><tr>';
echo '<td id="middle-column">';
print_heading(format_string($lamstwo->name));

//$strlamstwos = get_string("modulenameplural", "lamstwo");
//$strlamstwo  = get_string("modulename", "lamstwo");

//print_header("$course->shortname: $lamstwo->name", "$course->fullname",
//             "$navigation <A HREF=index.php?id=$course->id>$strlamstwos</A> -> $lamstwo->name",
//              "", "", true, update_module_button($cm->id, $course->id, $strlamstwo),
//              navmenu($course, $cm));

/// Print the main part of the page
if(isteacher($course->id,$USER->id)||isteacheredit($course->id,$USER->id)){
    $datetime =    date("F d,Y g:i a");
    $plaintext = trim($datetime).trim($USER->username).trim($LAMS2CONSTANTS->monitor_method).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
    $hash = sha1(strtolower($plaintext));
    $url = $CFG->lamstwo_serverurl.$LAMS2CONSTANTS->login_request.
        '?'.$LAMS2CONSTANTS->param_uid.'='.$USER->username.
        '&'.$LAMS2CONSTANTS->param_method.'='.$LAMS2CONSTANTS->monitor_method.
        '&'.$LAMS2CONSTANTS->param_timestamp.'='.urlencode($datetime).
        '&'.$LAMS2CONSTANTS->param_serverid.'='.$CFG->lamstwo_serverid.
        '&'.$LAMS2CONSTANTS->param_hash.'='.$hash.
        '&'.$LAMS2CONSTANTS->param_lsid.'='.$lamstwo->lesson_id.
        '&'.$LAMS2CONSTANTS->param_courseid.'='.$lamstwo->course.
		'&'.$LAMS2CONSTANTS->param_country.'='.trim($USER->country).
		'&'.$LAMS2CONSTANTS->param_lang.'='.substr(trim($USER->lang),0,2);
    print_simple_box_start('center');
    echo '<a href="#" onClick="javascript:window.open(\''.$url.'\',\'monitor\',\'location=0,toolbar=0,menubar=0,statusbar=0,width=796,height=570,resizable\')">'.get_string("openmonitor", "lamstwo").'</a>';
    print_simple_box_end();
    
    $plaintext = trim($datetime).trim($USER->username).trim($LAMS2CONSTANTS->learner_method).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
    $hash = sha1(strtolower($plaintext));
    $url = $CFG->lamstwo_serverurl.$LAMS2CONSTANTS->login_request.
        '?'.$LAMS2CONSTANTS->param_uid.'='.$USER->username.
        '&'.$LAMS2CONSTANTS->param_method.'='.$LAMS2CONSTANTS->learner_method.
        '&'.$LAMS2CONSTANTS->param_timestamp.'='.urlencode($datetime).
        '&'.$LAMS2CONSTANTS->param_serverid.'='.$CFG->lamstwo_serverid.
        '&'.$LAMS2CONSTANTS->param_hash.'='.$hash.
        '&'.$LAMS2CONSTANTS->param_lsid.'='.$lamstwo->lesson_id.
        '&'.$LAMS2CONSTANTS->param_courseid.'='.$lamstwo->course.
		'&'.$LAMS2CONSTANTS->param_country.'='.trim($USER->country).
		'&'.$LAMS2CONSTANTS->param_lang.'='.substr(trim($USER->lang),0,2);
    print_simple_box_start('center');
    echo '<a href="#" onClick="javascript:window.open(\''.$url.'\',\'learner\',\'location=0,toolbar=0,menubar=0,statusbar=0,width=796,height=570,resizable\',0)">'.get_string("openlearner", "lamstwo").'</a>';
    print_simple_box_end();
}else if(isstudent($course->id,$USER->id)){
    $datetime =    date("F d,Y g:i a");
    $plaintext = trim($datetime).trim($USER->username).trim($LAMS2CONSTANTS->learner_method).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
    $hash = sha1(strtolower($plaintext));
    $url = $CFG->lamstwo_serverurl.$LAMS2CONSTANTS->login_request.
        '?'.$LAMS2CONSTANTS->param_uid.'='.$USER->username.
        '&'.$LAMS2CONSTANTS->param_method.'='.$LAMS2CONSTANTS->learner_method.
        '&'.$LAMS2CONSTANTS->param_timestamp.'='.urlencode($datetime).
        '&'.$LAMS2CONSTANTS->param_serverid.'='.$CFG->lamstwo_serverid.
        '&'.$LAMS2CONSTANTS->param_hash.'='.$hash.
        '&'.$LAMS2CONSTANTS->param_lsid.'='.$lamstwo->lesson_id.
        '&'.$LAMS2CONSTANTS->param_courseid.'='.$lamstwo->course.
		'&'.$LAMS2CONSTANTS->param_country.'='.trim($USER->country).
		'&'.$LAMS2CONSTANTS->param_lang.'='.substr(trim($USER->lang),0,2);
    print_simple_box_start('center');
    echo '<a href="#" onClick="javascript:window.open(\''.$url.'\',\'learner\',\'location=0,toolbar=0,menubar=0,statusbar=0,width=796,height=570,resizable\',0)">'.get_string("openlearner", "lamstwo").'</a>';
    print_simple_box_end();
}

if ($lamstwo->introduction) {
    print_simple_box(format_text($lamstwo->introduction), 'center', '70%', '', 5, 'generalbox', 'description');
    echo '<br />';
}


/// Finish the page
echo '</td></tr></table>';

/// Finish the page
print_footer($course);

?>
