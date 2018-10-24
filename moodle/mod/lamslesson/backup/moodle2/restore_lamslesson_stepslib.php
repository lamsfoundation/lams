<?php

/**
 * @package moodlecore
 * @subpackage backup-moodle2
 * @copyright 2010 onwards Eloy Lafuente (stronk7) {@link http://stronk7.com}
 * @license   http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */
 
 /** Include required files */
 require_once($CFG->dirroot . '/mod/lamslesson/lib.php');
 require_once($CFG->libdir.'/xmlize.php');

/**
 * Define all the restore steps that will be used by the restore_lamslesson_activity_task
 */

/**
 * Structure step to restore one lamslesson activity
 */
class restore_lamslesson_activity_structure_step extends restore_activity_structure_step {

    protected function define_structure() {

        $paths = array();
        $userinfo = $this->get_setting_value('userinfo');

        $paths[] = new restore_path_element('lamslesson', '/activity/lamslesson');
        if ($userinfo) {
		// No need to restore userdata as that's already in LAMS
        }

        // Return the paths wrapped into standard activity structure
        return $this->prepare_activity_structure($paths);
    }

    protected function process_lamslesson($data) {
        global $DB;

        $data = (object)$data;
        $oldid = $data->id;
        $data->course = $this->get_courseid();

        $data->timemodified = $this->apply_date_offset($data->timemodified);
        
    	// Check whether we are duplicating a module activity. If so then clone LAMS lesson
        $type = $this->get_task()->get_info()->type;
        $mode = $this->get_task()->get_info()->mode;
        if (($type == backup::TYPE_1ACTIVITY) && ($mode == backup::MODE_IMPORT)) {
        
        	//clone LAMS lesson and update lamslesson's lesson_id value with the new lesson Id
        	$data->lesson_id = $this->clone_lamslesson($data->lesson_id, $data->course);
        }

        // insert the lamslesson record
        $newitemid = $DB->insert_record('lamslesson', $data);
        // immediately after inserting "activity" record, call this
        $this->apply_activity_instance($newitemid);
    }

    protected function after_execute() {
        // Add lamslesson related files, no need to match by itemname (just internally handled context)
        $this->add_related_files('mod_lamslesson', 'intro', null);
    }
    
    /**
	 * Clone lesson on LAMS server and return its lesson id.
	 *
	 * @param int $lsid The id of the lesson that needs to be cloned
	 * @param int $courseid The id of the course that the lesson is associated with
	 * @return int cloned lesson id
	 */
	private function clone_lamslesson($lsid,$courseid) {
	
		global $CFG, $USER;
		if (!isset($CFG->lamslesson_serverid, $CFG->lamslesson_serverkey) || $CFG->lamslesson_serverid == "") {
			print_error(get_string('notsetup', 'lamslesson'));
	    	return NULL;
	  	}
	    
	  	$datetime = lamslesson_get_datetime();
	    $username = $USER->username;
	  	$plaintext = $datetime.$username.$CFG->lamslesson_serverid.$CFG->lamslesson_serverkey;
	  	$hashvalue = sha1(strtolower($plaintext));
	  	$country = $USER->country;
	  	$lang = $USER->lang;
	
	  	$request = "$CFG->lamslesson_serverurl" . "/services/xml/LessonManager";
	
	  	$load = array('method'	=>	'clone',
			'serverId'	=>	$CFG->lamslesson_serverid,
			'datetime'	=>	$datetime,
			'hashValue'	=>	$hashvalue,
			'username'	=>	$username,
			'lsId'		=>	$lsid,
			'courseId'	=>	$courseid,
			'country'	=>	$country,
			'lang'		=>	$lang
		);
	
	  	// GET call to LAMS
	  	$xml = lamslesson_http_call_post($request, $load);
	  	
		if($xml == false) {
			print_error('restcallfail', 'lamslesson');
		}
	
	  	$xml_array = xmlize($xml);
	  	$lessonId = $xml_array['Lesson']['@']['lessonId'];

		if (!isset($lessonId) || $lessonId <= 0) {
			print_error('restcallfail', 'lamslesson');
		}
	  	
	  	return $lessonId;
	}
}
