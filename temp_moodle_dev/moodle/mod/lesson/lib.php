<?php  // $Id$
/**
 * Standard library of functions and constants for lesson
 *
 * @version $Id$
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package lesson
 **/

define("LESSON_MAX_EVENT_LENGTH", "432000");   // 5 days maximum

/**
 * Given an object containing all the necessary data,
 * (defined by the form in mod_form.php) this function
 * will create a new instance and return the id number
 * of the new instance.
 *
 * @param object $lesson Lesson post data from the form
 * @return int
 **/
function lesson_add_instance($lesson) {
    global $SESSION;

    lesson_process_pre_save($lesson);

    if (!$lesson->id = insert_record("lesson", $lesson)) {
        return false; // bad
    }

    lesson_process_post_save($lesson);

    lesson_grade_item_update(stripslashes_recursive($lesson));

    return $lesson->id;
}

/**
 * Given an object containing all the necessary data,
 * (defined by the form in mod_form.php) this function
 * will update an existing instance with new data.
 *
 * @param object $lesson Lesson post data from the form
 * @return boolean
 **/
function lesson_update_instance($lesson) {

    $lesson->id = $lesson->instance;

    lesson_process_pre_save($lesson);

    if (!$result = update_record("lesson", $lesson)) {
        return false; // Awe man!
    }

    lesson_process_post_save($lesson);

    // update grade item definition
    lesson_grade_item_update(stripslashes_recursive($lesson));

    // update grades - TODO: do it only when grading style changes
    lesson_update_grades(stripslashes_recursive($lesson), 0, false);

    return $result;
}


/*******************************************************************/
function lesson_delete_instance($id) {
/// Given an ID of an instance of this module,
/// this function will permanently delete the instance
/// and any data that depends on it.

    if (! $lesson = get_record("lesson", "id", "$id")) {
        return false;
    }

    $result = true;

    if (! delete_records("lesson", "id", "$lesson->id")) {
        $result = false;
    }
    if (! delete_records("lesson_pages", "lessonid", "$lesson->id")) {
        $result = false;
    }
    if (! delete_records("lesson_answers", "lessonid", "$lesson->id")) {
        $result = false;
    }
    if (! delete_records("lesson_attempts", "lessonid", "$lesson->id")) {
        $result = false;
    }
    if (! delete_records("lesson_grades", "lessonid", "$lesson->id")) {
        $result = false;
    }
    if (! delete_records("lesson_timer", "lessonid", "$lesson->id")) {
            $result = false;
    }
    if (! delete_records("lesson_branch", "lessonid", "$lesson->id")) {
            $result = false;
    }
    if (! delete_records("lesson_high_scores", "lessonid", "$lesson->id")) {
            $result = false;
    }
    if ($events = get_records_select('event', "modulename = 'lesson' and instance = '$lesson->id'")) {
        foreach($events as $event) {
            delete_event($event->id);
        }
    }
    $pagetypes = page_import_types('mod/lesson/');
    foreach ($pagetypes as $pagetype) {
        if (!delete_records('block_instance', 'pageid', $lesson->id, 'pagetype', $pagetype)) {
            $result = false;
        }
    }

    lesson_grade_item_delete($lesson);

    return $result;
}

/**
 * Given a course object, this function will clean up anything that
 * would be leftover after all the instances were deleted.
 *
 * As of now, this function just cleans the lesson_default table
 *
 * @param object $course an object representing the course that is being deleted
 * @param boolean $feedback to specify if the process must output a summary of its work
 * @return boolean
 */
function lesson_delete_course($course, $feedback=true) {

    $count = count_records('lesson_default', 'course', $course->id);
    delete_records('lesson_default', 'course', $course->id);

    //Inform about changes performed if feedback is enabled
    if ($feedback) {
        notify(get_string('deletedefaults', 'lesson', $count));
    }

    return true;
}

/*******************************************************************/
function lesson_user_outline($course, $user, $mod, $lesson) {
/// Return a small object with summary information about what a
/// user has done with a given particular instance of this module
/// Used for user activity reports.
/// $return->time = the time they did it
/// $return->info = a short text description

    if ($grades = get_records_select("lesson_grades", "lessonid = $lesson->id AND userid = $user->id",
                "grade DESC")) {
        foreach ($grades as $grade) {
            $max_grade = number_format($grade->grade * $lesson->grade / 100.0, 1);
            break;
        }
        $return->time = $grade->completed;
        if ($lesson->retake) {
            $return->info = get_string("gradeis", "lesson", $max_grade)." (".
                get_string("attempt", "lesson", count($grades)).")";
        } else {
            $return->info = get_string("gradeis", "lesson", $max_grade);
        }
    } else {
        $return->info = get_string("no")." ".get_string("attempts", "lesson");
    }
    return $return;
}

/*******************************************************************/
function lesson_user_complete($course, $user, $mod, $lesson) {
/// Print a detailed representation of what a  user has done with
/// a given particular instance of this module, for user activity reports.

    if ($attempts = get_records_select("lesson_attempts", "lessonid = $lesson->id AND userid = $user->id",
                "retry, timeseen")) {
        print_simple_box_start();
        $table->head = array (get_string("attempt", "lesson"),  get_string("numberofpagesviewed", "lesson"),
            get_string("numberofcorrectanswers", "lesson"), get_string("time"));
        $table->width = "100%";
        $table->align = array ("center", "center", "center", "center");
        $table->size = array ("*", "*", "*", "*");
        $table->cellpadding = 2;
        $table->cellspacing = 0;

        $retry = 0;
        $npages = 0;
        $ncorrect = 0;

        foreach ($attempts as $attempt) {
            if ($attempt->retry == $retry) {
                $npages++;
                if ($attempt->correct) {
                    $ncorrect++;
                }
                $timeseen = $attempt->timeseen;
            } else {
                $table->data[] = array($retry + 1, $npages, $ncorrect, userdate($timeseen));
                $retry++;
                $npages = 1;
                if ($attempt->correct) {
                    $ncorrect = 1;
                } else {
                    $ncorrect = 0;
                }
            }
        }
        if ($npages) {
                $table->data[] = array($retry + 1, $npages, $ncorrect, userdate($timeseen));
        }
        print_table($table);
        print_simple_box_end();
        // also print grade summary
        if ($grades = get_records_select("lesson_grades", "lessonid = $lesson->id AND userid = $user->id",
                    "grade DESC")) {
            foreach ($grades as $grade) {
                $max_grade = number_format($grade->grade * $lesson->grade / 100.0, 1);
                break;
            }
            if ($lesson->retake) {
                echo "<p>".get_string("gradeis", "lesson", $max_grade)." (".
                    get_string("attempts", "lesson").": ".count($grades).")</p>";
            } else {
                echo "<p>".get_string("gradeis", "lesson", $max_grade)."</p>";
            }
        }
    } else {
        echo get_string("no")." ".get_string("attempts", "lesson");
    }


    return true;
}

/**
 * Prints lesson summaries on MyMoodle Page
 *
 * Prints lesson name, due date and attempt information on
 * lessons that have a deadline that has not already passed
 * and it is available for taking.
 *
 * @param array $courses An array of course objects to get lesson instances from
 * @param array $htmlarray Store overview output array( course ID => 'lesson' => HTML output )
 */
function lesson_print_overview($courses, &$htmlarray) {
    global $USER, $CFG;

    if (!$lessons = get_all_instances_in_courses('lesson', $courses)) {
        return;
    }

/// Get Necessary Strings
    $strlesson       = get_string('modulename', 'lesson');
    $strnotattempted = get_string('nolessonattempts', 'lesson');
    $strattempted    = get_string('lessonattempted', 'lesson');

    $now = time();
    foreach ($lessons as $lesson) {
        if ($lesson->deadline != 0                                         // The lesson has a deadline
            and $lesson->deadline >= $now                                  // And it is before the deadline has been met
            and ($lesson->available == 0 or $lesson->available <= $now)) { // And the lesson is available

            // Lesson name
            if (!$lesson->visible) {
                $class = ' class="dimmed"';
            } else {
                $class = '';
            }
            $str = print_box("$strlesson: <a$class href=\"$CFG->wwwroot/mod/lesson/view.php?id=$lesson->coursemodule\">".
                             format_string($lesson->name).'</a>', 'name', '', true);

            // Deadline
            $str .= print_box(get_string('lessoncloseson', 'lesson', userdate($lesson->deadline)), 'info', '', true);

            // Attempt information
            if (has_capability('mod/lesson:manage', get_context_instance(CONTEXT_MODULE, $lesson->coursemodule))) {
                // Number of user attempts
                $attempts = count_records('lesson_attempts', 'lessonid', $lesson->id);
                $str     .= print_box(get_string('xattempts', 'lesson', $attempts), 'info', '', true);
            } else {
                // Determine if the user has attempted the lesson or not
                if (count_records('lesson_attempts', 'lessonid', $lesson->id, 'userid', $USER->id)) {
                    $str .= print_box($strattempted, 'info', '', true);
                } else {
                    $str .= print_box($strnotattempted, 'info', '', true);
                }
            }
            $str = print_box($str, 'lesson overview', '', true);

            if (empty($htmlarray[$lesson->course]['lesson'])) {
                $htmlarray[$lesson->course]['lesson'] = $str;
            } else {
                $htmlarray[$lesson->course]['lesson'] .= $str;
            }
        }
    }
}

/*******************************************************************/
function lesson_cron () {
/// Function to be run periodically according to the moodle cron
/// This function searches for things that need to be done, such
/// as sending out mail, toggling flags etc ...

    global $CFG;

    return true;
}

/**
 * Return grade for given user or all users.
 *
 * @param int $lessonid id of lesson
 * @param int $userid optional user id, 0 means all users
 * @return array array of grades, false if none
 */
function lesson_get_user_grades($lesson, $userid=0) {
    global $CFG;

    $user = $userid ? "AND u.id = $userid" : "";
    $fuser = $userid ? "AND uu.id = $userid" : "";

    if ($lesson->retake) {
        if ($lesson->usemaxgrade) {
            $sql = "SELECT u.id, u.id AS userid, MAX(g.grade) AS rawgrade
                      FROM {$CFG->prefix}user u, {$CFG->prefix}lesson_grades g
                     WHERE u.id = g.userid AND g.lessonid = $lesson->id
                           $user
                  GROUP BY u.id";
        } else {
            $sql = "SELECT u.id, u.id AS userid, AVG(g.grade) AS rawgrade
                      FROM {$CFG->prefix}user u, {$CFG->prefix}lesson_grades g
                     WHERE u.id = g.userid AND g.lessonid = $lesson->id
                           $user
                  GROUP BY u.id";
        }

    } else {
        // use only first attempts (with lowest id in lesson_grades table)
        $firstonly = "SELECT uu.id AS userid, MIN(gg.id) AS firstcompleted
                        FROM {$CFG->prefix}user uu, {$CFG->prefix}lesson_grades gg
                       WHERE uu.id = gg.userid AND gg.lessonid = $lesson->id
                             $fuser
                       GROUP BY uu.id";

        $sql = "SELECT u.id, u.id AS userid, g.grade AS rawgrade
                  FROM {$CFG->prefix}user u, {$CFG->prefix}lesson_grades g, ($firstonly) f
                 WHERE u.id = g.userid AND g.lessonid = $lesson->id
                       AND g.id = f.firstcompleted AND g.userid=f.userid
                       $user";
    }

    return get_records_sql($sql);
}

/**
 * Update grades in central gradebook
 *
 * @param object $lesson null means all lessons
 * @param int $userid specific user only, 0 mean all
 */
function lesson_update_grades($lesson=null, $userid=0, $nullifnone=true) {
    global $CFG;
    if (!function_exists('grade_update')) { //workaround for buggy PHP versions
        require_once($CFG->libdir.'/gradelib.php');
    }

    if ($lesson != null) {
        if ($grades = lesson_get_user_grades($lesson, $userid)) {
            lesson_grade_item_update($lesson, $grades);

        } else if ($userid and $nullifnone) {
            $grade = new object();
            $grade->userid   = $userid;
            $grade->rawgrade = NULL;
            lesson_grade_item_update($lesson, $grade);

        } else {
            lesson_grade_item_update($lesson);
        }

    } else {
        $sql = "SELECT l.*, cm.idnumber as cmidnumber, l.course as courseid
                  FROM {$CFG->prefix}lesson l, {$CFG->prefix}course_modules cm, {$CFG->prefix}modules m
                 WHERE m.name='lesson' AND m.id=cm.module AND cm.instance=l.id";
        if ($rs = get_recordset_sql($sql)) {
            while ($lesson = rs_fetch_next_record($rs)) {
                if ($lesson->grade != 0) {
                    lesson_update_grades($lesson, 0, false);
                } else {
                    lesson_grade_item_update($lesson);
                }
            }
            rs_close($rs);
        }
    }
}

/**
 * Create grade item for given lesson
 *
 * @param object $lesson object with extra cmidnumber
 * @param mixed optional array/object of grade(s); 'reset' means reset grades in gradebook
 * @return int 0 if ok, error code otherwise
 */
function lesson_grade_item_update($lesson, $grades=NULL) {
    global $CFG;
    if (!function_exists('grade_update')) { //workaround for buggy PHP versions
        require_once($CFG->libdir.'/gradelib.php');
    }

    if (array_key_exists('cmidnumber', $lesson)) { //it may not be always present
        $params = array('itemname'=>$lesson->name, 'idnumber'=>$lesson->cmidnumber);
    } else {
        $params = array('itemname'=>$lesson->name);
    }

    if ($lesson->grade > 0) {
        $params['gradetype']  = GRADE_TYPE_VALUE;
        $params['grademax']   = $lesson->grade;
        $params['grademin']   = 0;

    } else {
        $params['gradetype']  = GRADE_TYPE_NONE;
    }

    if ($grades  === 'reset') {
        $params['reset'] = true;
        $grades = NULL;
    }

    return grade_update('mod/lesson', $lesson->course, 'mod', 'lesson', $lesson->id, 0, $grades, $params);
}

/**
 * Delete grade item for given lesson
 *
 * @param object $lesson object
 * @return object lesson
 */
function lesson_grade_item_delete($lesson) {
    global $CFG;
    require_once($CFG->libdir.'/gradelib.php');

    return grade_update('mod/lesson', $lesson->course, 'mod', 'lesson', $lesson->id, 0, NULL, array('deleted'=>1));
}


/*******************************************************************/
function lesson_get_participants($lessonid) {
//Must return an array of user records (all data) who are participants
//for a given instance of lesson. Must include every user involved
//in the instance, independient of his role (student, teacher, admin...)

    global $CFG;

    //Get students
    $students = get_records_sql("SELECT DISTINCT u.id, u.id
                                 FROM {$CFG->prefix}user u,
                                      {$CFG->prefix}lesson_attempts a
                                 WHERE a.lessonid = '$lessonid' and
                                       u.id = a.userid");

    //Return students array (it contains an array of unique users)
    return ($students);
}

function lesson_get_view_actions() {
    return array('view','view all');
}

function lesson_get_post_actions() {
    return array('end','start', 'update grade attempt');
}

/**
 * Runs any processes that must run before
 * a lesson insert/update
 *
 * @param object $lesson Lesson form data
 * @return void
 **/
function lesson_process_pre_save(&$lesson) {
    $lesson->timemodified = time();

    if (empty($lesson->timed)) {
        $lesson->timed = 0;
    }
    if (empty($lesson->timespent) or !is_numeric($lesson->timespent) or $lesson->timespent < 0) {
        $lesson->timespent = 0;
    }
    if (!isset($lesson->completed)) {
        $lesson->completed = 0;
    }
    if (empty($lesson->gradebetterthan) or !is_numeric($lesson->gradebetterthan) or $lesson->gradebetterthan < 0) {
        $lesson->gradebetterthan = 0;
    } else if ($lesson->gradebetterthan > 100) {
        $lesson->gradebetterthan = 100;
    }

    // Conditions for dependency
    $conditions = new stdClass;
    $conditions->timespent = $lesson->timespent;
    $conditions->completed = $lesson->completed;
    $conditions->gradebetterthan = $lesson->gradebetterthan;
    $lesson->conditions = addslashes(serialize($conditions));
    unset($lesson->timespent);
    unset($lesson->completed);
    unset($lesson->gradebetterthan);

    if (!empty($lesson->password)) {
        $lesson->password = md5($lesson->password);
    } else {
        unset($lesson->password);
    }

    if ($lesson->lessondefault) {
        $default = new stdClass;
        $default = clone($lesson);
        unset($default->name);
        unset($default->timemodified);
        unset($default->available);
        unset($default->deadline);
        if ($default->id = get_field('lesson_default', 'id', 'course', $default->course)) {
            update_record('lesson_default', $default);
        } else {
            insert_record('lesson_default', $default);
        }
    }
    unset($lesson->lessondefault);
}

/**
 * Runs any processes that must be run
 * after a lesson insert/update
 *
 * @param object $lesson Lesson form data
 * @return void
 **/
function lesson_process_post_save(&$lesson) {
    if ($events = get_records_select('event', "modulename = 'lesson' and instance = '$lesson->id'")) {
        foreach($events as $event) {
            delete_event($event->id);
        }
    }

    $event = new stdClass;
    $event->description = $lesson->name;
    $event->courseid    = $lesson->course;
    $event->groupid     = 0;
    $event->userid      = 0;
    $event->modulename  = 'lesson';
    $event->instance    = $lesson->id;
    $event->eventtype   = 'open';
    $event->timestart   = $lesson->available;
    $event->visible     = instance_is_visible('lesson', $lesson);
    $event->timeduration = ($lesson->deadline - $lesson->available);

    if ($lesson->deadline and $lesson->available and $event->timeduration <= LESSON_MAX_EVENT_LENGTH) {
        // Single event for the whole lesson.
        $event->name = $lesson->name;
        add_event($event);
    } else {
        // Separate start and end events.
        $event->timeduration  = 0;
        if ($lesson->available) {
            $event->name = $lesson->name.' ('.get_string('lessonopens', 'lesson').')';
            add_event($event);
            unset($event->id); // So we can use the same object for the close event.
        }
        if ($lesson->deadline) {
            $event->name      = $lesson->name.' ('.get_string('lessoncloses', 'lesson').')';
            $event->timestart = $lesson->deadline;
            $event->eventtype = 'close';
            add_event($event);
        }
    }
}


/**
 * Implementation of the function for printing the form elements that control
 * whether the course reset functionality affects the lesson.
 * @param $mform form passed by reference
 */
function lesson_reset_course_form_definition(&$mform) {
    $mform->addElement('header', 'lessonheader', get_string('modulenameplural', 'lesson'));
    $mform->addElement('advcheckbox', 'reset_lesson', get_string('deleteallattempts','lesson'));
}

/**
 * Course reset form defaults.
 */
function lesson_reset_course_form_defaults($course) {
    return array('reset_lesson'=>1);
}

/**
 * Removes all grades from gradebook
 * @param int $courseid
 * @param string optional type
 */
function lesson_reset_gradebook($courseid, $type='') {
    global $CFG;

    $sql = "SELECT l.*, cm.idnumber as cmidnumber, l.course as courseid
              FROM {$CFG->prefix}lesson l, {$CFG->prefix}course_modules cm, {$CFG->prefix}modules m
             WHERE m.name='lesson' AND m.id=cm.module AND cm.instance=l.id AND l.course=$courseid";

    if ($lessons = get_records_sql($sql)) {
        foreach ($lessons as $lesson) {
            lesson_grade_item_update($lesson, 'reset');
        }
    }
}

/**
 * Actual implementation of the rest coures functionality, delete all the
 * lesson attempts for course $data->courseid.
 * @param $data the data submitted from the reset course.
 * @return array status array
 */
function lesson_reset_userdata($data) {
    global $CFG;

    $componentstr = get_string('modulenameplural', 'lesson');
    $status = array();

    if (!empty($data->reset_lesson)) {
        $lessonssql = "SELECT l.id
                         FROM {$CFG->prefix}lesson l
                        WHERE l.course={$data->courseid}";


        delete_records_select('lesson_timer', "lessonid IN ($lessonssql)");
        delete_records_select('lesson_high_scores', "lessonid IN ($lessonssql)");
        delete_records_select('lesson_grades', "lessonid IN ($lessonssql)");
        delete_records_select('lesson_attempts', "lessonid IN ($lessonssql)");

        // remove all grades from gradebook
        if (empty($data->reset_gradebook_grades)) {
            lesson_reset_gradebook($data->courseid);
        }

        $status[] = array('component'=>$componentstr, 'item'=>get_string('deleteallattempts', 'lesson'), 'error'=>false);
    }

    /// updating dates - shift may be negative too
    if ($data->timeshift) {
        shift_course_mod_dates('lesson', array('available', 'deadline'), $data->timeshift, $data->courseid);
        $status[] = array('component'=>$componentstr, 'item'=>get_string('datechanged'), 'error'=>false);
    }

    return $status;
}

/**
 * Returns all other caps used in module
 */
function lesson_get_extra_capabilities() {
    return array('moodle/site:accessallgroups');
}


//
/**
 * LAMS Function
 * This function creates new pages, branches and answers of a given lesson
 */
function add_pages__branches_answers($existinglesson,$userid,$oldid) {

	$pages= get_records("lesson_pages","lessonid",$oldid);
    
	$oldpages= array();
  	if ( $pages ) {
          foreach ($pages as $page) {
                $page->lessonid = $existinglesson->id;
                $page->contents = addslashes($page->contents);
                $page->oldid = $page->id;
                $page->title = addslashes($page->title);
                $page->timemodified = time();
                $page->id=insert_record("lesson_pages", $page);
               	$oldpages[$page->oldid]=$page->id; 
          } 
  	}
  	
 	$branches = get_records("lesson_branch", "lessonid",  $oldid);
 	     
    if ( $branches ) {
   
    	foreach ($branches as $branch) {
	      $branch->lessonid=$existinglesson->id;
	      $branch->userid=$userid;
	      $num=$branch->pageid;
	      $branch->pageid=$oldpages[$num];
	               
	      insert_record("lesson_branch", $branch); 
	     
	     
       }
    }
    $answers = get_records("lesson_answers", "lessonid",$oldid);
    if ( $answers ) {  
    	foreach ($answers as $answer) {
     	  $answer->lessonid=$existinglesson->id;
	      $answer->timecreated=time();
	      $answer->answer = addslashes($answer->answer);
	      $answer->response = addslashes($answer->response);
	      $num=$answer->pageid;
	        
	      $answer->pageid=$oldpages[$num];
	     
	      $answer->id=insert_record("lesson_answers", $answer);
    	}
    }
    // change page ids now that all new pages have been created
	$newpages= get_records("lesson_pages","lessonid",$existinglesson->id);
  	if ( $newpages ) {
          foreach ($newpages as $page) {
          		$num=$page->prevpageid;
                $page->prevpageid = $oldpages[$num];
                $num=$page->nextpageid;
                $page->nextpageid = $oldpages[$num];
                update_record("lesson_pages", $page);
      
          } 
  	}
}
//
/**
 * LAMS Function
 * This function clones an existing instance of Moodle lesson
 * replacing the course and the userid
 */
function lesson_clone_instance($id, $sectionref, $courseid,$userid) {
	global $CFG;
	
	$cm = get_record('course_modules', 'id', $id);
	// echo("hola");
	if (!$cm||$cm->instance==0) {
        // create a new lesson with default content
        $existinglesson->course = $courseid;
        $existinglesson->name = "Lesson";
        $existinglesson->is_lams = 1;
        $existinglesson->timemodified = time();
        $existinglesson->id = lesson_add_instance($existinglesson);//insert_record('lesson', $existinglesson);
       // print_r("blanc".$existinglesson);
      // echo($existinglesson);
    } else{
        // make a copy of an existing lesson
        $existinglesson = get_record('lesson', 'id', $cm->instance);
        $existinglesson->old_id = $existinglesson->id;
    	unset($existinglesson->id);
        $existinglesson->timemodified = time();
        $existinglesson->course = $courseid;
        $existinglesson->is_lams = 1;
        $existinglesson->id = lesson_add_instance($existinglesson);//insert_record('lesson', $existinglesson);
         // print_r("no blanc".$existinglesson);
      //      echo("hola2");
    }
	 //echo("hola3");
    $module = get_record('modules', 'name', 'lesson');
    $section = get_course_section($sectionref, $courseid);
    
    // module parameters
    $cm->course = $courseid;
    $cm->module = $module->id;
    $cm->instance = $existinglesson->id;
    $cm->added = time();
    $cm->section = $section->id;
	$cm->is_lams = 1; 
    $cm->course = $courseid;
    $cm->id = insert_record('course_modules', $cm);
	
    //adds the new lesson to the sequence variable in section table
    $existinglesson->section = $sectionref;
    $existinglesson->coursemodule = $cm->id;
    $existinglesson->instance = $cm->instance;
         
    require_once($CFG->dirroot.'/course/lib.php');
    add_mod_to_section($existinglesson);
    
    //add pages branches and answers for the new lesson
    if($existinglesson){
  
    	add_pages__branches_answers($existinglesson,$userid, $existinglesson->old_id);
    }
    
    return $cm->id;
}

/**
 * LAMS Function
 * Serialize a lesson instance and return serialized string to LAMS
 */
function lesson_export_instance($id) {
    $cm = get_record('course_modules', 'id', $id);
	if ($cm) {
        if ($lesson = get_record('lesson', 'id', $cm->instance)) {
           //get all pages from the lesson
          	$pages = get_records('lesson_pages', 'lessonid', $lesson->id);
			$branches = get_record('lesson_branch','lessonid',$lesson->id);
			$answers = get_record('lesson_answers','lessonid',$lesson->id);
            //our file will contain the lesson, its pages and categories 
            $all=array($lesson,$pages,$branches,$answers);
             // serialize lesson into a string; assuming lesson object
            // doesn't contain binary data in any of its columns
			$s = serialize($all);
            header('Content-Description: File Transfer');
            header('Content-Type: text/plain');
            header('Content-Length: ' . strlen($s));
            echo $s;
            exit;
        }
    }
    header('HTTP/1.1 500 Internal Error');
    exit;
}

/**
 * LAMS Function
 * Deserializes a serialized Moodle lesson, and creates a new instance of it
 */
function lesson_import_instance($filepath, $userid, $courseid, $sectionid) {
    // file contents contains serialized lesson object
    $filestr = file_get_contents($filepath);
    $all=unserialize($filestr);
    
    //we split the array so we can get the lesson, pages, branches and answers values
    $lesson = $all[0];
    $pages = $all[1];
    $branches = $all[2];
    $answers = $all[3];
    // import this lesson into a new course
    $lesson->course = $courseid;

    // escape text columns for saving into database
    $lesson->name = addslashes($lesson->name);
    $lesson->timemodified =time();
    $lesson->oldid = $lesson->id;


    if ( ! $lesson->id = insert_record('lesson', $lesson) ) {
        return 0; 
    }else{
    	//add pages branches and answers for the new lesson
    	add_pages__branches_answers($lesson,$userid,$lesson->oldid);
    }
    $module = get_record('modules', 'name', 'lesson');
    $section = get_course_section($sectionid, $courseid);

    $cm->course = $courseid;
    $cm->module = $module->id;
    $cm->instance = $lesson->id;
    $cm->added = time();
    $cm->section = $section->section;
	$cm->is_lams = 1; 
    $cm->id = insert_record('course_modules', $cm);
    

    return $cm->id;
}

/**
 * LAMS Function
 * Return a statistic for a given user in this Moodle lesson for use in branching
 */
function lesson_get_tool_output($id, $userid) {
    $cm = get_record('course_modules', 'id', $id);
    if ($cm) {
		$grade = get_record('lesson_grades', 'userid', $userid, 'lessonid', $cm->instance);
		if ($grade) {
			$percentage =( $grade->grade)*10;
			return $percentage;
		}else{
			return 0;	
		}
    }
    return 0;
}

/**
 * LAMS Function
 * Return an HTML representation of a user's activity in this lesson
 */
function lesson_export_portfolio($id, $userid) {
    global $CFG;
    $text = 'This tool does not support export portfolio.  It may be accessible via ';
    $text .= '<a href="'.$CFG->wwwroot.'/mod/lesson/view.php?id='.$id.'">this</a> link.';
    return $text;

}


?>
