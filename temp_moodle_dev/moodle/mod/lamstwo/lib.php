<?php  // $Id$

/// Library of functions and constants for module lamstwo
require_once($CFG->libdir.'/datalib.php');
require_once($CFG->libdir.'/moodlelib.php');
require_once($CFG->libdir.'/soap/nusoap.php');
require_once($CFG->libdir.'/xmlize.php');

$LAMS2CONSTANTS->login_request   = '/LoginRequest';
$LAMS2CONSTANTS->param_uid       = 'uid';
$LAMS2CONSTANTS->param_serverid  = 'sid';
$LAMS2CONSTANTS->param_timestamp = 'ts';
$LAMS2CONSTANTS->param_hash      = 'hash';
$LAMS2CONSTANTS->param_method    = 'method';
$LAMS2CONSTANTS->param_courseid  = 'courseid';
$LAMS2CONSTANTS->param_coursename = 'courseName';
$LAMS2CONSTANTS->param_country   = 'country';
$LAMS2CONSTANTS->param_lang      = 'lang';
$LAMS2CONSTANTS->param_lsid      = 'lsid';
$LAMS2CONSTANTS->author_method   = 'author';
$LAMS2CONSTANTS->monitor_method  = 'monitor';
$LAMS2CONSTANTS->learner_method  = 'learner';
$LAMS2CONSTANTS->custom_csv      = 'customCSV';

/*
 * Given an object containing all the necessary data,
 * (defined by the form in mod.html) this function
 * will create a new instance and return the id number
 * of the new instance.
 */
function lamstwo_add_instance($lamstwo) {
    global $USER, $LAMS2CONSTANTS;
    
    $lamstwo->timemodified = time();
    
    if (!$lamstwo->id = insert_record('lamstwo', $lamstwo)) {
		return false;
	}
	return $lamstwo->id;
}


function lamstwo_update_instance($lamstwo) {
/// Given an object containing all the necessary data,
/// (defined by the form in mod.html) this function
/// will update an existing instance with new data.
    //echo "enter lamstwo_update_instance<BR>";
  $lamstwo->timemodified = time();
  $lamstwo->id = $lamstwo->instance;
  /*
  lamstwo_delete_lesson($USER->username,$lamstwo->lesson_id);
  $start_date = "";
  if($lamstwo->start_date) $start_date=$lamstwo->start_date;
  $country = "";
  if($USER->country) $country=trim($USER->country);
  $lang = "";
  if($USER->lang) $lang = substr(trim($USER->lang),0,2);
  $lamstwo->lesson_id = lamstwo_get_lesson($USER->username,$lamstwo->sequence_id,$lamstwo->course,$lamstwo->name,$lamstwo->introduction,$start_date,$country,$lang);
  if(!$lamstwo->lesson_id){
      return false;
  }
    # May have to add extra stuff in here #
	//echo $lamstwo->id."<BR>";
    //echo $lamstwo->sequence_id."<BR>";
    //echo $lamstwo->course."<BR>";
    //echo $lamstwo->name."<BR>";
    //echo $lamstwo->introduction."<BR>";
    //echo $lamstwo->lesson_id."<BR>";
    //echo "exit lamstwo_update_instance<BR>";
*/
  return update_record('lamstwo', $lamstwo);
}


function lamstwo_delete_instance($id) {
/// Given an ID of an instance of this module,
/// this function will permanently delete the instance
/// and any data that depends on it.

  if (! $lamstwo = get_record('lamstwo', 'id', $id)) {
      return false;
  }

  $result = true;

  # Delete any dependent records here #
  lamstwo_delete_lesson($USER->username,$lamstwo->lesson_id);
  if (! delete_records('lamstwo', 'id', $lamstwo->id)) {
      $result = false;
  }

  return $result;
}

function lamstwo_user_outline($course, $user, $mod, $lamstwo) {
/// Return a small object with summary information about what a
/// user has done with a given particular instance of this module
/// Used for user activity reports.
/// $return->time = the time they did it
/// $return->info = a short text description

  return $return;
}

function lamstwo_user_complete($course, $user, $mod, $lamstwo) {
/// Print a detailed representation of what a  user has done with
/// a given particular instance of this module, for user activity reports.

  return true;
}

function lamstwo_print_recent_activity($course, $isteacher, $timestart) {
/// Given a course and a time, this module should find recent activity
/// that has occurred in lamstwo activities and print it out.
/// Return true if there was output, or false is there was none.

  global $CFG;

  return false;  //  True if anything was printed, otherwise false
}

function lamstwo_cron () {
/// Function to be run periodically according to the moodle cron
/// This function searches for things that need to be done, such
/// as sending out mail, toggling flags etc ...

  global $CFG;

  return true;
}

function lamstwo_grades($lamstwoid) {
	global $CFG, $USER;
	
	if (!isset($CFG->lamstwo_serverid, $CFG->lamstwo_serverkey) || $CFG->lamstwo_serverid == '') {
    	print_error('Can\'t retrieve lesson progress: please check your lamstwo configuration settings.');
        return NULL;
    }
    
    $datetime = date('F d,Y g:i a');
    $datetime_encoded = urlencode($datetime);
    $plaintext = $datetime.$USER->username.$CFG->lamstwo_serverid.$CFG->lamstwo_serverkey;
    $hashvalue = sha1(strtolower($plaintext));

    // get list of lamstwo_lessons
    $sql = "SELECT * FROM {$CFG->prefix}lamstwo_lesson WHERE lamstwo=$lamstwoid";
    $lamstwolessons = get_records_sql($sql);
    $return->maxgrade = sizeof($lamstwolessons);
    
    // get list of course userids
    $userids = lamstwo_get_course_userids($lamstwoid);
    foreach ($userids as $userid) {
    	$return->grades[$userid] = 0;
    }
    
    if (!empty($lamstwolessons)) {
    	foreach ($lamstwolessons as $lamstwolessonid => $lamstwolesson) {
    		$lsid = $lamstwolesson->lesson_id;
    		$courseid = $lamstwolesson->course;
    		$service = '/services/xml/LessonManager';
    		$request = "$CFG->lamstwo_serverurl$service?method=studentProgress&serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&username=$USER->username&lsId=$lsid&courseId=$courseid";

    		// GET call to LAMS
    		$response = file_get_contents($request);
    		$xml_array = xmlize($response);
    		
    		if (!empty($xml_array['LessonProgress']['#'])) {
    			foreach ($xml_array['LessonProgress']['#']['LearnerProgress'] as $learnerprogress) {
    				$username = $learnerprogress['@']['username'];
    				$lessoncomplete = $learnerprogress['@']['lessonComplete'];
    				//echo "Lessonid=$lsid, username=$username, lessoncomplete=$lessoncomplete\n";
    				if ($lessoncomplete == 'true') {
    					$user = get_record('user', 'username', $username);
    					$return->grades[$user->id]++;
    				} 
    			}
    		}
    		
    		//traverse_xmlize($xml_array);
    		//print implode("", $GLOBALS['traverse_array']);
    		//print "\n\n<br/><br/>";
    	}
    }
    
	return $return;
}

function lamstwo_get_participants($lamstwoid) {
//Must return an array of user records (all data) who are participants
//for a given instance of lamstwo. Must include every user involved
//in the instance, independient of his role (student, teacher, admin...)
//See other modules as example.

  return false;
}

function lamstwo_scale_used ($lamstwoid,$scaleid) {
//This function returns if a scale is being used by one lamstwo
//it it has support for grading and scales. Commented code should be
//modified if necessary. See forum, glossary or journal modules
//as reference.

  $return = false;

  //$rec = get_record("lamstwo","id","$lamstwoid","scale","-$scaleid");
  //
  //if (!empty($rec)  && !empty($scaleid)) {
  //    $return = true;
  //}

  return $return;
}

//////////////////////////////////////////////////////////////////////////////////////
/// Any other lamstwo functions go here.  Each of them must have a name that
/// starts with lamstwo_

function lamstwo_get_soap_client($relativeurl) {
    global $CFG;
    if(!isset($CFG->lamstwo_serverurl))
    {
        return NULL;
    }
    $wsdl = $CFG->lamstwo_serverurl.$relativeurl;
    $s = new soap_client($wsdl,true,false,false,false,false,5,5);
	$s->soap_defencoding = 'UTF-8';
	$s->decode_utf8 = false; 
    return $s;
}

/**
 * Get sequences(learning designs) for the user in lamstwo
 *
 * @param string $username The username of the user. Set this to "" if you would just like to get sequences for the currently logged in user.
 * @return string to define the tree structure
 * @TODO complete the documentation of this function
 */
function lamstwo_get_sequences($username,$courseid,$country,$lang) {
    global $CFG,$USER;
    if(!isset($CFG->lamstwo_serverid)||!isset($CFG->lamstwo_serverkey)||!isset($CFG->lamstwo_serverurl))
    {
        return get_string("notsetup", "lamstwo");
    }
    $relativeurl="/services/LearningDesignRepositoryService?wsdl";
    $s = lamstwo_get_soap_client($relativeurl);
    if(is_null($s)){
        return NULL;
    }
    $datetime =    date("F d,Y g:i a");

    if(!isset($username)){
        $username = $USER->username;
    }
    $rawstring = trim($datetime).trim($username).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
    $hashvalue = sha1(strtolower($rawstring));
	$mode = 2; //monitor mode.  This will only retrieve valid learning designs.
    $parameters = array($CFG->lamstwo_serverid,$datetime,$hashvalue,$username,$courseid,$mode,$country,$lang);
    $result = $s->call('getLearningDesigns',$parameters);//Array of simpleLearningDesign objects
    if($s->getError()){//if some exception happened
        $result = $s->getError();//return the string describing the error
    }
    unset($s);
	$pattern = '/\'(\d+)\'/';
	$replacement = '\'javascript:selectSequence($1)\'';
    return preg_replace($pattern,$replacement,$result);
}

/**
 * Get sequences(learning designs) for the user in lamstwo using the REST interface
 *
 * @param string $username The username of the user. Set this to "" if you would just like to get sequences for the currently logged in user.
 * @return string to define the tree structure
 * @TODO complete the documentation of this function
 */
function lamstwo_get_sequences_rest($username,$courseid,$coursename,$coursecreatedate,$country,$lang) {
    global $CFG,$USER;
    if(!isset($CFG->lamstwo_serverid)||!isset($CFG->lamstwo_serverkey)||!isset($CFG->lamstwo_serverurl))
    {
        return get_string('notsetup', 'lamstwo');
    }

    // append month/year to course name
    $coursename = $coursename.' '.date('n/Y', $coursecreatedate);

    // generate hash
    $datetime = date('F d,Y g:i a');
    $datetime_encoded = urlencode($datetime);
    $rawstring = trim($datetime).trim($username).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
    $hashvalue = sha1(strtolower($rawstring));

    // Put together REST URL
    $service = '/services/xml/LearningDesignRepository';
    $request = "$CFG->lamstwo_serverurl$service?serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&username=$username&courseId=$courseid&courseName=".urlencode($coursename)."&mode=2&country=$country&lang=$lang";

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
    			print_error('restcall503', 'lamstwo', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    			break;
    		case 403:
    			print_error('restcall403', 'lamstwo', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    			break;
    		case 400:
    			print_error('restcall400', 'lamstwo', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    			break;
    		default:
    			print_error('restcalldefault', 'lamstwo', $CFG->wwwroot.'/course/view.php?id='.$courseid, $status_code);
    	}
    } else {
    	print_error('restcallfail', 'lamstwo', $CFG->wwwroot.'/course/view.php?id='.$courseid);
    }

	$xml_array = xmlize($xml);
	
	//traverse_xmlize($xml_array);
	//print implode("", $GLOBALS['traverse_array']);
	
	return lamstwo_process_array($xml_array['Folder']) . ']';
}


/*
 * Convert workspace contents from an xmlize array into a string that Tigra Tree (javascript library)
 * can recognise.
 */
function lamstwo_process_array($xml_array) {
	$output = '';
	if (empty($xml_array['@']['resourceId'])) {
		// it's a folder
		$folder_name = preg_replace("/'/", "$1\'", $xml_array['@']['name']);
		$output .= "['" . $folder_name . "',null,";
		if (!empty($xml_array['#']['LearningDesign'])) {
			$lds = $xml_array['#']['LearningDesign'];
			for($i=0; $i<sizeof($lds); $i++) {
				$output .= lamstwo_process_array($lds[$i]);
				if ($i < sizeof($lds)-1) {
					$output .= ",";
				}
			}
		}
		if (!empty($xml_array['#']['Folder'])) {
			if (!empty($xml_array['#']['LearningDesign'])) {
				$output .= ',';
			}
			$folders = $xml_array['#']['Folder'];
			for($i=0; $i<sizeof($folders); $i++) {
				$output .= lamstwo_process_array($folders[$i]) . "]";
				if ($i < sizeof($folders)-1) {
					$output .= ',';
				}
			}
		}
		if (empty($xml_array['#']['LearningDesign']) && empty($xml_array['#']['Folder'])) {
			$output .= "['',null]";
		}
	} else {
		// it's a sequence
		$ld_name = preg_replace("/'/", "$1\'", $xml_array['@']['name']);
		$output .= "['" . $ld_name . "',";
		$output .= "'javascript:selectSequence(" . $xml_array['@']['resourceId'] . ")']";
	}
	return $output;
}


/*
 * Add a lesson instance.
 */
function lamstwo_add_lesson($form) {
    global $USER, $LAMS2CONSTANTS;
    
    $form->timemodified = time();
    
    $locale = lamstwo_get_locale($form->course);
    
    if (isset($form->schedule) && $form->schedule) {
    	$form->start_date = date('j/n/y g:i A', $form->schedulestart);
    } else {
    	$form->start_date = '';  // avoid PHP notice
    }
    
    // start the lesson
    $form->lesson_id = lamstwo_get_lesson(
        $USER->username, $form->sequence_id, $form->course, 
        $form->name, $form->intro, $form->start_date,
        $locale['country'], $locale['lang'], $form->customCSV
    );
	
    if (!isset($form->lesson_id) || $form->lesson_id <= 0) {
        return false;
    }
	
    if (!$form->id = insert_record('lamstwo_lesson', $form)) {
        return false;
    }
    //print_r($form);
	
    $members = lamstwo_get_members($form->course, $form->lamstwo, $form->groupid);
	
    // call threaded lams servlet to populate the class
    $result = lamstwo_fill_lesson($USER->username, $form->lesson_id,
        $form->course, $locale['country'], $locale['lang'], $members['learners'], $members['monitors']
    );
	
    return $form->id;
}


/*
 * Make call to LAMS that will populate the LAMS lesson with students and teachers from Moodle course.
 * The method on the LAMS side runs in a separate thread. 
 */
function lamstwo_fill_lesson($username,$lsid,$courseid,$country,$lang,$learneridstr,$monitoridstr) {
	global $CFG, $USER;
	if (!isset($CFG->lamstwo_serverid, $CFG->lamstwo_serverkey) || $CFG->lamstwo_serverid == '') {
    	print_error('Can\'t create LAMS lesson: please check your lamstwo configuration settings.');
        return NULL;
    }
    
    $datetime =    date('F d,Y g:i a');
    $datetime_encoded = urlencode($datetime);
    if(!isset($username)){
        $username = $USER->username;
    }
    $plaintext = $datetime.$username.$CFG->lamstwo_serverid.$CFG->lamstwo_serverkey;
    //echo $plaintext;
    $hashvalue = sha1(strtolower($plaintext));
    //echo $hashvalue;
    
    $learneridstr = urlencode($learneridstr);
    $monitoridstr = urlencode($monitoridstr);
    
	// join lesson
	$service = '/services/xml/LessonManager';
	$request = "$CFG->lamstwo_serverurl$service?method=join&serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&username=$username&lsId=$lsid&courseId=$courseid&country=$country&lang=$lang&learnerIds=$learneridstr&monitorIds=$monitoridstr";

	// GET call to LAMS
	return file_get_contents($request);
}

/**
 * Get lesson id from lamstwo
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
function lamstwo_get_lesson($username,$ldid,$courseid,$title,$desc,$startdate,$country,$lang,$customcsv='') {
    //echo "enter lamstwo_get_lesson<BR>";
    global $CFG, $USER;
    if (!isset($CFG->lamstwo_serverid, $CFG->lamstwo_serverkey) || $CFG->lamstwo_serverid == "") {
    	print_error('Can\'t create LAMS lesson: please check your lamstwo configuration settings.');
        return NULL;
    }
    
    $relativeurl="/services/LessonManagerService?wsdl";
    $s = lamstwo_get_soap_client($relativeurl);
    if(is_null($s)){
        echo "soap client is null<BR>";
        return NULL;
    }
	
    $datetime =    date("F d,Y g:i a");
    $datetime_encoded = urlencode($datetime);
    if(!isset($username)){
        $username = $USER->username;
    }
    $plaintext = $datetime.$username.$CFG->lamstwo_serverid.$CFG->lamstwo_serverkey;
    //echo $plaintext;
    $hashvalue = sha1(strtolower($plaintext));
    //echo $hashvalue;
    
    /*
    $title = urlencode($title);
    $desc = urlencode($desc);
    $startdate = urlencode($startdate);
	*/
    
	if($startdate){
	    $parameters = array($CFG->lamstwo_serverid,$datetime,$hashvalue,$username,$ldid,$courseid,$title,$desc,$startdate,$country,$lang,$customcsv);
	    $result = $s->call('scheduleLesson',$parameters);
	    /*$service = "/services/xml/LessonManager";
	    $request = "$CFG->lamstwo_serverurl$service?method=schedule&serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&username=$username&ldId=$ldid&courseId=$courseid&title=$title&desc=$desc&startdate=$startdate&country=$country&lang=$lang";
	    //$request = "$CFG->lamstwo_serverurl$service?method=delete&serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&username=$username&lsId=1";
	    echo "schedule request: $request";
	    // GET call to LAMS
	    $xml = file_get_contents($request);
	    print_r($http_response_header);
	    echo "<pre>\n$xml</pre>\n";*/
	}else{
	    $parameters = array($CFG->lamstwo_serverid,$datetime,$hashvalue,$username,$ldid,$courseid,$title,$desc,$country,$lang,$customcsv);
	    $result = $s->call('startLesson',$parameters);
	    /*$service = "/services/xml/LessonManager";
	    $request = "$CFG->lamstwo_serverurl$service?method=start&serverId=$CFG->lamstwo_serverid&datetime=$datetime_encoded&hashValue=$hashvalue&username=$username&ldId=$ldid&courseId=$courseid&title=$title&desc=$desc&country=$country&lang=$lang";
	    echo "start request: $request";
	    // GET call to LAMS
	    $xml = file_get_contents($request);
	    print_r($http_response_header);
	    echo "<br/>\n$xml<br/>\n";*/
	}
	
    if($s->getError()){
        $result = $s->getError();
    	echo 'lamstwo_get_lesson: '.$result;
    }
    unset($s);
    
    return $result;
}

/**
 * Delete learning session(lesson) from lamstwo
 *
 * @param string $username The username of the user. Set this to "" if you would just like the currently logged in user to create the lesson
 * @param int $lsid The id of the learning session(lesson)
 * @return true or false
 */
function lamstwo_delete_lesson($username,$lsid) {
    //echo "enter lamstwo_get_lesson<BR>";
    global $CFG,$USER;
    if(!isset($CFG->lamstwo_serverid)||!isset($CFG->lamstwo_serverkey))
    {
        return "The LAMSv2 serverId and serverKey have not been set up";
    }
    $relativeurl="/services/LessonManagerService?wsdl";
    $s = lamstwo_get_soap_client($relativeurl);
    if(is_null($s)){
        return "Failed to get soap client based on:".$relativeurl;
    }
    $datetime =    date("F d,Y g:i a");
    if(!isset($username)){
        $username = $USER->username;
    }
    $plaintext = $datetime.$username.$CFG->lamstwo_serverid.$CFG->lamstwo_serverkey;
    //echo $plaintext;
    $hashvalue = sha1(strtolower($plaintext));
    //echo $hashvalue;
    $parameters = array($CFG->lamstwo_serverid,$datetime,$hashvalue,$username,$lsid);
    $result = $s->call('deleteLesson',$parameters);
    if($s->getError()){
        $result = $s->getError();
    }
    unset($s);
    return $result;
}

/**
 * Verify if the server_url, server_id and server_key are set properly
 */
function lamstwo_verify($url, $id, $key){
    $relativeurl="/services/VerificationService?wsdl";
    $wsdl = $url.$relativeurl;
    $s = new soap_client($wsdl,true,false,false,false,false,3,3);
    if(is_null($s)){
        return NULL;
    }
    $datetime =    date("F d,Y g:i a");
	$plaintext = $datetime.$id.$key;
    //echo $plaintext;
    $hashvalue = sha1(strtolower($plaintext));
    //echo $hashvalue;
    $parameters = array($id,$datetime,$hashvalue);
    $result = $s->call('verify',$parameters);
    if($s->getError()){
        $result = $s->getError();
    }
    unset($s);
    return $result;
}


/**
 * Return array with 2 keys 'country' and 'lang', to be sent to LAMS as the
 * basis for a LAMS locale like en_AU.  Makes best effort to choose appropriate
 * locale based on course, user, or server setting.
 */
function lamstwo_get_locale($courseid) {

	global $CFG, $USER;
	$locale = array('country' => '', 'lang' => '');
	
	if ($CFG->country != '') {
		$locale['country'] = trim($CFG->country);
	}
	
	// return course's language and server's country, if either exist
	if ($course = get_record('course', 'id', $courseid)) {
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


/**
 * Return URL to join a LAMS lesson as a learner or staff depending on method.
 * URL redirects LAMS to learner or monitor interface depending on method.
 */
function lamstwo_get_url($username, $lang, $country, $lessonid, $courseid, $coursename, $coursecreatedate, $method, $customcsv='') {
    global $CFG, $LAMS2CONSTANTS;

    // append month/year to course name
    $coursename = $coursename.' '.date('n/Y', $coursecreatedate);
    
    $datetime = date('F d,Y g:i a');
    $plaintext = trim($datetime)
        .trim($username)
        .trim($method)
        .trim($CFG->lamstwo_serverid)
        .trim($CFG->lamstwo_serverkey);
    $hash = sha1(strtolower($plaintext));
    $url = $CFG->lamstwo_serverurl.$LAMS2CONSTANTS->login_request.
        '?'.$LAMS2CONSTANTS->param_uid.'='.$username.
        '&'.$LAMS2CONSTANTS->param_method.'='.$method.
        '&'.$LAMS2CONSTANTS->param_timestamp.'='.urlencode($datetime).
        '&'.$LAMS2CONSTANTS->param_serverid.'='.$CFG->lamstwo_serverid.
        '&'.$LAMS2CONSTANTS->param_hash.'='.$hash.
        ($method==$LAMS2CONSTANTS->author_method ? '' : '&'.$LAMS2CONSTANTS->param_lsid.'='.$lessonid).
        '&'.$LAMS2CONSTANTS->param_courseid.'='.$courseid.
        '&'.$LAMS2CONSTANTS->param_coursename.'='.urlencode($coursename).
		'&'.$LAMS2CONSTANTS->param_country.'='.trim($country).
		'&'.$LAMS2CONSTANTS->param_lang.'='.substr(trim($lang),0,2);
    if ($customcsv != '') {
      $url .= '&'.$LAMS2CONSTANTS->custom_csv.'='.urlencode($customcsv);
    }
    return $url;
}


/*
 * Returns list of userids of users in the given context
 */
function lamstwo_get_course_userids($lamstwoid, $context=NULL) {
	global $CFG;

	if ($context == NULL) {
		$lamstwo = get_record('lamstwo', 'id', $lamstwoid);
		if (! $cm = get_coursemodule_from_instance('lamstwo', $lamstwo->id, $lamstwo->course)) {
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
	$users = get_records_sql($sql);
	$userids = array_keys($users);  // turn list of id-backed objects into list of ids
	
	return $userids;
}


/*
 * Returns a list of learners and monitors in the given course or group.
 */
function lamstwo_get_members($courseid, $lamstwoid, $groupid) {
	global $CFG;

	$learneridstr = '';
	$monitoridstr = '';
	
	if (! $cm = get_coursemodule_from_instance('lamstwo', $lamstwoid, $courseid)) {
		return array('learners' => '', 'monitors' => '');
	}
	$context = get_context_instance(CONTEXT_MODULE, $cm->id);
	
	if (!$groupid) {  // get all course members
		$userids = lamstwo_get_course_userids($lamstwoid, $context);
	} else {  // get members of group
		$userids = groups_get_members($groupid);
	}
	
	foreach ($userids as $userid) {
		$user = get_record('user', 'id', $userid);
		if (has_capability('mod/lams:manage', $context, $user->id)) {
			$monitoridstr .= "$user->username,";
		}
		if (has_capability('mod/lams:participate', $context, $user->id)) {
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


/*
 * Returns local lamstwo copy of grades as list of row-objects.
 */
function lamstwo_get_grades($lamstwolessonid) {
	global $CFG;
	$sql = "SELECT * FROM {$CFG->prefix}lamstwo_grade WHERE lamstwolesson=$lamstwolessonid";
	$lamstwogrades = get_records_sql($sql);
	if (isset($lamstwogrades) && sizeof($lamstwogrades) > 0) {
		return $lamstwogrades;
	} else {
		return NULL;
	}
}

?>
