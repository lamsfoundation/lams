<?php

/**
 * English strings for lamslesson
 *
 * You can have a rather longer description of the file as well,
 * if you like, and it can span multiple lines.
 *
 * @package   mod_lamslesson
 * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

defined('MOODLE_INTERNAL') || die();

$string["modulename"] = "LAMS Lesson";
$string["modulenameplural"] = "LAMS Lessons";
$string["modulename_help"] = "The LAMS Lesson module allows teachers to create LAMS lessons within Moodle.

LAMS provides teachers with an intuitive visual authoring environment for creating sequences of learning activities. These activities can include a range of individual tasks, small group work and whole class activities based on both content and collaboration.

Once a sequence is created, it can reuse in one or many courses. 

Additionally, LAMS provides a real-time followup and tracking monitoring interface where teachers can interact with students as they go thru the learning activities. 

For further information visit: lamsfoundation.org.

";
$string["modulename_link"] = "lamslesson";
$string["lamslessonfieldset"] = "Custom example fieldset";
$string["lamslessonname"] = "Lesson name";
$string["lamslessonname_help"] = "This is the content of the help tooltip associated with the lamslessonname field. Markdown syntax is supported.";
$string["lamslesson"] = "LAMS Lesson";
$string["pluginadministration"] = "LAMS Lesson administration";
$string["pluginname"] = "LAMS Lesson";
$string["selectsequence"] = "Select sequence";
$string["displaydesign"] = "Display image design?";
$string["displaydesign_help"] = "If enabled, when the lesson is display to students, it will show the learning design diagram.";
$string["allowlearnerrestart"] = "Students can restart the lesson?";
$string["allowlearnerrestart_help"] = "If enabled, the students will be able to restart the lesson and start from the beginning at any time. On each restart, previous progress is eliminated.";
$string["availablesequences"] = "Sequences";
$string["openauthor"] = "Author new LAMS lessons";
$string["refresh"] = "Refresh";
$string["lamslesson:manage"] = "Manage lessons";
$string["lamslesson:participate"] = "Participate in lessons";
$string["adminheader"] = "LAMS Server Configuration";
$string["admindescription"] = "Configure your LAMS server settings. Make <strong>sure</strong> that the values you enter here correspond with the once you already entered in your LAMS server. Otherwise the integration might not work.";
$string["serverurl"] = "LAMS Server URL:";
$string["serverurlinfo"] = "Here you need to enter the URL for your LAMS server. ie: http://localhost:8080/lams/.";
$string["serverid"] = "Server ID:";
$string["serveridinfo"] = "What is the Server ID you entered in your LAMS server?";
$string["serverkey"] = "Server Key:";
$string["serverkeyinfo"] = "What is the Server Key you entered in your LAMS server?";
$string["validationbutton"] = "Validate settings";
$string["validationheader"] = "Settings validation";
$string["validationinfo"] = "Before you save your settings, please press the button to validate them with the LAMS server. If the validation is correct, save these settings. If not, please check that the settings you have entered match with the values in the LAMS server";
$string["validationhelp"] = "Need help? check out the";
$string["offsetbutton"] = "Calculate offset";
$string["offsetinfo"] = "If you are enforcing the login request time to live limit, it's important that you set up the time difference (in minutes) between the LAMS and your Moodle server. Click on the Calculate offset to see if there's a time difference between your LAMS and Moodle server. Take the offset time displayed and add it to \"Offset time difference\" setting.";
$string["servertimeoffset"] = "Offset time difference (minutes)";
$string["servertimeoffsetinfo"] = "This is the time difference (or time offset) between the LAMS and Moodle server.";
$string["offsetheader"] = "Time offset";
$string["lamsmoodlehelp"] = "LAMS-Moodle integration tutorial";
$string["validationsuccessful"] = "Validation successful! You can now save your settings and start using LAMS within Moodle.";
$string["validationfailed"] = "Validation failed: please check that the settings you have entered match with the settings in LAMS";
$string["restcallfail"] = "Call to LAMS failed: received no response or connection was refused. Please check that you have the correct LAMS server URL and that it is online.";
$string["sequencenotselected"] = "You must select a sequence to proceed.";
$string["previewthislesson"] = "Preview this lesson";
$string["updatewarning"] = "Warning: selecting another sequence than the current one will create a new lesson for the students. This might result in some slightly confused students";
$string["currentsequence"] = "Current sequence:";
$string["nolessons"] = "There are no LAMS lessons yet in this instance.";
$string["lessonname"] = "Lesson name";
$string["links"] = "Links";
$string["introduction"] = "Introduction";
$string["openmonitor"] = "Monitor this lesson";
$string["lastmodified"] = "Last modified";
$string["openlesson"] = "Open Lesson";
$string["empty"] = "empty";
$string["completionfinish"] = "Show as complete when user finished the lesson";
$string["yourprogress"] = "Your Lesson Progress";
$string["youhavecompleted"] = "You have completed: ";
$string["outof"] = "out of approximately";
$string["lessonincompleted"] = "Lesson is not yet completed";
$string["lessoncompleted"] = "You have completed this lesson";
$string["activities"] = "activities";
$string["ymmv"] = "Total activities depend on your learning path.";
$string["yourmarkis"] = "Your final mark/grade is:";
$string["outofmark"] = "out of";
