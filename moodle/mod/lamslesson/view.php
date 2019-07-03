<?php

// This file is part of Moodle - http://moodle.org/
//
// Moodle is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodle is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodle.  If not, see <http://www.gnu.org/licenses/>.


/**
 * Prints a particular instance of lamslesson
 *
 * You can have a rather longer description of the file as well,
 * if you like, and it can span multiple lines.
 *
 * @package   mod_lamslesson
 * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

require_once(dirname(dirname(dirname(__FILE__))).'/config.php');
require_once(dirname(__FILE__).'/lib.php');

$id = optional_param('id', 0, PARAM_INT); // course_module ID, or
$n  = optional_param('n', 0, PARAM_INT);  // lamslesson instance ID - it should be named as the first character of the module

if ($id) {
    $cm         = get_coursemodule_from_id('lamslesson', $id, 0, false, MUST_EXIST);
    $course     = $DB->get_record('course', array('id' => $cm->course), '*', MUST_EXIST);
    $lamslesson  = $DB->get_record('lamslesson', array('id' => $cm->instance), '*', MUST_EXIST);
} elseif ($n) {
    $lamslesson  = $DB->get_record('lamslesson', array('id' => $n), '*', MUST_EXIST);
    $course     = $DB->get_record('course', array('id' => $lamslesson->course), '*', MUST_EXIST);
    $cm         = get_coursemodule_from_instance('lamslesson', $lamslesson->id, $course->id, false, MUST_EXIST);
} else {
    print_error('You must specify a course_module ID or an instance ID');
}

require_login($course, true, $cm);

$context = get_context_instance(CONTEXT_MODULE, $cm->id);

add_to_log($course->id, 'lamslesson', 'view', "view.php?id=$cm->id", $lamslesson->name, $cm->id);

/// Print the page header

$PAGE->set_url('/mod/lamslesson/view.php', array('id' => $cm->id));
$PAGE->set_title($lamslesson->name);
$PAGE->set_heading($course->shortname);
// $PAGE->set_button(update_module_button($cm->id, $course->id, get_string('modulename', 'lamslesson')));

// Main page
$options_html = '';
$canmanage = has_capability('mod/lamslesson:manage', $context);

// Log the lamslesson view.
add_to_log($course->id, "lamslesson", "view lamslesson", "view.php?id=$cm->id", "$lamslesson->id", $cm->id);
        
// Check capabilities

$canparticipate = has_capability('mod/lamslesson:participate', $context);



// Output starts here
echo $OUTPUT->header();

// Main LAMS region
echo $OUTPUT->heading($lamslesson->name);
echo $OUTPUT->box_start('generalbox', 'instructions');
echo '<p>';
echo format_module_intro('lamslesson', $lamslesson, $cm->id);
echo '</p>';
echo '<br>';

if ($lamslesson->displaydesign) {
   // Get design image
   // For now we always will get this as PNG rather than SVG
   $design_image = lamslesson_get_design_image($USER->username,$course->id,$course->shortname,$COURSE->timecreated,"au","en",$lamslesson->sequence_id,"2");
   echo '<div><img class="centerimage" src="' . $design_image . '"></div>';
}

echo '<div class="smalltext">' . get_string('lastmodified', 'lamslesson') . ": " .  userdate($lamslesson->timemodified) .'</div>';
echo $OUTPUT->box_end();

echo $OUTPUT->box_start('generalbox', 'intro');
if ($canparticipate || $canmanage) {
  $learnerurl = lamslesson_get_url($USER->username, $USER->firstname, $USER->lastname, $USER->email, $USER->lang, $USER->country, $lamslesson->lesson_id, $course->id, $course->fullname, $course->timecreated, LAMSLESSON_PARAM_LEARNER_STRICT_METHOD);
  $popupaction = new popup_action('click', $learnerurl, 'openlesson', array('height' => 720, 'width' => 1280));

  echo '<div class="centerlink">';
  echo $OUTPUT->action_link('#', get_string('openlesson', 'lamslesson'), $popupaction, array('class' => 'btn btn-success'), new pix_icon('t/go'));
  echo '</div>';


}
if ($canmanage) {
    $monitorurl = lamslesson_get_url($USER->username, $USER->firstname, $USER->lastname, $USER->email, $USER->lang, $USER->country, $lamslesson->lesson_id, $course->id, $course->fullname, $course->timecreated, LAMSLESSON_PARAM_MONITOR_METHOD);
    $popupmonitoraction = new popup_action('click', $monitorurl, 'monitorlesson', array('height' => 720, 'width' => 1280));

  echo '<div class="centerlink">';
  echo $OUTPUT->action_link('#', get_string('openmonitor', 'lamslesson'), $popupmonitoraction, array('class' => 'btn btn-default'), new pix_icon('i/dashboard'));
  echo '</div>';
}

echo $OUTPUT->box_end();

// Once we have progress info ready

$progress = lamslesson_get_student_progress($USER->username,$lamslesson->lesson_id, $course->id,$USER->firstname,$USER->lastname,$USER->email,$USER->country,$USER->lang);

// Progress details


// If the user has attempted at least 1 activity, then we present the
// progress information
if ($progress['attemptedActivities'] > 0 && $canparticipate && $progress['lessonComplete'] == 0) {
  echo $OUTPUT->box_start('generalbox', 'intro');
  echo '<div class="progress-header">' . get_string('yourprogress','lamslesson') . '</div>';
  echo '<p>';
  echo  get_string('lessonincompleted','lamslesson') . ' ';
  echo '</p>';
  echo '<p>';
  echo get_string('youhavecompleted','lamslesson') . ' ' . $progress['activitiesCompleted'] . ' ' .get_string('outof','lamslesson'). ' ' .$progress['activityCount'] . ' ' . get_string('activities','lamslesson') . '<span class="super">[*]</span>';
  echo '</p>';
  echo '<div class="smalltext"><span class="super">*</span> ' . get_string('ymmv','lamslesson') . '</div>';
  echo $OUTPUT->box_end();
}

// If lesson is completed 
if ($progress['lessonComplete'] == 'true') {
  echo $OUTPUT->box_start('generalbox', 'intro');
  $moodle_completion = lamslesson_get_moodle_completion($course,$cm);

  // First let's update the moodle completion accordingly
  // if in Moodle it shows that it hasn't been completed.
  if ($moodle_completion->completionstate == 0){ 
    lamslesson_set_as_completed($cm,$course,$lamslesson);
  }

  echo '<div class="progress-header">' . get_string('lessoncompleted','lamslesson') . ' ' . $OUTPUT->pix_icon('e/tick', get_string('lessoncompleted','lamslesson')) . '</div>';


  // Does this lesson has to record a score in Moodle?
  if ($lamslesson->grade != 0) {
    // Now let's get the score from LAMS and add it into gradebook
    // Getting result from LAMS

    $gradebookmark = lamslesson_get_lams_outputs($USER->username,$lamslesson,$USER->username);

    if (!empty($gradebookmark)) {
      echo '<div class="centerlink">' . get_string('yourmarkis', 'lamslesson') . ' ' . round($gradebookmark, 2) . ' ' . get_string('outofmark', 'lamslesson') . ' ' . $lamslesson->grade . '.</div>';
    }

  }
  echo $OUTPUT->box_end();

}

echo $OUTPUT->footer();

