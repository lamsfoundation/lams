<?PHP  // $Id$

/// Library of functions and constants for module lamstwo
require_once($CFG->libdir.'/datalib.php');
require_once($CFG->libdir.'/moodlelib.php');
require_once($CFG->libdir.'/soap/nusoap.php');


function lamstwo_add_instance($lamstwo) {
/// Given an object containing all the necessary data,
/// (defined by the form in mod.html) this function
/// will create a new instance and return the id number
/// of the new instance.
    global $USER;
    $lamstwo->timemodified = time();
	$lamstwo->lesson_id = lamstwo_get_lesson($USER->username,$lamstwo->sequence_id,$lamstwo->course,$lamstwo->name,$lamstwo->introduction,$lamstwo->start_date,trim($USER->country),substr(trim($USER->lang),0,2));
	return insert_record("lamstwo", $lamstwo);
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
  return update_record("lamstwo", $lamstwo);
}


function lamstwo_delete_instance($id) {
/// Given an ID of an instance of this module,
/// this function will permanently delete the instance
/// and any data that depends on it.

  if (! $lamstwo = get_record("lamstwo", "id", "$id")) {
      return false;
  }

  $result = true;

  # Delete any dependent records here #
  lamstwo_delete_lesson($USER->username,$lamstwo->lesson_id);
  if (! delete_records("lamstwo", "id", "$lamstwo->id")) {
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
/// Must return an array of grades for a given instance of this module,
/// indexed by user.  It also returns a maximum allowed grade.
///
///    $return->grades = array of grades;
///    $return->maxgrade = maximum allowed grade;
///
///    return $return;

  return NULL;
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
function lamstwo_get_lesson($username,$ldid,$courseid,$title,$desc,$startdate,$country,$lang) {
    //echo "enter lamstwo_get_lesson<BR>";
    global $CFG,$USER;
    if(!isset($CFG->lamstwo_serverid)||!isset($CFG->lamstwo_serverkey))
    {
        //echo "serverid or serverkey is not set<BR>";
        return NULL;
    }
    $relativeurl="/services/LessonManagerService?wsdl";
    $s = lamstwo_get_soap_client($relativeurl);
    if(is_null($s)){
        //echo "soap client is null<BR>";
        return NULL;
    }
    $datetime =    date("F d,Y g:i a");
    if(!isset($username)){
        $username = $USER->username;
    }
    $plaintext = $datetime.$username.$CFG->lamstwo_serverid.$CFG->lamstwo_serverkey;
    //echo $plaintext;
    $hashvalue = sha1(strtolower($plaintext));
    //echo $hashvalue;
	if($startdate){
	    $parameters = array($CFG->lamstwo_serverid,$datetime,$hashvalue,$username,$ldid,$courseid,$title,$desc,$startdate,$country,$lang);
	    $result = $s->call('scheduleLesson',$parameters);
	}else{
	    $parameters = array($CFG->lamstwo_serverid,$datetime,$hashvalue,$username,$ldid,$courseid,$title,$desc,$country,$lang);
	    $result = $s->call('startLesson',$parameters);
	}
    if($s->getError()){
        $result = $s->getError();
    	echo 'result:'.$result.'<BR>exit lamstwo_get_lesson<BR>';
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

?>
