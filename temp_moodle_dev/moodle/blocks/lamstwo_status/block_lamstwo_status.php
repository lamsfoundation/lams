<?php //$Id$

require_once($CFG->libdir.'/moodlelib.php');
require_once($CFG->libdir.'/xmlize.php');

/*
 * Requires LAMS 2.1
 */

class block_lamstwo_status extends block_base {
    
    const STATUS_NOT_STARTED = 'status_not_started';
    const STATUS_STARTED = 'status_started';
    const STATUS_COMPLETED = 'status_completed';
    const STATUS_UNKNOWN = 'status_unknown';
    
    function init() {
        $this->title = get_string('block_title', 'lamstwo');
        $this->version = 2009070800;
    }
    
    function get_content() {
        if ($this->content !== NULL) {
            return $this->content;
        }
        
        $this->content = new stdClass;
        $this->content->text = $this->print_all_lesson_status();
        $this->content->footer = '';
        
        return $this->content;
    }
    
    function print_all_lesson_status() {
        $return_str = '<table width="100%">';
        $array = $this->get_all_lesson_status();
        foreach ($array as $lamstwo_id => $status_obj_array) {
            $return_str .= '<tr><th colspan="2" align="centre">'.$status_obj_array[0]->lamstwo_name.'</th></tr>';
            foreach ($status_obj_array as $status_obj) {
                $status_str =  get_string($status_obj->status_i18n_key, 'lamstwo');
                $return_str .= '<tr><td>'.$status_obj->lesson_name.'</td><td align="right">'.$status_str.'</td></tr>';
            }
        }
        $return_str .= '</table>';
        return $return_str;
    }
    
    function get_all_lesson_status() {
        global $CFG, $USER, $COURSE;
        $status_array = array();
        
        // get course module ids
        $cms = get_records_sql("SELECT cm.* FROM {$CFG->prefix}course_modules cm, {$CFG->prefix}modules m 
            WHERE cm.module=m.id AND m.name='lamstwo' AND cm.course={$COURSE->id}");
        
        foreach ($cms as $cmid => $cm) {
            $context = get_context_instance(CONTEXT_MODULE, $cmid);
            // check is learner
            if (has_capability('mod/lams:participate', $context)) {
                $lessons = get_records_sql("SELECT ll.lesson_id, ll.name AS lesson_name, l.id AS lamstwo_id, l.name AS lamstwo_name
                	FROM {$CFG->prefix}lamstwo_lesson ll, {$CFG->prefix}lamstwo l 
                	WHERE ll.lamstwo=l.id AND ll.lamstwo={$cm->instance}");
                // get status for all lessons
                foreach ($lessons as $obj) {
                    $xml_array = $this->get_single_lesson_status($USER->username, $COURSE->id, $obj->lesson_id);
                    $status_obj = new stdClass();
                    $status_obj->lamstwo_name = $obj->lamstwo_name;
                    $status_obj->lesson_id = $obj->lesson_id;
                    $status_obj->lesson_name = $obj->lesson_name;
                    $status_obj->status_i18n_key = $this->process_xml($xml_array);
                    $status_array[$obj->lamstwo_id][] = $status_obj;
                }
            }
        }
        
        return $status_array;
    }
    
    /*
     * Returns lams lesson status for a learner in a single lesson
     */
    function get_single_lesson_status($username, $courseid, $lsId) {
        global $CFG, $USER;
        if(!isset($CFG->lamstwo_serverid) || !isset($CFG->lamstwo_serverkey) || !isset($CFG->lamstwo_serverurl)) {
            return get_string('notsetup', 'lamstwo');
        }
    
        // generate hash
        $datetime = date('F d,Y g:i a');
        $datetime_encoded = urlencode($datetime);
        $rawstring = trim($datetime).trim($username).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
        $hashvalue = sha1(strtolower($rawstring));
    
        // Put together REST URL
        $service = '/services/xml/LessonManager';
        $request = "$CFG->lamstwo_serverurl$service?serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&method=singleStudentProgress&username=$username&courseId=$courseid&lsId=$lsId&progressUser=$username";
    
        // GET call to LAMS
        $xml = file_get_contents($request);
        
        if(!empty($http_response_header[0])) {
        	// Retrieve HTTP status code
        	list($version, $status_code, $msg) = explode(' ', $http_response_header[0], 3);
    
        	// Check the HTTP Status code
        	switch($status_code) {
        		case 200:
        			break;
        		case 503:
        			break;
        		case 403:
        			break;
        		case 400:
        			break;
        		default:
        	}
        } else {
        	//return get_string('restcallfail', 'lamstwo');
        	//print_error('restcallfail', 'lamstwo', $CFG->wwwroot.'/course/view.php?id='.$courseid);
        }
        
    	$xml_array = xmlize($xml);
    	return $xml_array;
    }
    
    /*
     * Return i18n key of learner's status in lams lesson based on xml returned from lams server
     */
    function process_xml($xml_array) {
        $status = self::STATUS_UNKNOWN;
        $attributes = $xml_array['LessonProgress']['#']['LearnerProgress'][0]['@'];
        if ($attributes['lessonComplete'] == 'true') {
            $status = self::STATUS_COMPLETED;
        } else if ($attributes['attemptedActivities'] > 0 ) {
            $status = self::STATUS_STARTED;
        } else if ($attributes['attemptedActivities'] == 0 && $attributes['activitiesCompleted'] == 0) {
            $status = self::STATUS_NOT_STARTED;
        }
        return $status;
    }
}


?>