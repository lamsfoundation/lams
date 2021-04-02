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
 * Prepares and launches a preview lesson
 *
 * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

require_once(dirname(dirname(dirname(__FILE__))).'/config.php');
require_once(dirname(__FILE__).'/lib.php');

$course = optional_param('course', 0, PARAM_INT); // course
$ldId  = optional_param('ldId', 0, PARAM_INT);  // learning design ID we want to create a preview instance for.


if ($course != 0) {
    $course     = $DB->get_record('course', array('id' => $course), '*', MUST_EXIST);
} else {
    print_error('You must specify a course');
}

require_login($course);

// Check permissions and capabilities:

// $context = get_context_instance(CONTEXT_COURSE, $course->id);
$context = context_course::instance($course->id);

$canmanage = has_capability('mod/lamslesson:manage', $context);
if ($canmanage) {

  // Get a lessonID from LAMS first
  $lessonID = lamslesson_get_lesson($USER->username, $ldId, $course->id, 'preview', 'preview', null, LAMSLESSON_PARAM_PREVIEW_METHOD, $USER->country, $USER->lang,'',0); 

  // With the lesson now we put together the URL 
  $learnerurl = lamslesson_get_url($USER->username, $USER->firstname, $USER->lastname, $USER->email, $USER->lang, $USER->country, $lessonID, $course->id, $course->fullname, $course->timecreated, LAMSLESSON_PARAM_LEARNER_METHOD);
  header('Location:'.$learnerurl."'");
  die();

} else {
  // Don't have permission so finish here
  print_error('Sorry, you don\'t have permissions to perform this task');
}
