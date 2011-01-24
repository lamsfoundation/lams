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
 * Library of interface functions and constants for module LAMS Lesson
 *
 * All the core Moodle functions, neeeded to allow the module to work
 * integrated in Moodle should be placed here.
 * All the lamslesson specific functions, needed to implement all the module
 * logic, should go to locallib.php. This will help to save some memory when
 * Moodle is performing actions across all modules.
 *
 * @package   mod_lamslesson
 * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

defined('MOODLE_INTERNAL') || die();

/** Include required files */
require_once($CFG->libdir.'/datalib.php');
require_once($CFG->libdir.'/moodlelib.php');
require_once($CFG->libdir.'/xmlize.php');

/// CONSTANTS ///////////////////////////////////////////////////////////

define('LAMSLESSON_LOGIN_REQUEST', '/LoginRequest');
define('LAMSLESSON_PARAM_UID', 'uid');
define('LAMSLESSON_PARAM_SERVERID', 'sid');
define('LAMSLESSON_PARAM_TIMESTAMP', 'ts');
define('LAMSLESSON_PARAM_HASH', 'hash');
define('LAMSLESSON_PARAM_METHOD', 'method');
define('LAMSLESSON_PARAM_COURSEID', 'courseid');
define('LAMSLESSON_PARAM_COURSENAME', 'courseName');
define('LAMSLESSON_PARAM_COUNTRY', 'country');
define('LAMSLESSON_PARAM_LANG', 'lang');
define('LAMSLESSON_PARAM_LSID', 'lsid');
define('LAMSLESSON_PARAM_AUTHOR_METHOD', 'author');
define('LAMSLESSON_PARAM_MONITOR_METHOD', 'monitor');
define('LAMSLESSON_PARAM_LEARNER_METHOD', 'learner');
define('LAMSLESSON_PARAM_PREVIEW_METHOD', 'preview');
define('LAMSLESSON_PARAM_SINGLE_PROGRESS_METHOD', 'singleStudentProgress');
define('LAMSLESSON_PARAM_PROGRESS_METHOD', 'studentProgress');
define('LAMSLESSON_PARAM_CUSTOM_CSV', 'customCSV');
define('LAMSLESSON_LD_SERVICE', '/services/xml/LearningDesignRepository');
define('LAMSLESSON_LESSON_MANAGER', '/services/xml/LessonManager');
define('LAMSLESSON_POPUP_OPTIONS', 'location=0,toolbar=0,menubar=0,statusbar=0,width=996,height=700,resizable');

/**
 * If you for some reason need to use global variables instead of constants, do not forget to make them
 * global as this file can be included inside a function scope. However, using the global variables
 * at the module level is not a recommended.
 */
//global $NEWMODULE_GLOBAL_VARIABLE;
//$NEWMODULE_QUESTION_OF = array('Life', 'Universe', 'Everything');

/**
 * Given an object containing all the necessary data,
 * (defined by the form in mod_form.php) this function
 * will create a new instance and return the id number
 * of the new instance.
 *
 * @param object $lamslesson An object from the form in mod_form.php
 * @return int The id of the newly inserted lamslesson record
 */
function lamslesson_add_instance($lamslesson) {
    global $DB;

    $lamslesson->timecreated = time();
    lamslesson_add_lesson($lamslesson);

    # You may have to add extra stuff in here #

    return $DB->insert_record('lamslesson', $lamslesson);
}

/**
 * Given an object containing all the necessary data,
 * (defined by the form in mod_form.php) this function
 * will update an existing instance with new data.
 *
 * @param object $lamslesson An object from the form in mod_form.php
 * @return boolean Success/Fail
 */
function lamslesson_update_instance($lamslesson) {
    global $DB;

    $lamslesson->timemodified = time();
    $lamslesson->id = $lamslesson->instance;

    # You may have to add extra stuff in here #
    lamslesson_add_lesson($lamslesson);

    return $DB->update_record('lamslesson', $lamslesson);
}

/**
 * Given an ID of an instance of this module,
 * this function will permanently delete the instance
 * and any data that depends on it.
 *
 * @param int $id Id of the module instance
 * @return boolean Success/Failure
 */
function lamslesson_delete_instance($id) {
    global $DB;

    if (! $lamslesson = $DB->get_record('lamslesson', array('id' => $id))) {
        return false;
    }

    # Delete any dependent records here #

    $DB->delete_records('lamslesson', array('id' => $lamslesson->id));

    return true;
}

/**
 * Return a small object with summary information about what a
 * user has done with a given particular instance of this module
 * Used for user activity reports.
 * $return->time = the time they did it
 * $return->info = a short text description
 *
 * @return null
 * @todo Finish documenting this function
 */
function lamslesson_user_outline($course, $user, $mod, $lamslesson) {
    $return = new stdClass;
    $return->time = 0;
    $return->info = '';
    return $return;
}

/**
 * Print a detailed representation of what a user has done with
 * a given particular instance of this module, for user activity reports.
 *
 * @return boolean
 * @todo Finish documenting this function
 */
function lamslesson_user_complete($course, $user, $mod, $lamslesson) {
    return true;
}

/**
 * Given a course and a time, this module should find recent activity
 * that has occurred in lamslesson activities and print it out.
 * Return true if there was output, or false is there was none.
 *
 * @return boolean
 * @todo Finish documenting this function
 */
function lamslesson_print_recent_activity($course, $viewfullnames, $timestart) {
    return false;  //  True if anything was printed, otherwise false
}

/**
 * Function to be run periodically according to the moodle cron
 * This function searches for things that need to be done, such
 * as sending out mail, toggling flags etc ...
 *
 * @return boolean
 * @todo Finish documenting this function
 **/
function lamslesson_cron () {
    return true;
}

/**
 * Must return an array of users who are participants for a given instance
 * of lamslesson. Must include every user involved in the instance,
 * independient of his role (student, teacher, admin...). The returned
 * objects must contain at least id property.
 * See other modules as example.
 *
 * @param int $lamslessonid ID of an instance of this module
 * @return boolean|array false if no participants, array of objects otherwise
 */
function lamslesson_get_participants($lamslessonid) {
    return false;
}

/**
 * This function returns if a scale is being used by one lamslesson
 * if it has support for grading and scales. Commented code should be
 * modified if necessary. See forum, glossary or journal modules
 * as reference.
 *
 * @param int $lamslessonid ID of an instance of this module
 * @return mixed
 * @todo Finish documenting this function
 */
function lamslesson_scale_used($lamslessonid, $scaleid) {
    global $DB;

    $return = false;

    //$rec = $DB->get_record("lamslesson", array("id" => "$lamslessonid", "scale" => "-$scaleid"));
    //
    //if (!empty($rec) && !empty($scaleid)) {
    //    $return = true;
    //}

    return $return;
}

/**
 * Checks if scale is being used by any instance of lamslesson.
 * This function was added in 1.9
 *
 * This is used to find out if scale used anywhere
 * @param $scaleid int
 * @return boolean True if the scale is used by any lamslesson
 */
function lamslesson_scale_used_anywhere($scaleid) {
    global $DB;

    if ($scaleid and $DB->record_exists('lamslesson', 'grade', -$scaleid)) {
        return true;
    } else {
        return false;
    }
}

/**
 * Execute post-uninstall custom actions for the module
 * This function was added in 1.9
 *
 * @return boolean true if success, false on error
 */
function lamslesson_uninstall() {
    return true;
}


/**
 * Get sequences(learning designs) for the user in lamslesson using the REST interface
 *
 * @param string $username The username of the user. Set this to "" if you would just like to get sequences for the currently logged in user.
 * @return string to define the tree structure
 * 
 */
function lamslesson_get_sequences_rest($username,$courseid,$coursename,$coursecreatedate,$country,$lang) {
    global $CFG,$USER;
    if(!isset($CFG->lamslesson_serverurl)||!isset($CFG->lamslesson_serverid)||!isset($CFG->lamslesson_serverkey))
    {
        return get_string('notsetup', 'lamslesson');
    }

    // append month/year to course name
    $coursename = $coursename.' '.date('n/Y', $coursecreatedate);

    // generate hash
    $datetime = date('F d,Y g:i a');
    $datetime_encoded = urlencode($datetime);
    $rawstring = trim($datetime).trim($username).trim($CFG->lamslesson_serverid).trim($CFG->lamslesson_serverkey);
    $hashvalue = sha1(strtolower($rawstring));


    // Put together REST URL
    $request = "$CFG->lamslesson_serverurl".LAMSLESSON_LD_SERVICE."?serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" . $hashvalue . "&username=" . $username  . "&courseId=" . $courseid . "&courseName=" . urlencode($coursename) . "&mode=2&country=" . $country . "&lang=$lang";

    //print($request);
    // GET call to LAMS
    $xml = @file_get_contents($request);

    if(!empty($http_response_header[0])) {
    	// Retrieve HTTP status code
    	list($version, $status_code, $msg) = explode(' ', $http_response_header[0], 3);

    	// Check the HTTP Status code
    	switch($status_code) {
    		case 200:
    			break;
    		case 503:
    			print_error('restcall503', 'lamslesson', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    			break;
    		case 403:
    			print_error('restcall403', 'lamslesson', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    			break;
    		case 400:
    			print_error('restcall400', 'lamslesson', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    			break;
    		default:
    			print_error('restcalldefault', 'lamslesson', $CFG->wwwroot.'/course/view.php?id='.$courseid, $status_code);
    	}
    } else {
    	print_error('restcallfail', 'lamslesson', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    }

    $xml_array = xmlize($xml);

    $result = lamslesson_process_array($xml_array['Folder']);
    return $result;

}

/*
 * Convert workspace contents from an xmlize array into a string that YUI Tree
 * can use.
 */
function lamslesson_process_array($array) {
  $output = '';
  if (empty($array['@']['resourceId'])) {
    // it's a folder
    $folder_name = preg_replace("/'/", "$1\'", $array['@']['name']);
    $output .= "{type:'Text', label:'" . $folder_name . "',id:0";
    
    if (empty($array['#']['LearningDesign']) && empty($array['#']['Folder'])) {
      $output .= ",expanded:0,children:[{type:'HTML',html:'<i>-" . get_string('empty', 'lamslesson') . "-</i>', id:0}]}";
      return $output;
    } else {
      $output .= ",children:[";
    }

    if (!empty($array['#']['LearningDesign'])) {
      $lds = $array['#']['LearningDesign'];
      for($i=0; $i<sizeof($lds); $i++) {
	$output .= "," . lamslesson_process_sequence($lds[$i]) ;
      }
    }

    if (!empty($array['#']['Folder'])) {

      $folders = $array['#']['Folder'];

      for($i=0; $i<sizeof($folders); $i++) {
	$output .= "," . lamslesson_process_array($folders[$i]);
	if ($i < sizeof($folders)-1) {
	  if (!empty($array['#']['Folder']['#'])) {
	    $output .= ']},';
	  }
	}
      }
    }
    $output .= "]}";
  }
  return $output;
}

function lamslesson_process_sequence($xml_node) {
  $output = '';
  $ld_name = preg_replace("/'/", "$1\'", $xml_node['@']['name']);
  $output .= "{type:'Text',label:'" . $ld_name . "',id:'" . $xml_node['@']['resourceId'] . "'}";
  return $output;
}


/*
 * Add a lesson instance.
 */
function lamslesson_add_lesson($form) {
  global $USER, $DB;
    
    $form->timemodified = time();
    
    $locale = lamslesson_get_locale($form->course);
    
    // start the lesson
    $form->lesson_id = lamslesson_get_lesson(
        $USER->username, $form->sequence_id, $form->course, 
        $form->name, $form->intro, 'start',
        $locale['country'], $locale['lang'], $form->customCSV
    );

    if (!isset($form->lesson_id) || $form->lesson_id <= 0) {
        return false;
    }

    $members = lamslesson_get_members($form);
	
    // call threaded lams servlet to populate the class
    $result = lamslesson_fill_lesson($USER->username, $form->lesson_id,
				     $form->course, $locale['country'], $locale['lang'], $members['learners'], $members['monitors']
				     );
    
    // log adding of lesson
    $cmid = 0;
    if ($cm = get_coursemodule_from_instance('lamslesson', $form->coursemodule, $form->course)) {
      $cmid = $cm->id;
    }
    //    add_to_log($form->course, 'lamslesson', 'add lesson', 'view.php?id='.$cmid, $form->id, $cmid);
	
    //    return $form->id;
}

/**
 * Return array with 2 keys 'country' and 'lang', to be sent to LAMS as the
 * basis for a LAMS locale like en_AU.  Makes best effort to choose appropriate
 * locale based on course, user, or server setting.
 */
function lamslesson_get_locale($courseid) {

  global $CFG, $USER, $DB;
  $locale = array('country' => '', 'lang' => '');
	
  if ($CFG->country != '') {
    $locale['country'] = trim($CFG->country);
  }
	
  // return course's language and server's country, if either exist
  if ($course = $DB->get_record('course', array('id' => $courseid))) {
    if ($course->lang != '') {
      $locale['lang'] = substr(trim($course->lang), 0, 2);
      return $locale;
    }
  }

    
  // use user's country and language if course has no language set
  $locale['country'] = trim($USER->country);
  $locale['lang'] = substr(trim($USER->lang), 0, 2);
    
  return $locale;
}

/*
 * Returns a list of learners and monitors in the given course or group.
 */
function lamslesson_get_members($form) {
  global $CFG, $DB;

  $learneridstr = '';
  $monitoridstr = '';
	
  $context = get_context_instance(CONTEXT_MODULE, $form->coursemodule);
	
  if (!$form->groupingid) {  // get all course members
    $userids = lamslesson_get_course_userids($form->coursemodule, $context);
  } else {  // get members of group
    $userids = groups_get_members($forum->groupid);
  }
	
  foreach ($userids as $userid) {
    $user = $DB->get_record('user', array('id'=>$userid));
    if (has_capability('mod/lamslesson:manage', $context, $user->id)) {
      $monitoridstr .= "$user->username,";
    }
    if (has_capability('mod/lamslesson:participate', $context, $user->id)) {
      $learneridstr .= "$user->username,";
    }
  }
	
  // remove trailing comma
  $learneridstr = substr($learneridstr, 0, strlen($learneridstr)-1);
  $monitoridstr = substr($monitoridstr, 0, strlen($monitoridstr)-1);
	
  //echo "learneridstr: $learneridstr\n";
  //echo "monitoridstr: $monitoridstr\n";
	
  $members = array('learners' => $learneridstr, 'monitors' => $monitoridstr);
  return $members;
}

/**
 * Get lesson id from lamslesson
 *
 * @param string $username The username of the user. Set this to "" if you would just like the currently logged in user to create the lesson
 * @param int $ldid The id of the learning design that the lesson is based on
 * @param int $courseid The id of the course that the lesson is associated with.
 * @param string $title The title of the lesson
 * @param string $desc The description of the lesson
 * @param string $country The Country's ISO code
 * @param string $lang The Language's ISO code
 * @return int lesson id
 */
function lamslesson_get_lesson($username,$ldid,$courseid,$title,$desc,$method,$country,$lang,$customcsv='') {

  global $CFG, $USER;
  if (!isset($CFG->lamslesson_serverid, $CFG->lamslesson_serverkey) || $CFG->lamslesson_serverid == "") {
    print_error(get_string('notsetup', 'lamslesson'));
    return NULL;
  }
    
  $datetime =    date("F d,Y g:i a");
  $datetime_encoded = urlencode($datetime);
  if(!isset($username)){
    $username = $USER->username;
  }
  $plaintext = $datetime.$username.$CFG->lamslesson_serverid.$CFG->lamslesson_serverkey;
  $hashvalue = sha1(strtolower($plaintext));

  $title = urlencode($title);
  $desc = urlencode($desc);

  $request = "$CFG->lamslesson_serverurl" . LAMSLESSON_LESSON_MANAGER . "?method=" . $method . "&serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" . $hashvalue . "&username=" . $username . "&ldId=" . $ldid . "&courseId=" . $courseid . "&title=" . $title . "&desc=" . $desc . "&country=" . $country . "&lang=" . $lang;

  // GET call to LAMS
  $xml = file_get_contents($request);

  $xml_array = xmlize($xml);
  $lessonId = $xml_array['Lesson']['@']['lessonId'];
  return $lessonId;
}

/*
 * Make call to LAMS that will populate the LAMS lesson with students and teachers from Moodle course.
 * The method on the LAMS side runs in a separate thread. 
 */
function lamslesson_fill_lesson($username,$lsid,$courseid,$country,$lang,$learneridstr,$monitoridstr) {
  global $CFG, $USER;
  if (!isset($CFG->lamslesson_serverid, $CFG->lamslesson_serverkey) || $CFG->lamslesson_serverid == '') {
    print_error(get_string('notsetup', 'lamslesson'));
    return NULL;

  }
    
  $datetime =    date('F d,Y g:i a');
  $datetime_encoded = urlencode($datetime);
  if(!isset($username)){
    $username = $USER->username;
  }
  $plaintext = $datetime.$username.$CFG->lamslesson_serverid.$CFG->lamslesson_serverkey;
  $hashvalue = sha1(strtolower($plaintext));
    
  $learneridstr = urlencode($learneridstr);
  $monitoridstr = urlencode($monitoridstr);

  // join lesson
  $request = "$CFG->lamslesson_serverurl" . LAMSLESSON_LESSON_MANAGER . "?method=join&serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" . $hashvalue . "&username=" . $username . "&lsId=" . $lsid . "&courseId=" . $courseid . "&country=" . $country . "&lang=" . $lang . "&learnerIds=" . $learneridstr . "&monitorIds=" . $monitoridstr;
    
  // GET call to LAMS
  return file_get_contents($request);
}

/**
 * Return URL to join a LAMS lesson as a learner or staff depending on method.
 * URL redirects LAMS to learner or monitor interface depending on method.
 */
function lamslesson_get_url($username, $lang, $country, $lessonid, $courseid, $coursename, $coursecreatedate, $method, $customcsv='') {
    global $CFG;

    // append month/year to course name
    $coursename = $coursename.' '.date('n/Y', $coursecreatedate);
    
    $datetime = date('F d,Y g:i a');
    $plaintext = trim($datetime)
        .trim($username)
        .trim($method)
        .trim($CFG->lamslesson_serverid)
        .trim($CFG->lamslesson_serverkey);
    $hash = sha1(strtolower($plaintext));
    $url = $CFG->lamslesson_serverurl. LAMSLESSON_LOGIN_REQUEST .
        '?'.LAMSLESSON_PARAM_UID.'='.$username.
        '&'.LAMSLESSON_PARAM_METHOD.'='.$method.
        '&'.LAMSLESSON_PARAM_TIMESTAMP.'='.urlencode($datetime).
        '&'.LAMSLESSON_PARAM_SERVERID.'='.$CFG->lamslesson_serverid.
        '&'.LAMSLESSON_PARAM_HASH.'='.$hash.
        ($method==LAMSLESSON_PARAM_AUTHOR_METHOD ? '' : '&'. LAMSLESSON_PARAM_LSID .'='.$lessonid).
        '&'. LAMSLESSON_PARAM_COURSEID .'='.$courseid.
        '&'. LAMSLESSON_PARAM_COURSENAME .'='.urlencode($coursename).
		'&'. LAMSLESSON_PARAM_COUNTRY .'='.trim($country).
		'&'. LAMSLESSON_PARAM_LANG .'='.substr(trim($lang),0,2);
    if ($customcsv != '') {
      $url .= '&'. LAMSLESSON_PARAM_CUSTOM_CSV .'='.urlencode($customcsv);
    }
    return $url;
}


/*
 * Returns list of userids of users in the given context
 */
function lamslesson_get_course_userids($lamslessonid, $context=NULL) {
  global $CFG, $DB;

	if ($context == NULL) {
	  $lamslesson = $DB->get_record('lamslesson', array('id' => $lamslessonid));
		if (! $cm = get_coursemodule_from_instance('lamslesson', $lamslesson->id, $lamslesson->course)) {
			error('Course Module ID was incorrect');
		}
		$context = get_context_instance(CONTEXT_MODULE, $cm->id);
	}
	
	// we are looking for all users assigned in this context or higher
	if ($usercontexts = get_parent_contexts($context)) {
		$listofcontexts = '('.implode(',', $usercontexts).')';
	} else {
		$sitecontext = get_context_instance(CONTEXT_SYSTEM);
		$listofcontexts = '('.$sitecontext->id.')'; // must be site
	}
	$sql = "SELECT u.id
		FROM {$CFG->prefix}user u INNER JOIN {$CFG->prefix}role_assignments r ON u.id=r.userid
		WHERE r.contextid IN $listofcontexts OR r.contextid=$context->id
		AND u.deleted=0 AND u.username!='guest'";
	$users = $DB->get_records_sql($sql);
	$userids = array_keys($users);  // turn list of id-backed objects into list of ids
	return $userids;
}



/*
 * Gets all the student progress for a lesson in one go
 * 
 */
function lamslesson_get_student_progress($username,$ldid,$courseid) {
  global $CFG;
  if (!isset($CFG->lamslesson_serverid, $CFG->lamslesson_serverkey) || $CFG->lamslesson_serverid == "") {
    print_error(get_string('notsetup', 'lamslesson'));
    return NULL;
  }
    
  $datetime =    date("F d,Y g:i a");
  $datetime_encoded = urlencode($datetime);

  $plaintext = $datetime.$username.$CFG->lamslesson_serverid.$CFG->lamslesson_serverkey;
  $hashvalue = sha1(strtolower($plaintext));

  $request = "$CFG->lamslesson_serverurl" . LAMSLESSON_LESSON_MANAGER . "?method=" . LAMSLESSON_PARAM_SINGLE_PROGRESS_METHOD . "&serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" . $hashvalue ."&lsId=" . $ldid . "&courseId=" . $courseid . "&progressUser=" . $username . "&username=" . $username;
  
  // GET call to LAMS
  
  $xml = file_get_contents($request);

  $xml_array = xmlize($xml);

  $response = $xml_array['LessonProgress']['#'];
  $learnerProgress = $response['LearnerProgress']['0'];
  
  //   print_r($learnerProgress['@']['activitiesCompleted']);
  // die();


  return $learnerProgress['@'];
}


//returns the moodle completion state for a user)
function lamslesson_get_moodle_completion($course,$cm) {

  $completion = new completion_info($course);
  return $completion->get_data($cm);

}

function lamslesson_set_as_completed($cm,$course,$lamslesson) {
  // Update completion state
  $completion = new completion_info($course);
  if ($completion->is_enabled($cm) && $lamslesson->completionfinish) {
    lamslesson_set_completion_state($cm,$completion,COMPLETION_COMPLETE);
  }
}

function lamslesson_set_as_incomplete($cm,$course,$lamslesson) {
  // Update completion state
  $completion = new completion_info($course);
  if ($completion->is_enabled($cm) && $lamslesson->completionfinish) {
    lamslesson_set_completion_state($cm,$completion,COMPLETION_INCOMPLETE);
  }
}

function lamslesson_set_completion_state($cm,$completion,$state) {
  //Update completion state

  switch ($state) {
  case COMPLETION_COMPLETE: 
    
    $completion->update_state($cm, COMPLETION_COMPLETE);
    break;

  default:
    $completion->update_state($cm, COMPLETION_INCOMPLETE);
    break;
  }
      

}



